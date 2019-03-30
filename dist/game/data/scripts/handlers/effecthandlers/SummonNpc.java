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
package handlers.effecthandlers;

import com.l2jmobius.commons.util.Rnd;
import com.l2jmobius.gameserver.data.xml.impl.NpcData;
import com.l2jmobius.gameserver.model.Location;
import com.l2jmobius.gameserver.model.Spawn;
import com.l2jmobius.gameserver.model.StatsSet;
import com.l2jmobius.gameserver.model.actor.Npc;
import com.l2jmobius.gameserver.model.actor.instance.DecoyInstance;
import com.l2jmobius.gameserver.model.actor.instance.EffectPointInstance;
import com.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import com.l2jmobius.gameserver.model.actor.templates.NpcTemplate;
import com.l2jmobius.gameserver.model.conditions.Condition;
import com.l2jmobius.gameserver.model.effects.AbstractEffect;
import com.l2jmobius.gameserver.model.effects.EffectType;
import com.l2jmobius.gameserver.model.skills.BuffInfo;
import com.l2jmobius.gameserver.model.skills.targets.TargetType;

/**
 * Summon Npc effect implementation.
 * @author Zoey76
 */
public final class SummonNpc extends AbstractEffect
{
	private int _despawnDelay;
	private final int _npcId;
	private final int _npcCount;
	private final boolean _randomOffset;
	private final boolean _isSummonSpawn;
	
	public SummonNpc(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params)
	{
		super(attachCond, applyCond, set, params);
		
		_despawnDelay = params.getInt("despawnDelay", 20000);
		_npcId = params.getInt("npcId", 0);
		_npcCount = params.getInt("npcCount", 1);
		_randomOffset = params.getBoolean("randomOffset", false);
		_isSummonSpawn = params.getBoolean("isSummonSpawn", false);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.SUMMON_NPC;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		if ((info.getEffected() == null) || !info.getEffected().isPlayer() || info.getEffected().isAlikeDead() || info.getEffected().getActingPlayer().inObserverMode())
		{
			return;
		}
		
		if ((_npcId <= 0) || (_npcCount <= 0))
		{
			LOGGER.warning(SummonNpc.class.getSimpleName() + ": Invalid NPC ID or count skill ID: " + info.getSkill().getId());
			return;
		}
		
		final PlayerInstance player = info.getEffected().getActingPlayer();
		if (player.isMounted())
		{
			return;
		}
		
		final NpcTemplate npcTemplate = NpcData.getInstance().getTemplate(_npcId);
		if (npcTemplate == null)
		{
			LOGGER.warning(SummonNpc.class.getSimpleName() + ": Spawn of the nonexisting NPC ID: " + _npcId + ", skill ID:" + info.getSkill().getId());
			return;
		}
		
		switch (npcTemplate.getType())
		{
			case "Decoy":
			{
				final DecoyInstance decoy = new DecoyInstance(npcTemplate, player, _despawnDelay);
				decoy.setCurrentHp(decoy.getMaxHp());
				decoy.setCurrentMp(decoy.getMaxMp());
				decoy.setHeading(player.getHeading());
				decoy.setInstanceId(player.getInstanceId());
				decoy.setSummoner(player);
				decoy.spawnMe(player.getX(), player.getY(), player.getZ());
				player.setDecoy(decoy);
				break;
			}
			case "EffectPoint": // TODO: Implement proper signet skills.
			{
				final EffectPointInstance effectPoint = new EffectPointInstance(npcTemplate, player);
				effectPoint.setCurrentHp(effectPoint.getMaxHp());
				effectPoint.setCurrentMp(effectPoint.getMaxMp());
				int x = player.getX();
				int y = player.getY();
				int z = player.getZ();
				
				if (info.getSkill().getTargetType() == TargetType.GROUND)
				{
					final Location wordPosition = player.getActingPlayer().getCurrentSkillWorldPosition();
					if (wordPosition != null)
					{
						x = wordPosition.getX();
						y = wordPosition.getY();
						z = wordPosition.getZ();
					}
				}
				
				effectPoint.setIsInvul(true);
				effectPoint.setSummoner(player);
				effectPoint.spawnMe(x, y, z);
				_despawnDelay = NpcData.getInstance().getTemplate(_npcId).getParameters().getInt("despawn_time") * 1000;
				if (_despawnDelay > 0)
				{
					effectPoint.scheduleDespawn(_despawnDelay);
				}
				break;
			}
			default:
			{
				Spawn spawn;
				try
				{
					spawn = new Spawn(_npcId);
				}
				catch (Exception e)
				{
					LOGGER.warning(SummonNpc.class.getSimpleName() + ": " + e.getMessage());
					return;
				}
				
				int x = player.getX();
				int y = player.getY();
				if (_randomOffset)
				{
					x += (Rnd.nextBoolean() ? Rnd.get(20, 50) : Rnd.get(-50, -20));
					y += (Rnd.nextBoolean() ? Rnd.get(20, 50) : Rnd.get(-50, -20));
				}
				
				spawn.setXYZ(x, y, player.getZ());
				spawn.setHeading(player.getHeading());
				spawn.stopRespawn();
				
				final Npc npc = spawn.doSpawn(_isSummonSpawn);
				npc.setSummoner(player);
				npc.setName(npcTemplate.getName());
				npc.setTitle(npcTemplate.getName());
				if (_despawnDelay > 0)
				{
					npc.scheduleDespawn(_despawnDelay);
				}
				npc.broadcastInfo();
			}
		}
	}
}
