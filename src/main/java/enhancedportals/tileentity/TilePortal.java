package enhancedportals.tileentity;

import enhancedportals.block.BlockPortal;
import enhancedportals.item.ItemPaintbrush;
import enhancedportals.network.ClientProxy;
import enhancedportals.network.GuiHandler;
import enhancedportals.utility.GeneralUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public class TilePortal extends TilePortalPart
{
    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
        TileController controller = getPortalController();

        if (stack != null && controller != null && controller.isFinalized())
        {
            if (GeneralUtils.isWrench(stack))
            {
                GuiHandler.openGui(player, controller, GuiHandler.PORTAL_CONTROLLER_A);
                return true;
            }
            else if (stack.getItem() == ItemPaintbrush.instance)
            {
                GuiHandler.openGui(player, controller, player.isSneaking() ? GuiHandler.TEXTURE_C : GuiHandler.TEXTURE_B);
                return true;
            }
        }
        
        return false;
    }

    public IIcon getBlockTexture(int side)
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
                return Block.getBlockFromItem(controller.activeTextureData.getPortalItem().getItem()).getIcon(side, controller.activeTextureData.getPortalItem().getItemDamage());
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

    @Override
    public void addDataToPacket(NBTTagCompound tag)
    {
        
    }

    @Override
    public void onDataPacket(NBTTagCompound tag)
    {
        
    }
}
