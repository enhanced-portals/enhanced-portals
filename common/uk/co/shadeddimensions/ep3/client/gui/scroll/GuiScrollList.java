package uk.co.shadeddimensions.ep3.client.gui.scroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import org.lwjgl.input.Mouse;

import uk.co.shadeddimensions.ep3.client.gui.base.GuiEnhancedPortals;

public abstract class GuiScrollList extends Gui
{
    protected GuiEnhancedPortals parent;
    protected int x, y, w, h, selected, top, left;
    protected float scrolled, initialClickY;
    protected long lastClickTimer;

    public GuiScrollList(GuiEnhancedPortals p, int X, int Y, int W, int H)
    {
        parent = p;
        x = X;
        y = Y;
        w = W;
        h = H;
        selected = -1;
        scrolled = 0f;
        initialClickY = -2f;        
    }

    public void drawBackground(int mouseX, int mouseY)
    {
        drawBackgroundStart();
        
        for (int i = 0; i < getElementCount(); i++)
        {
            if (isElementVisible(i))
            {
                drawElement(i, left, top + (i * (getEntryHeight() + getEntrySpacing())) - (int)scrolled, isSelected(i), mouseX, mouseY);
            }
        }

        if (Mouse.isButtonDown(0))
        {
            if (initialClickY == -1f)
            {
                boolean flag = true;

                if (mouseY >= top && mouseY <= top + h && mouseX >= left && mouseX <= left + w)
                {
                    int k1 = mouseY - top + (int) scrolled;
                    int l1 = k1 / (getEntryHeight() + getEntrySpacing());
                    
                    if (l1 >= 0 && l1 < getElementCount())
                    {
                        boolean doubleClick = l1 == selected && Minecraft.getSystemTime() - lastClickTimer < 250L;
                        elementClicked(l1, mouseX, mouseY, doubleClick);
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
        drawBackgroundEnd();
    }
    
    protected void drawOverlays()
    {
        
    }

    public void drawForeground(int mouseX, int mouseY)
    {
        
    }

    protected abstract void elementClicked(int i, int mouseX, int mouseY, boolean wasDoubleClick);
    protected abstract void drawElement(int i, int x, int y, boolean isSelected, int mouseX, int mouseY);
    protected abstract int getElementCount();
    protected abstract int getEntryHeight();
    protected abstract int getEntrySpacing();
    protected abstract void drawBackgroundStart();
    protected abstract void drawBackgroundEnd();

    protected boolean isSelected(int i)
    {
        return i == selected;
    }
    
    protected boolean isMouseOver(int i, int mouseX, int mouseY)
    {
        if (!isElementVisible(i))
        {
            return false;
        }
        
        if (mouseX >= left && mouseX <= left + w)
        {
            int k1 = mouseY - top + (int) scrolled;
            int l1 = k1 / (getEntryHeight() + getEntrySpacing());
            
            return l1 == i;
        }
        
        return false;
    }

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
        return (yPos - scrolled >= -getEntryHeight()) && (yPos - scrolled + getEntryHeight() < h + getEntryHeight()) ? true : false;
    }
    
    public void init()
    {
        top = parent.getGuiTop() + y;
        left = parent.getGuiLeft() + x;
    }
}
