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
import com.l2jmobius.gameserver.network.L2GameClient;
import com.l2jmobius.gameserver.network.serverpackets.ExPledgeCrestLarge;

/**
 * Fomat : chd c: (id) 0xD0 h: (subid) 0x10 d: the crest id This is a trigger
 * @author -Wooden-
 */
public final class RequestExPledgeCrestLarge implements IClientIncomingPacket
{
	private int _crestId;
	
	@Override
	public boolean read(L2GameClient client, PacketReader packet)
	{
		_crestId = packet.readD();
		return true;
	}
	
	@Override
	public void run(L2GameClient client)
	{
		client.sendPacket(new ExPledgeCrestLarge(_crestId));
	}
}