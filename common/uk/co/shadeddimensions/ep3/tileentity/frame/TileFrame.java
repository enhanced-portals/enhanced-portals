package uk.co.shadeddimensions.ep3.tileentity.frame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TileFrame extends TilePortalFrame
{
    public boolean activate(EntityPlayer player)
    {
        ItemStack stack = player.inventory.getCurrentItem();

        if (stack != null)
        {
            if (stack.itemID == CommonProxy.itemInPlaceUpgrade.itemID)
            {
                // TODO: In-place upgrade
                return true;
            }
            else if (stack.itemID == CommonProxy.itemPaintbrush.itemID)
            {
                TilePortalController controller = getPortalController();

                if (controller != null)
                {
                    CommonProxy.openGui(player, GUIs.TexturesFrame, controller);
                    return true;
                }
            }
            else if (ItemHelper.isWrench(stack) && !player.isSneaking())
            {
                if (player.isSneaking())
                {
                    
                }
                else
                {
                    TilePortalController controller = getPortalController();
    
                    if (controller != null)
                    {
                        CommonProxy.openGui(player, GUIs.PortalController, controller);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public Icon getBlockTexture(int side, int pass)
    {
        return pass == 1 ? BlockFrame.overlayIcons[0] : super.getBlockTexture(side, pass);
    }
}
