package enhancedportals.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.EnumChatFormatting;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Commands;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Settings;

public class CommandBlocks
{
    @SuppressWarnings("rawtypes")
    public static List addTabCompletionOptions(ICommandSender par1iCommandSender, String[] args)
    {
        System.out.println(args.length);

        if (args.length == 3)
        {
            if (args[1].equalsIgnoreCase(Commands.BORDER_BLOCKS) || args[1].equalsIgnoreCase(Commands.DESTROY_BLOCKS))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, new String[] { Commands.ADD, Commands.REMOVE, Commands.RESET, Commands.CLEAR, Commands.LIST });
            }
        }

        return null;
    }

    public static void processCommand(ICommandSender sender, String[] args)
    {
        if (args[1].equalsIgnoreCase(Commands.BORDER_BLOCKS))
        {
            if (args[2].equalsIgnoreCase(Commands.ADD))
            {
                if (args.length == 4)
                {
                    try
                    {
                        int blockid = Integer.parseInt(args[3]);

                        if (Settings.BorderBlocks.contains(blockid))
                        {
                            sender.sendChatToPlayer(EnumChatFormatting.RED + "Block ID already exists.");
                            return;
                        }

                        Settings.BorderBlocks.add(blockid);

                        sender.sendChatToPlayer(String.format(Localization.localizeString(Commands.BORDER_BLOCKS_ADD_SUCCESS), blockid));
                    }
                    catch (Exception e)
                    {
                        throw new WrongUsageException(Commands.BLOCKID_USAGE, new Object[] { Commands.BLOCKS + " " + Commands.BORDER_BLOCKS + " " + Commands.ADD });
                    }
                }
                else
                {
                    throw new WrongUsageException(Commands.BORDER_BLOCKS_USAGE, new Object[0]);
                }
            }
            else if (args[2].equalsIgnoreCase(Commands.REMOVE))
            {
                if (args.length == 4)
                {
                    try
                    {
                        int blockid = Integer.parseInt(args[3]);

                        if (!Settings.BorderBlocks.contains(blockid))
                        {
                            sender.sendChatToPlayer(EnumChatFormatting.RED + "Block isn't in the list.");
                            return;
                        }

                        Settings.BorderBlocks.remove((Object) blockid);

                        sender.sendChatToPlayer(String.format(Localization.localizeString(Commands.BORDER_BLOCKS_REMOVE_SUCCESS), blockid));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        throw new WrongUsageException(Commands.BLOCKID_USAGE, new Object[] { Commands.BLOCKS + " " + Commands.BORDER_BLOCKS + " " + Commands.REMOVE });
                    }
                }
                else
                {
                    throw new WrongUsageException(Commands.BORDER_BLOCKS_USAGE, new Object[0]);
                }
            }
            else if (args[2].equalsIgnoreCase(Commands.CLEAR))
            {
                Settings.BorderBlocks.clear();
                Settings.BorderBlocks.add(BlockIds.Obsidian);
                Settings.BorderBlocks.add(BlockIds.PortalModifier);

                sender.sendChatToPlayer(Localization.localizeString(Commands.BORDER_BLOCKS_CLEAR));
            }
            else if (args[2].equalsIgnoreCase(Commands.RESET))
            {
                Settings.BorderBlocks.clear();
                Settings.BorderBlocks.add(BlockIds.Obsidian);
                Settings.BorderBlocks.add(BlockIds.PortalModifier);
                Settings.BorderBlocks.add(BlockIds.ObsidianStairs);

                sender.sendChatToPlayer(Localization.localizeString(Commands.BORDER_BLOCKS_RESET));
            }
            else if (args[2].equalsIgnoreCase(Commands.LIST))
            {
                if (Settings.BorderBlocks.size() <= 2)
                {
                    sender.sendChatToPlayer(EnumChatFormatting.RED + "There are no custom block IDs.");
                    return;
                }

                String str = "";

                for (int i : Settings.BorderBlocks)
                {
                    if (i == BlockIds.Obsidian || i == BlockIds.PortalModifier)
                    {
                        continue;
                    }

                    str = str + ", " + i;
                }

                str = str.substring(2);

                sender.sendChatToPlayer("Block IDs: " + str);
            }
            else
            {
                throw new WrongUsageException(Commands.BORDER_BLOCKS_USAGE, new Object[0]);
            }
        }
        else if (args[1].equalsIgnoreCase(Commands.DESTROY_BLOCKS))
        {
            if (args[2].equalsIgnoreCase(Commands.ADD))
            {

            }
            else if (args[2].equalsIgnoreCase(Commands.REMOVE))
            {

            }
            else if (args[2].equalsIgnoreCase(Commands.CLEAR))
            {

            }
            else if (args[2].equalsIgnoreCase(Commands.RESET))
            {

            }
            else if (args[2].equalsIgnoreCase(Commands.LIST))
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
}
