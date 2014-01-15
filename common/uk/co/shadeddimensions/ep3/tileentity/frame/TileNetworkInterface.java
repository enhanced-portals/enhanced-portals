package uk.co.shadeddimensions.ep3.tileentity.frame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TileNetworkInterface extends TilePortalFrame
{
    @Override
    public boolean activate(EntityPlayer player)
    {
        ItemStack item = player.inventory.getCurrentItem();

        if (item != null)
        {
            if (ItemHelper.isWrench(item))
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
}
