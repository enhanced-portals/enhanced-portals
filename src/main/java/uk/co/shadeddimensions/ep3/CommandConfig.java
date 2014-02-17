package uk.co.shadeddimensions.ep3;

import uk.co.shadeddimensions.ep3.network.GuiHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandConfig extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "ep3cfg";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return null;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        ((EntityPlayer) icommandsender).openGui(EnhancedPortals.instance, GuiHandler.CONFIG, ((EntityPlayer) icommandsender).worldObj, 0, 0, 0);
    }

    @Override
    public int compareTo(Object o)
    {
        return 0;
    }
}
