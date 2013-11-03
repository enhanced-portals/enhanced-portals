package uk.co.shadeddimensions.ep3.client.gui.elements;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.client.gui.GuiDiallingDevice;
import uk.co.shadeddimensions.ep3.client.gui.base.GuiEnhancedPortals;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;

public class GuiGlyphIdentifierSelector extends Gui
{
    public static final int MAX_GLYPHS = 27, GLYPHS_PER_ROW = 9;
    
    protected int x, y;
    protected GuiEnhancedPortals parent;
    protected GlyphIdentifier selectedGlyphs;
    protected int[] counter;
    
    public GuiGlyphIdentifierSelector(int xPos, int yPos, GuiEnhancedPortals gui)
    {
        x = xPos;
        y = yPos;
        parent = gui;
        selectedGlyphs = new GlyphIdentifier();
        counter = new int[MAX_GLYPHS];
    }
    
    public GlyphIdentifier getSelectedIdentifier()
    {
        return new GlyphIdentifier(selectedGlyphs);
    }
    
    public void setGlyphsToIdentifier(GlyphIdentifier identifier)
    {
        if (identifier == null)
        {
            identifier = new GlyphIdentifier();
        }
        else
        {
            identifier = new GlyphIdentifier(identifier); // Duplicate it, so we're not editing the same identifier
        }
        
        selectedGlyphs = identifier;
        counter = new int[MAX_GLYPHS];
        
        for (int i : selectedGlyphs.getGlyphs())
        {
            counter[i]++;
        }
    }
    
    public void drawBackground(int mouseX, int mouseY)
    {
        parent.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(parent.getGuiLeft() + x, parent.getGuiTop() + y, 0, 0, 162, 54);
        
        parent.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/glyphs.png"));
        for (int i = 0; i < MAX_GLYPHS; i++)
        {
            int xPos = parent.getGuiLeft() + x + (i % GLYPHS_PER_ROW) * 18, yPos = parent.getGuiTop() + y + (i / GLYPHS_PER_ROW) * 18;            
            boolean hover = isMouseOver(i, mouseX, mouseY) && (!(parent instanceof GuiDiallingDevice) || !((GuiDiallingDevice) parent).showOverlay);
            
            if (hover)
            {
                parent.drawRectangle(xPos + 1, yPos + 1, 16, 16, 0xAAFFFFFF, false);
            }
            
            drawTexturedModalRect(xPos + 1, yPos + 1, (i % 9) * 16, (isSelected(i) || hover ? 0 : 48) + (i / 9) * 16, 16, 16);
            
            if (counter[i] > 0)
            {
                String s = "" + counter[i];
                parent.getFontRenderer().drawStringWithShadow(s, xPos + 18 - parent.getFontRenderer().getStringWidth(s), yPos + 18 - parent.getFontRenderer().FONT_HEIGHT, 0xFFFFFF);
                parent.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/glyphs.png"));
            }
        }
    }
    
    protected boolean isSelected(int glyph)
    {
        return selectedGlyphs.hasGlyph(glyph);
    }
    
    protected boolean isMouseOver(int glyph, int mouseX, int mouseY)
    {
        int xPos = 1 + parent.getGuiLeft() + x + (glyph % GLYPHS_PER_ROW) * 18, yPos = 1 + parent.getGuiTop() + y + (glyph / GLYPHS_PER_ROW) * 18;
        
        return mouseX >= xPos && mouseX <= xPos + 16 && mouseY >= yPos && mouseY <= yPos + 16;
    }
    
    public void drawForeground(int mouseX, int mouseY)
    {
        
    }
    
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        for (int i = 0; i < MAX_GLYPHS; i++)
        {
            if (isMouseOver(i, mouseX, mouseY))
            {
                glyphClicked(i, mouseButton);
            }
        }
    }
    
    protected void glyphClicked(int glyph, int mouseButton)
    {
        if (mouseButton == 0 && selectedGlyphs.size() < 9)
        {
            counter[glyph]++;
            selectedGlyphs.addGlyph(glyph);
        }
        else if (mouseButton == 1 && !selectedGlyphs.isEmpty())
        {
            if (selectedGlyphs.hasGlyph(glyph))
            {
                for (int i = selectedGlyphs.size() - 1; i >= 0; i--)
                {
                    if (selectedGlyphs.get(i) == glyph)
                    {
                        counter[glyph]--;
                        selectedGlyphs.remove(i);
                        break;
                    }
                }
            }
        }
    }

    public void clearSelection()
    {
        selectedGlyphs.getGlyphs().clear();
        counter = new int[MAX_GLYPHS];
    }

    public void randomize(boolean ctrlKeyDown)
    {
        selectedGlyphs = new GlyphIdentifier();
        counter = new int[MAX_GLYPHS];
        
        for (int i = 0; i < (ctrlKeyDown ? 9 : (ClientProxy.random.nextInt(9) + 1)); i++)
        {
            int glyph = ClientProxy.random.nextInt(MAX_GLYPHS);
            selectedGlyphs.addGlyph(glyph);
            counter[glyph]++;
        }
    }
}
