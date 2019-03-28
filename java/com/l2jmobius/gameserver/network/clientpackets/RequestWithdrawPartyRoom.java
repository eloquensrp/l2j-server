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

import com.l2jmobius.commons.network.PacketReader;
import com.l2jmobius.gameserver.model.PartyMatchRoom;
import com.l2jmobius.gameserver.model.PartyMatchRoomList;
import com.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import com.l2jmobius.gameserver.network.GameClient;
import com.l2jmobius.gameserver.network.SystemMessageId;
import com.l2jmobius.gameserver.network.serverpackets.ExClosePartyRoom;

/**
 * @author Gnacik
 */
public final class RequestWithdrawPartyRoom implements IClientIncomingPacket
{
	private int _roomid;
	@SuppressWarnings("unused")
	private int _unk1;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_roomid = packet.readD();
		_unk1 = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance _player = client.getPlayer();
		
		if (_player == null)
		{
			return;
		}
		
		final PartyMatchRoom _room = PartyMatchRoomList.getInstance().getRoom(_roomid);
		if (_room == null)
		{
			return;
		}
		
		if ((_player.isInParty() && _room.getOwner().isInParty()) && (_player.getParty().getLeaderObjectId() == _room.getOwner().getParty().getLeaderObjectId()))
		{
			// If user is in party with Room Owner
			// is not removed from Room
			
			// _activeChar.setPartyMatching(0);
			_player.broadcastUserInfo();
		}
		else
		{
			_room.deleteMember(_player);
			
			_player.setPartyRoom(0);
			// _activeChar.setPartyMatching(0);
			
			_player.sendPacket(new ExClosePartyRoom());
			_player.sendPacket(SystemMessageId.YOU_HAVE_EXITED_THE_PARTY_ROOM);
		}
	}
}
