package com.alz.enhancedportals.core.client.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.alz.enhancedportals.core.client.model.ModelDialHomeDevice;

public class TileEntityDHDRenderer extends TileEntitySpecialRenderer
{
    private ModelDialHomeDevice model;

    public TileEntityDHDRenderer()
    {
        model = new ModelDialHomeDevice();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glRotatef(180, 0F, 0F, 1F);

        switch (tile.blockMetadata)
        {
            case 2:
                GL11.glRotatef(180, 0F, 1F, 0F);
                break;
            case 3:
                GL11.glRotatef(0, 0F, 1F, 0F);
                break;
            case 4:
                GL11.glRotatef(90, 0F, 1F, 0F);
                break;
            case 5:
                GL11.glRotatef(-90, 0F, 1F, 0F);
                break;
        }

        bindTextureByName("/mods/enhancedportals/textures/blocks/dialHomeDevice.png");
        model.renderAll();
        GL11.glPopMatrix();
    }
}
