package uk.co.shadeddimensions.enhancedportals.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.util.Texture;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.client.FMLClientHandler;
import enhancedcore.util.MathHelper;
import enhancedportals.client.renderer.BlockInterface;

public class TilePortalPartRenderer extends TileEntitySpecialRenderer
{
    private RenderBlocks renderBlocks;
    private BlockInterface portalBlock;

    public TilePortalPartRenderer()
    {
        portalBlock = new BlockInterface();
        renderBlocks = new RenderBlocks();
    }

    private void renderBlock(World worldObj, int xCoord, int yCoord, int zCoord, int meta)
    {
        Tessellator tessellator = Tessellator.instance;

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
            if (worldObj.getBlockMaterial(xCoord, yCoord, zCoord - 1) != Material.portal)
            {
                renderBlocks.renderFaceZNeg(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(2));
            }

            if (worldObj.getBlockMaterial(xCoord, yCoord, zCoord + 1) != Material.portal)
            {
                renderBlocks.renderFaceZPos(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(3));
            }
        }
        else if (meta == 2) // Z
        {
            if (worldObj.getBlockMaterial(xCoord - 1, yCoord, zCoord) != Material.portal)
            {
                renderBlocks.renderFaceXNeg(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(2));
            }

            if (worldObj.getBlockMaterial(xCoord + 1, yCoord, zCoord + 1) != Material.portal)
            {
                renderBlocks.renderFaceXPos(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(3));
            }
        }
        else if (meta == 3) // XZ
        {
            if (worldObj.getBlockMaterial(xCoord, yCoord - 1, zCoord) != Material.portal)
            {
                renderBlocks.renderFaceYNeg(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(0));
            }

            if (worldObj.getBlockMaterial(xCoord, yCoord + 1, zCoord) != Material.portal)
            {
                renderBlocks.renderFaceYPos(null, 0, 0, 0, portalBlock.getBlockTextureFromSide(1));
            }
        }

        tessellator.draw();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        TileMultipart portal = (TileMultipart) tile;
        World world = portal.worldObj;
        int xCoord = portal.xCoord, yCoord = portal.yCoord, zCoord = portal.zCoord, meta = (int) f;

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        FMLClientHandler.instance().getClient().renderEngine.func_110577_a(TextureMap.field_110575_b);

        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glScalef(1F, 1F, 1F);

        setupTexture(portal);
        setCubeBounds(world, xCoord, yCoord, zCoord, meta);
        renderBlock(world, xCoord, yCoord, zCoord, meta);

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    private void setCubeBounds(World worldObj, int xCoord, int yCoord, int zCoord, int meta)
    {
        byte thickness = 0;
        float thick = 0.125F * thickness, thickA = MathHelper.clampFloat(0.375F - thick, 0F, 1F), thickB = MathHelper.clampFloat(0.625F + thick, 0F, 1F);

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

    private void setupTexture(TileMultipart portal)
    {
        Texture texture = new Texture();

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
}
