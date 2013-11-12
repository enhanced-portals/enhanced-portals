package uk.co.shadeddimensions.ep3.client.gui.New;

import org.lwjgl.opengl.GL11;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import cofh.gui.GuiBase;

public class GuiBaseResizable extends GuiBase
{
    protected static int xSizeDefault = 176, ySizeDefault = 166;    
    protected int maxWidth, maxHeight, minWidth, minHeight, currentWidth, currentHeight, RESIZE_RATE = 8;
    protected boolean open;
        
    public GuiBaseResizable(Container container, int maxW, int maxH, int minW, int minH, boolean s)
    {
        super(container, new ResourceLocation("enhancedportals", "textures/gui/sizableGui.png"));
        maxWidth = maxW;
        maxHeight = maxH;
        minWidth = minW;
        minHeight = minH;
        open = s;
        currentWidth = open ? maxWidth : minWidth;
        currentHeight = open ? maxHeight : minHeight;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        updateElements();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);
        
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, currentWidth - 4, currentHeight - 4); // Top Left, Left, Top & Centre
        drawTexturedModalRect(guiLeft + currentWidth - 4, guiTop, 252, 0, 4, currentHeight - 4); // Top Right & Right
        drawTexturedModalRect(guiLeft + currentWidth - 4, guiTop + currentHeight - 4, 252, 252, 4, 4); // Bottom Right
        drawTexturedModalRect(guiLeft, guiTop + currentHeight - 4, 0, 252, currentWidth - 4, 4); // Bottom Left & Bottom

        drawElements();
        drawTabs();
        
        if (open)
        {
            if (currentHeight < maxHeight)
            {
                currentHeight += RESIZE_RATE;
            }
            else if (currentWidth < maxWidth)
            {
                currentWidth += RESIZE_RATE;
            }
        }
        else
        {
            if (currentHeight > minHeight)
            {
                currentHeight -= RESIZE_RATE;
            }
            else if (currentWidth > minWidth)
            {
                currentWidth -= RESIZE_RATE;
            }
        }
    }
    
    protected void toggleState()
    {
        open = !open;
    }
    
    protected boolean isFullyOpen()
    {
        return currentHeight >= maxHeight && currentWidth >= maxWidth;
    }
    
    protected boolean isFullyClosed()
    {
        return currentHeight <= minHeight && currentWidth <= minWidth;
    }
}
