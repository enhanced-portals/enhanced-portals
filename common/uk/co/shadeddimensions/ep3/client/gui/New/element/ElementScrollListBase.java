package uk.co.shadeddimensions.ep3.client.gui.New.element;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;

import uk.co.shadeddimensions.ep3.client.gui.New.IElementHandler;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;

public class ElementScrollListBase extends ElementBase
{
    protected int actualSelected, selected, top, left;
    protected float scrolled, initialClickY;
    protected long lastClickTimer;
    protected ResourceLocation texture;
    @SuppressWarnings("rawtypes")
    protected ArrayList list;

    public ElementScrollListBase(GuiBase gui, ResourceLocation t, int posX, int posY, int width, int height)
    {
        super(gui, posX, posY);
        sizeX = width;
        sizeY = height;
        selected = actualSelected = -1;
        scrolled = 0f;
        initialClickY = -2f;
        top = gui.getGuiTop() + posY;
        left = gui.getGuiLeft() + posX;
        texture = t;
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
                drawElement(i, left, top + (i * (getEntrySize() + getEntrySpacing())) - (int)scrolled + 1, actualSelected == i, mouseX, mouseY);
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
                    int entry = k1 / (getEntrySize() + getEntrySpacing());

                    if (entry >= 0 && entry < getElementCount())
                    {
                        if (entry == selected && Minecraft.getSystemTime() - lastClickTimer < 250L)
                        {
                            entrySelected(entry, mouseX, mouseY);
                        }
                        
                        selected = entry;
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

    protected void entrySelected(int entry, int mouseX, int mouseY)
    {
        actualSelected = entry;
        notifyParent();
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
            Minecraft.getMinecraft().fontRenderer.drawString("No entries found", posX + 5, posY + 3, 0x999999);
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
        
    }
    
    protected int getElementCount()
    {
        return list != null ? list.size() : 0;
    }
    
    protected int getEntrySize()
    {
        return 30;
    }
    
    protected int getEntrySpacing()
    {
        return 1;
    }

    protected void restrictScrollAmount()
    {
        int i = getElementCount() * (getEntrySize() + getEntrySpacing());

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

    protected boolean isElementVisible(int i)
    {
        int yPos = (i * (getEntrySize() + getEntrySpacing()));
        return (yPos - scrolled >= -getEntrySize()) && (yPos - scrolled + getEntrySize() < sizeY + getEntrySize()) ? true : false;
    }

    @Override
    public String getTooltip()
    {
        return null;
    }
    
    protected void notifyParent()
    {
        if (gui instanceof IElementHandler)
        {
            ((IElementHandler) gui).onElementChanged(this, actualSelected);
        }
    }
}
