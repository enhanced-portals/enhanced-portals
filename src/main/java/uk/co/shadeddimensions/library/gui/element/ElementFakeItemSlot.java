package uk.co.shadeddimensions.library.gui.element;

import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;
import uk.co.shadeddimensions.library.util.GuiUtils;

public class ElementFakeItemSlot extends ElementItemIconWithSlot
{
    public ElementFakeItemSlot(IGuiBase parent, int x, int y, ItemStack stack)
    {
        super(parent, x, y, stack);
    }

    public ElementFakeItemSlot(IGuiBase parent, int x, int y, ItemStack stack, boolean big)
    {
        super(parent, x, y, stack, big);
    }

    @Override
    public void draw()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        gui.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(posX, posY, sizeX == 18 ? 0 : 18, 0, sizeX, sizeY);

        int buffer = sizeX == 18 ? 1 : 5;
        GuiUtils.drawItemStack(gui, item, posX + buffer, posY + buffer);

        if (intersectsWith(gui.getMouseX(), gui.getMouseY()) && !isDisabled())
        {
            Gui.drawRect(posX + 1, posY + 1, posX + sizeX - 1, posY + sizeY - 1, 0x88FFFFFF);
        }

        if (isDisabled())
        {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            gui.getTextureManager().bindTexture(texture);
            drawTexturedModalRect(posX + 1, posY + 1, 15, 54, 16, 16);
        }
    }

    public ItemStack getStack()
    {
        return item;
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (!isDisabled() && gui.isItemStackAllowedInFakeSlot(this, gui.getMinecraft().thePlayer.inventory.getItemStack()))
        {
            item = gui.getMinecraft().thePlayer.inventory.getItemStack();
            gui.handleElementFakeSlotItemChange(this);
        }

        return true;
    }
}
