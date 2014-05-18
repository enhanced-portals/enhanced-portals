package enhancedportals.tileentity;

import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import enhancedportals.EnhancedPortals;
import enhancedportals.item.ItemPaintbrush;
import enhancedportals.network.GuiHandler;
import enhancedportals.utility.GeneralUtils;

@InterfaceList(value = { @Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = EnhancedPortals.MODID_COMPUTERCRAFT), @Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = EnhancedPortals.MODID_OPENCOMPUTERS) })
public class TileNetworkInterface extends TileFrame implements IPeripheral, SimpleComponent
{
    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
        if (player.isSneaking())
        {
            return false;
        }

        TileController controller = getPortalController();

        if (stack != null && controller != null && controller.isFinalized())
        {
            if (GeneralUtils.isWrench(stack) && !player.isSneaking())
            {
                if (controller.getIdentifierUnique() == null)
                {
                    if (!worldObj.isRemote)
                    {
                        player.addChatComponentMessage(new ChatComponentText(EnhancedPortals.localizeError("noUidSet")));
                    }
                }
                else
                {
                    GuiHandler.openGui(player, controller, GuiHandler.NETWORK_INTERFACE_A);
                }
            }
            else if (stack.getItem() == ItemPaintbrush.instance)
            {
                GuiHandler.openGui(player, controller, GuiHandler.TEXTURE_A);
                return true;
            }
        }

        return false;
    }

    @Override
    public void addDataToPacket(NBTTagCompound tag)
    {

    }

    @Override
    @Method(modid = EnhancedPortals.MODID_COMPUTERCRAFT)
    public void attach(IComputerAccess computer)
    {

    }

    @Override
    @Method(modid = EnhancedPortals.MODID_COMPUTERCRAFT)
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        if (method == 0) // dial
        {
            getPortalController().connectionDial();
        }
        else if (method == 1) // terminate
        {
            getPortalController().connectionTerminate();
        }

        return null;
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    @Method(modid = EnhancedPortals.MODID_COMPUTERCRAFT)
    public void detach(IComputerAccess computer)
    {

    }

    @Callback
    @Method(modid = EnhancedPortals.MODID_OPENCOMPUTERS)
    public Object[] dial(Context context, Arguments args)
    {
        getPortalController().connectionDial();
        return new Object[] { true };
    }

    @Override
    @Method(modid = EnhancedPortals.MODID_COMPUTERCRAFT)
    public boolean equals(IPeripheral other)
    {
        return other == this;
    }

    @Override
    @Method(modid = EnhancedPortals.MODID_OPENCOMPUTERS)
    public String getComponentName()
    {
        return "ep_interface_network";
    }

    @Override
    @Method(modid = EnhancedPortals.MODID_COMPUTERCRAFT)
    public String[] getMethodNames()
    {
        return new String[] { "dial", "terminate" };
    }

    @Override
    @Method(modid = EnhancedPortals.MODID_COMPUTERCRAFT)
    public String getType()
    {
        return "ep_interface_network";
    }

    @Override
    public void onDataPacket(NBTTagCompound tag)
    {

    }

    @Callback
    @Method(modid = EnhancedPortals.MODID_OPENCOMPUTERS)
    public Object[] terminate(Context context, Arguments args)
    {
        getPortalController().connectionTerminate();
        return new Object[] { true };
    }
}
