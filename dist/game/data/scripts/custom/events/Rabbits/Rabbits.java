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
package custom.events.Rabbits;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jmobius.Config;
import org.l2jmobius.commons.util.CommonUtil;
import org.l2jmobius.gameserver.model.WorldObject;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.quest.Event;
import org.l2jmobius.gameserver.model.skills.Skill;
import org.l2jmobius.gameserver.util.Broadcast;

/**
 * Rabbits event.<br>
 * Chests are hidden at Fantasy Isle and players must use the Rabbit transformation's skills to find and open them.
 * @author Gnacik, Zoey76
 */
public class Rabbits extends Event
{
	// NPCs
	private static final int NPC_MANAGER = 900101;
	private static final int CHEST = 900102;
	// Skills
	private static final SkillHolder RABBIT_MAGIC_EYE = new SkillHolder(629, 1);
	private static final SkillHolder RABBIT_TORNADO = new SkillHolder(630, 1);
	private static final SkillHolder RABBIT_TRANSFORMATION = new SkillHolder(2428, 1);
	private static final SkillHolder RAID_CURSE = new SkillHolder(4515, 1);
	// Misc
	private static final int EVENT_TIME = 10;
	private static final int TOTAL_CHEST_COUNT = 75;
	private static final int TRANSFORMATION_ID = 105;
	private final Set<Npc> _npcs = ConcurrentHashMap.newKeySet(TOTAL_CHEST_COUNT + 1);
	private final List<PlayerInstance> _players = new ArrayList<>();
	private boolean _isActive = false;
	
	/**
	 * Drop data:<br>
	 * Higher the chance harder the item.<br>
	 * ItemId, chance in percent, min amount, max amount
	 */
	// @formatter:off
	private static final int[][] DROPLIST =
	{
		{  1540,  80, 10, 15 },	// Quick Healing Potion
		{  1538,  60,  5, 10 },	// Blessed Scroll of Escape
		{  3936,  40,  5, 10 },	// Blessed Scroll of Ressurection
		{  6387,  25,  5, 10 },	// Blessed Scroll of Ressurection Pets
		{ 22025,  15,  5, 10 },	// Powerful Healing Potion
		{  6622,  10,  1, 1 },	// Giant's Codex
		{ 20034,   5,  1, 1 },	// Revita Pop
		{ 20004,   1,  1, 1 },	// Energy Ginseng
		{ 20004,   0,  1, 1 }	// Energy Ginseng
	};
	// @formatter:on
	
	private Rabbits()
	{
		addFirstTalkId(NPC_MANAGER, CHEST);
		addTalkId(NPC_MANAGER);
		addStartNpc(NPC_MANAGER);
		addSkillSeeId(CHEST);
		addAttackId(CHEST);
	}
	
	@Override
	public boolean eventStart(PlayerInstance eventMaker)
	{
		// Don't start event if its active
		if (_isActive)
		{
			eventMaker.sendMessage("Event " + getName() + " is already started!");
			return false;
		}
		
		// Check starting conditions
		if (!Config.CUSTOM_NPC_DATA)
		{
			LOGGER.info(getName() + ": Event can't be started, because custom NPCs are disabled!");
			eventMaker.sendMessage("Event " + getName() + " can't be started because custom NPCs are disabled!");
			return false;
		}
		
		// Set Event active
		_isActive = true;
		
		// Spawn Manager
		recordSpawn(_npcs, NPC_MANAGER, -59227, -56939, -2039, 64106, false, 0);
		// Spawn Chests
		for (int i = 0; i <= TOTAL_CHEST_COUNT; i++)
		{
			recordSpawn(_npcs, CHEST, getRandom(-60653, -58772), getRandom(-55830, -58146), -2030, 0, false, EVENT_TIME * 60000);
		}
		
		// Announce event start
		Broadcast.toAllOnlinePlayers("Rabbits Event: Chests spawned!");
		Broadcast.toAllOnlinePlayers("Rabbits Event: Go to Fantasy Isle and grab some rewards!");
		Broadcast.toAllOnlinePlayers("Rabbits Event: You have " + EVENT_TIME + " minutes!");
		Broadcast.toAllOnlinePlayers("Rabbits Event: After that time all chests will disappear...");
		// Schedule event end
		startQuestTimer("END_RABBITS_EVENT", EVENT_TIME * 60000, null, eventMaker);
		return true;
	}
	
