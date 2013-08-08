package uk.co.shadeddimensions.enhancedportals.client.gui;

import net.minecraft.client.gui.Gui;

public class GuiRectangle extends Gui
{
    int x, y, w, h;
    String str;
    boolean selected = false;
    
    public GuiRectangle(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    public GuiRectangle(int x, int y, String s)
    {
        this.x = x;
        this.y = y;
        this.w = this.h = 16;
        this.str = s;
    }
    
    public boolean inRectangle(GuiFrameController parent, int mX, int mY)
    {
        mX -= parent.getLeft();
        mY -= parent.getTop();
        
        return x <= mX && mX <= x + w && y <= mY && mY <= y + h;
    }
    
    public void draw(GuiFrameController parent, int mX, int mY, boolean disabled)
    {
        drawTexturedModalRect(parent.getLeft() + x, parent.getTop() + y, disabled ? 48 : inRectangle(parent, mX, mY) ? 16 : 0, parent.getYSize(), w, h);
        
        if (selected && !disabled)
        {
            drawTexturedModalRect(parent.getLeft() + x, parent.getTop() + y, 32, parent.getYSize(), w, h);
        }
        else
        {
        	selected = false;
        }
    }
    
    public void drawForeground(GuiFrameController parent, int mX, int mY)
    {
        parent.fontRenderer.drawString(str, x + ((w / 2) - (parent.fontRenderer.getStringWidth(str) / 2)), y + ((h / 2) - 4), 0x909090);
    }
}
