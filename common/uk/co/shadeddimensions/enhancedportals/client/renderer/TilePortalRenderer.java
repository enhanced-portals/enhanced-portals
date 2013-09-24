package uk.co.shadeddimensions.enhancedportals.client.renderer;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.portal.StackHelper;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TilePortalController;

public class TilePortalRenderer extends TileEntitySpecialRenderer
{
    private RenderBlocks renderBlocks;

    public TilePortalRenderer()
    {
        renderBlocks = new RenderBlocks();

        renderBlocks.renderMinX = renderBlocks.renderMinY = renderBlocks.renderMinZ = 0;
        renderBlocks.renderMaxX = renderBlocks.renderMaxY = renderBlocks.renderMaxZ = 1;
    }

    private void renderPortal(TilePortal portal, TilePortalController controller, Tessellator tessellator)
    {
        int x = portal.xCoord, y = portal.yCoord, z = portal.zCoord;
        World world = portal.worldObj;
        Color c = new Color(controller == null ? 0xFFFFFF : controller.PortalColour);
        float red = c.getRed() / 255f, green = c.getGreen() / 255f, blue = c.getBlue() / 255f;

        Block baseBlock = CommonProxy.blockPortal;
        int baseMeta = 5;

        if (controller != null)
        {
            ItemStack stack = controller.getStackInSlot(1);

            if (StackHelper.isStackDye(stack))
            {
                baseMeta = StackHelper.getDyeColour(stack);
            }
            else if (StackHelper.isItemStackValidForPortalTexture(stack))
            {
                baseBlock = Block.blocksList[stack.itemID];
                baseMeta = stack.getItemDamage();
            }
        }

        float f3 = 0.5F;
        float f4 = 1.0F;
        float f5 = 0.8F;
        float f6 = 0.6F;
        float f7 = f4 * red;
        float f8 = f4 * green;
        float f9 = f4 * blue;
        float f10 = f3;
        float f11 = f5;
        float f12 = f6;
        float f13 = f3;
        float f14 = f5;
        float f15 = f6;
        float f16 = f3;
        float f17 = f5;
        float f18 = f6;

        f10 = f3 * red;
        f11 = f5 * red;
        f12 = f6 * red;
        f13 = f3 * green;
        f14 = f5 * green;
        f15 = f6 * green;
        f16 = f3 * blue;
        f17 = f5 * blue;
        f18 = f6 * blue;

        if (CommonProxy.blockPortal.shouldSideBeRendered(world, x, y - 1, z, 0) && world.getBlockId(x, y - 1, z) != CommonProxy.blockPortal.blockID)
        {
            // tessellator.setBrightness(CommonProxy.blockPortal.getMixedBrightnessForBlock(world, x, y - 1, z));
            tessellator.setColorOpaque_F(f10, f13, f16);
            renderBlocks.renderFaceYNeg(null, 0, 0, 0, baseBlock.getIcon(0, baseMeta));
        }

        if (CommonProxy.blockPortal.shouldSideBeRendered(world, x, y + 1, z, 1) && world.getBlockId(x, y + 1, z) != CommonProxy.blockPortal.blockID)
        {
            // tessellator.setBrightness(CommonProxy.blockPortal.getMixedBrightnessForBlock(world, x, y + 1, z));
            tessellator.setColorOpaque_F(f7, f8, f9);
            renderBlocks.renderFaceYPos(null, 0, 0, 0, baseBlock.getIcon(1, baseMeta));
        }

        if (CommonProxy.blockPortal.shouldSideBeRendered(world, x, y, z - 1, 2) && world.getBlockId(x, y, z - 1) != CommonProxy.blockPortal.blockID)
        {
            // tessellator.setBrightness(CommonProxy.blockPortal.getMixedBrightnessForBlock(world, x, y, z - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZNeg(null, 0, 0, 0, baseBlock.getIcon(2, baseMeta));
        }

        if (CommonProxy.blockPortal.shouldSideBeRendered(world, x, y, z + 1, 3) && world.getBlockId(x, y, z + 1) != CommonProxy.blockPortal.blockID)
        {
            // tessellator.setBrightness(CommonProxy.blockPortal.getMixedBrightnessForBlock(world, x, y, z + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZPos(null, 0, 0, 0, baseBlock.getIcon(3, baseMeta));
        }

        if (CommonProxy.blockPortal.shouldSideBeRendered(world, x - 1, y, z, 4) && world.getBlockId(x - 1, y, z) != CommonProxy.blockPortal.blockID)
        {
            // tessellator.setBrightness(CommonProxy.blockPortal.getMixedBrightnessForBlock(world, x - 1, y, z));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXNeg(null, 0, 0, 0, baseBlock.getIcon(4, baseMeta));
        }

        if (CommonProxy.blockPortal.shouldSideBeRendered(world, x + 1, y, z, 5) && world.getBlockId(x + 1, y, z) != CommonProxy.blockPortal.blockID)
        {
            // tessellator.setBrightness(CommonProxy.blockPortal.getMixedBrightnessForBlock(world, x + 1, y, z));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXPos(null, 0, 0, 0, baseBlock.getIcon(5, baseMeta));
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        TilePortal portal = (TilePortal) tile;
        TilePortalController controller = portal.getController();
        Tessellator tessellator = Tessellator.instance;
        int meta = portal.getBlockMetadata();

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        bindTexture(TextureMap.locationBlocksTexture);

        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glScalef(1F, 1F, 1F);
        tessellator.startDrawingQuads();

        setupPortal(portal, meta);
        renderPortal(portal, controller, tessellator);

        tessellator.draw();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    private void setupPortal(TilePortal portal, int meta)
    {
        byte thickness = 0;
        float thick = 0.125F * thickness, thickA = MathHelper.clamp_float(0.375F - thick, 0F, 1F), thickB = MathHelper.clamp_float(0.625F + thick, 0F, 1F);

        if (meta == 1)
        {
            renderBlocks.renderMinX = renderBlocks.renderMinY = 0f;
            renderBlocks.renderMinZ = thickA;

            renderBlocks.renderMaxX = renderBlocks.renderMaxY = 1f;
            renderBlocks.renderMaxZ = thickB;
        }
        else if (meta == 2)
        {
            renderBlocks.renderMinZ = renderBlocks.renderMinY = 0f;
            renderBlocks.renderMinX = thickA;

            renderBlocks.renderMaxZ = renderBlocks.renderMaxY = 1f;
            renderBlocks.renderMaxX = thickB;
        }
        else if (meta == 3)
        {
            renderBlocks.renderMinX = renderBlocks.renderMinZ = 0f;
            renderBlocks.renderMinY = thickA;

            renderBlocks.renderMaxX = renderBlocks.renderMaxZ = 1f;
            renderBlocks.renderMaxY = thickB;
        }
        else
        {
            renderBlocks.renderMinX = renderBlocks.renderMinY = renderBlocks.renderMinZ = 0f;

            renderBlocks.renderMaxX = renderBlocks.renderMaxY = renderBlocks.renderMaxZ = 1f;
        }
    }
}
