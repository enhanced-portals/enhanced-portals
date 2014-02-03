package uk.co.shadeddimensions.library.gui.element;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementScrollPanelOverlay extends ElementScrollPanel
{
    ResourceLocation overlayTexture;
    int paddingSize;
    boolean top = true, bottom = true, left = true, right = true;

    public ElementScrollPanelOverlay(IGuiBase parent, int x, int y, int w, int h, ResourceLocation tex, int padding)
    {
        super(parent, x, y, w, h);
        overlayTexture = tex;
        paddingSize = padding;
    }

    @Override
    public void draw()
    {
        super.draw();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        gui.getTextureManager().bindTexture(overlayTexture);

        if (top)
        {
            drawTexturedModalRect(posX, posY - paddingSize, relativePosX, relativePosY - paddingSize, sizeX, paddingSize);
        }

        if (bottom)
        {
            drawTexturedModalRect(posX, posY + sizeY, relativePosX, relativePosY + sizeY, sizeX, paddingSize);
        }

        if (left)
        {
            drawTexturedModalRect(posX - paddingSize, posY, relativePosX - paddingSize, relativePosY, paddingSize, sizeY);
        }

        if (right)
        {
            drawTexturedModalRect(posX + sizeX, posY, relativePosX + sizeX, relativePosY, paddingSize, sizeY);
        }
    }

    public ElementScrollPanelOverlay setSides(boolean t, boolean b, boolean l, boolean r)
    {
        top = t;
        bottom = b;
        left = l;
        right = r;

        return this;
    }
}
