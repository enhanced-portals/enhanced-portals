package enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import enhancedportals.block.BlockFrame;
import enhancedportals.common.ISidedBlockTexture;
import enhancedportals.network.ClientProxy;
import enhancedportals.network.CommonProxy;
import enhancedportals.utility.GeneralUtils;

public abstract class TileFrame extends TilePortalPart implements ISidedBlockTexture
{
    protected boolean wearingGoggles = GeneralUtils.isWearingGoggles();

    protected boolean shouldShowOverlay()
    {
        return wearingGoggles || CommonProxy.forceShowFrameOverlays;
    }
    
    @Override
    public void breakBlock(Block b, int oldMetadata)
    {
        if (b == worldObj.getBlock(xCoord, yCoord, zCoord))
        {
            return;
        }

        TileController controller = getPortalController();

        if (controller != null)
        {
            controller.onPartFrameBroken();
        }
    }

    @Override
    public IIcon getBlockTexture(int side, int pass)
    {
        if (pass == 0)
        {
            TileController controller = getPortalController();

            if (controller != null)
            {
                if (controller.activeTextureData.hasCustomFrameTexture() && ClientProxy.customFrameTextures.size() > controller.activeTextureData.getCustomFrameTexture() && ClientProxy.customFrameTextures.get(controller.activeTextureData.getCustomFrameTexture()) != null)
                {
                    return ClientProxy.customFrameTextures.get(controller.activeTextureData.getCustomFrameTexture());
                }
                else if (controller.activeTextureData.getFrameItem() != null && controller.activeTextureData.getFrameItem().getItem() instanceof ItemBlock)
                {
                    return Block.getBlockFromItem(controller.activeTextureData.getFrameItem().getItem()).getIcon(side, controller.activeTextureData.getFrameItem().getItemDamage());
                }
            }
            else
            {
                return BlockFrame.connectedTextures.getBaseIcon();
            }

            return BlockFrame.connectedTextures.getIconForSide(worldObj, xCoord, yCoord, zCoord, side);
        }
        else
        {
        	int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        	
        	if (meta == BlockFrame.DIALLING_DEVICE)
        	{
        		return BlockFrame.overlayIcons[meta];
        	}
        	
        	return shouldShowOverlay() ? BlockFrame.overlayIcons[meta] : BlockFrame.overlayIcons[0];
        }
    }

    public int getColour()
    {
        TileController controller = getPortalController();

        if (controller != null)
        {
            return controller.activeTextureData.getFrameColour();
        }

        return 0xFFFFFF;
    }

    public void onBlockDismantled()
    {
        TileController controller = getPortalController();

        if (controller != null)
        {
            controller.deconstruct();
        }
    }

    @Override
    public void updateEntity()
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT && Minecraft.getSystemTime() % 10 == 0)
        {
            boolean wGoggles = GeneralUtils.isWearingGoggles();

            if (wGoggles != wearingGoggles)
            {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                wearingGoggles = wGoggles;
            }
        }
    }
}
