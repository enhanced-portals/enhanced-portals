package enhancedportals.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.GuiHandler;
import enhancedportals.utility.GeneralUtils;

public class TileProgrammableInterface extends TileFrame
{
    // public LuaScriptEngine scriptEngine = new LuaScriptEngine();
    // public Bindings scriptBindings = scriptEngine.createBindings();
    //public CompiledScript script;
    //public String scriptData;

    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
        if (GeneralUtils.isWrench(player.inventory.getCurrentItem()) && !player.isSneaking())
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
        }

        return false;
    }

    @Override
    public void addDataToPacket(NBTTagCompound tag)
    {
        // tag.setString("script", scriptData == null ? " " : scriptData);
    }

    /**
     * 0 = No, 1 = Yes, -1 = Not yet, try again later
     * 
     * @param entity
     * @return
     */
    public byte canEntityTeleport(Entity entity)
    {
        /*
         * handleBindings(entity); LuaFunction luaFunc = (LuaFunction) scriptBindings.get("canTeleport");
         * 
         * if (luaFunc == null) { return 1; }
         * 
         * LuaValue result = luaFunc.call(); return (byte) (result.checkboolean() ? 1 : 0);
         */
        return 1;
    }

    public void entityEnter(Entity entity)
    {
        /*
         * handleBindings(entity); LuaFunction luaFunc = (LuaFunction) scriptBindings.get("onEnter");
         * 
         * if (luaFunc == null) { return; }
         * 
         * luaFunc.call();
         */
    }

    public void entityExit(Entity entity)
    {
        /*
         * handleBindings(entity); LuaFunction luaFunc = (LuaFunction) scriptBindings.get("onExit");
         * 
         * if (luaFunc == null) { return; }
         * 
         * luaFunc.call();
         */
    }

    void handleBindings(Entity e)
    {
        /*
         * scriptBindings.remove("Entity");
         * 
         * if (e != null) { scriptBindings.put("Entity", new enhancedportals.script.Entity(e)); }
         * 
         * scriptBindings.put("World", new World(worldObj)); scriptBindings.put("Portal", new Portal(getPortalController()));
         */
    }

    @Override
    public void onDataPacket(NBTTagCompound tag)
    {
        // scriptData = tag.getString("script");
    }

    public void portalCreated()
    {
        /*
         * handleBindings(null); LuaFunction luaFunc = (LuaFunction) scriptBindings.get("onPortalCreated");
         * 
         * if (luaFunc == null) { return; }
         * 
         * luaFunc.call();
         */
    }

    public void portalRemoved()
    {
        /*
         * handleBindings(null); LuaFunction luaFunc = (LuaFunction) scriptBindings.get("onPortalRemoved");
         * 
         * if (luaFunc == null) { return; }
         * 
         * luaFunc.call();
         */
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        /*
         * scriptData = compound.getString("script");
         * 
         * if (scriptData != null && scriptData.length() > 0) { try { updateScript(scriptData); } catch (ScriptException e) { EnhancedPortals.logger.warn("Unable to load saved script for Programmable Interface at " + xCoord + "," + yCoord + "," + zCoord); EnhancedPortals.logger.catching(e); } }
         */
    }

    public void scriptError(Exception e, String func)
    {
        EnhancedPortals.logger.warn("Error parsing function: " + func);
        EnhancedPortals.logger.catching(e);
    }

    public void updateScript(String s) //throws ScriptException
    {
        // script = scriptEngine.compile(ScriptCommon.getFullProgram(s));
        // script.eval(scriptBindings);
        // scriptData = s;
    }

    @Override
    public void validate()
    {
        super.validate();
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        // compound.setString("script", scriptData == null ? " " : scriptData);
    }
}
