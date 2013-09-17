package uk.co.shadeddimensions.enhancedportals.util;

import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;

public class RenderUtils
{
    public static void renderFrameBlock(Block block, int meta, int x, int y, int z, float red, float green, float blue, RenderBlocks renderer)
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
    
    public static boolean renderBlockTexture(Block block, Icon blockIcon, int x, int y, int z, float red, float green, float blue, RenderBlocks renderer)
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
    
    public static boolean renderBlock(Block block, int meta, int x, int y, int z, float red, float green, float blue, RenderBlocks renderer)
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
    static boolean renderPortalFrame(Block block, int x, int y, int z, float red, float green, float blue, RenderBlocks renderer)
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
                renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, BlockFrame.typeOverlayIcons[0]);
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
                renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, BlockFrame.typeOverlayIcons[0]);
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
                renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, BlockFrame.typeOverlayIcons[0]);
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
                renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, BlockFrame.typeOverlayIcons[0]);
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
                renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, BlockFrame.typeOverlayIcons[0]);
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
                renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, BlockFrame.typeOverlayIcons[0]);
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
