package uk.co.shadeddimensions.ep3.tileentity.frame;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TileNetworkInterface extends TilePortalFrame implements IPeripheral
{
    @Override
    public boolean activate(EntityPlayer player)
    {
        ItemStack item = player.inventory.getCurrentItem();

        if (item != null)
        {
            if (ItemHelper.isWrench(item) && !player.isSneaking())
            {
                TilePortalController controller = getPortalController();

                if (controller != null && controller.isFullyInitialized())
                {
                    CommonProxy.openGui(player, GUIs.NetworkInterface, controller);
                    return true;
                }
            }
            else if (item != null && item.itemID == CommonProxy.itemPaintbrush.itemID)
            {
                TilePortalController controller = getPortalController();

                if (controller != null && controller.isFullyInitialized())
                {
                    CommonProxy.openGui(player, GUIs.TexturesFrame, controller);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Icon getBlockTexture(int side, int pass)
    {
        if (pass == 0)
        {
            return super.getBlockTexture(side, pass);
        }

        return CommonProxy.forceShowFrameOverlays || GeneralUtils.isWearingGoggles() ? BlockFrame.overlayIcons[3] : BlockFrame.overlayIcons[0];
    }

    void dial() throws Exception
    {
        TilePortalController controller = getPortalController();

        if (controller == null)
        {
            throw new Exception("Can't find portal controller");
        }

        controller.createPortal();
    }

    void terminate() throws Exception
    {
        TilePortalController controller = getPortalController();

        if (controller == null)
        {
            throw new Exception("Can't find portal controller");
        }

        controller.removePortal();
    }

    /* IPeripheral */
    @Override
    public String getType()
    {
        return "Network Interface";
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "dial", "terminate" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        if (method == 0) // dial
        {
            dial();
        }
        else if (method == 1) // terminate
        {
            terminate();
        }

        return null;
    }

    @Override
    public boolean canAttachToSide(int side)
    {
        return true;
    }

    @Override
    public void attach(IComputerAccess computer)
    {

    }

    @Override
    public void detach(IComputerAccess computer)
    {

    }
}
