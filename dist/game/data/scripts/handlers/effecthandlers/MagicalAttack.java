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
import com.l2jmobius.gameserver.enums.ShotType;
import com.l2jmobius.gameserver.model.StatsSet;
import com.l2jmobius.gameserver.model.actor.Creature;
import com.l2jmobius.gameserver.model.conditions.Condition;
import com.l2jmobius.gameserver.model.effects.AbstractEffect;
import com.l2jmobius.gameserver.model.effects.EffectType;
import com.l2jmobius.gameserver.model.skills.BuffInfo;
import com.l2jmobius.gameserver.model.stats.Formulas;
import com.l2jmobius.gameserver.model.stats.Stats;

/**
 * Magical Attack effect implementation.
 * @author Adry_85
 */
public final class MagicalAttack extends AbstractEffect
{
	public MagicalAttack(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params)
	{
		super(attachCond, applyCond, set, params);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.MAGICAL_ATTACK;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		final Creature target = info.getEffected();
		final Creature creature = info.getEffector();
		
		// TODO: Unhardcode Cubic Skill to avoid double damage
		if (creature.isAlikeDead() || (info.getSkill().getId() == 4049))
		{
			return;
		}
		
		if (target.isPlayer() && target.getActingPlayer().isFakeDeath())
		{
			target.stopFakeDeath(true);
		}
		
		final boolean sps = info.getSkill().useSpiritShot() && creature.isChargedShot(ShotType.SPIRITSHOTS);
		final boolean bss = info.getSkill().useSpiritShot() && creature.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		final boolean mcrit = Formulas.calcMCrit(creature.getMCriticalHit(target, info.getSkill()));
		final byte shld = Formulas.calcShldUse(creature, target, info.getSkill());
		final int damage = (int) Formulas.calcMagicDam(creature, target, info.getSkill(), shld, sps, bss, mcrit);
		
		if (damage > 0)
		{
			// Manage attack or cast break of the target (calculating rate, sending message...)
			if (!target.isRaid() && Formulas.calcAtkBreak(target, damage))
			{
				target.breakAttack();
				target.breakCast();
			}
			
			// Shield Deflect Magic: Reflect all damage on caster.
			if (target.getStat().calcStat(Stats.VENGEANCE_SKILL_MAGIC_DAMAGE, 0, target, info.getSkill()) > Rnd.get(100))
			{
				creature.reduceCurrentHp(damage, target, info.getSkill());
				creature.notifyDamageReceived(damage, target, info.getSkill(), mcrit, false);
			}
			else
			{
				target.reduceCurrentHp(damage, creature, info.getSkill());
				target.notifyDamageReceived(damage, creature, info.getSkill(), mcrit, false);
				creature.sendDamageMessage(target, damage, mcrit, false, false);
			}
		}
		
		if (info.getSkill().isSuicideAttack())
		{
			creature.doDie(creature);
		}
	}
}