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

import java.util.ArrayList;
import java.util.List;

import org.l2jmobius.gameserver.model.TimeStamp;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * Skill Cool Time server packet implementation.
 * @author KenM, Zoey76, Mobius
 */
public class SkillCoolTime extends ServerPacket
{
	private final long _currentTime;
	private final List<TimeStamp> _skillReuseTimeStamps = new ArrayList<>();
	
	public SkillCoolTime(Player player)
	{
		_currentTime = System.currentTimeMillis();
		for (TimeStamp ts : player.getSkillReuseTimeStamps().values())
		{
			if (_currentTime < ts.getStamp())
			{
				_skillReuseTimeStamps.add(ts);
			}
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.SKILL_COOL_TIME.writeId(this);
		writeInt(_skillReuseTimeStamps.size());
		for (TimeStamp ts : _skillReuseTimeStamps)
		{
			writeInt(ts.getSkillId());
			writeInt(ts.getSkillLevel());
			writeInt((int) ts.getReuse() / 1000);
			writeInt((int) Math.max(ts.getStamp() - _currentTime, 0) / 1000);
		}
	}
}
