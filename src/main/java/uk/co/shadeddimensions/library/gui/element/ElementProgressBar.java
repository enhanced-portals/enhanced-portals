package uk.co.shadeddimensions.library.gui.element;

import java.util.List;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementProgressBar extends ElementBase
{
    protected int currentProgress, maxProgress, autoIncrement;

    public ElementProgressBar(IGuiBase parent, int x, int y, int max)
    {
        this(parent, x, y, 0, max);
    }

    public ElementProgressBar(IGuiBase parent, int x, int y, int progress, int max)
    {
        super(parent, x, y, 42, 4);
        currentProgress = progress;
        maxProgress = max;
    }

    @Override
    public void addTooltip(List<String> list)
    {
        list.add(currentProgress + " / " + maxProgress);
    }

    public ElementProgressBar decrementProgress(int progress)
    {
        if (!isDisabled())
        {
            currentProgress -= progress;
        }

        return this;
    }

    @Override
    public void draw()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        gui.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(posX, posY, 44, 18, sizeX, sizeY);

        int width = 0;

        if (currentProgress > 0)
        {
            width = Math.round((float) currentProgress * sizeX / maxProgress);
        }

        drawTexturedModalRect(posX, posY, 44, 18 + 4, width, sizeY);
    }

    public int getMaximum()
    {
        return maxProgress;
    }

    public int getProgress()
    {
        return currentProgress;
    }

    public ElementProgressBar incrementProgress(int progress)
    {
        if (!isDisabled())
        {
            currentProgress += progress;
        }

        return this;
    }

    public ElementProgressBar setAutoIncrement(int amount)
    {
        autoIncrement = amount;

        return this;
    }

    public ElementProgressBar setMaximum(int max)
    {
        maxProgress = max;

        return this;
    }

    public ElementProgressBar setProgress(int progress)
    {
        if (!isDisabled())
        {
            currentProgress = progress;
        }

        return this;
    }

    @Override
    public void update()
    {
        if (autoIncrement > 0)
        {
            incrementProgress(autoIncrement);

            if (currentProgress > maxProgress)
            {
                setProgress(0);
            }
        }
    }
}
