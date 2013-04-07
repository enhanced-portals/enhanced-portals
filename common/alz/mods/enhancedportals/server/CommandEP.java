package alz.mods.enhancedportals.server;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.Configuration;
import alz.mods.enhancedportals.reference.Reference;

public class CommandEP extends CommandBase
{
	// TODO
	// finish op commands

	@Override
	public String getCommandName()
	{
		return "ep";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender commandSender)
	{
		return super.canCommandSenderUseCommand(commandSender);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender commandSender, String[] args)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, new String[] { "teleporting", "dying", "dyingCosts", "borderBlock", "destroyBlock", "maxPortalSize", "pigmenSpawnChance" });
		}
		else if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("teleporting") || args[0].equalsIgnoreCase("dying") || args[0].equalsIgnoreCase("dyingCosts"))
			{
				return getListOfStringsMatchingLastWord(args, new String[] { "true", "false" });
			}
			else if (args[0].equalsIgnoreCase("borderBlock") || args[0].equalsIgnoreCase("destroyBlock"))
			{
				return getListOfStringsMatchingLastWord(args, new String[] { "add", "remove" });
			}
			else if (args[0].equalsIgnoreCase("maxPortalSize"))
			{
				return getListOfStringsMatchingLastWord(args, new String[] { "none", "25", "50", "100" });
			}
			else if (args[0].equalsIgnoreCase("pigmenSpawnChance"))
			{
				return getListOfStringsMatchingLastWord(args, new String[] { "0", "25", "50", "75", "100", "none", "normal" });
			}
		}

		return null;
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] args)
	{
		if (args.length > 0)
		{
			String command = args[0];

			if (command.equalsIgnoreCase("teleporting"))
			{
				if (args.length == 2)
				{
					if (args[1].equalsIgnoreCase("true"))
					{
						Reference.Settings.AllowTeleporting = true;
						Reference.Settings.updateSetting(Configuration.CATEGORY_GENERAL, "AllowTeleporting", true);
					}
					else if (args[1].equalsIgnoreCase("false"))
					{
						Reference.Settings.AllowTeleporting = false;
						Reference.Settings.updateSetting(Configuration.CATEGORY_GENERAL, "AllowTeleporting", false);
					}
				}

				if (args.length >= 1)
				{
					commandSender.sendChatToPlayer("Teleporting is " + EnumChatFormatting.GOLD + (Reference.Settings.AllowTeleporting ? "enabled" : "disabled"));
				}
			}
			else if (command.equalsIgnoreCase("dying"))
			{
				if (args.length == 2)
				{
					if (args[1].equalsIgnoreCase("true"))
					{
						Reference.Settings.CanDyePortals = true;
						Reference.Settings.CanDyeByThrowing = true;
						Reference.Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CanDyePortals", true);
						Reference.Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CanDyeByThrowing", true);
					}
					else if (args[1].equalsIgnoreCase("false"))
					{
						Reference.Settings.CanDyePortals = false;
						Reference.Settings.CanDyeByThrowing = false;
						Reference.Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CanDyePortals", false);
						Reference.Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CanDyeByThrowing", false);
					}
				}

				if (args.length >= 1)
				{
					commandSender.sendChatToPlayer("Dying portals is " + EnumChatFormatting.GOLD + (Reference.Settings.CanDyePortals ? "enabled" : "disabled"));
				}
			}			
			else if (command.equalsIgnoreCase("dyingCosts"))
			{
				if (args.length == 2)
				{
					if (args[1].equalsIgnoreCase("true"))
					{
						Reference.Settings.DoesDyingCost = true;
						Reference.Settings.updateSetting(Configuration.CATEGORY_GENERAL, "DoesDyingCost", true);
					}
					else if (args[1].equalsIgnoreCase("false"))
					{
						Reference.Settings.DoesDyingCost = false;
						Reference.Settings.updateSetting(Configuration.CATEGORY_GENERAL, "DoesDyingCost", false);
					}
				}

				if (args.length >= 1)
				{
					commandSender.sendChatToPlayer("Dying portals " + EnumChatFormatting.GOLD + (Reference.Settings.DoesDyingCost ? "does" : "does not") + EnumChatFormatting.WHITE + " consume the dye");
				}
			}
		}
	}
}
