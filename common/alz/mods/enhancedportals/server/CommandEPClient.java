package alz.mods.enhancedportals.server;

import java.util.List;

import alz.mods.enhancedportals.reference.Settings;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.Configuration;

public class CommandEPClient extends CommandBase
{
    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender commandSender, String[] args)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, new String[] { "soundFrequency", "particleFrequency", "version", "inPortalEffect" });
        }
        else if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("soundFrequency") || args[0].equalsIgnoreCase("particleFrequency"))
            {
                return getListOfStringsMatchingLastWord(args, new String[] { "0", "25", "50", "75", "100" });
            }
            else if (args[0].equalsIgnoreCase("inPortalEffect"))
            {
                return getListOfStringsMatchingLastWord(args, new String[] { "true", "false" });
            }
        }

        return null;
    }

    // TODO
    // commands for the client
    // change sound frequency
    // change portal frequency
    // see version number

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1iCommandSender)
    {
        return true;
    }

    @Override
    public String getCommandName()
    {
        return "epc";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args)
    {
        if (args.length > 0)
        {
            String command = args[0];

            if (command.equalsIgnoreCase("inPortalEffect"))
            {
                if (args.length == 2)
                {
                    if (args[1].equalsIgnoreCase("true"))
                    {
                        Settings.RenderPortalEffects = true;
                        Settings.updateSetting(Configuration.CATEGORY_GENERAL, "RenderPortalEffects", true);
                    }
                    else if (args[1].equalsIgnoreCase("false"))
                    {
                        Settings.RenderPortalEffects = false;
                        Settings.updateSetting(Configuration.CATEGORY_GENERAL, "RenderPortalEffects", false);
                    }
                }

                if (args.length >= 1)
                {
                    commandSender.sendChatToPlayer("Portal effects are " + EnumChatFormatting.GOLD + (Settings.RenderPortalEffects ? "enabled" : "disabled"));
                }
            }
        }
    }
}
