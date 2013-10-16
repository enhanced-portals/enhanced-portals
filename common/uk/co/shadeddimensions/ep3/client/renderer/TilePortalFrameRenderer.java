package uk.co.shadeddimensions.ep3.client.renderer;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import cpw.mods.fml.client.FMLClientHandler;

public class TilePortalFrameRenderer extends TileEntitySpecialRenderer
{
    private RenderBlocks renderBlocks;

    public TilePortalFrameRenderer()
    {
        renderBlocks = new RenderBlocks();

        renderBlocks.renderMinX = renderBlocks.renderMinY = renderBlocks.renderMinZ = 0;
        renderBlocks.renderMaxX = renderBlocks.renderMaxY = renderBlocks.renderMaxZ = 1;
    }

    private boolean isWearingGoggles()
    {
        EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;

        if (player != null && player.inventory.armorInventory[3] != null)
        {
            return player.inventory.armorInventory[3].itemID == CommonProxy.itemGoggles.itemID;
        }

        return false;
    }

    private void renderOverlay(World world, int x, int y, int z, int meta, TilePortalFrame frame, TilePortalController controller, Tessellator tessellator)
    {
        int portalShape = controller != null ? controller.portalType : 0;
        Icon overlayIcon = /*isWearingGoggles() ? meta >= 1 ? BlockFrame.typeOverlayIcons[meta] : null :*/ null; // TODO

        Color c = new Color(0x00FF00);
        float red = c.getRed() / 255f, green = c.getGreen() / 255f, blue = c.getBlue() / 255f;
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

        /*if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y - 1, z, 0))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord - 1, frame.zCoord));
            tessellator.setColorOpaque_F(f10, f13, f16);

            if (frame.isTouchingPortal[0])
            {
                renderBlocks.renderFaceYNeg(null, 0, 0, 0, BlockFrame.portal);
            }
            else
            {
                renderBlocks.renderFaceYNeg(null, 0, 0, 0, BlockFrame.connectedTextures.getIconForFace(world, x, y, z, 0));
            }
        }

        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y + 1, z, 1))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord + 1, frame.zCoord));
            tessellator.setColorOpaque_F(f7, f8, f9);

            if (frame.isTouchingPortal[1])
            {
                renderBlocks.renderFaceYPos(null, 0, 0, 0, BlockFrame.portal);
            }
            else
            {
                renderBlocks.renderFaceYPos(null, 0, 0, 0, BlockFrame.connectedTextures.getIconForFace(world, x, y, z, 1));
            }
        }

        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y, z - 1, 2))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);

            if (frame.isTouchingPortal[2])
            {
                renderBlocks.renderFaceZNeg(null, 0, 0, 0, BlockFrame.portal);
            }
            else
            {
                renderBlocks.renderFaceZNeg(null, 0, 0, 0, BlockFrame.connectedTextures.getIconForFace(world, x, y, z, 2));
            }
        }

        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y, z + 1, 3))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);

            if (frame.isTouchingPortal[3])
            {
                renderBlocks.renderFaceZPos(null, 0, 0, 0, BlockFrame.portal);
            }
            else
            {
                renderBlocks.renderFaceZPos(null, 0, 0, 0, BlockFrame.connectedTextures.getIconForFace(world, x, y, z, 3));
            }
        }

        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x - 1, y, z, 4))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord - 1, frame.yCoord, frame.zCoord));
            tessellator.setColorOpaque_F(f12, f15, f18);

            if (frame.isTouchingPortal[4])
            {
                renderBlocks.renderFaceXNeg(null, 0, 0, 0, BlockFrame.portal);
            }
            else
            {
                renderBlocks.renderFaceXNeg(null, 0, 0, 0, BlockFrame.connectedTextures.getIconForFace(world, x, y, z, 4));
            }
        }

        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x + 1, y, z, 5))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord + 1, frame.yCoord, frame.zCoord));
            tessellator.setColorOpaque_F(f12, f15, f18);

            if (frame.isTouchingPortal[5])
            {
                renderBlocks.renderFaceXPos(null, 0, 0, 0, BlockFrame.portal);
            }
            else
            {
                renderBlocks.renderFaceXPos(null, 0, 0, 0, BlockFrame.connectedTextures.getIconForFace(world, x, y, z, 5));
            }
        }*/
    }
    
    private void renderBaseBlock(World world, int x, int y, int z, TilePortalFrame frame, TilePortalController controller, Tessellator tessellator)
    {
        Block baseBlock = Block.blockNetherQuartz;
        int baseMeta = 0;
        Color c = new Color(0xFFFFFF);

        if (controller != null)
        {
            c = new Color(controller.frameColour);

            if (controller.getStackInSlot(0) != null)
            {
                baseBlock = Block.blocksList[controller.getStackInSlot(0).itemID];
                baseMeta = controller.getStackInSlot(0).getItemDamage();
            }
        }

        float red = c.getRed() / 255f, green = c.getGreen() / 255f, blue = c.getBlue() / 255f;
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

        if (baseBlock.shouldSideBeRendered(world, x, y - 1, z, 0))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord - 1, frame.zCoord));
            tessellator.setColorOpaque_F(f10, f13, f16);
            renderBlocks.renderFaceYNeg(null, 0, 0, 0, baseBlock.getIcon(0, baseMeta));
        }

        if (baseBlock.shouldSideBeRendered(world, x, y + 1, z, 1))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord + 1, frame.zCoord));
            tessellator.setColorOpaque_F(f7, f8, f9);
            renderBlocks.renderFaceYPos(null, 0, 0, 0, baseBlock.getIcon(1, baseMeta));
        }

        if (baseBlock.shouldSideBeRendered(world, x, y, z - 1, 2))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZNeg(null, 0, 0, 0, baseBlock.getIcon(2, baseMeta));
        }

        if (baseBlock.shouldSideBeRendered(world, x, y, z + 1, 3))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZPos(null, 0, 0, 0, baseBlock.getIcon(3, baseMeta));
        }

        if (baseBlock.shouldSideBeRendered(world, x - 1, y, z, 4))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord - 1, frame.yCoord, frame.zCoord));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXNeg(null, 0, 0, 0, baseBlock.getIcon(4, baseMeta));
        }

        if (baseBlock.shouldSideBeRendered(world, x + 1, y, z, 5))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord + 1, frame.yCoord, frame.zCoord));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXPos(null, 0, 0, 0, baseBlock.getIcon(5, baseMeta));
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        TilePortalPart frame = (TilePortalPart) tile;
        int x2 = frame.xCoord, y2 = frame.yCoord, z2 = frame.zCoord;
        TilePortalController controller = frame instanceof TilePortalController ? (TilePortalController) frame : frame.getPortalController();
        Tessellator tessellator = Tessellator.instance;

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

        renderBaseBlock(frame.worldObj, x2, y2, z2, (TilePortalFrame) frame, controller, tessellator);
        renderOverlay(frame.worldObj, x2, y2, z2, frame.getBlockMetadata(), (TilePortalFrame) frame, controller, tessellator);

        tessellator.draw();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
