package uk.co.shadeddimensions.ep3.tileentity.portal;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.block.BlockPortal;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TilePortal extends TilePortalPart
{
    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
        TileController controller = getPortalController();

        if (stack != null && controller != null && controller.isFinalized())
        {
            if (ItemHelper.isWrench(stack))
            {
                GuiHandler.openGui(player, controller, GuiHandler.PORTAL_CONTROLLER);
                return true;
            }
            else if (stack.itemID == ItemPaintbrush.ID)
            {
                GuiHandler.openGui(player, controller, player.isSneaking() ? GuiHandler.TEXTURE_PARTICLE : GuiHandler.TEXTURE_PORTAL);
                return true;
            }
        }
        
        return false;
    }

    public Icon getBlockTexture(int side)
    {
        TileController controller = getPortalController();

        if (controller != null)
        {
            if (controller.activeTextureData.hasCustomPortalTexture() && ClientProxy.customPortalTextures.size() > controller.activeTextureData.getCustomPortalTexture() && ClientProxy.customPortalTextures.get(controller.activeTextureData.getCustomPortalTexture()) != null)
            {
                return ClientProxy.customPortalTextures.get(controller.activeTextureData.getCustomPortalTexture());
            }
            else if (controller.activeTextureData.getPortalItem() != null && controller.activeTextureData.getPortalItem().getItem() instanceof ItemBlock)
            {
                return Block.blocksList[((ItemBlock) controller.activeTextureData.getPortalItem().getItem()).getBlockID()].getIcon(side, controller.activeTextureData.getPortalItem().getItemDamage());
            }
        }

        return BlockPortal.instance.getIcon(side, 0);
    }

    public int getColour()
    {
        TileController controller = getPortalController();

        if (controller != null)
        {
            return controller.activeTextureData.getPortalColour();
        }

        return 0xFFFFFF;
    }

    public void onEntityCollidedWithBlock(Entity entity)
    {
        TileController controller = getPortalController();

        if (controller != null)
        {
            controller.onEntityEnterPortal(entity, this);
        }
    }
}
