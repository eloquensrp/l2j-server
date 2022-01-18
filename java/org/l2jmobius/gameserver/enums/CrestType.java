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
package org.l2jmobius.gameserver.enums;

public enum CrestType
{
	PLEDGE(1),
	PLEDGE_LARGE(2),
	ALLY(3);
	
	private final int _id;
	
	CrestType(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public static CrestType getById(int id)
	{
		for (CrestType crestType : values())
		{
			if (crestType.getId() == id)
			{
				return crestType;
			}
		}
		return null;
	}
}