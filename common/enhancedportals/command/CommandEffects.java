package enhancedportals.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import enhancedportals.lib.Commands;
import enhancedportals.lib.Settings;
import enhancedportals.lib.Strings;

public class CommandEffects
{
    @SuppressWarnings("rawtypes")
    public static List addTabCompletionOptions(ICommandSender par1iCommandSender, String[] args)
    {
        if (args.length == 3)
        {
            if (args[1].equalsIgnoreCase(Commands.PARTICLES) || args[1].equalsIgnoreCase(Commands.SOUNDS) || args[1].equalsIgnoreCase(Commands.PORTAL_EFFECTS))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, new String[] { Commands.ON, Commands.OFF });
            }
        }

        return null;
    }

    public static void processCommand(ICommandSender sender, String[] args)
    {
        if (args[1].equalsIgnoreCase(Commands.PARTICLES))
        {
            if (args[2].equalsIgnoreCase(Commands.ON))
            {
                Settings.ParticleLevel = 100;
                Settings.setConfigOption("Effects", "Particles", 100);
                //sender.sendChatToPlayer(Strings.ChatParticlesOn.toString());
            }
            else if (args[2].equalsIgnoreCase(Commands.OFF))
            {
                Settings.ParticleLevel = 0;
                Settings.setConfigOption("Effects", "Particles", 0);
                //sender.sendChatToPlayer(Strings.ChatParticlesOff.toString());
            }
            else
            {
                throw new WrongUsageException(Commands.ONOFF_USAGE, new Object[] { Commands.EFFECTS + " " + Commands.PARTICLES });
            }
        }
        else if (args[1].equalsIgnoreCase(Commands.SOUNDS))
        {
            if (args[2].equalsIgnoreCase(Commands.ON))
            {
                Settings.SoundLevel = 100;
                Settings.setConfigOption("Effects", "Sounds", 100);
                //sender.sendChatToPlayer(Strings.ChatSoundsOn.toString());
            }
            else if (args[2].equalsIgnoreCase(Commands.OFF))
            {
                Settings.SoundLevel = 0;
                Settings.setConfigOption("Effects", "Sounds", 0);
                //sender.sendChatToPlayer(Strings.ChatSoundsOff.toString());
            }
            else
            {
                throw new WrongUsageException(Commands.ONOFF_USAGE, new Object[] { Commands.EFFECTS + " " + Commands.SOUNDS });
            }
        }
        else if (args[1].equalsIgnoreCase(Commands.PORTAL_EFFECTS))
        {
            if (args[2].equalsIgnoreCase(Commands.ON))
            {
                Settings.RenderPortalEffect = true;
                Settings.setConfigOption("Effects", "RenderPortalEffect", true);
                //sender.sendChatToPlayer(Strings.ChatPortalEffectsOn.toString());
            }
            else if (args[2].equalsIgnoreCase(Commands.OFF))
            {
                Settings.RenderPortalEffect = false;
                Settings.setConfigOption("Effects", "RenderPortalEffect", false);
                //sender.sendChatToPlayer(Strings.ChatPortalEffectsOff.toString());
            }
            else
            {
                throw new WrongUsageException(Commands.ONOFF_USAGE, new Object[] { Commands.EFFECTS + " " + Commands.PORTAL_EFFECTS });
            }
        }
        else
        {
            throw new WrongUsageException(Commands.EFFECTS_USAGE, new Object[0]);
        }
    }
}
