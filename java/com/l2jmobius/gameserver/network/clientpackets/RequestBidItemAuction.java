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
import com.l2jmobius.gameserver.instancemanager.ItemAuctionManager;
import com.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import com.l2jmobius.gameserver.model.itemauction.ItemAuction;
import com.l2jmobius.gameserver.model.itemauction.ItemAuctionInstance;
import com.l2jmobius.gameserver.model.itemcontainer.Inventory;
import com.l2jmobius.gameserver.network.GameClient;

/**
 * @author Forsaiken
 */
public final class RequestBidItemAuction implements IClientIncomingPacket
{
	private int _instanceId;
	private long _bid;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_instanceId = packet.readD();
		_bid = packet.readQ();
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
		
		// can't use auction fp here
		if (!client.getFloodProtectors().getTransaction().tryPerformAction("auction"))
		{
			player.sendMessage("You are bidding too fast.");
			return;
		}
		
		if ((_bid < 0) || (_bid > Inventory.MAX_ADENA))
		{
			return;
		}
		
		final ItemAuctionInstance instance = ItemAuctionManager.getInstance().getManagerInstance(_instanceId);
		if (instance != null)
		{
			final ItemAuction auction = instance.getCurrentAuction();
			if (auction != null)
			{
				auction.registerBid(player, _bid);
			}
		}
	}
}