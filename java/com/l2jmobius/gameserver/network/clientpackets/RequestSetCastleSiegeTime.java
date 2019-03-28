/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jmobius.gameserver.network.clientpackets;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import com.l2jmobius.Config;
import com.l2jmobius.commons.network.PacketReader;
import com.l2jmobius.gameserver.instancemanager.CastleManager;
import com.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import com.l2jmobius.gameserver.model.entity.Castle;
import com.l2jmobius.gameserver.network.GameClient;
import com.l2jmobius.gameserver.network.SystemMessageId;
import com.l2jmobius.gameserver.network.serverpackets.SiegeInfo;
import com.l2jmobius.gameserver.network.serverpackets.SystemMessage;
import com.l2jmobius.gameserver.util.Broadcast;

/**
 * @author UnAfraid
 */
public class RequestSetCastleSiegeTime implements IClientIncomingPacket
{
	private int _castleId;
	private long _time;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_castleId = packet.readD();
		_time = packet.readD();
		_time *= 1000;
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		final Castle castle = CastleManager.getInstance().getCastleById(_castleId);
		if ((player == null) || (castle == null))
		{
			LOGGER.log(Level.WARNING, "[C]RequestSetCastleSiegeTime: activeChar: " + player + " castle: " + castle + " castleId: " + _castleId);
			return;
		}
		if ((castle.getOwnerId() > 0) && (castle.getOwnerId() != player.getClanId()))
		{
			LOGGER.log(Level.WARNING, "[C]RequestSetCastleSiegeTime: activeChar: " + player + " castle: " + castle + " castleId: " + _castleId + " is trying to change siege date of not his own castle!");
			return;
		}
		else if (!player.isClanLeader())
		{
			LOGGER.log(Level.WARNING, "[C]RequestSetCastleSiegeTime: activeChar: " + player + " castle: " + castle + " castleId: " + _castleId + " is trying to change siege date but is not clan leader!");
			return;
		}
		else if (!castle.getIsTimeRegistrationOver())
		{
			if (isSiegeTimeValid(castle.getSiegeDate().getTimeInMillis(), _time))
			{
				castle.getSiegeDate().setTimeInMillis(_time);
				castle.setIsTimeRegistrationOver(true);
				castle.getSiege().saveSiegeDate();
				final SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ANNOUNCED_THE_NEXT_CASTLE_SIEGE_TIME);
				msg.addCastleId(_castleId);
				Broadcast.toAllOnlinePlayers(msg);
				player.sendPacket(new SiegeInfo(castle, player));
			}
			else
			{
				LOGGER.log(Level.WARNING, "[C]RequestSetCastleSiegeTime: activeChar: " + player + " castle: " + castle + " castleId: " + _castleId + " is trying to an invalid time (" + new Date(_time) + " !");
			}
		}
		else
		{
			LOGGER.log(Level.WARNING, "[C]RequestSetCastleSiegeTime: activeChar: " + player + " castle: " + castle + " castleId: " + _castleId + " is trying to change siege date but currently not possible!");
		}
	}
	
	private static boolean isSiegeTimeValid(long siegeDate, long choosenDate)
	{
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(siegeDate);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		
		final Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(choosenDate);
		
		for (int hour : Config.SIEGE_HOUR_LIST)
		{
			cal1.set(Calendar.HOUR_OF_DAY, hour);
			if (isEqual(cal1, cal2, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND))
			{
				return true;
			}
		}
		return false;
	}
	
	private static boolean isEqual(Calendar cal1, Calendar cal2, int... fields)
	{
		for (int field : fields)
		{
			if (cal1.get(field) != cal2.get(field))
			{
				return false;
			}
		}
		return true;
	}
}
