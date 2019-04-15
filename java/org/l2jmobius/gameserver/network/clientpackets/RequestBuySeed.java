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

import static org.l2jmobius.gameserver.model.itemcontainer.Inventory.MAX_ADENA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.l2jmobius.Config;
import org.l2jmobius.commons.network.PacketReader;
import org.l2jmobius.gameserver.datatables.ItemTable;
import org.l2jmobius.gameserver.instancemanager.CastleManager;
import org.l2jmobius.gameserver.instancemanager.CastleManorManager;
import org.l2jmobius.gameserver.model.SeedProduction;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.instance.MerchantInstance;
import org.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import org.l2jmobius.gameserver.model.entity.Castle;
import org.l2jmobius.gameserver.model.holders.ItemHolder;
import org.l2jmobius.gameserver.model.items.Item;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;
import org.l2jmobius.gameserver.util.Util;

/**
 * @author l3x
 */
public class RequestBuySeed implements IClientIncomingPacket
{
	private static final int BATCH_LENGTH = 12; // length of the one item
	private int _manorId;
	private List<ItemHolder> _items = null;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_manorId = packet.readD();
		final int count = packet.readD();
		if ((count <= 0) || (count > Config.MAX_ITEM_IN_PACKET) || ((count * BATCH_LENGTH) != packet.getReadableBytes()))
		{
			return false;
		}
		
		_items = new ArrayList<>(count);
		for (int i = 0; i < count; i++)
		{
			final int itemId = packet.readD();
			final long cnt = packet.readQ();
			if ((cnt < 1) || (itemId < 1))
			{
				_items = null;
				return false;
			}
			_items.add(new ItemHolder(itemId, cnt));
		}
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
		else if (!client.getFloodProtectors().getManor().tryPerformAction("BuySeed"))
		{
			player.sendMessage("You are buying seeds too fast!");
			return;
		}
		else if (_items == null)
		{
			client.sendActionFailed();
			return;
		}
		
		final CastleManorManager manor = CastleManorManager.getInstance();
		if (manor.isUnderMaintenance())
		{
			client.sendActionFailed();
			return;
		}
		
		final Castle castle = CastleManager.getInstance().getCastleById(_manorId);
		if (castle == null)
		{
			client.sendActionFailed();
			return;
		}
		
		final Npc manager = player.getLastFolkNPC();
		if (!(manager instanceof MerchantInstance) || !manager.canInteract(player) || (manager.getTemplate().getParameters().getInt("manor_id", -1) != _manorId))
		{
			client.sendActionFailed();
			return;
		}
		
		long totalPrice = 0;
		int slots = 0;
		int totalWeight = 0;
		
		final Map<Integer, SeedProduction> _productInfo = new HashMap<>();
		for (ItemHolder ih : _items)
		{
			final SeedProduction sp = manor.getSeedProduct(_manorId, ih.getId(), false);
			if ((sp == null) || (sp.getPrice() <= 0) || (sp.getAmount() < ih.getCount()) || ((MAX_ADENA / ih.getCount()) < sp.getPrice()))
			{
				client.sendActionFailed();
				return;
			}
			
			// Calculate price
			totalPrice += (sp.getPrice() * ih.getCount());
			if (totalPrice > MAX_ADENA)
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to purchase over " + MAX_ADENA + " adena worth of goods.", Config.DEFAULT_PUNISH);
				client.sendActionFailed();
				return;
			}
			
			// Calculate weight
			final Item template = ItemTable.getInstance().getTemplate(ih.getId());
			totalWeight += ih.getCount() * template.getWeight();
			
			// Calculate slots
			if (!template.isStackable())
			{
				slots += ih.getCount();
			}
			else if (player.getInventory().getItemByItemId(ih.getId()) == null)
			{
				slots++;
			}
			_productInfo.put(ih.getId(), sp);
		}
		
		if (!player.getInventory().validateWeight(totalWeight))
		{
			player.sendPacket(SystemMessageId.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT);
			return;
		}
		else if (!player.getInventory().validateCapacity(slots))
		{
			player.sendPacket(SystemMessageId.YOUR_INVENTORY_IS_FULL);
			return;
		}
		else if ((totalPrice < 0) || (player.getAdena() < totalPrice))
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		
		// Proceed the purchase
		for (ItemHolder i : _items)
		{
			final SeedProduction sp = _productInfo.get(i.getId());
			final long price = sp.getPrice() * i.getCount();
			
			// Take Adena and decrease seed amount
			if (!sp.decreaseAmount(i.getCount()) || !player.reduceAdena("Buy", price, player, false))
			{
				// failed buy, reduce total price
				totalPrice -= price;
				continue;
			}
			
			// Add item to player's inventory
			player.addItem("Buy", i.getId(), i.getCount(), manager, true);
		}
		
		// Adding to treasury for Manor Castle
		if (totalPrice > 0)
		{
			castle.addToTreasuryNoTax(totalPrice);
			
			final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_ADENA_DISAPPEARED);
			sm.addLong(totalPrice);
			player.sendPacket(sm);
			
			if (Config.ALT_MANOR_SAVE_ALL_ACTIONS)
			{
				manor.updateCurrentProduction(_manorId, _productInfo.values());
			}
		}
	}
}