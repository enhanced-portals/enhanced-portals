package enhancedportals.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import buildcraft.core.render.RenderEntityBlock.BlockInterface;
import enhancedcore.util.MathHelper;
import enhancedportals.lib.Textures;
import enhancedportals.tileentity.TileEntityNetherPortal;

public class TileEntityNetherPortalRenderer extends TileEntitySpecialRenderer
{
    private RenderBlocks renderBlocks;
    private BlockInterface portalBlock;

    public TileEntityNetherPortalRenderer()
    {
        portalBlock = new BlockInterface();
        renderBlocks = new RenderBlocks();
    }

    private void renderBlock(TileEntityNetherPortal portal)
    {
        Tessellator tessellator = Tessellator.instance;
        int meta = portal.getBlockMetadata();
        float f = portalBlock.getBlockBrightness(portal.worldObj, portal.xCoord, portal.yCoord, portal.zCoord), light = f / 1.3F;

        renderBlocks.renderMaxX = portalBlock.maxX;
        renderBlocks.renderMinX = portalBlock.minX;
        renderBlocks.renderMaxY = portalBlock.maxY;
        renderBlocks.renderMinY = portalBlock.minY;
        renderBlocks.renderMaxZ = portalBlock.maxZ;
        renderBlocks.renderMinZ = portalBlock.minZ;
        renderBlocks.enableAO = false;

        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(light, light, light);

        if (meta == 2 || meta == 3)
        {
            renderBlocks.renderFaceZNeg(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(2));
            renderBlocks.renderFaceZPos(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(3));
        }
        else if (meta == 4 || meta == 5)
        {
            renderBlocks.renderFaceXNeg(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(2));
            renderBlocks.renderFaceXPos(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(3));
        }
        else if (meta == 6 || meta == 7)
        {
            renderBlocks.renderFaceYNeg(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(0));
            renderBlocks.renderFaceYPos(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(1));
        }

        tessellator.draw();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        TileEntityNetherPortal portal = (TileEntityNetherPortal) tile;
        ItemStack stack = Textures.getItemStackFromTexture(portal.texture);

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        bindTextureByName(stack.getItem().getSpriteNumber() == 0 ? "/terrain.png" : "/gui/items.png");

        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glScalef(1F, 1F, 1F);

        setupTexture(portal);
        setCubeBounds(portal);
        renderBlock(portal);

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    private void setCubeBounds(TileEntityNetherPortal portal)
    {
        int meta = portal.getBlockMetadata();
        byte thickness = portal.thickness;
        float thick = 0.125F * thickness, thickA = MathHelper.clampFloat(0.375F - thick, 0F, 1F), thickB = MathHelper.clampFloat(0.625F + thick, 0F, 1F);

        if (meta == 2 || meta == 3)
        {
            portalBlock.minX = 0F;
            portalBlock.minY = 0F;
            portalBlock.minZ = thickA;

            portalBlock.maxX = 1F;
            portalBlock.maxY = 1F;
            portalBlock.maxZ = thickB;
        }
        else if (meta == 4 || meta == 5)
        {
            portalBlock.minX = thickA;
            portalBlock.minY = 0F;
            portalBlock.minZ = 0F;

            portalBlock.maxX = thickB;
            portalBlock.maxY = 1F;
            portalBlock.maxZ = 1F;
        }
        else if (meta == 6 || meta == 7)
        {
            portalBlock.minX = 0F;
            portalBlock.minY = thickA;
            portalBlock.minZ = 0F;

            portalBlock.maxX = 1F;
            portalBlock.maxY = thickB;
            portalBlock.maxZ = 1F;
        }
    }

    private void setupTexture(TileEntityNetherPortal portal)
    {
        if (portal.texture.startsWith("B:"))
        {
            int id = Integer.parseInt(portal.texture.substring(2).split(":")[0]);

            portalBlock.baseBlock = Block.blocksList[id];
        }
        else if (portal.texture.startsWith("L:"))
        {
            portalBlock.baseBlock = Block.waterStill;
        }
        else
        {
            portalBlock.baseBlock = Block.portal;
        }

        portalBlock.texture = Textures.getTexture(portal.texture).getPortalTexture();
    }
}
