package uk.co.shadeddimensions.enhancedportals.client.renderer;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import uk.co.shadeddimensions.enhancedportals.util.RenderUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class PortalRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        // TODO: This. Redo and finish it.
        
        TilePortal portal = (TilePortal) world.getBlockTileEntity(x, y, z);
        TilePortalFrameController controller = portal.getControllerValidated();
        int meta = portal.getBlockMetadata();
        byte thickness = 0;
        float thick = 0.125F * thickness, thickA = MathHelper.clamp_float(0.375F - thick, 0F, 1F), thickB = MathHelper.clamp_float(0.625F + thick, 0F, 1F);

        Block baseBlock = Block.waterStill;
        int baseMeta = 0, colour = 0xFFFFFF;
        
        if (controller != null)
        {
            if (controller.getStackInSlot(1) != null)
            {
                baseBlock = Block.blocksList[controller.getStackInSlot(1).itemID];
                baseMeta = controller.getStackInSlot(1).getItemDamage();
            }
            
            colour = controller.PortalColour;
        }
        
        if (meta == 1) // X
        {            
            renderer.setRenderBounds(0, 0, thickA, 1, 1, thickB);
        }
        else if (meta == 2) // Z
        {
            renderer.setRenderBounds(thickA, 0, 0, thickB, 1, 1);
        }
        else if (meta == 3) // XZ
        {
            renderer.setRenderBounds(0, thickA, 0, 1, thickB, 1);
        }
        else
        {
            return true;
        }
                
        Color c = new Color(colour);
        RenderUtils.renderBlockTexture(baseBlock, Block.portal.getIcon(0, 0), x, y, z, c.getRed() / 255, c.getGreen() / 255, c.getBlue() / 255, renderer);        
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
        return ClientProxy.portalRenderType;
    }
}
