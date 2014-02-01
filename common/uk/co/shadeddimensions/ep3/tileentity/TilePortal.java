package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TilePortal extends TilePortalPart
{
    public boolean activate(EntityPlayer player)
    {
        ItemStack item = player.inventory.getCurrentItem();

        if (item != null)
        {
            if (ItemHelper.isWrench(item))
            {
                TilePortalController controller = getPortalController();

                if (controller != null)
                {
                    CommonProxy.openGui(player, GUIs.PortalController, controller);
                    return true;
                }
            }
            else if (item.itemID == CommonProxy.itemPaintbrush.itemID)
            {
                TilePortalController controller = getPortalController();

                if (controller != null && controller.isFullyInitialized())
                {
                    CommonProxy.openGui(player, player.isSneaking() ? GUIs.TexturesParticle : GUIs.TexturesPortal, controller);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        if (oldBlockID != worldObj.getBlockId(xCoord, yCoord, zCoord))
        {
            TilePortalController controller = getPortalController();

            if (controller != null)
            {
                controller.partBroken(true);
            }
        }
    }

    public void onEntityCollidedWithBlock(Entity entity)
    {
        TilePortalController controller = getPortalController();

        if (controller != null && controller.isFullyInitialized() && controller.isPortalActive)
        {
            controller.onEntityEnterPortal(entity, this);
        }
    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public int getColourMultiplier()
    {
        TilePortalController controller = getPortalController();

        if (controller != null)
        {
            return controller.activeTextureData.getPortalColour();
        }

        return 0xFFFFFF;
    }
}
