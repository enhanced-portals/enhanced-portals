package uk.co.shadeddimensions.ep3.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class PortalRenderer implements ISimpleBlockRenderingHandler
{
    public static int ID = 0;

    public PortalRenderer()
    {

    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        int meta = world.getBlockMetadata(x, y, z), light = 230;
        Tessellator tessellator = Tessellator.instance;
        
        int colour = CommonProxy.blockPortal.colorMultiplier(world, x, y, z);
        float r = (float)(colour >> 16 & 255) / 255.0F;
        float g = (float)(colour >> 8 & 255) / 255.0F;
        float b = (float)(colour & 255) / 255.0F;

        float f5 = 0.8F;
        float f11 = f5;
        float f14 = f5;
        float f17 = f5;

        f11 = f5 * r;
        f14 = f5 * g;
        f17 = f5 * b;

        if (meta < 4)
        {
            if (CommonProxy.blockPortal.shouldSideBeRendered(world, x, y - 1, z, 0))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceYNeg(CommonProxy.blockPortal, (double)x, (double)y, (double)z, renderer.getBlockIcon(CommonProxy.blockPortal, world, x, y, z, 0));
            }

            if (CommonProxy.blockPortal.shouldSideBeRendered(world, x, y + 1, z, 1))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceYPos(CommonProxy.blockPortal, (double)x, (double)y, (double)z, renderer.getBlockIcon(CommonProxy.blockPortal, world, x, y, z, 1));
            }

            if (CommonProxy.blockPortal.shouldSideBeRendered(world, x, y, z - 1, 2))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceZNeg(CommonProxy.blockPortal, (double)x, (double)y, (double)z, renderer.getBlockIcon(CommonProxy.blockPortal, world, x, y, z, 2));
            }

            if (CommonProxy.blockPortal.shouldSideBeRendered(world, x, y, z + 1, 3))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceZPos(CommonProxy.blockPortal, (double)x, (double)y, (double)z, renderer.getBlockIcon(CommonProxy.blockPortal, world, x, y, z, 3));
            }

            if (CommonProxy.blockPortal.shouldSideBeRendered(world, x - 1, y, z, 4))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceXNeg(CommonProxy.blockPortal, (double)x, (double)y, (double)z, renderer.getBlockIcon(CommonProxy.blockPortal, world, x, y, z, 4));
            }

            if (CommonProxy.blockPortal.shouldSideBeRendered(world, x + 1, y, z, 5))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceXPos(CommonProxy.blockPortal, (double)x, (double)y, (double)z, renderer.getBlockIcon(CommonProxy.blockPortal, world, x, y, z, 5));
            }
        }
        else if (meta > 3)
        {
            tessellator.addTranslation(x, y, z);
            tessellator.setColorOpaque_F(f11, f14, f17);
            Icon c = block.getBlockTexture(world, x, y, z, 0);

            float u = c.getMinU();
            float v = c.getMinV();
            float U = c.getMaxU();
            float V = c.getMaxV();

            if (meta == 4)
            {
                tessellator.addVertexWithUV(1, 0, 0, U, V);
                tessellator.addVertexWithUV(1, 1, 0, u, V);
                tessellator.addVertexWithUV(0, 1, 1, u, v);
                tessellator.addVertexWithUV(0, 0, 1, u, V);
                
                /*tessellator.addVertexWithUV(1, 0, 0.2, U, V);
                tessellator.addVertexWithUV(1, 1, 0.2, u, V);
                tessellator.addVertexWithUV(0.2, 1, 1, u, v);
                tessellator.addVertexWithUV(0.2, 0, 1, u, V);

                tessellator.addVertexWithUV(0, 0, 0.8, U, V);
                tessellator.addVertexWithUV(0, 1, 0.8, u, V);
                tessellator.addVertexWithUV(0.8, 1, 0, u, v);
                tessellator.addVertexWithUV(0.8, 0, 0, u, V);*/
            }
            else if (meta == 5)
            {
                tessellator.addVertexWithUV(1, 0, 1, U, V);
                tessellator.addVertexWithUV(1, 1, 1, u, V);
                tessellator.addVertexWithUV(0, 1, 0, u, v);
                tessellator.addVertexWithUV(0, 0, 0, u, V);
                
                /*tessellator.addVertexWithUV(1, 0, 0.8, U, V);
                tessellator.addVertexWithUV(1, 1, 0.8, u, V);
                tessellator.addVertexWithUV(0.2, 1, 0, u, v);
                tessellator.addVertexWithUV(0.2, 0, 0, u, V);

                tessellator.addVertexWithUV(0, 0, 0.2, U, V);
                tessellator.addVertexWithUV(0, 1, 0.2, u, V);
                tessellator.addVertexWithUV(0.8, 1, 1, u, v);
                tessellator.addVertexWithUV(0.8, 0, 1, u, V);*/
            }

            tessellator.addTranslation(-x, -y, -z);
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return ID;
    }
}
