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

        if (meta < 4)
        {
            int colour = CommonProxy.blockPortal.colorMultiplier(world, x, y, z);
            float r = (float)(colour >> 16 & 255) / 255.0F;
            float g = (float)(colour >> 8 & 255) / 255.0F;
            float b = (float)(colour & 255) / 255.0F;

            float f3 = 0.5F;
            float f4 = 1.0F;
            float f5 = 0.8F;
            float f6 = 0.6F;
            float f7 = f4 * r;
            float f8 = f4 * g;
            float f9 = f4 * b;
            float f10 = f3;
            float f11 = f5;
            float f12 = f6;
            float f13 = f3;
            float f14 = f5;
            float f15 = f6;
            float f16 = f3;
            float f17 = f5;
            float f18 = f6;

            f10 = f3 * r;
            f11 = f5 * r;
            f12 = f6 * r;
            f13 = f3 * g;
            f14 = f5 * g;
            f15 = f6 * g;
            f16 = f3 * b;
            f17 = f5 * b;
            f18 = f6 * b;

            if (CommonProxy.blockPortal.shouldSideBeRendered(world, x, y - 1, z, 0))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f10, f13, f16);
                renderer.renderFaceYNeg(CommonProxy.blockPortal, (double)x, (double)y, (double)z, renderer.getBlockIcon(CommonProxy.blockPortal, world, x, y, z, 0));
            }

            if (CommonProxy.blockPortal.shouldSideBeRendered(world, x, y + 1, z, 1))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f7, f8, f9);
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
                tessellator.setColorOpaque_F(f12, f15, f18);
                renderer.renderFaceXNeg(CommonProxy.blockPortal, (double)x, (double)y, (double)z, renderer.getBlockIcon(CommonProxy.blockPortal, world, x, y, z, 4));
            }

            if (CommonProxy.blockPortal.shouldSideBeRendered(world, x + 1, y, z, 5))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f12, f15, f18);
                renderer.renderFaceXPos(CommonProxy.blockPortal, (double)x, (double)y, (double)z, renderer.getBlockIcon(CommonProxy.blockPortal, world, x, y, z, 5));
            }
        }
        else if (meta > 3)
        {
            tessellator.addTranslation(x, y, z);
            Icon c = block.getBlockTexture(world, x, y, z, 0);
            tessellator.setBrightness(240); // TODO Why are some brighter than others?

            float u = c.getMinU();
            float v = c.getMinV();
            float U = c.getMaxU();
            float V = c.getMaxV();

            if (meta == 4)
            {
                double d1 = 0.8, d2 = 0.2, min = -0.2, max = 1.2, d3 = 0.8, d4 = 0.2, min2 = -0.2, max2 = 1.2;

                if (world.getBlockId(x + 1, y, z - 1) == CommonProxy.blockFrame.blockID)
                {
                    max = 1.4;
                    min = -0.4;
                    d2 = 0;
                    d1 = 1;
                }
                else if (world.getBlockId(x - 1, y, z + 1) == CommonProxy.blockFrame.blockID)
                {
                    max2 = 1.4;
                    min2 = -0.4;
                    d4 = 0;
                    d3 = 1;
                }

                tessellator.addVertexWithUV(max, 0, d2, U, V);
                tessellator.addVertexWithUV(max, 1, d2, u, V);
                tessellator.addVertexWithUV(d4, 1, max2, u, v);
                tessellator.addVertexWithUV(d4, 0, max2, u, V);

                tessellator.addVertexWithUV(min2, 0, d3, U, V);
                tessellator.addVertexWithUV(min2, 1, d3, u, V);
                tessellator.addVertexWithUV(d1, 1, min, u, v);
                tessellator.addVertexWithUV(d1, 0, min, u, V);
            }
            else if (meta == 5)
            {
                double d1 = 0.8, d2 = 0.2, min = -0.2, max = 1.2;

                if (world.getBlockId(x + 1, y, z + 1) == CommonProxy.blockFrame.blockID)
                {
                    max = 1.4;
                    d1 = 1;
                }
                else if (world.getBlockId(x - 1, y, z - 1) == CommonProxy.blockFrame.blockID)
                {
                    min = -0.4;
                    d2 = 0;
                }

                tessellator.addVertexWithUV(max, 0, d1, U, V);
                tessellator.addVertexWithUV(max, 1, d1, u, V);
                tessellator.addVertexWithUV(d2, 1, min, u, v);
                tessellator.addVertexWithUV(d2, 0, min, u, V);

                tessellator.addVertexWithUV(min, 0, d2, U, V);
                tessellator.addVertexWithUV(min, 1, d2, u, V);
                tessellator.addVertexWithUV(d1, 1, max, u, v);
                tessellator.addVertexWithUV(d1, 0, max, u, V);
            }

            tessellator.addTranslation(-x, -y, -z);
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
        return ID;
    }
}
