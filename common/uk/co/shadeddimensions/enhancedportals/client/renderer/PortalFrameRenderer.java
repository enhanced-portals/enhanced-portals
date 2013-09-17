package uk.co.shadeddimensions.enhancedportals.client.renderer;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import uk.co.shadeddimensions.enhancedportals.util.RenderUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class PortalFrameRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        TilePortalFrame frame = (TilePortalFrame) world.getBlockTileEntity(x, y, z);
        TilePortalFrameController control = null;
        
        if (frame instanceof TilePortalFrameController)
        {
            control = (TilePortalFrameController) frame;
        }
        else
        {
            control = frame.getControllerValidated();
        }
        
        int baseBlock = CommonProxy.blockFrame.blockID, baseMetadata = 0, blockMeta = world.getBlockMetadata(x, y, z);
        
        if (control != null)
        {
            ItemStack s = control.getStackInSlot(0);
            
            if (s != null)
            {
                if (s.getItemSpriteNumber() == 0)
                {
                    baseBlock = s.itemID;
                    baseMetadata = s.getItemDamage();
                }
            }
        }
        
        Color c = new Color(control == null ? 0xFFFFFF : control.FrameColour);
        RenderUtils.renderFrameBlock(Block.blocksList[baseBlock], baseMetadata, x, y, z, c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, renderer);
        
        if (ClientProxy.isWearingGoggles)
        {
            if (blockMeta == 1)
            {
                RenderUtils.renderBlockTexture(CommonProxy.blockFrame, BlockFrame.typeOverlayIcons[1], x, y, z, 1f, 1f, 1f, renderer);
            }
            else if (blockMeta == 2)
            {
                RenderUtils.renderBlockTexture(CommonProxy.blockFrame, BlockFrame.typeOverlayIcons[2], x, y, z, 1f, 1f, 1f, renderer);
            }
        }
        
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory()
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return ClientProxy.portalFrameRenderType;
    }
}
