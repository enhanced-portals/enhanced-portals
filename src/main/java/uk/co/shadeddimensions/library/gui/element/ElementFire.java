package uk.co.shadeddimensions.library.gui.element;

import java.util.List;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementFire extends ElementProgressBar
{
    boolean showTooltip;

    public ElementFire(IGuiBase parent, int x, int y)
    {
        this(parent, x, y, 0, 100, false);
    }

    public ElementFire(IGuiBase parent, int x, int y, int max)
    {
        this(parent, x, y, 0, max, false);
    }

    public ElementFire(IGuiBase parent, int x, int y, int progress, int max, boolean tooltip)
    {
        super(parent, x, y, progress, max);
        sizeX = 14;
        sizeY = 14;
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
        drawTexturedModalRect(posX, posY, 88, 0, sizeX, sizeY);

        int height = 0;

        if (currentProgress > 0)
        {
            height = Math.round((float) currentProgress * sizeY / maxProgress);
            height = height == 14 ? 13 : height;
        }

        drawTexturedModalRect(posX, posY + height, 88 + sizeX, height, sizeX, sizeY - height);

        if (isDisabled())
        {
            drawTexturedModalRect(posX, posY, 0, 53, 15, 15);
        }
    }
}
