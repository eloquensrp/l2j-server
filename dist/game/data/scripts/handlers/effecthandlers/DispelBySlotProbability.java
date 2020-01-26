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

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.model.EffectList;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.conditions.Condition;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.effects.EffectType;
import org.l2jmobius.gameserver.model.skills.AbnormalType;
import org.l2jmobius.gameserver.model.skills.BuffInfo;

/**
 * Dispel By Slot Probability effect implementation.
 * @author Adry_85, Zoey76
 */
public class DispelBySlotProbability extends AbstractEffect
{
	private final String _dispel;
	private final Map<AbnormalType, Short> _dispelAbnormals;
	private final int _rate;
	
	public DispelBySlotProbability(Condition attachCond, Condition applyCond, StatSet set, StatSet params)
	{
		super(attachCond, applyCond, set, params);
		
		_dispel = params.getString("dispel", null);
		_rate = params.getInt("rate", 0);
		if ((_dispel != null) && !_dispel.isEmpty())
		{
			_dispelAbnormals = new EnumMap<>(AbnormalType.class);
			for (String ngtStack : _dispel.split(";"))
			{
				final String[] ngt = ngtStack.split(",");
				_dispelAbnormals.put(AbnormalType.getAbnormalType(ngt[0]), Short.MAX_VALUE);
			}
		}
		else
		{
			_dispelAbnormals = Collections.<AbnormalType, Short> emptyMap();
		}
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.DISPEL;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		if (_dispelAbnormals.isEmpty())
		{
			return;
		}
		
		final Creature effected = info.getEffected();
		final EffectList effectList = effected.getEffectList();
		// There is no need to iterate over all buffs,
		// Just iterate once over all slots to dispel and get the buff with that abnormal if exists,
		// Operation of O(n) for the amount of slots to dispel (which is usually small) and O(1) to get the buff.
		for (Entry<AbnormalType, Short> entry : _dispelAbnormals.entrySet())
		{
			if ((Rnd.get(100) < _rate))
			{
				// Dispel transformations (buff and by GM)
				if ((entry.getKey() == AbnormalType.TRANSFORM) && (effected.isTransformed() || (effected.isPlayer() || (entry.getValue() == effected.getActingPlayer().getTransformationId()) || (entry.getValue() < 0))))
				{
					info.getEffected().stopTransformation(true);
				}
				
				final BuffInfo toDispel = effectList.getBuffInfoByAbnormalType(entry.getKey());
				if (toDispel == null)
				{
					continue;
				}
				
				if ((toDispel.getSkill().getAbnormalType() == entry.getKey()) && (entry.getValue() >= toDispel.getSkill().getAbnormalLvl()))
				{
					effectList.stopSkillEffects(true, entry.getKey());
				}
			}
		}
	}
}
