package uk.co.shadeddimensions.enhancedportals.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.util.PortalTexture;
import uk.co.shadeddimensions.enhancedportals.util.Texture;
import enhancedportals.client.renderer.BlockInterface;

public class TilePortalRenderer extends TileEntitySpecialRenderer
{
    private RenderBlocks renderBlocks;
    private BlockInterface portalBlock;

    public TilePortalRenderer()
    {
        portalBlock = new BlockInterface();
        renderBlocks = new RenderBlocks();
    }

    private void renderBlock(TilePortal portal)
    {
        Tessellator tessellator = Tessellator.instance;
        int meta = portal.getBlockMetadata();

        renderBlocks.renderMaxX = portalBlock.maxX;
        renderBlocks.renderMinX = portalBlock.minX;
        renderBlocks.renderMaxY = portalBlock.maxY;
        renderBlocks.renderMinY = portalBlock.minY;
        renderBlocks.renderMaxZ = portalBlock.maxZ;
        renderBlocks.renderMinZ = portalBlock.minZ;
        renderBlocks.enableAO = false;

        tessellator.startDrawingQuads();

        if (meta == 1) // X
        {
            if (portal.worldObj.getBlockMaterial(portal.xCoord, portal.yCoord, portal.zCoord - 1) != Material.portal)
            {
                renderBlocks.renderFaceZNeg(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(2));
            }

            if (portal.worldObj.getBlockMaterial(portal.xCoord, portal.yCoord, portal.zCoord + 1) != Material.portal)
            {
                renderBlocks.renderFaceZPos(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(3));
            }
        }
        else if (meta == 2) // Z
        {
            if (portal.worldObj.getBlockMaterial(portal.xCoord - 1, portal.yCoord, portal.zCoord) != Material.portal)
            {
                renderBlocks.renderFaceXNeg(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(2));
            }

            if (portal.worldObj.getBlockMaterial(portal.xCoord + 1, portal.yCoord, portal.zCoord + 1) != Material.portal)
            {
                renderBlocks.renderFaceXPos(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(3));
            }
        }
        else if (meta == 3) // XZ
        {
            if (portal.worldObj.getBlockMaterial(portal.xCoord, portal.yCoord - 1, portal.zCoord) != Material.portal)
            {
                renderBlocks.renderFaceYNeg(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(0));
            }

            if (portal.worldObj.getBlockMaterial(portal.xCoord, portal.yCoord + 1, portal.zCoord) != Material.portal)
            {
                renderBlocks.renderFaceYPos(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(1));
            }
        }

        tessellator.draw();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        TilePortal portal = (TilePortal) tile;
        // Texture texture = portal.texture;
        // ItemStack stack = texture.getItemStack();

        // if (stack.itemID == Item.netherStar.itemID)
        // {
        // return;
        // }

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        func_110628_a(TextureMap.field_110575_b);

        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glScalef(1F, 1F, 1F);

        // test();

        setupTexture(portal);
        setCubeBounds(portal);
        renderBlock(portal);

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    private void setCubeBounds(TilePortal portal)
    {
        int meta = portal.getBlockMetadata();
        byte thickness = 0;
        float thick = 0.125F * thickness, thickA = MathHelper.clamp_float(0.375F - thick, 0F, 1F), thickB = MathHelper.clamp_float(0.625F + thick, 0F, 1F);

        if (meta == 1) // X
        {
            portalBlock.minX = 0F;
            portalBlock.minY = 0F;
            portalBlock.minZ = thickA;

            portalBlock.maxX = 1F;
            portalBlock.maxY = 1F;
            portalBlock.maxZ = thickB;
        }
        else if (meta == 2) // Z
        {
            portalBlock.minX = thickA;
            portalBlock.minY = 0F;
            portalBlock.minZ = 0F;

            portalBlock.maxX = thickB;
            portalBlock.maxY = 1F;
            portalBlock.maxZ = 1F;
        }
        else if (meta == 3) // XZ
        {
            portalBlock.minX = 0F;
            portalBlock.minY = thickA;
            portalBlock.minZ = 0F;

            portalBlock.maxX = 1F;
            portalBlock.maxY = thickB;
            portalBlock.maxZ = 1F;
        }
        else if (meta == 4) // XYZ
        {
            portalBlock.minX = 0F;
            portalBlock.minY = 0F;
            portalBlock.minZ = 0F;

            portalBlock.maxX = 1F;
            portalBlock.maxY = 1F;
            portalBlock.maxZ = 1F;
        }
    }

    private void setupTexture(TilePortal portal)
    {
        PortalTexture texture = portal.getPortalTexture();

        if (texture.Texture.startsWith("B:"))
        {
            int id = Integer.parseInt(texture.Texture.substring(2).split(":")[0]);

            portalBlock.baseBlock = Block.blocksList[id];
        }
        else if (texture.Texture.startsWith("F:"))
        {
            portalBlock.baseBlock = Block.waterStill;
        }
        else
        {
            portalBlock.baseBlock = Block.portal;
        }

        portalBlock.texture = Texture.getTexture(texture.Texture, 0);
    }

    private void test()
    {

    }
}
