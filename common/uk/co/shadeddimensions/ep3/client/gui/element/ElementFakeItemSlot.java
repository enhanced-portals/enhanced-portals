package uk.co.shadeddimensions.ep3.client.gui.element;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidContainerRegistry;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.IElementHandler;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;

public class ElementFakeItemSlot extends ElementBase
{
    ItemStack item;
    RenderItem itemRenderer = new RenderItem();

    public ElementFakeItemSlot(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
        sizeX = sizeY = 18;
    }

    public ItemStack getItem()
    {
        return item;
    }

    public void setItem(ItemStack i)
    {
        item = i;
    }

    @Override
    public void draw()
    {
        if (!isVisible())
        {
            return;
        }

        GL11.glColor4f(1f, 1f, 1f, 1f);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(posX, posY, 0, 0, 18, 18);

        if (intersectsWith(gui.getMouseX(), gui.getMouseY()))
        {
            Gui.drawRect(posX + 1, posY + 1, posX + sizeX - 1, posY + sizeY - 1, 0x88FFFFFF);
        }

        if (item != null)
        {
            itemRenderer.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, item, posX + 1, posY + 1);
            GL11.glDisable(GL11.GL_LIGHTING);
        }
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (!isVisible())
        {
            return false;
        }

        ItemStack s = Minecraft.getMinecraft().thePlayer.inventory.getItemStack();

        if (s == null || s.getItem() instanceof ItemBlock || FluidContainerRegistry.isFilledContainer(s))
        {
            item = s;

            if (gui instanceof IElementHandler)
            {
                ((IElementHandler) gui).onElementChanged(this, item);
            }
        }

        return true;
    }

    @Override
    public void addTooltip(List<String> list)
    {
        if (isVisible())
        {
            list.add(item.getDisplayName());
        }
    }
}
