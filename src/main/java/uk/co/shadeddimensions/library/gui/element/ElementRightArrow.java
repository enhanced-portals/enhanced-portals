package uk.co.shadeddimensions.library.gui.element;

import java.util.List;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementRightArrow extends ElementProgressBar
{
    boolean showTooltip;

    public ElementRightArrow(IGuiBase parent, int x, int y)
    {
        this(parent, x, y, 0, 100, false);
    }

    public ElementRightArrow(IGuiBase parent, int x, int y, int max)
    {
        this(parent, x, y, 0, max, false);
    }

    public ElementRightArrow(IGuiBase parent, int x, int y, int progress, int max, boolean tooltip)
    {
        super(parent, x, y, progress, max);
        sizeX = 22;
        sizeY = 15;
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
        drawTexturedModalRect(posX, posY, 44, 0, sizeX, sizeY);

        int width = 0;

        if (currentProgress > 0)
        {
            width = Math.round((float) currentProgress * sizeX / maxProgress);
        }

        drawTexturedModalRect(posX, posY, 44 + sizeX, 0, width, sizeY);

        if (isDisabled())
        {
            drawTexturedModalRect(posX + 3, posY, 0, 53, 15, 15);
        }
    }
}
