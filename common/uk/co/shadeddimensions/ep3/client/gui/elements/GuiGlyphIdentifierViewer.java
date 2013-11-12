package uk.co.shadeddimensions.ep3.client.gui.elements;

import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.client.gui.New.GuiDiallingDevice;
import uk.co.shadeddimensions.ep3.client.gui.base.GuiEnhancedPortals;

public class GuiGlyphIdentifierViewer extends GuiGlyphIdentifierSelector
{
    GuiGlyphIdentifierSelector selector;

    public GuiGlyphIdentifierViewer(int xPos, int yPos, GuiEnhancedPortals gui, GuiGlyphIdentifierSelector sel)
    {
        super(xPos, yPos, gui);
        selector = sel;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        parent.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(parent.getGuiLeft() + x, parent.getGuiTop() + y, 0, 0, 162, 18);
        
        parent.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/glyphs.png"));
        for (int i = 0; i < selector.selectedGlyphs.size(); i++)
        {
            int xPos = parent.getGuiLeft() + x + (i % GLYPHS_PER_ROW) * 18, yPos = parent.getGuiTop() + y + (i / GLYPHS_PER_ROW) * 18, glyph = selector.selectedGlyphs.get(i);
            
            if (isMouseOver(i, mouseX, mouseY))
            {
                parent.drawRectangle(xPos + 1, yPos + 1, 16, 16, 0xAAFFFFFF, false);
            }
            
            drawTexturedModalRect(xPos + 1, yPos + 1, (glyph % 9) * 16, (glyph / 9) * 16, 16, 16);
        }
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        for (int i = 0; i < selector.selectedGlyphs.size(); i++)
        {
            if (isMouseOver(i, mouseX, mouseY))
            {
                if (mouseButton < 2)
                {
                    selector.counter[selector.selectedGlyphs.get(i)]--;
                    selector.selectedGlyphs.remove(i);
                }
            }
        }
    }
}
