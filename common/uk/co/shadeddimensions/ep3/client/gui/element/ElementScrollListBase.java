package uk.co.shadeddimensions.ep3.client.gui.element;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Mouse;

import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;

public abstract class ElementScrollListBase extends ElementBase
{
    protected int selected, top, left;
    protected float scrolled, initialClickY;
    protected long lastClickTimer;

    public ElementScrollListBase(GuiBase gui, int posX, int posY, int width, int height)
    {
        super(gui, posX, posY);
        sizeX = width;
        sizeY = height;
        selected = -1;
        scrolled = 0f;
        initialClickY = -2f;
        top = gui.getGuiTop() + posY;
        left = gui.getGuiLeft() + posX;
    }

    @Override
    public void draw()
    {
        int mouseX = gui.getMouseX(), mouseY = gui.getMouseY();
        drawBackground();

        for (int i = 0; i < getElementCount(); i++)
        {
            if (isElementVisible(i))
            {
                drawElement(i, left, top + (i * (getEntryHeight() + getEntrySpacing())) - (int)scrolled, selected == i, mouseX, mouseY);
            }
        }

        if (Mouse.isButtonDown(0))
        {
            if (initialClickY == -1f)
            {
                boolean flag = true;

                if (mouseY >= top && mouseY <= top + sizeY && mouseX >= left && mouseX <= left + sizeX)
                {
                    int k1 = mouseY - top + (int) scrolled;
                    int l1 = k1 / (getEntryHeight() + getEntrySpacing());

                    if (l1 >= 0 && l1 < getElementCount())
                    {
                        if (l1 == selected && Minecraft.getSystemTime() - lastClickTimer < 250L)
                        {
                            elementClicked(l1, mouseX, mouseY);
                        }
                        
                        selected = l1;
                        lastClickTimer = Minecraft.getSystemTime();
                    }

                    if (flag)
                    {
                        initialClickY = (float) mouseY;
                    }
                    else
                    {
                        initialClickY = -2f;
                    }
                }
                else
                {
                    initialClickY = -2f;
                }
            }
            else if (initialClickY >= 0f)
            {
                scrolled -= (float) mouseY - initialClickY;
                initialClickY = (float) mouseY;
            }
        }
        else
        {
            initialClickY = -1f;
        }

        restrictScrollAmount();
        drawOverlays();
        drawForeground();
    }

    /***
     * Gets drawn before all the elements in the list
     */
    protected abstract void drawBackground();

    /***
     * Gets drawn after all the elements in the list, after drawing the overlays
     */
    protected abstract void drawForeground();

    /***
     * Gets drawn after all the elements in the list, to hide ones that shouldn't be visible
     */
    protected abstract void drawOverlays();

    protected abstract void elementClicked(int i, int mouseX, int mouseY);
    protected abstract void drawElement(int i, int x, int y, boolean isSelected, int mouseX, int mouseY);
    protected abstract int getElementCount();
    protected abstract int getEntryHeight();
    protected abstract int getEntrySpacing();

    protected void restrictScrollAmount()
    {
        int i = getElementCount() * (getEntryHeight() + getEntrySpacing());

        if (i < 0)
        {
            i /= 2;
        }

        if (scrolled < 0f)
        {
            scrolled = 0f;
        }

        if (scrolled >= (float) i - getEntryHeight() - getEntrySpacing())
        {
            scrolled = (float) i - getEntryHeight() - getEntrySpacing();
        }
    }

    protected boolean isElementVisible(int i)
    {
        int yPos = (i * (getEntryHeight() + getEntrySpacing()));
        return (yPos - scrolled >= -getEntryHeight()) && (yPos - scrolled + getEntryHeight() < sizeY + getEntryHeight()) ? true : false;
    }

    @Override
    public String getTooltip()
    {
        return null;
    }
}
