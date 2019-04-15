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
package org.l2jmobius.gameserver.network.clientpackets;

import org.l2jmobius.commons.network.PacketReader;
import org.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.TargetUnselected;

/**
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestTargetCanceld implements IClientIncomingPacket
{
	private int _unselect;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_unselect = packet.readH();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (player.isLockedTarget())
		{
			player.sendPacket(SystemMessageId.FAILED_TO_DISABLE_ATTACK_TARGET);
			return;
		}
		
		if (_unselect == 0)
		{
			if (player.isCastingNow() && player.canAbortCast())
			{
				player.abortCast();
			}
			else if (player.getTarget() != null)
			{
				player.setTarget(null);
			}
		}
		else if (player.getTarget() != null)
		{
			player.setTarget(null);
		}
		else if (player.isInAirShip())
		{
			player.broadcastPacket(new TargetUnselected(player));
		}
	}
}