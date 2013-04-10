package alz.mods.enhancedportals.server;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.Configuration;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Settings;

public class CommandEP extends CommandBase
{
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
                return getListOfStringsMatchingLastWord(args, new String[] { "add", "remove", "list" });
            }
            else if (args[0].equalsIgnoreCase("maxPortalSize"))
            {
                return getListOfStringsMatchingLastWord(args, new String[] { "noLimit", "25", "50", "100" });
            }
            else if (args[0].equalsIgnoreCase("pigmenSpawnChance"))
            {
                return getListOfStringsMatchingLastWord(args, new String[] { "0", "25", "50", "75", "100" });
            }
        }

        return null;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender commandSender)
    {
        return super.canCommandSenderUseCommand(commandSender);
    }

    @Override
    public String getCommandName()
    {
        return "ep";
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
                        Settings.AllowTeleporting = true;
                        Settings.updateSetting(Configuration.CATEGORY_GENERAL, "AllowTeleporting", true);
                    }
                    else if (args[1].equalsIgnoreCase("false"))
                    {
                        Settings.AllowTeleporting = false;
                        Settings.updateSetting(Configuration.CATEGORY_GENERAL, "AllowTeleporting", false);
                    }
                }

                if (args.length >= 1)
                {
                    commandSender.sendChatToPlayer("Teleporting is " + EnumChatFormatting.GOLD + (Settings.AllowTeleporting ? "enabled" : "disabled"));
                }
            }
            else if (command.equalsIgnoreCase("dying"))
            {
                if (args.length == 2)
                {
                    if (args[1].equalsIgnoreCase("true"))
                    {
                        Settings.CanDyePortals = true;
                        Settings.CanDyeByThrowing = true;
                        Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CanDyePortals", true);
                        Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CanDyeByThrowing", true);
                    }
                    else if (args[1].equalsIgnoreCase("false"))
                    {
                        Settings.CanDyePortals = false;
                        Settings.CanDyeByThrowing = false;
                        Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CanDyePortals", false);
                        Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CanDyeByThrowing", false);
                    }
                }

                if (args.length >= 1)
                {
                    commandSender.sendChatToPlayer("Dying portals is " + EnumChatFormatting.GOLD + (Settings.CanDyePortals ? "enabled" : "disabled"));
                }
            }
            else if (command.equalsIgnoreCase("dyingCosts"))
            {
                if (args.length == 2)
                {
                    if (args[1].equalsIgnoreCase("true"))
                    {
                        Settings.DoesDyingCost = true;
                        Settings.updateSetting(Configuration.CATEGORY_GENERAL, "DoesDyingCost", true);
                    }
                    else if (args[1].equalsIgnoreCase("false"))
                    {
                        Settings.DoesDyingCost = false;
                        Settings.updateSetting(Configuration.CATEGORY_GENERAL, "DoesDyingCost", false);
                    }
                }

                if (args.length >= 1)
                {
                    commandSender.sendChatToPlayer("Dying portals " + EnumChatFormatting.GOLD + (Settings.DoesDyingCost ? "does" : "does not") + EnumChatFormatting.WHITE + " consume the dye");
                }
            }
            else if (command.equalsIgnoreCase("borderBlock"))
            {
                if (args.length == 2)
                {
                    if (args[1].equalsIgnoreCase("list"))
                    {
                        String theList = "";
                        
                        for (int id : Settings.BorderBlocks)
                        {
                            if (id == 0 || id == Reference.BlockIDs.DialDevice || id == Reference.BlockIDs.Obsidian || id == Reference.BlockIDs.PortalModifier || id == Reference.BlockIDs.ObsidianStairs)
                            {
                                continue;
                            }
                            
                            theList += id + ", ";
                        }
                        
                        if (theList.length() > 0)
                        {
                            theList = theList.substring(0, theList.length() - 2);
                            commandSender.sendChatToPlayer("Custom border blocks: " + theList);
                        }
                        else
                        {
                            commandSender.sendChatToPlayer("There are no custom border blocks");
                        }
                    }
                    else if (args[1].equalsIgnoreCase("reset"))
                    {
                        Settings.BorderBlocks = Settings.BorderBlocks_Default;
                        Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CustomBorderBlocks", Settings.makeString(Settings.BorderBlocks));
                        commandSender.sendChatToPlayer("Border blocks have been reset succesfully.");
                    }
                    else if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))
                    {
                        commandSender.sendChatToPlayer("You need to specifiy a block ID. Ex: borderBlock " + args[1] + " 2");
                    }
                }
                else if (args.length == 3)
                {
                    if (args[1].equalsIgnoreCase("add"))
                    {
                        try
                        {
                            int id = Integer.parseInt(args[2]);
                            
                            if (Settings.BorderBlocks.contains(id))
                            {
                                commandSender.sendChatToPlayer("Could not add block ID " + EnumChatFormatting.GOLD + id);
                                return;
                            }
                            
                            if (Settings.BorderBlocks.add(id))
                            {
                                commandSender.sendChatToPlayer("Successfully added block ID " + EnumChatFormatting.GOLD + id);
                                Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CustomBorderBlocks", Settings.makeString(Settings.BorderBlocks));
                            }
                            else
                            {
                                commandSender.sendChatToPlayer("Could not add block ID " + EnumChatFormatting.GOLD + id);
                            }
                        }
                        catch (Exception e)
                        {
                            commandSender.sendChatToPlayer("Invalid block ID");
                        }
                    }
                    else if (args[1].equalsIgnoreCase("remove"))
                    {
                        try
                        {
                            int id = Integer.parseInt(args[2]);
                            
                            if (Settings.BorderBlocks.remove((Object)id))
                            {
                                commandSender.sendChatToPlayer("Removed block ID " + EnumChatFormatting.GOLD + id + EnumChatFormatting.WHITE + " successfully");
                                Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CustomBorderBlocks", Settings.makeString(Settings.BorderBlocks));
                            }
                            else
                            {
                                commandSender.sendChatToPlayer("Failed to remove block ID " + EnumChatFormatting.GOLD + id);
                            }
                        }
                        catch (Exception e)
                        {
                            commandSender.sendChatToPlayer("Invalid block ID");
                        }
                    }
                }
                else
                {
                    commandSender.sendChatToPlayer("Usage: borderBlock add/remove blockID");
                }
            }
            else if (command.equalsIgnoreCase("destroyBlock"))
            {
                if (args.length == 2)
                {
                    if (args[1].equalsIgnoreCase("list"))
                    {
                        String theList = "";
                        
                        for (int id : Settings.RemovableBlocks)
                        {                            
                            theList += id + ", ";
                        }
                        
                        if (theList.length() > 0)
                        {
                            theList = theList.substring(0, theList.length() - 2);
                            commandSender.sendChatToPlayer("Custom removable blocks: " + theList);
                        }
                        else if (args[1].equalsIgnoreCase("reset"))
                        {
                            Settings.RemovableBlocks = Settings.RemovableBlocks_Default;
                            Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CustomDestroyBlocks", Settings.makeString(Settings.RemovableBlocks));
                            commandSender.sendChatToPlayer("Border blocks have been reset succesfully.");
                        }
                        else
                        {
                            commandSender.sendChatToPlayer("There are no custom removable blocks");
                        }
                    }
                    else if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))
                    {
                        commandSender.sendChatToPlayer("You need to specifiy a block ID. Ex: destroyBlock " + args[1] + " 2");
                    }
                }
                else if (args.length == 3)
                {
                    if (args[1].equalsIgnoreCase("add"))
                    {
                        try
                        {
                            int id = Integer.parseInt(args[2]);
                            
                            if (Settings.RemovableBlocks.contains(id))
                            {
                                commandSender.sendChatToPlayer("Could not add block ID " + EnumChatFormatting.GOLD + id);
                                return;
                            }
                            
                            if (Settings.RemovableBlocks.add(id))
                            {
                                commandSender.sendChatToPlayer("Successfully added block ID " + EnumChatFormatting.GOLD + id);
                                Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CustomDestroyBlocks", Settings.makeString(Settings.RemovableBlocks));
                            }
                            else
                            {
                                commandSender.sendChatToPlayer("Could not add block ID " + EnumChatFormatting.GOLD + id);
                            }
                        }
                        catch (Exception e)
                        {
                            commandSender.sendChatToPlayer("Invalid block ID");
                        }
                    }
                    else if (args[1].equalsIgnoreCase("remove"))
                    {
                        try
                        {
                            int id = Integer.parseInt(args[2]);
                            
                            if (Settings.RemovableBlocks.remove((Object)id))
                            {
                                commandSender.sendChatToPlayer("Removed block ID " + EnumChatFormatting.GOLD + id + EnumChatFormatting.WHITE + " successfully");
                                Settings.updateSetting(Configuration.CATEGORY_GENERAL, "CustomDestroyBlocks", Settings.makeString(Settings.RemovableBlocks));
                            }
                            else
                            {
                                commandSender.sendChatToPlayer("Failed to remove block ID " + EnumChatFormatting.GOLD + id);
                            }
                        }
                        catch (Exception e)
                        {
                            commandSender.sendChatToPlayer("Invalid block ID");
                        }
                    }
                }
                else
                {
                    commandSender.sendChatToPlayer("Usage: destroyBlock add/remove blockID");
                }
            }
        }
    }
}