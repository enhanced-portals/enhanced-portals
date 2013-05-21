package enhancedportals.client.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import enhancedportals.client.model.ModelDialDevice;
import enhancedportals.lib.Reference;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;

public class TileEntityDialDeviceRenderer extends TileEntitySpecialRenderer
{
    private ModelDialDevice model;

    public TileEntityDialDeviceRenderer()
    {
        model = new ModelDialDevice();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glRotatef(180, 0F, 0F, 1F);

        if (tile.getBlockMetadata() == 2)
        {
            GL11.glRotatef(180, 0F, 1F, 0F);
        }
        else if (tile.getBlockMetadata() == 3)
        {
            GL11.glRotatef(0, 0F, 1F, 0F);
        }
        else if (tile.getBlockMetadata() == 4)
        {
            GL11.glRotatef(90, 0F, 1F, 0F);
        }
        else if (tile.getBlockMetadata() == 5)
        {
            GL11.glRotatef(-90, 0F, 1F, 0F);
        }

        bindTextureByName("/mods/" + Reference.MOD_ID + "/textures/blocks/" + (tile instanceof TileEntityDialDeviceBasic ? "basicDial" : "dial") + "Device.png");
        model.renderAll();
        GL11.glPopMatrix();
    }
}
