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
package com.l2jmobius.gameserver.network;

import java.util.logging.Logger;

import com.l2jmobius.commons.concurrent.ThreadPool;
import com.l2jmobius.gameserver.instancemanager.AntiFeedManager;
import com.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import com.l2jmobius.gameserver.model.events.EventDispatcher;
import com.l2jmobius.gameserver.model.events.impl.creature.player.OnPlayerLogout;
import com.l2jmobius.gameserver.network.serverpackets.IClientOutgoingPacket;
import com.l2jmobius.gameserver.taskmanager.AttackStanceTaskManager;

/**
 * @author NB4L1
 */
public final class Disconnection
{
	private static final Logger LOGGER = Logger.getLogger(Disconnection.class.getName());
	
	public static GameClient getClient(GameClient client, PlayerInstance player)
	{
		if (client != null)
		{
			return client;
		}
		
		if (player != null)
		{
			return player.getClient();
		}
		
		return null;
	}
	
	public static PlayerInstance getActiveChar(GameClient client, PlayerInstance player)
	{
		if (player != null)
		{
			return player;
		}
		
		if (client != null)
		{
			return client.getPlayer();
		}
		
		return null;
	}
	
	private final GameClient _client;
	private final PlayerInstance _player;
	
	private Disconnection(GameClient client)
	{
		this(client, null);
	}
	
	public static Disconnection of(GameClient client)
	{
		return new Disconnection(client);
	}
	
	private Disconnection(PlayerInstance player)
	{
		this(null, player);
	}
	
	public static Disconnection of(PlayerInstance player)
	{
		return new Disconnection(player);
	}
	
	private Disconnection(GameClient client, PlayerInstance player)
	{
		_client = getClient(client, player);
		_player = getActiveChar(client, player);
		
		// Anti Feed
		AntiFeedManager.getInstance().onDisconnect(_client);
		
		if (_client != null)
		{
			_client.setPlayer(null);
		}
		
		if (_player != null)
		{
			_player.setClient(null);
		}
	}
	
	public static Disconnection of(GameClient client, PlayerInstance player)
	{
		return new Disconnection(client, player);
	}
	
	public Disconnection storeMe()
	{
		try
		{
			if ((_player != null) && _player.isOnline())
			{
				_player.storeMe();
			}
		}
		catch (RuntimeException e)
		{
			LOGGER.warning(e.getMessage());
		}
		
		return this;
	}
	
	public Disconnection deleteMe()
	{
		try
		{
			if ((_player != null) && _player.isOnline())
			{
				EventDispatcher.getInstance().notifyEventAsync(new OnPlayerLogout(_player), _player);
				_player.deleteMe();
			}
		}
		catch (RuntimeException e)
		{
			LOGGER.warning(e.getMessage());
		}
		
		return this;
	}
	
	public Disconnection close(boolean toLoginScreen)
	{
		if (_client != null)
		{
			_client.close(toLoginScreen);
		}
		
		return this;
	}
	
	public Disconnection close(IClientOutgoingPacket packet)
	{
		if (_client != null)
		{
			_client.close(packet);
		}
		
		return this;
	}
	
	public void defaultSequence(boolean toLoginScreen)
	{
		defaultSequence();
		close(toLoginScreen);
	}
	
	public void defaultSequence(IClientOutgoingPacket packet)
	{
		defaultSequence();
		close(packet);
	}
	
	private void defaultSequence()
	{
		storeMe();
		deleteMe();
	}
	
	public void onDisconnection()
	{
		if (_player != null)
		{
			ThreadPool.schedule(() -> defaultSequence(), _player.canLogout() ? 0 : AttackStanceTaskManager.COMBAT_TIME);
		}
	}
}