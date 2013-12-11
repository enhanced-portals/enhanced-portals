package uk.co.shadeddimensions.ep3.client.renderer.tile;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

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
        Icon[] icons = new Icon[2];
        boolean hasSetIcon = false;
        int brightnessOverride = -1;
        Color c = new Color(0xFFFFFF);

        if (controller != null)
        {
            c = new Color(controller.activeTextureData.getFrameColour());
            ItemStack s = controller.getStackInSlot(0);

            if (controller.activeTextureData.hasCustomFrameTexture())
            {
                if (ClientProxy.customFrameTextures.size() > controller.activeTextureData.getCustomFrameTexture() && ClientProxy.customFrameTextures.get(controller.activeTextureData.getCustomFrameTexture()) != null)
                {
                    icons[0] = ClientProxy.customFrameTextures.get(controller.activeTextureData.getCustomFrameTexture());
                    hasSetIcon = true;
                }
            }
            else if (s != null)
            {
                if (s.getItem() instanceof ItemBlock)
                {                    
                    icons[1] = Block.blocksList[((ItemBlock) s.getItem()).getBlockID()].getIcon(meta == 3 ? 1 : 2, s.getItemDamage());
                    hasSetIcon = true;
                }
                else if (FluidContainerRegistry.isFilledContainer(s))
                {
                    icons[1] = FluidContainerRegistry.getFluidForFilledItem(s).getFluid().getStillIcon();
                    hasSetIcon = true;
                }
            }

            TileModuleManipulator m = controller.blockManager.getModuleManipulator(controller.worldObj);

            if (m != null && m.isFrameGhost())
            {
                brightnessOverride = 244;
            }
        }

        renderBlocks.setRenderBounds(0, 0, 0, 1, 1, 1);

        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            int X = x + d.offsetX, Y = y + d.offsetY, Z = z + d.offsetZ;

            if (CommonProxy.blockFrame.shouldSideBeRendered(world, X, Y, Z, i))
            {
                tessellator.setBrightness(brightnessOverride != -1 ? brightnessOverride : CommonProxy.blockFrame.getMixedBrightnessForBlock(world, X, Y, Z));
                tessellator.setColorOpaque_F(0.8f * (c.getRed() / 255f), 0.8f * (c.getGreen() / 255f), 0.8f * (c.getBlue() / 255f));

                if (hasSetIcon)
                {
                    for (Icon icon : icons)
                    {
                        if (icon != null)
                        {
                            renderFace(i, icon);
                        }
                    }
                }
                else
                {
                    renderFace(i, CommonProxy.blockFrame.getBlockTexture(world, x, y, z, i));
                }

                if ((isWearingGoggles() && meta >= 1 || meta == 4))
                {
                    tessellator.setColorOpaque(255, 255, 255);
                    renderFace(i, BlockFrame.overlayIcons[meta - 1]);
                }
            }
        }
    }

    private void renderPortal(World world, int x, int y, int z, int meta, TilePortal portal, TilePortalController controller, Tessellator tessellator)
    {
        Icon[] icons = new Icon[2];
        boolean hasSetIcon = false;
        Color c = new Color(0xFFFFFF);

        if (controller != null)
        {
            TileModuleManipulator m = controller.blockManager.getModuleManipulator(controller.worldObj);

            if (m != null && m.isPortalInvisible())
            {
                return;
            }

            c = new Color(controller.activeTextureData.getPortalColour());
            ItemStack s = controller.getStackInSlot(1);

            if (controller.activeTextureData.hasCustomPortalTexture())
            {
                if (ClientProxy.customPortalTextures.size() > controller.activeTextureData.getCustomPortalTexture() && ClientProxy.customPortalTextures.get(controller.activeTextureData.getCustomPortalTexture()) != null)
                {
                    icons[0] = ClientProxy.customPortalTextures.get(controller.activeTextureData.getCustomPortalTexture());
                    hasSetIcon = true;
                }
            }

            if (s != null)
            {
                if (s.getItem() instanceof ItemBlock)
                {
                    icons[1] = Block.blocksList[((ItemBlock) s.getItem()).getBlockID()].getIcon(meta == 3 ? 1 : 2, s.getItemDamage());
                    hasSetIcon = true;
                }
                else if (FluidContainerRegistry.isFilledContainer(s))
                {
                    icons[1] = FluidContainerRegistry.getFluidForFilledItem(s).getFluid().getStillIcon();
                    hasSetIcon = true;
                }
            }
        }

        if (!hasSetIcon)
        {
            icons[0] = CommonProxy.blockPortal.getIcon(0, meta);
        }

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

        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            int X = x + d.offsetX, Y = y + d.offsetY, Z = z + d.offsetZ;

            if (CommonProxy.blockPortal.shouldSideBeRendered(world, X, Y, Z, i))
            {
                tessellator.setBrightness(CommonProxy.blockFrame.getMixedBrightnessForBlock(world, X, Y, Z));
                tessellator.setColorOpaque_F(0.8f * (c.getRed() / 255f), 0.8f * (c.getGreen() / 255f), 0.8f * (c.getBlue() / 255f));

                for (Icon icon : icons)
                {
                    if (icon != null)
                    {
                        renderFace(i, icon);
                    }
                }
            }
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
            renderPortal(portalPart.worldObj, x2, y2, z2, portalPart.getBlockMetadata(), (TilePortal) portalPart, controller, tessellator);
        }
        else
        {
            renderFrame(portalPart.worldObj, x2, y2, z2, portalPart.getBlockMetadata(), portalPart, controller, tessellator);
        }

        tessellator.draw();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    void renderFace(int face, Icon texture)
    {
        if (face == 0)
        {
            renderBlocks.renderFaceYNeg(null, 0, 0, 0, texture);
        }
        else if (face == 1)
        {
            renderBlocks.renderFaceYPos(null, 0, 0, 0, texture);
        }
        else if (face == 2)
        {
            renderBlocks.renderFaceZNeg(null, 0, 0, 0, texture);
        }
        else if (face == 3)
        {
            renderBlocks.renderFaceZPos(null, 0, 0, 0, texture);
        }
        else if (face == 4)
        {
            renderBlocks.renderFaceXNeg(null, 0, 0, 0, texture);
        }
        else if (face == 5)
        {
            renderBlocks.renderFaceXPos(null, 0, 0, 0, texture);
        }
    }
}
