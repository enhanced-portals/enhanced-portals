package enhancedportals.client.renderer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import enhancedportals.client.model.ModelDialDevice;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Reference;

import cpw.mods.fml.client.FMLClientHandler;

public class ItemDialDeviceRenderer implements IItemRenderer
{
    private ModelDialDevice model;

    public ItemDialDeviceRenderer()
    {
        model = new ModelDialDevice();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    private void render(float x, float y, float z, boolean inventory, boolean basic)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture("/mods/" + Reference.MOD_ID + "/textures/blocks/" + (basic ? "basicDial" : "dial") + "Device.png");

        GL11.glPushMatrix();
        GL11.glTranslatef(x + 0.5F, y + 1.5F, z + 0.5F);
        GL11.glRotatef(180, 90, 0, -90);

        if (inventory)
        {
            GL11.glRotatef(180, 0, 1.0F, 0);
        }

        model.renderAll();
        GL11.glPopMatrix();
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        boolean basic = false;
        
        if (item.itemID == BlockIds.DialHomeDeviceBasic)
        {
            basic = true;
        }
        
        if (type == ItemRenderType.ENTITY)
        {
            render(-0.5F, 0F, -0.5F, false, basic);
        }
        else if (type == ItemRenderType.EQUIPPED)
        {
            render(0F, 0.4F, 0F, false, basic);
        }
        else if (type == ItemRenderType.INVENTORY)
        {
            render(1F, 0.65F, 1F, true, basic);
        }
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }
}