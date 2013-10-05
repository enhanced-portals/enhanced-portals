package uk.co.shadeddimensions.ep3.gui.slider;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class GuiRGBSlider extends GuiBetterSlider
{
    String originalText;

    public GuiRGBSlider(int id, int x, int y, String displayText, float initialValue)
    {
        super(id, x, y, displayText + ": " + (int) (255 * initialValue), initialValue, 75);
        originalText = displayText;
    }

    public int getValue()
    {
        return (int) (255 * sliderValue);
    }

    @Override
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
    {
        if (drawButton)
        {
            if (dragging)
            {
                sliderValue = (float) (par2 - (xPosition + 4)) / (float) (width - 8);

                if (sliderValue < 0.0F)
                {
                    sliderValue = 0.0F;
                }

                if (sliderValue > 1.0F)
                {
                    sliderValue = 1.0F;
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            displayString = originalText + ": " + (int) (255 * sliderValue);
            drawTexturedModalRect(xPosition + (int) (sliderValue * (width - 8)), yPosition, 0, 66, 4, 20);
            drawTexturedModalRect(xPosition + (int) (sliderValue * (width - 8)) + 4, yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (super.mousePressed(par1Minecraft, par2, par3))
        {
            sliderValue = (float) (par2 - (xPosition + 4)) / (float) (width - 8);

            if (sliderValue < 0.0F)
            {
                sliderValue = 0.0F;
            }

            if (sliderValue > 1.0F)
            {
                sliderValue = 1.0F;
            }

            dragging = true;
            displayString = originalText + ": " + (int) (255 * sliderValue);
            return true;
        }
        else
        {
            return false;
        }
    }
}
