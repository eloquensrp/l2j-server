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
package handlers.targethandlers;

import java.util.ArrayList;
import java.util.List;

import com.l2jmobius.Config;
import com.l2jmobius.gameserver.handler.ITargetTypeHandler;
import com.l2jmobius.gameserver.model.WorldObject;
import com.l2jmobius.gameserver.model.actor.Creature;
import com.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import com.l2jmobius.gameserver.model.skills.Skill;
import com.l2jmobius.gameserver.model.skills.targets.TargetType;
import com.l2jmobius.gameserver.util.Util;

/**
 * @author UnAfraid
 */
public class PartyNotMe implements ITargetTypeHandler
{
	@Override
	public WorldObject[] getTargetList(Skill skill, Creature creature, boolean onlyFirst, Creature target)
	{
		final List<Creature> targetList = new ArrayList<>();
		if (creature.getParty() != null)
		{
			final List<PlayerInstance> partyList = creature.getParty().getMembers();
			for (PlayerInstance partyMember : partyList)
			{
				if ((partyMember == null) || partyMember.isDead())
				{
					continue;
				}
				else if (partyMember == creature)
				{
					continue;
				}
				else if (!Util.checkIfInRange(Config.ALT_PARTY_RANGE, creature, partyMember, true))
				{
					continue;
				}
				else if ((skill.getAffectRange() > 0) && !Util.checkIfInRange(skill.getAffectRange(), creature, partyMember, true))
				{
					continue;
				}
				else
				{
					targetList.add(partyMember);
					
					if ((partyMember.getSummon() != null) && !partyMember.getSummon().isDead())
					{
						targetList.add(partyMember.getSummon());
					}
				}
			}
		}
		return targetList.toArray(new Creature[targetList.size()]);
	}
	
	@Override
	public Enum<TargetType> getTargetType()
	{
		return TargetType.PARTY_NOTME;
	}
}
