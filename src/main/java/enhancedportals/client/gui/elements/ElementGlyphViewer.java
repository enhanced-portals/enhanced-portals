package enhancedportals.client.gui.elements;

import java.util.List;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import enhancedportals.client.gui.BaseGui;

public class ElementGlyphViewer extends BaseElement
{
    ElementGlyphSelector selector;

    public ElementGlyphViewer(BaseGui gui, int x, int y, ElementGlyphSelector s)
    {
        super(gui, x, y, 162, 18);
        selector = s;
        texture = new ResourceLocation("enhancedportals", "textures/gui/player_inventory.png");
    }

    @Override
    protected void drawBackground()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        parent.getMinecraft().renderEngine.bindTexture(texture);
        parent.drawTexturedModalRect(posX, posY, 7, 7, sizeX, sizeY);
    }

    @Override
    public void addTooltip(List<String> list)
    {

    }

    @Override
    protected void drawContent()
    {
        parent.getMinecraft().renderEngine.bindTexture(ElementGlyphSelector.glyphs);

        for (int i = 0; i < 9; i++)
        {
            if (selector.selectedGlyphs.size() <= i)
            {
                break;
            }

            int glyph = selector.selectedGlyphs.get(i), X2 = i % 9 * 18, X = glyph % 9 * 18, Y = glyph / 9 * 18, mouseX = parent.getMouseX() + parent.getGuiLeft(), mouseY = parent.getMouseY() + parent.getGuiTop();

            if (mouseX >= posX + X2 && mouseX < posX + X2 + 18 && mouseY >= posY && mouseY < posY + 18)
            {
                parent.drawRect(posX + X2 + 1, posY + 1, posX + X2 + 17, posY + 17, 0x66FFFFFF);
            }

            drawTexturedModalRect(posX + X2, posY, X, Y, 18, 18);
        }
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        for (int i = 0; i < selector.selectedGlyphs.size(); i++)
        {
            int X = posX - parent.getGuiLeft() + ((i % 9) * 18);

            if (x >= X && x < X + 18)
            {
                selector.removeGlyphAt(i);
            }
        }

        return false;
    }

    @Override
    public void update()
    {

    }
}
