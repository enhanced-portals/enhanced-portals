package uk.co.shadeddimensions.ep3.client.gui.elements;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.library.gui.IGuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementBase;

public class ElementGlyph extends ElementBase
{
    int glyph;

    public ElementGlyph(IGuiBase gui, int posX, int posY, int glyph)
    {
        super(gui, posX, posY, 18, 18);
        this.glyph = glyph;
        texture = new ResourceLocation("enhancedportals", CommonProxy.useAlternateGlyphs ? "textures/gui/glyphs_2.png" : "textures/gui/glyphs.png");
    }

    @Override
    public void draw()
    {
        if (glyph == -1)
        {
            return;
        }

        if (!isDisabled() && intersectsWith(gui.getMouseX(), gui.getMouseY()))
        {
            Gui.drawRect(posX + 1, posY + 1, posX + sizeX - 1, posY + sizeY - 1, 0x44FFFFFF);
        }

        GL11.glColor3f(1f, 1f, 1f);
        gui.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(posX, posY, glyph % 9 * sizeX, glyph / 9 * sizeY, sizeX, sizeY);
    }
}