	@Override
	public boolean eventStop()
	{
		// Don't stop inactive event
		if (!_isActive)
		{
			return false;
		}
		
		// Set inactive
		_isActive = false;
		
		// Cancel timer
		cancelQuestTimers("END_RABBITS_EVENT");
		
		// Despawn NPCs
		for (Npc npc : _npcs)
		{
			npc.deleteMe();
		}
		_npcs.clear();
		
		for (PlayerInstance player : _players)
		{
			if (player.getTransformationId() == TRANSFORMATION_ID)
			{
				player.untransform();
			}
		}
		_players.clear();
		
		// Announce event end
		Broadcast.toAllOnlinePlayers("Rabbits Event: Event has finished.");
		
		return true;
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "900101-1.htm":
			{
				htmltext = "900101-1.htm";
				break;
			}
			case "transform":
			{
				if (player.isTransformed() || player.isInStance())
				{
					player.untransform();
				}
				
				RABBIT_TRANSFORMATION.getSkill().applyEffects(npc, player);
				_players.add(player);
				break;
			}
			case "END_RABBITS_EVENT":
			{
				Broadcast.toAllOnlinePlayers("Rabbits Event: Time up!");
				eventStop();
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return npc.getId() + ".htm";
	}
	
	@Override
	public String onSkillSee(Npc npc, PlayerInstance caster, Skill skill, WorldObject[] targets, boolean isSummon)
	{
		if (skill.getId() == RABBIT_TORNADO.getSkillId())
		{
			if (!npc.isInvisible() && CommonUtil.contains(targets, npc))
			{
				dropItem(npc, caster, DROPLIST);
				npc.deleteMe();
				_npcs.remove(npc);
				
				if (_npcs.isEmpty())
				{
					Broadcast.toAllOnlinePlayers("Rabbits Event: No more chests...");
					eventStop();
				}
			}
		}
		else if ((skill.getId() == RABBIT_MAGIC_EYE.getSkillId()) && npc.isInvisible() && npc.isInsideRadius2D(caster, skill.getAffectRange()))
		{
			npc.setInvisible(false);
		}
		return super.onSkillSee(npc, caster, skill, targets, isSummon);
	}
	
	@Override
	public String onAttack(Npc npc, PlayerInstance attacker, int damage, boolean isSummon, Skill skill)
	{
		if (_isActive && ((skill == null) || (skill.getId() != RABBIT_TORNADO.getSkillId())))
		{
			RAID_CURSE.getSkill().applyEffects(npc, attacker);
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	private void dropItem(Npc npc, PlayerInstance player, int[][] droplist)
	{
		final int chance = getRandom(100);
		for (int[] drop : droplist)
		{
			if (chance > drop[1])
			{
				npc.dropItem(player, drop[0], getRandom(drop[2], drop[3]));
				return;
			}
		}
	}
	
	private void recordSpawn(Set<Npc> npcs, int npcId, int x, int y, int z, int heading, boolean randomOffSet, long despawnDelay)
	{
		final Npc npc = addSpawn(npcId, x, y, z, heading, randomOffSet, despawnDelay);
		if (npc.getId() == CHEST)
		{
			npc.setImmobilized(true);
			npc.disableCoreAI(true);
			npc.setInvisible(true);
		}
		npcs.add(npc);
	}
	
	@Override
	public boolean eventBypass(PlayerInstance player, String bypass)
	{
		return false;
	}
	
	public static void main(String[] args)
	{
		new Rabbits();
	}
}
