package uk.co.shadeddimensions.library.gui.element;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementIcon extends ElementBase
{
    int texU, texV;

    public ElementIcon(IGuiBase parent, int x, int y, int icon)
    {
        super(parent, x, y, 0, 0);

        switch (icon)
        {
            default:
            case 0:
                sizeX = sizeY = 18;
                break;

            case 1:
                texU = 18;
                sizeX = sizeY = 26;
                break;

            case 2:
                texU = 44;
                sizeX = 22;
                sizeY = 15;
                break;

            case 3:
                texU = 88;
                sizeX = 14;
                sizeY = 13;
                break;

            case 4:
                texU = 116;
                sizeX = sizeY = 13;
                break;

            case 5:
                texV = 53;
                sizeX = sizeY = 15;
                break;
        }
    }

    @Override
    public void draw()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        gui.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(posX, posY, texU, texV, sizeX, sizeY);
    }
}
