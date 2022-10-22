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
package org.l2jmobius.gameserver.taskmanager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.gameserver.model.actor.Creature;

/**
 * Movement task manager class.
 * @author Mobius
 */
public class MovementTaskManager
{
	private static final Set<Set<Creature>> POOLS = ConcurrentHashMap.newKeySet();
	private static final int POOL_SIZE = 1000;
	private static final int TASK_DELAY = 100;
	
	protected MovementTaskManager()
	{
	}
	
	private class Movement implements Runnable
	{
		private final Set<Creature> _creatures;
		
		public Movement(Set<Creature> creatures)
		{
			_creatures = creatures;
		}
		
		@Override
		public void run()
		{
			_creatures.removeIf(Creature::updatePosition);
		}
	}
	
	/**
	 * Add a Creature to moving objects of MovementTaskManager.
	 * @param creature The Creature to add to moving objects of MovementTaskManager.
	 */
	public synchronized void registerMovingObject(Creature creature)
	{
		for (Set<Creature> pool : POOLS)
		{
			if (pool.contains(creature))
			{
				return;
			}
		}
		
		for (Set<Creature> pool : POOLS)
		{
			if (pool.size() < POOL_SIZE)
			{
				pool.add(creature);
				return;
			}
		}
		
		final Set<Creature> pool = ConcurrentHashMap.newKeySet(POOL_SIZE);
		pool.add(creature);
		ThreadPool.scheduleAtFixedRate(new Movement(pool), TASK_DELAY, TASK_DELAY);
		POOLS.add(pool);
	}
	
	public static final MovementTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final MovementTaskManager INSTANCE = new MovementTaskManager();
	}
}
