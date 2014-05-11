package enhancedportals.tileentity.portal;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.GuiHandler;
import enhancedportals.script.ScriptCommon;

public class TileProgrammableInterface extends TileFrame
{
    ScriptEngineManager scriptManager = new ScriptEngineManager();
    ScriptEngine scriptEngine = scriptManager.getEngineByName("JavaScript");
    CompiledScript script;

    @Override
    public void addDataToPacket(NBTTagCompound tag)
    {

    }

    @Override
    public void onDataPacket(NBTTagCompound tag)
    {

    }

    public void updateScript(String s) throws ScriptException
    {
        script = ((Compilable) scriptEngine).compile(s);
        script.eval();
    }

    public boolean canEntityTeleport(Entity entity)
    {
        try
        {
            return Boolean.parseBoolean(((Invocable) scriptEngine).invokeFunction("canEntityTeleport", entity.getEntityId()).toString());
        }
        catch (NoSuchMethodException e)
        {
            return true;
        }
        catch (ScriptException e)
        {
            // TODO LOG
            e.printStackTrace();
        }

        return false;
    }

    public void entityEnter(Entity entity)
    {
        try
        {
            Boolean.parseBoolean(((Invocable) scriptEngine).invokeFunction("entityEnter", entity.getEntityId()).toString());
        }
        catch (NoSuchMethodException e)
        {
            // Do nothing
        }
        catch (ScriptException e)
        {
            // TODO LOG
        }
    }

    public void entityExit(Entity entity)
    {
        try
        {
            Boolean.parseBoolean(((Invocable) scriptEngine).invokeFunction("entityExit", entity.getEntityId()).toString());
        }
        catch (NoSuchMethodException e)
        {
            // Do nothing
        }
        catch (ScriptException e)
        {
            // TODO LOG
        }
    }

    public void portalCreated()
    {
        try
        {
            Boolean.parseBoolean(((Invocable) scriptEngine).invokeFunction("portalCreated").toString());
        }
        catch (NoSuchMethodException e)
        {
            // Do nothing
        }
        catch (ScriptException e)
        {
            // TODO LOG
        }
    }

    public void portalRemoved()
    {
        try
        {
            Boolean.parseBoolean(((Invocable) scriptEngine).invokeFunction("portalRemoved").toString());
        }
        catch (NoSuchMethodException e)
        {
            // Do nothing
        }
        catch (ScriptException e)
        {
            // TODO LOG
        }
    }

    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
        TileController controller = getPortalController();

        if (worldObj.isRemote)
        {
            return controller != null;
        }

        if (controller != null && controller.isFinalized())
        {
            player.openGui(EnhancedPortals.instance, GuiHandler.PROGRAMMABLE_INTERFACE, worldObj, xCoord, yCoord, zCoord);
            return true;
        }

        return false;
    }
}
