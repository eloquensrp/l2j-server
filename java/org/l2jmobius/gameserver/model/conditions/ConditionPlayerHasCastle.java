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
package org.l2jmobius.gameserver.model.conditions;

import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.model.item.ItemTemplate;
import org.l2jmobius.gameserver.model.skills.Skill;

/**
 * The Class ConditionPlayerHasCastle.
 * @author MrPoke
 */
public class ConditionPlayerHasCastle extends Condition
{
	private final int _castle;
	
	/**
	 * Instantiates a new condition player has castle.
	 * @param castle the castle
	 */
	public ConditionPlayerHasCastle(int castle)
	{
		_castle = castle;
	}
	
	/**
	 * Test impl.
	 * @return true, if successful
	 */
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, ItemTemplate item)
	{
		if (effector.getActingPlayer() == null)
		{
			return false;
		}
		
		final Clan clan = effector.getActingPlayer().getClan();
		if (clan == null)
		{
			return _castle == 0;
		}
		
		// Any castle
		if (_castle == -1)
		{
			return clan.getCastleId() > 0;
		}
		return clan.getCastleId() == _castle;
	}
}
