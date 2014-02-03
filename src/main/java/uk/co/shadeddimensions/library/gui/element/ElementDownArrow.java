package uk.co.shadeddimensions.library.gui.element;

import java.util.List;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementDownArrow extends ElementProgressBar
{
    boolean showTooltip;

    public ElementDownArrow(IGuiBase parent, int x, int y)
    {
        this(parent, x, y, 0, 100, false);
    }

    public ElementDownArrow(IGuiBase parent, int x, int y, int max)
    {
        this(parent, x, y, 0, max, false);
    }

    public ElementDownArrow(IGuiBase parent, int x, int y, int progress, int max, boolean tooltip)
    {
        super(parent, x, y, progress, max);
        sizeX = 8;
        sizeY = 27;
        showTooltip = tooltip;
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
        drawTexturedModalRect(posX, posY, 0, 26, sizeX, sizeY);

        if (!isDisabled())
        {
            int height = 0;

            if (currentProgress > 0)
            {
                height = Math.round((float) currentProgress * sizeY / maxProgress);
            }

            drawTexturedModalRect(posX, posY, sizeX, 26, sizeX, height);
        }
    }
}
