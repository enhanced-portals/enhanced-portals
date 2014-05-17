package enhancedportals.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidContainerRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import enhancedportals.block.BlockPortal;
import enhancedportals.tileentity.TileController;
import enhancedportals.tileentity.TilePortal;

public class PortalRenderer implements ISimpleBlockRenderingHandler
{
    public static int ID = 0;

    public PortalRenderer()
    {

    }

    @Override
    public int getRenderId()
    {
        return ID;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        renderer.setOverrideBlockTexture(BlockPortal.instance.getBlockTextureFromSide(0));
        renderer.renderBlockAsItem(Blocks.portal, 0, 0xFFFFFF);
        renderer.clearOverrideBlockTexture();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        TilePortal portal = (TilePortal) world.getTileEntity(x, y, z);
        TileController controller = portal.getPortalController();
        Tessellator tessellator = Tessellator.instance;
        int meta = world.getBlockMetadata(x, y, z), light = 230, colour = BlockPortal.instance.colorMultiplier(world, x, y, z);

        if (controller != null)
        {
            if (controller.instability > 0)
            {
                light = 240 + controller.instability / 10;
            }

            ItemStack i = controller.activeTextureData.getPortalItem();

            if (i != null)
            {
                if (FluidContainerRegistry.isFilledContainer(i))
                {
                    renderer.setOverrideBlockTexture(FluidContainerRegistry.getFluidForFilledItem(i).getFluid().getStillIcon());
                }
                else
                {
                    renderer.setOverrideBlockTexture(Block.getBlockFromItem(i.getItem()).getIcon(0, i.getItemDamage()));
                }
            }
        }

        float f5 = 0.8F;
        float f11 = f5;
        float f14 = f5;
        float f17 = f5;

        f11 = f5 * ((colour >> 16 & 255) / 255F);
        f14 = f5 * ((colour >> 8 & 255) / 255F);
        f17 = f5 * ((colour & 255) / 255F);

        if (meta < 4)
        {
            if (BlockPortal.instance.shouldSideBeRendered(world, x, y - 1, z, 0))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceYNeg(BlockPortal.instance, x, y, z, renderer.getBlockIcon(BlockPortal.instance, world, x, y, z, 0));
            }

            if (BlockPortal.instance.shouldSideBeRendered(world, x, y + 1, z, 1))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceYPos(BlockPortal.instance, x, y, z, renderer.getBlockIcon(BlockPortal.instance, world, x, y, z, 1));
            }

            if (BlockPortal.instance.shouldSideBeRendered(world, x, y, z - 1, 2))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceZNeg(BlockPortal.instance, x, y, z, renderer.getBlockIcon(BlockPortal.instance, world, x, y, z, 2));
            }

            if (BlockPortal.instance.shouldSideBeRendered(world, x, y, z + 1, 3))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceZPos(BlockPortal.instance, x, y, z, renderer.getBlockIcon(BlockPortal.instance, world, x, y, z, 3));
            }

            if (BlockPortal.instance.shouldSideBeRendered(world, x - 1, y, z, 4))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceXNeg(BlockPortal.instance, x, y, z, renderer.getBlockIcon(BlockPortal.instance, world, x, y, z, 4));
            }

            if (BlockPortal.instance.shouldSideBeRendered(world, x + 1, y, z, 5))
            {
                tessellator.setBrightness(light);
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceXPos(BlockPortal.instance, x, y, z, renderer.getBlockIcon(BlockPortal.instance, world, x, y, z, 5));
            }
        }
        else if (meta > 3)
        {
            tessellator.addTranslation(x, y, z);
            tessellator.setBrightness(light);
            tessellator.setColorOpaque_F(f11, f14, f17);
            IIcon c = block.getIcon(world, x, y, z, 0);

            float u = c.getMinU();
            float v = c.getMinV();
            float U = c.getMaxU();
            float V = c.getMaxV();

            if (meta == 4)
            {
                tessellator.addVertexWithUV(1, 0, 0, U, V);
                tessellator.addVertexWithUV(1, 1, 0, U, v);
                tessellator.addVertexWithUV(0, 1, 1, u, v);
                tessellator.addVertexWithUV(0, 0, 1, u, V);
                
                tessellator.addVertexWithUV(0, 1, 1, U, V);
                tessellator.addVertexWithUV(1, 1, 0, U, v);
                tessellator.addVertexWithUV(1, 0, 0, u, v);
                tessellator.addVertexWithUV(0, 0, 1, u, V);
            }
            else if (meta == 5)
            {
                tessellator.addVertexWithUV(1, 0, 1, U, V);
                tessellator.addVertexWithUV(1, 1, 1, U, v);
                tessellator.addVertexWithUV(0, 1, 0, u, v);
                tessellator.addVertexWithUV(0, 0, 0, u, V);
                
                tessellator.addVertexWithUV(0, 1, 0, U, V);
                tessellator.addVertexWithUV(1, 1, 1, U, v);
                tessellator.addVertexWithUV(1, 0, 1, u, v);
                tessellator.addVertexWithUV(0, 0, 0, u, V);
            }

            tessellator.addTranslation(-x, -y, -z);
        }
        
        renderer.clearOverrideBlockTexture();
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }
}
