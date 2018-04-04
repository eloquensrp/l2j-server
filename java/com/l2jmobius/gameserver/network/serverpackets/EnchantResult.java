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
package com.l2jmobius.gameserver.network.serverpackets;

import com.l2jmobius.commons.network.PacketWriter;
import com.l2jmobius.gameserver.network.OutgoingPackets;

public class EnchantResult implements IClientOutgoingPacket
{
	private final int _result;
	private final int _crystal;
	private final int _count;
	
	public EnchantResult(int result, int crystal, int count)
	{
		_result = result;
		_crystal = crystal;
		_count = count;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.ENCHANT_RESULT.writeId(packet);
		packet.writeD(_result);
		packet.writeD(_crystal);
		packet.writeQ(_count);
		return true;
	}
}
