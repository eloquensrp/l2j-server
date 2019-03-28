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
import com.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import com.l2jmobius.gameserver.network.GameClient;
import com.l2jmobius.gameserver.network.serverpackets.ExListPartyMatchingWaitingRoom;

/**
 * @author Gnacik
 */
public class RequestListPartyMatchingWaitingRoom implements IClientIncomingPacket
{
	private int _page;
	private int _minlvl;
	private int _maxlvl;
	private int _mode; // 1 - waitlist 0 - room waitlist
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_page = packet.readD();
		_minlvl = packet.readD();
		_maxlvl = packet.readD();
		_mode = packet.readD();
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
		
		_player.sendPacket(new ExListPartyMatchingWaitingRoom(_player, _page, _minlvl, _maxlvl, _mode));
	}
}