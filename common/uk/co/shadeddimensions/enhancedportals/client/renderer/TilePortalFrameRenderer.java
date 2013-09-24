package uk.co.shadeddimensions.enhancedportals.client.renderer;

import java.awt.Color;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TilePortalController;

public class TilePortalFrameRenderer extends TileEntitySpecialRenderer
{
    private RenderBlocks renderBlocks;
    
    public TilePortalFrameRenderer()
    {
        renderBlocks = new RenderBlocks();
        
        renderBlocks.renderMinX = renderBlocks.renderMinY = renderBlocks.renderMinZ = 0;
        renderBlocks.renderMaxX = renderBlocks.renderMaxY = renderBlocks.renderMaxZ = 1;
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        TilePortalFrame frame = (TilePortalFrame) tile;
        TilePortalController controller = frame.getControllerValidated();
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
        
        renderFrame(frame, controller, tessellator);
        renderFrameOverlay(frame, controller, tessellator, 1);
        
        tessellator.draw();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
    
    private void renderFrame(TilePortalFrame frame, TilePortalController controller, Tessellator tessellator)
    {
        int x = frame.xCoord, y = frame.yCoord, z = frame.zCoord;
        World world = frame.worldObj;
        Color c = new Color(frame instanceof TilePortalController ? ((TilePortalController) frame).FrameColour : controller == null ? 0xFFFFFF : controller.FrameColour);
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
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y - 1, z, 0))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord - 1, frame.zCoord));            
            tessellator.setColorOpaque_F(f10, f13, f16);
            renderBlocks.renderFaceYNeg(null, 0, 0, 0, CommonProxy.blockFrame.getBlockTexture(world, x, y, z, 0));
        }
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y + 1, z, 1))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord + 1, frame.zCoord)); 
            tessellator.setColorOpaque_F(f7, f8, f9);
            renderBlocks.renderFaceYPos(null, 0, 0, 0, CommonProxy.blockFrame.getBlockTexture(world, x, y, z, 1));
        }
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y, z - 1, 2))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZNeg(null, 0, 0, 0, CommonProxy.blockFrame.getBlockTexture(world, x, y, z, 2));
        }
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y, z + 1, 3))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZPos(null, 0, 0, 0, CommonProxy.blockFrame.getBlockTexture(world, x, y, z, 3));
        }
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x - 1, y, z, 4))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord - 1, frame.yCoord, frame.zCoord));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXNeg(null, 0, 0, 0, CommonProxy.blockFrame.getBlockTexture(world, x, y, z, 4));
        }
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x + 1, y, z, 5))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord + 1, frame.yCoord, frame.zCoord));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXPos(null, 0, 0, 0, CommonProxy.blockFrame.getBlockTexture(world, x, y, z, 5));
        }
    }
    
    private void renderFrameOverlay(TilePortalFrame frame, TilePortalController controller, Tessellator tessellator, int pass)
    {
        if (pass != 1)
        {
            return;
        }
        
        int x = frame.xCoord, y = frame.yCoord, z = frame.zCoord, meta = frame.getBlockMetadata();
        World world = frame.worldObj;
        Icon overlayIcon = pass == 1 && ClientProxy.isWearingGoggles ? meta >= 1 ? BlockFrame.typeOverlayIcons[meta] : null : null;

        if (overlayIcon == null)
        {
            return;
        }
        
        float red = 1f, green = 1f, blue = 1f;        
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
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y - 1, z, 0))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord - 1, frame.zCoord));            
            tessellator.setColorOpaque_F(f10, f13, f16);
            renderBlocks.renderFaceYNeg(null, 0, 0, 0, overlayIcon);
        }
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y + 1, z, 1))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord + 1, frame.zCoord)); 
            tessellator.setColorOpaque_F(f7, f8, f9);
            renderBlocks.renderFaceYPos(null, 0, 0, 0, overlayIcon);
        }
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y, z - 1, 2))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZNeg(null, 0, 0, 0, overlayIcon);
        }
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y, z + 1, 3))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZPos(null, 0, 0, 0, overlayIcon);
        }
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x - 1, y, z, 4))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord - 1, frame.yCoord, frame.zCoord));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXNeg(null, 0, 0, 0, overlayIcon);
        }
        
        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x + 1, y, z, 5))
        {
            tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord + 1, frame.yCoord, frame.zCoord));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXPos(null, 0, 0, 0, overlayIcon);
        }
    }
}
