package uk.co.shadeddimensions.ep3.client.gui.New.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;

public class ElementGlyphViewer extends ElementBase
{
    ElementGlyphSelector selector;
    
    public ElementGlyphViewer(GuiBase gui, ElementGlyphSelector sel, int posX, int posY)
    {
        super(gui, posX, posY);
        selector = sel;
        sizeX = 161;
        sizeY = 17;
    }

    @Override
    public void draw()
    {
        int mouseX = gui.getMouseX(), mouseY = gui.getMouseY();

        if (intersectsWith(mouseX, mouseY))
        {
            int x = ((mouseX - posX + gui.getGuiLeft()) / 18), y = ((mouseY - posY + gui.getGuiTop()) / 18);
            Gui.drawRect(posX + (x * 18) + 1, posY + (y * 18) + 1, posX + 17 + (x * 18), posY + 17 + (y * 18), 0x88FFFFFF);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/glyphs" + (CommonProxy.useAlternateGlyphs ? "_2" : "") + ".png"));
        
        for (int i = 0; i < selector.getGlyphIdentifier().size(); i++)
        {
            int glyph = selector.getGlyphIdentifier().get(i);
            drawTexturedModalRect(posX + (i % 9) * 18, posY, (glyph % 9) * 18, (glyph / 9) * 18, 18, 18);
        }
    }
    
    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (intersectsWith(x, y))
        {            
            int element = ((x - posX + gui.getGuiLeft()) / 18);
            selector.removeGlyph(element);
            return true;
        }

        return false;
    }

    @Override
    public String getTooltip()
    {
        return null;
    }
}
