package enhancedportals.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import enhancedportals.lib.Commands;

public class CommandEP extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "ep";
    }
    
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1iCommandSender)
    {
        return super.canCommandSenderUseCommand(par1iCommandSender);
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] args)
    {
        if (args.length > 0)
        {
            String commandName = args[0];
            
            if (commandName.equalsIgnoreCase(Commands.EFFECTS))
            {
                CommandEffects.processCommand(icommandsender, args);
            }
            else if (commandName.equalsIgnoreCase(Commands.BLOCKS))
            {
                CommandBlocks.processCommand(icommandsender, args);
            }
            else if (commandName.equalsIgnoreCase(Commands.CONTROL))
            {
                
            }
            else
            {
                throw new WrongUsageException(Commands.USAGE, new Object[0]);
            }
        }
        else
        {
            throw new WrongUsageException(Commands.USAGE, new Object[0]);
        }
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender par1iCommandSender, String[] args)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, new String[] { Commands.EFFECTS, Commands.BLOCKS, Commands.CONTROL });
        }
        else if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase(Commands.EFFECTS))
            {
                return getListOfStringsMatchingLastWord(args, new String[] { Commands.PARTICLES, Commands.SOUNDS, Commands.PORTAL_EFFECTS });
            }
            else if (args[0].equalsIgnoreCase(Commands.BLOCKS))
            {
                return getListOfStringsMatchingLastWord(args, new String[] { Commands.BORDER_BLOCKS, Commands.DESTROY_BLOCKS });
            }
            else if (args[0].equalsIgnoreCase(Commands.CONTROL))
            {
                return getListOfStringsMatchingLastWord(args, new String[] { });
            }
        }
        else if (args.length >= 3)
        {
            if (args[0].equalsIgnoreCase(Commands.EFFECTS))
            {
                return CommandEffects.addTabCompletionOptions(par1iCommandSender, args);
            }
            else if (args[0].equalsIgnoreCase(Commands.BLOCKS))
            {
                return CommandBlocks.addTabCompletionOptions(par1iCommandSender, args);
            }
        }
        
        return null;
    }
        
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public List getCommandAliases()
    {
        List list = new ArrayList();
        list.add("enhancedportals");
        return list;
    }
}
