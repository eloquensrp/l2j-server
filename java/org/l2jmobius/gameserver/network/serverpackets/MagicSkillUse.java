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
package org.l2jmobius.gameserver.network.serverpackets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.interfaces.IPositionable;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * MagicSkillUse server packet implementation.
 * @author UnAfraid, NosBit
 */
public class MagicSkillUse extends ServerPacket
{
	private final int _skillId;
	private final int _skillLevel;
	private final int _hitTime;
	private final int _reuseDelay;
	private final Creature _creature;
	private final Creature _target;
	private final List<Integer> _unknown = Collections.emptyList();
	private final List<Location> _groundLocations;
	
	public MagicSkillUse(Creature creature, Creature target, int skillId, int skillLevel, int hitTime, int reuseDelay)
	{
		super(55);
		_creature = creature;
		_target = target;
		_skillId = skillId;
		_skillLevel = skillLevel;
		_hitTime = hitTime;
		_reuseDelay = reuseDelay;
		_groundLocations = creature.isPlayer() && (creature.getActingPlayer().getCurrentSkillWorldPosition() != null) ? Arrays.asList(creature.getActingPlayer().getCurrentSkillWorldPosition()) : Collections.<Location> emptyList();
	}
	
	public MagicSkillUse(Creature creature, int skillId, int skillLevel, int hitTime, int reuseDelay)
	{
		this(creature, creature, skillId, skillLevel, hitTime, reuseDelay);
	}
	
	@Override
	public void write()
	{
		ServerPackets.MAGIC_SKILL_USE.writeId(this);
		writeInt(_creature.getObjectId());
		writeInt(_target.getObjectId());
		writeInt(_skillId);
		writeInt(_skillLevel);
		writeInt(_hitTime);
		writeInt(_reuseDelay);
		writeInt(_creature.getX());
		writeInt(_creature.getY());
		writeInt(_creature.getZ());
		writeShort(_unknown.size()); // TODO: Implement me!
		for (int unknown : _unknown)
		{
			writeShort(unknown);
		}
		writeShort(_groundLocations.size());
		for (IPositionable target : _groundLocations)
		{
			writeInt(target.getX());
			writeInt(target.getY());
			writeInt(target.getZ());
		}
		writeInt(_target.getX());
		writeInt(_target.getY());
		writeInt(_target.getZ());
	}
}
