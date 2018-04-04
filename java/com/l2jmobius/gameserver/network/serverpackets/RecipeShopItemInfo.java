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
import com.l2jmobius.gameserver.model.actor.instance.L2PcInstance;
import com.l2jmobius.gameserver.network.OutgoingPackets;

public class RecipeShopItemInfo implements IClientOutgoingPacket
{
	private final L2PcInstance _player;
	private final int _recipeId;
	
	public RecipeShopItemInfo(L2PcInstance player, int recipeId)
	{
		_player = player;
		_recipeId = recipeId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.RECIPE_SHOP_ITEM_INFO.writeId(packet);
		packet.writeD(_player.getObjectId());
		packet.writeD(_recipeId);
		packet.writeD((int) _player.getCurrentMp());
		packet.writeD(_player.getMaxMp());
		packet.writeD(0xffffffff);
		return true;
	}
}
