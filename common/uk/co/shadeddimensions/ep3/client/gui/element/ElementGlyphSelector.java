package uk.co.shadeddimensions.ep3.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;

public class ElementGlyphSelector extends ElementBase
{
    GlyphIdentifier identifier;
    int[] counter;
    int theCount;

    public ElementGlyphSelector(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
        sizeX = 161;
        sizeY = 53;
        counter = new int[27];
        theCount = 0;
        identifier = new GlyphIdentifier();
    }
    
    public ElementGlyphSelector(GuiBase gui, int posX, int posY, GlyphIdentifier i)
    {
        super(gui, posX, posY);
        sizeX = 161;
        sizeY = 53;
        counter = new int[27];
        theCount = 0;
        setIdentifierTo(i);
    }

    @Override
    public void draw()
    {
        if (!isVisible())
        {
            return;
        }

        int mouseX = gui.getMouseX(), mouseY = gui.getMouseY();

        if (intersectsWith(mouseX, mouseY))
        {
            int x = ((mouseX - posX + gui.getGuiLeft()) / 18), y = ((mouseY - posY + gui.getGuiTop()) / 18);
            Gui.drawRect(posX + (x * 18) + 1, posY + (y * 18) + 1, posX + 17 + (x * 18), posY + 17 + (y * 18), 0x88FFFFFF);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/glyphs" + (CommonProxy.useAlternateGlyphs ? "_2" : "") + ".png"));
        drawTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY);
        
        for (int i = 0; i < 27; i++)
        {
            int x = posX + (i % 9) * 18, y = posY + (i / 9) * 18, count = getGlyphCount(i);
            
            if (count > 0)
            {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("" + count, x + 18 - Minecraft.getMinecraft().fontRenderer.getStringWidth("" + count), y + 10, 0xFFFFFF);
            }
        }
    }

    private int getGlyphCount(int i)
    {
        return counter[i];
    }
    
    private void incrementGlyphCount(int i)
    {
        counter[i]++;
        theCount++;
        identifier.addGlyph(i);
    }
    
    private void decrementGlyphCount(int i)
    {
        counter[i]--;
        theCount--;
        identifier.removeLast(i);
    }
        
    public void reset()
    {
        counter = new int[27];
        theCount = 0;
        identifier = new GlyphIdentifier();
    }
    
    public void setIdentifierTo(GlyphIdentifier i)
    {
        reset();
        identifier = i == null ? new GlyphIdentifier() : new GlyphIdentifier(i);
        theCount = identifier.size();
        
        for (int j = 0; j < theCount; j++)
        {
            counter[identifier.get(j)]++;
        }
    }
    
    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (!isVisible())
        {
            return false;
        }
        
        if (intersectsWith(x, y))
        {
            int X = ((x - posX + gui.getGuiLeft()) / 18), Y = ((y - posY + gui.getGuiTop()) / 18), element = (Y * 9) + X;
    
            if (element >= 0 && element <= 27)
            {
                if (mouseButton == 0 && theCount < 9)
                {
                    incrementGlyphCount(element);
                    return true;
                }
                else if (mouseButton == 1 && getGlyphCount(element) > 0)
                {
                    decrementGlyphCount(element);
                    return true;
                }
            }
        }
        
        return false;
    }

    public GlyphIdentifier getGlyphIdentifier()
    {
        return identifier;
    }

    @Override
    public String getTooltip()
    {
        return null;
    }

    public void removeGlyph(int index)
    {
        if (index >= 0 && index < identifier.size())
        {
            int glyph = identifier.get(index);
            theCount--;
            counter[glyph]--;
            identifier.remove(index);
        }
    }
}
