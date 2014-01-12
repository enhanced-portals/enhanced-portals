package uk.co.shadeddimensions.ep3.tileentity.frame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TileNetworkInterface extends TilePortalPart
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
}
