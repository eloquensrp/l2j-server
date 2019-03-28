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
package com.l2jmobius.gameserver.model.actor.status;

import com.l2jmobius.gameserver.ai.CtrlEvent;
import com.l2jmobius.gameserver.model.actor.Creature;
import com.l2jmobius.gameserver.model.actor.instance.PetInstance;
import com.l2jmobius.gameserver.network.SystemMessageId;
import com.l2jmobius.gameserver.network.serverpackets.SystemMessage;

public class PetStatus extends SummonStatus
{
	private int _currentFed = 0; // Current Fed of the PetInstance
	
	public PetStatus(PetInstance activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public final void reduceHp(double value, Creature attacker)
	{
		reduceHp(value, attacker, true, false, false);
	}
	
	@Override
	public final void reduceHp(double value, Creature attacker, boolean awake, boolean isDOT, boolean isHpConsumption)
	{
		if (getActiveChar().isDead())
		{
			return;
		}
		
		super.reduceHp(value, attacker, awake, isDOT, isHpConsumption);
		
		if (attacker == null)
		{
			return;
		}
		
		if (!isDOT && (getActiveChar().getOwner() != null))
		{
			final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOUR_PET_RECEIVED_S2_DAMAGE_BY_C1);
			sm.addString(attacker.getName());
			sm.addInt((int) value);
			getActiveChar().sendPacket(sm);
		}
		getActiveChar().getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, attacker);
	}
	
	public int getCurrentFed()
	{
		return _currentFed;
	}
	
	public void setCurrentFed(int value)
	{
		_currentFed = value;
	}
	
	@Override
	public PetInstance getActiveChar()
	{
		return (PetInstance) super.getActiveChar();
	}
}
