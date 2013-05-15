package enhancedportals.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import enhancedportals.lib.Commands;

public class CommandBlocks
{
    public static void processCommand(ICommandSender sender, String[] args)
    {
        if (args[0].equalsIgnoreCase(Commands.BORDER_BLOCKS))
        {            
            if (args[1].equalsIgnoreCase(Commands.ADD))
            {
                
            }
            else if (args[1].equalsIgnoreCase(Commands.REMOVE))
            {
                
            }
            else if (args[1].equalsIgnoreCase(Commands.CLEAR))
            {
                
            }
            else if (args[1].equalsIgnoreCase(Commands.RESET))
            {
                
            }
            else
            {
                throw new WrongUsageException(Commands.BORDER_BLOCKS_USAGE, new Object[0]);
            }
        }
        else if (args[0].equalsIgnoreCase(Commands.DESTROY_BLOCKS))
        {
            if (args[1].equalsIgnoreCase(Commands.ADD))
            {
                
            }
            else if (args[1].equalsIgnoreCase(Commands.REMOVE))
            {
                
            }
            else if (args[1].equalsIgnoreCase(Commands.CLEAR))
            {
                
            }
            else if (args[1].equalsIgnoreCase(Commands.RESET))
            {
                
            }
            else
            {
                throw new WrongUsageException(Commands.DESTROY_BLOCKS_USAGE, new Object[0]);
            }
        }
        else
        {
            throw new WrongUsageException(Commands.BLOCKS_USAGE, new Object[0]);
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static List addTabCompletionOptions(ICommandSender par1iCommandSender, String[] args)
    {
        System.out.println(args.length);
        
        if (args.length == 3)
        {
            if (args[1].equalsIgnoreCase(Commands.BORDER_BLOCKS) || args[1].equalsIgnoreCase(Commands.DESTROY_BLOCKS))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, new String[] { Commands.ADD, Commands.REMOVE, Commands.RESET, Commands.CLEAR });
            }
        }
        
        return null;
    }
}
