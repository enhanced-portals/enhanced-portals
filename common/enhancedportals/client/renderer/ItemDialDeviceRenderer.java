package enhancedportals.client.renderer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import enhancedportals.client.model.ModelDialDevice;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Reference;

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

    private void render(float x, float y, float z, ItemRenderType type, boolean basic)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture("/mods/" + Reference.MOD_ID + "/textures/blocks/" + (basic ? "basicDial" : "dial") + "Device.png"); // TODO

        GL11.glPushMatrix();
        GL11.glTranslatef(x + 0.5F, y + 1.5F, z + 0.5F);
        GL11.glRotatef(180, 90, 0, -90);

        if (type == ItemRenderType.INVENTORY)
        {
            GL11.glRotatef(180, 0, 1.0F, 0);
        }
        else if (type == ItemRenderType.EQUIPPED)
        {
            GL11.glRotatef(270, 0, 1.0F, 0);
        }

        model.renderAll();
        GL11.glPopMatrix();
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        boolean basic = false;

        if (item.itemID == BlockIds.DialDeviceBasic)
        {
            basic = true;
        }

        if (type == ItemRenderType.ENTITY)
        {
            render(-0.5F, 0F, -0.5F, ItemRenderType.ENTITY, basic);
        }
        else if (type == ItemRenderType.EQUIPPED)
        {
            render(0F, 0.4F, 0F, ItemRenderType.EQUIPPED, basic);
        }
        else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
            render(0F, 0.4F, 0F, ItemRenderType.EQUIPPED_FIRST_PERSON, basic);
        }
        else if (type == ItemRenderType.INVENTORY)
        {
            render(1F, 0.7F, 1F, ItemRenderType.INVENTORY, basic);
        }
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }
}
