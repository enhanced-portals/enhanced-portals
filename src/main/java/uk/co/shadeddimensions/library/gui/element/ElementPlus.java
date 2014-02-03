package uk.co.shadeddimensions.library.gui.element;

import java.util.List;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementPlus extends ElementProgressBar
{
    boolean showTooltip, horizontal;

    public ElementPlus(IGuiBase parent, int x, int y)
    {
        this(parent, x, y, 0, 100, false, false);
    }

    public ElementPlus(IGuiBase parent, int x, int y, int max)
    {
        this(parent, x, y, 0, max, false, false);
    }

    public ElementPlus(IGuiBase parent, int x, int y, int progress, int max, boolean tooltip, boolean horiz)
    {
        super(parent, x, y, progress, max);
        sizeX = 13;
        sizeY = 13;
        showTooltip = tooltip;
        horizontal = horiz;
    }

    @Override
    public void addTooltip(List<String> list)
    {
        if (showTooltip)
        {
            super.addTooltip(list);
        }
    }

    @Override
    public void draw()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        gui.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(posX, posY, 116, 0, sizeX, sizeY);

        if (horizontal)
        {
            int width = 0;

            if (currentProgress > 0)
            {
                width = Math.round((float) currentProgress * sizeX / maxProgress);
            }

            drawTexturedModalRect(posX, posY, 116 + sizeX, 0, width, sizeY);
        }
        else
        {
            int height = 0;

            if (currentProgress > 0)
            {
                height = Math.round((float) currentProgress * sizeY / maxProgress);
            }

            drawTexturedModalRect(posX, posY + sizeY - height, 116 + sizeX, sizeY - height, sizeX, height);
        }

        if (isDisabled())
        {
            drawTexturedModalRect(posX - 1, posY - 1, 0, 53, 15, 15);
        }
    }
}
