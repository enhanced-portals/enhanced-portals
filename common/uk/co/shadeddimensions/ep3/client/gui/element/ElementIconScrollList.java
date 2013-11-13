package uk.co.shadeddimensions.ep3.client.gui.element;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;

import uk.co.shadeddimensions.ep3.client.gui.IElementHandler;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;

public class ElementIconScrollList extends ElementBase
{
    protected int actualSelected, selected, top, left, elementsPerRow, DEFAULT_SELECTION = -1;
    protected float scrolled, initialClickY;
    protected long lastClickTimer;
    protected ResourceLocation texture;
    @SuppressWarnings("rawtypes")
    protected ArrayList list;

    public ElementIconScrollList(GuiBase gui, ResourceLocation t, int posX, int posY, int width, int height, ArrayList<Icon> theList)
    {
        super(gui, posX, posY);
        sizeX = width;
        sizeY = height;
        selected = -1;
        scrolled = 0f;
        initialClickY = -2f;
        top = gui.getGuiTop() + posY;
        left = gui.getGuiLeft() + posX;
        elementsPerRow = sizeX / (getEntrySize() + (getEntrySpacing() * 2));
        actualSelected = DEFAULT_SELECTION;
        texture = t;
        list = theList;
    }

    public void setSelected(int i)
    {
        actualSelected = i;
    }

    @Override
    public void draw()
    {
        if (!isVisible())
        {
            return;
        }

        int mouseX = gui.getMouseX() + gui.getGuiLeft(), mouseY = gui.getMouseY() + gui.getGuiTop();
        drawBackground();

        for (int i = 0; i < getElementCount(); i++)
        {
            if (isElementVisible(i))
            {
                int row = (i / elementsPerRow) * (getEntrySize() + getEntrySpacing()),
                        col = (i % elementsPerRow) * (getEntrySize() + getEntrySpacing());

                drawElement(i, left + col + getEntrySpacing(), top + row - (int)scrolled + 1, actualSelected == i, mouseX, mouseY);
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
                    int row = k1 / (getEntrySize() + getEntrySpacing());

                    if (row >= 0 && row <= (getElementCount() / elementsPerRow))
                    {
                        int entry = (mouseX - left) / (getEntrySize() + getEntrySpacing());

                        if (entry < elementsPerRow)
                        {
                            entry = (row * elementsPerRow) + entry;

                            if (entry < getElementCount())
                            {
                                if (entry == selected && Minecraft.getSystemTime() - lastClickTimer < 250L)
                                {
                                    actualSelected = actualSelected == entry ? DEFAULT_SELECTION : entry;
                                    notifyParent();
                                }
                            }

                            selected = entry;
                            lastClickTimer = Minecraft.getSystemTime();
                        }
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
        else if (Mouse.isButtonDown(1) && actualSelected > DEFAULT_SELECTION && mouseY >= top && mouseY <= top + sizeY && mouseX >= left && mouseX <= left + sizeX)
        {
            actualSelected = DEFAULT_SELECTION;
            initialClickY = -1f;
            notifyParent();
        }
        else
        {
            initialClickY = -1f;
        }

        restrictScrollAmount();
        drawOverlays();
        drawForeground();
    }

    protected void notifyParent()
    {
        if (gui instanceof IElementHandler)
        {
            ((IElementHandler) gui).onElementChanged(this, actualSelected);
        }
    }

    /***
     * Gets drawn before all the elements in the list
     */
    protected void drawBackground()
    {

    }

    /***
     * Gets drawn after all the elements in the list, after drawing the overlays
     */
    protected void drawForeground()
    {
        if (getElementCount() == 0)
        {
            Minecraft.getMinecraft().fontRenderer.drawString("No custom icons found", posX, posY + 3, 0x999999);
        }
    }

    /***
     * Gets drawn after all the elements in the list, to hide ones that shouldn't be visible
     */
    protected void drawOverlays()
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        gui.drawTexturedModalRect(posX, posY - getEntrySize(), posX - gui.getGuiLeft(), posY - gui.getGuiTop() - getEntrySize(), sizeX, getEntrySize());
        gui.drawTexturedModalRect(posX, posY + sizeY, posX - gui.getGuiLeft(), posY - gui.getGuiTop() + sizeY, sizeX, getEntrySize());
    }

    protected void drawElement(int i, int x, int y, boolean isSelected, int mouseX, int mouseY)
    {
        if (isSelected)
        {
            Gui.drawRect(x - 1, y - 1, x + getEntrySize() + 1, y + getEntrySize() + 1, 0x88FFFFFF);
        }

        gui.drawIcon((Icon) list.get(i), x, y, 0);
    }

    protected int getElementCount()
    {
        return list.size();
    }

    protected int getEntrySize()
    {
        return 16;
    }

    protected int getEntrySpacing()
    {
        return 2;
    }

    protected void restrictScrollAmount()
    {
        int i = (getElementCount() / elementsPerRow) * (getEntrySize() + getEntrySpacing());

        if (i < 0)
        {
            i /= 2;
        }

        if (scrolled < 0f)
        {
            scrolled = 0f;
        }

        if (scrolled >= (float) i - getEntrySize() - getEntrySpacing())
        {
            scrolled = (float) i - getEntrySize() - getEntrySpacing();
        }
    }

    protected int getElementZCoord(int i)
    {
        return ((i % elementsPerRow) * (getEntrySize() + getEntrySpacing())) + getEntrySpacing();
    }

    protected int getElementYCoord(int i)
    {
        return (int) (getRealElementYCoord(i) - scrolled);        
    }

    protected int getRealElementYCoord(int i)
    {        
        return (i / elementsPerRow) * (getEntrySize() + getEntrySpacing());
    }

    protected boolean isElementVisible(int i)
    {
        int pos = getElementYCoord(i);
        return pos > -getEntrySize() && pos < sizeY - 1;
    }

    @Override
    public String getTooltip()
    {
        return null;
    }
}
