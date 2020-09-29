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
package handlers.voicedcommandhandlers;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.handler.IVoicedCommandHandler;
import org.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import org.l2jmobius.gameserver.taskmanager.AutoPotionTaskManager;

/**
 * @author Mobius, Gigi
 */
public class AutoPotion implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"apon",
		"apoff"
	};
	
	@Override
	public boolean useVoicedCommand(String command, PlayerInstance activeChar, String target)
	{
		if (!Config.AUTO_POTIONS_ENABLED || (activeChar == null))
		{
			return false;
		}
		if (activeChar.getLevel() < Config.AUTO_POTION_MIN_LEVEL)
		{
			activeChar.sendMessage("You need to be at least " + Config.AUTO_POTION_MIN_LEVEL + " to use auto potions.");
			return false;
		}
		
		if (command.equals("apon"))
		{
			AutoPotionTaskManager.getInstance().add(activeChar);
			activeChar.sendMessage("Auto potions is enabled.");
			return true;
		}
		else if (command.equals("apoff"))
		{
			AutoPotionTaskManager.getInstance().remove(activeChar);
			activeChar.sendMessage("Auto potions is disabled.");
		}
		return false;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}