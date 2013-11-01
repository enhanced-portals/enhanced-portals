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

import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import cpw.mods.fml.client.FMLClientHandler;

public class TileFrameRenderer extends TileEntitySpecialRenderer
{
    private RenderBlocks renderBlocks;

    public TileFrameRenderer()
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

    private void renderFrame(World world, int x, int y, int z, int meta, TilePortalPart frame, TilePortalController controller, Tessellator tessellator)
    {
        Icon overrideIcon = null;
        Block baseBlock = CommonProxy.blockFrame;
        int baseMeta = 0, brightnessOverride = -1;
        Color c = new Color(0xFFFFFF);

        if (controller != null)
        {
            c = new Color(controller.activeTextureData.getFrameColour());

            if (controller.activeTextureData.hasCustomFrameTexture())
            {
                if (ClientProxy.customFrameTextures.size() > controller.activeTextureData.getCustomFrameTexture() && (Icon) ClientProxy.customFrameTextures.get(controller.activeTextureData.getCustomFrameTexture()) != null)
                {
                    overrideIcon = (Icon) ClientProxy.customFrameTextures.get(controller.activeTextureData.getCustomFrameTexture());
                }
            }
            else if (controller.getStackInSlot(0) != null)
            {
                baseBlock = Block.blocksList[controller.getStackInSlot(0).itemID];
                baseMeta = controller.getStackInSlot(0).getItemDamage();
            }

            TileModuleManipulator m = controller.getModuleManipulator();

            if (m != null && m.isFrameGhost())
            {
                brightnessOverride = 244;
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

        renderBlocks.setRenderBounds(0, 0, 0, 1, 1, 1);

        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y - 1, z, 0))
        {
            tessellator.setBrightness(brightnessOverride > -1 ? brightnessOverride : baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord - 1, frame.zCoord));
            tessellator.setColorOpaque_F(f10, f13, f16);
            renderBlocks.renderFaceYNeg(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.blockID != CommonProxy.blockFrame.blockID ? baseBlock.getIcon(0, baseMeta) : baseBlock.getBlockTexture(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord, 0));

            if (((isWearingGoggles() && meta >= 1) || meta == 4) && frame.worldObj.getBlockId(frame.xCoord, frame.yCoord - 1, frame.zCoord) != CommonProxy.blockPortal.blockID)
            {
                renderBlocks.renderFaceYNeg(null, 0, 0, 0, BlockFrame.overlayIcons[meta - 1]);
            }
        }

        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y + 1, z, 1))
        {
            tessellator.setBrightness(brightnessOverride > -1 ? brightnessOverride : baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord + 1, frame.zCoord));
            tessellator.setColorOpaque_F(f7, f8, f9);
            renderBlocks.renderFaceYPos(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.blockID != CommonProxy.blockFrame.blockID ? baseBlock.getIcon(1, baseMeta) : baseBlock.getBlockTexture(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord, 1));

            if (((isWearingGoggles() && meta >= 1) || meta == 4) && frame.worldObj.getBlockId(frame.xCoord, frame.yCoord + 1, frame.zCoord) != CommonProxy.blockPortal.blockID)
            {
                renderBlocks.renderFaceYPos(null, 0, 0, 0, BlockFrame.overlayIcons[meta - 1]);
            }
        }

        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y, z - 1, 2))
        {
            tessellator.setBrightness(brightnessOverride > -1 ? brightnessOverride : baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZNeg(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.blockID != CommonProxy.blockFrame.blockID ? baseBlock.getIcon(2, baseMeta) : baseBlock.getBlockTexture(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord, 2));

            if (((isWearingGoggles() && meta >= 1) || meta == 4) && frame.worldObj.getBlockId(frame.xCoord, frame.yCoord, frame.zCoord - 1) != CommonProxy.blockPortal.blockID)
            {
                renderBlocks.renderFaceZNeg(null, 0, 0, 0, BlockFrame.overlayIcons[meta - 1]);
            }
        }

        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x, y, z + 1, 3))
        {
            tessellator.setBrightness(brightnessOverride > -1 ? brightnessOverride : baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZPos(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.blockID != CommonProxy.blockFrame.blockID ? baseBlock.getIcon(3, baseMeta) : baseBlock.getBlockTexture(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord, 3));

            if (((isWearingGoggles() && meta >= 1) || meta == 4) && frame.worldObj.getBlockId(frame.xCoord, frame.yCoord, frame.zCoord + 1) != CommonProxy.blockPortal.blockID)
            {
                renderBlocks.renderFaceZPos(null, 0, 0, 0, BlockFrame.overlayIcons[meta - 1]);
            }
        }

        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x - 1, y, z, 4))
        {
            tessellator.setBrightness(brightnessOverride > -1 ? brightnessOverride : baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord - 1, frame.yCoord, frame.zCoord));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXNeg(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.blockID != CommonProxy.blockFrame.blockID ? baseBlock.getIcon(4, baseMeta) : baseBlock.getBlockTexture(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord, 4));

            if (((isWearingGoggles() && meta >= 1) || meta == 4) && frame.worldObj.getBlockId(frame.xCoord - 1, frame.yCoord, frame.zCoord) != CommonProxy.blockPortal.blockID)
            {
                renderBlocks.renderFaceXNeg(null, 0, 0, 0, BlockFrame.overlayIcons[meta - 1]);
            }
        }

        if (CommonProxy.blockFrame.shouldSideBeRendered(world, x + 1, y, z, 5))
        {
            tessellator.setBrightness(brightnessOverride > -1 ? brightnessOverride : baseBlock.getMixedBrightnessForBlock(frame.worldObj, frame.xCoord + 1, frame.yCoord, frame.zCoord));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXPos(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.blockID != CommonProxy.blockFrame.blockID ? baseBlock.getIcon(5, baseMeta) : baseBlock.getBlockTexture(frame.worldObj, frame.xCoord, frame.yCoord, frame.zCoord, 5));

            if (((isWearingGoggles() && meta >= 1) || meta == 4) && frame.worldObj.getBlockId(frame.xCoord + 1, frame.yCoord, frame.zCoord) != CommonProxy.blockPortal.blockID)
            {
                renderBlocks.renderFaceXPos(null, 0, 0, 0, BlockFrame.overlayIcons[meta - 1]);
            }
        }
    }

    private void renderPortal(int x, int y, int z, int meta, TilePortal portal, TilePortalController controller, Tessellator tessellator)
    {
        Icon overrideIcon = null;
        Block baseBlock = CommonProxy.blockPortal;
        int baseMeta = 0;
        Color c = new Color(0xFFFFFF);

        if (controller != null)
        {
            c = new Color(controller.activeTextureData.getPortalColour());

            if (controller.activeTextureData.hasCustomPortalTexture())
            {
                if (ClientProxy.customPortalTextures.size() > controller.activeTextureData.getCustomPortalTexture() && (Icon) ClientProxy.customPortalTextures.get(controller.activeTextureData.getCustomPortalTexture()) != null)
                {
                    overrideIcon = (Icon) ClientProxy.customPortalTextures.get(controller.activeTextureData.getCustomPortalTexture());
                }
            }
            else if (controller.getStackInSlot(1) != null)
            {
                baseBlock = Block.blocksList[controller.getStackInSlot(1).itemID];
                baseMeta = controller.getStackInSlot(1).getItemDamage();
            }

            TileModuleManipulator m = controller.getModuleManipulator();

            if (m != null && m.isPortalInvisible())
            {
                return;
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

        if (meta == 1) // X
        {
            renderBlocks.setRenderBounds(0f, 0f, 0.375f, 1f, 1f, 0.625f);
        }
        else if (meta == 2) // Z
        {
            renderBlocks.setRenderBounds(0.375f, 0f, 0f, 0.625f, 1f, 1f);
        }
        else if (meta == 3) // XZ
        {
            renderBlocks.setRenderBounds(0, 0.375f, 0f, 1f, 0.625f, 1f);
        }
        else
        {
            renderBlocks.setRenderBounds(0f, 0f, 0f, 1f, 1f, 1f);
        }

        if (CommonProxy.blockPortal.shouldSideBeRendered(portal.worldObj, x, y - 1, z, 0))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(portal.worldObj, x, y - 1, z));
            tessellator.setColorOpaque_F(f10, f13, f16);
            renderBlocks.renderFaceYNeg(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.getIcon(0, baseMeta));
        }

        if (CommonProxy.blockPortal.shouldSideBeRendered(portal.worldObj, x, y + 1, z, 1))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(portal.worldObj, x, y + 1, z));
            tessellator.setColorOpaque_F(f7, f8, f9);
            renderBlocks.renderFaceYPos(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.getIcon(1, baseMeta));
        }

        if (CommonProxy.blockPortal.shouldSideBeRendered(portal.worldObj, x, y, z - 1, 2))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(portal.worldObj, x, y, z - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZNeg(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.getIcon(2, baseMeta));
        }

        if (CommonProxy.blockPortal.shouldSideBeRendered(portal.worldObj, x, y, z + 1, 3))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(portal.worldObj, x, y, z + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderBlocks.renderFaceZPos(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.getIcon(3, baseMeta));
        }

        if (CommonProxy.blockPortal.shouldSideBeRendered(portal.worldObj, x - 1, y, z, 4))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(portal.worldObj, x - 1, y, z));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXNeg(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.getIcon(4, baseMeta));
        }

        if (CommonProxy.blockPortal.shouldSideBeRendered(portal.worldObj, x + 1, y, z, 5))
        {
            tessellator.setBrightness(baseBlock.getMixedBrightnessForBlock(portal.worldObj, x + 1, y, z));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderBlocks.renderFaceXPos(null, 0, 0, 0, overrideIcon != null ? overrideIcon : baseBlock.getIcon(5, baseMeta));
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {        
        TilePortalPart portalPart = (TilePortalPart) tile;
        int x2 = portalPart.xCoord, y2 = portalPart.yCoord, z2 = portalPart.zCoord;
        TilePortalController controller = portalPart instanceof TilePortalController ? (TilePortalController) portalPart : portalPart.getPortalController();
        Tessellator tessellator = Tessellator.instance;
        renderBlocks.blockAccess = portalPart.worldObj;

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

        if (tile instanceof TilePortal)
        {
            renderPortal(x2, y2, z2, portalPart.getBlockMetadata(), (TilePortal) portalPart, controller, tessellator);
        }
        else
        {
            renderFrame(portalPart.worldObj, x2, y2, z2, portalPart.getBlockMetadata(), portalPart, controller, tessellator);
        }

        tessellator.draw();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
