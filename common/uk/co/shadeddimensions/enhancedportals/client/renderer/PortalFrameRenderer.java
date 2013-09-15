package uk.co.shadeddimensions.enhancedportals.client.renderer;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class PortalFrameRenderer implements ISimpleBlockRenderingHandler
{    
    public PortalFrameRenderer()
    {
        
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        renderBlockInventory(CommonProxy.blockFrame, metadata, renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        TilePortalFrame frame = (TilePortalFrame) world.getBlockTileEntity(x, y, z);
        TilePortalFrameController control = null;
        
        if (frame instanceof TilePortalFrameController)
        {
            control = (TilePortalFrameController) frame;
        }
        else
        {
            control = frame.getControllerValidated();
        }
        
        int baseBlock = CommonProxy.blockFrame.blockID, baseMetadata = 0, blockMeta = world.getBlockMetadata(x, y, z);
        
        if (control != null)
        {
            ItemStack s = control.getStackInSlot(0);
            
            if (s != null)
            {
                if (s.getItemSpriteNumber() == 0)
                {
                    baseBlock = s.itemID;
                    baseMetadata = s.getItemDamage();
                }
            }
        }
        
        Color c = new Color(control == null ? 0xFFFFFF : control.FrameColour);
        renderBaseBlock(Block.blocksList[baseBlock], baseMetadata, x, y, z, c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, renderer);
        
        if (ClientProxy.isWearingGoggles)
        {
            if (blockMeta == 1)
            {
                renderBlockTexture(CommonProxy.blockFrame, BlockFrame.controllerOverlay, x, y, z, 1f, 1f, 1f, renderer);
            }
            else if (blockMeta == 2)
            {
                renderBlockTexture(CommonProxy.blockFrame, BlockFrame.redstoneOverlay, x, y, z, 1f, 1f, 1f, renderer);
            }
        }
        
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return ClientProxy.portalFrameRenderType;
    }
    
    void renderBaseBlock(Block block, int meta, int x, int y, int z, float red, float green, float blue, RenderBlocks renderer)
    {
        if (block instanceof BlockFrame)
        {
            renderPortalFrame(block, x, y, z, red, green, blue, renderer);
        }
        else
        {
            renderBlock(block, meta, x, y, z, red, green, blue, renderer);
        }
    }
    
    boolean renderBlockTexture(Block block, Icon blockIcon, int x, int y, int z, float red, float green, float blue, RenderBlocks renderer)
    {
        renderer.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
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

        int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0))
        {
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z));
            tessellator.setColorOpaque_F(f10, f13, f16);
            renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, blockIcon);
            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1))
        {
            tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z));
            tessellator.setColorOpaque_F(f7, f8, f9);
            renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, blockIcon);
            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2))
        {
            tessellator.setBrightness(renderer.renderMinZ > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, blockIcon);

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3))
        {
            tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, blockIcon);

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4))
        {
            tessellator.setBrightness(renderer.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, blockIcon);

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5))
        {
            tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z));
            tessellator.setColorOpaque_F(f12, f15, f18);
            renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, blockIcon);

            flag = true;
        }

        return flag;
    }
    
    void renderBlockInventory(Block block, int meta, RenderBlocks renderer)
    {
        double x, y, z;
        float red, green, blue;
        
        red = green = blue = 1f;
        x = y = z = 1.0;
        
        renderer.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
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

        //tessellator.setBrightness(l);
        tessellator.setColorOpaque_F(f10, f13, f16);
        renderer.renderFaceYNeg(block, x, y, z, renderer.getBlockIconFromSideAndMetadata(block, 0, meta));

        //tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z));
        tessellator.setColorOpaque_F(f7, f8, f9);
        renderer.renderFaceYPos(block, x, y, z, renderer.getBlockIconFromSideAndMetadata(block, 1, meta));

        // tessellator.setBrightness(renderer.renderMinZ > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1));
        tessellator.setColorOpaque_F(f11, f14, f17);
        renderer.renderFaceZNeg(block, x, y, z, renderer.getBlockIconFromSideAndMetadata(block, 2, meta));

        //tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1));
        tessellator.setColorOpaque_F(f11, f14, f17);
        renderer.renderFaceZPos(block, x, y, z, renderer.getBlockIconFromSideAndMetadata(block, 3, meta));

        //tessellator.setBrightness(renderer.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z));
        tessellator.setColorOpaque_F(f12, f15, f18);
        renderer.renderFaceXNeg(block, x, y, z, renderer.getBlockIconFromSideAndMetadata(block, 4, meta));

        //tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z));
        tessellator.setColorOpaque_F(f12, f15, f18);
        renderer.renderFaceXPos(block, x, y, z, renderer.getBlockIconFromSideAndMetadata(block, 5, meta));

    }
    
    boolean renderBlock(Block block, int meta, int x, int y, int z, float red, float green, float blue, RenderBlocks renderer)
    {
        renderer.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
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

        int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0))
        {
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z));
            tessellator.setColorOpaque_F(f10, f13, f16);
            renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1))
        {
            tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z));
            tessellator.setColorOpaque_F(f7, f8, f9);
            renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, renderer.getBlockIconFromSideAndMetadata(block, 1, meta));
            flag = true;
        }

        Icon icon;

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2))
        {
            tessellator.setBrightness(renderer.renderMinZ > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            icon = renderer.getBlockIconFromSideAndMetadata(block, 2, meta);
            renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, icon);

            if (RenderBlocks.fancyGrass && icon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f11 * red, f14 * green, f17 * blue);
                renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3))
        {
            tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            icon = renderer.getBlockIconFromSideAndMetadata(block, 3, meta);
            renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, icon);

            if (RenderBlocks.fancyGrass && icon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f11 * red, f14 * green, f17 * blue);
                renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4))
        {
            tessellator.setBrightness(renderer.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z));
            tessellator.setColorOpaque_F(f12, f15, f18);
            icon = renderer.getBlockIconFromSideAndMetadata(block, 4, meta);
            renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, icon);

            if (RenderBlocks.fancyGrass && icon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f12 * red, f15 * green, f18 * blue);
                renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5))
        {
            tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z));
            tessellator.setColorOpaque_F(f12, f15, f18);
            icon = renderer.getBlockIconFromSideAndMetadata(block, 5, meta);
            renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, icon);

            if (RenderBlocks.fancyGrass && icon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f12 * red, f15 * green, f18 * blue);
                renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        return flag;
    }
    
    /***
     * To be used to render the portal frame only. Allows the use of connected textures. Has no metadata support.
     */
    boolean renderPortalFrame(Block block, int x, int y, int z, float red, float green, float blue, RenderBlocks renderer)
    {
        renderer.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
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

        int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0))
        {
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z));
            
            if (renderer.blockAccess.getBlockId(x, y - 1, z) == CommonProxy.blockPortal.blockID)
            {
                tessellator.setColorOpaque_F(255, 255, 255);
                renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, BlockFrame.connectedToPortal);
            }
            else
            {
                tessellator.setColorOpaque_F(f10, f13, f16);
                renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 0));
            }
            
            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1))
        {
            tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z));
            
            if (renderer.blockAccess.getBlockId(x, y + 1, z) == CommonProxy.blockPortal.blockID)
            {
                tessellator.setColorOpaque_F(255, 255, 255);
                renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, BlockFrame.connectedToPortal);
            }
            else
            {
                tessellator.setColorOpaque_F(f7, f8, f9);
                renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 1));
            }
            
            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2))
        {
            tessellator.setBrightness(renderer.renderMinZ > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1));
                        
            if (renderer.blockAccess.getBlockId(x, y, z - 1) == CommonProxy.blockPortal.blockID)
            {
                tessellator.setColorOpaque_F(255, 255, 255);
                renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, BlockFrame.connectedToPortal);
            }
            else
            {
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 2));
            }

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3))
        {
            tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1));
                        
            if (renderer.blockAccess.getBlockId(x, y, z + 1) == CommonProxy.blockPortal.blockID)
            {
                tessellator.setColorOpaque_F(255, 255, 255);
                renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, BlockFrame.connectedToPortal);
            }
            else
            {
                tessellator.setColorOpaque_F(f11, f14, f17);
                renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 3));
            }

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4))
        {
            tessellator.setBrightness(renderer.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z));
                       
            if (renderer.blockAccess.getBlockId(x - 1, y, z) == CommonProxy.blockPortal.blockID)
            {
                tessellator.setColorOpaque_F(255, 255, 255);
                renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, BlockFrame.connectedToPortal);
            }
            else
            {
                tessellator.setColorOpaque_F(f12, f15, f18);
                renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 4));
            }

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5))
        {
            tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z));
            
            if (renderer.blockAccess.getBlockId(x + 1, y, z) == CommonProxy.blockPortal.blockID)
            {
                tessellator.setColorOpaque_F(255, 255, 255);
                renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, BlockFrame.connectedToPortal);
            }
            else
            {
                tessellator.setColorOpaque_F(f12, f15, f18);
                renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 5));
            }

            flag = true;
        }

        return flag;
    }
}
