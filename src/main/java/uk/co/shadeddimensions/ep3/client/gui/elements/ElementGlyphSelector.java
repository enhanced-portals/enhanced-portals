package uk.co.shadeddimensions.ep3.client.gui.elements;

import java.util.ArrayList;

import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.library.gui.IGuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementBaseContainer;

public class ElementGlyphSelector extends ElementBaseContainer
{
    ArrayList<Integer> selectedGlyphs = new ArrayList<Integer>();
    int[] glyphCount = new int[27];

    public ElementGlyphSelector(IGuiBase parent, int x, int y)
    {
        super(parent, x, y, 162, 54);

        for (int i = 0; i < 27; i++)
        {
            addElement(new ElementGlyph(parent, i % 9 * 18, i / 9 * 18, i));
        }
    }

    public void addGlyph(int i)
    {
        if (selectedGlyphs.size() < 9)
        {
            selectedGlyphs.add(i);
            glyphCount[i]++;
        }
    }

    @Override
    public void draw()
    {
        super.draw();

        for (int i = 0; i < glyphCount.length; i++)
        {
            String s = "" + (glyphCount[i] > 0 ? glyphCount[i] : "");
            gui.getFontRenderer().drawStringWithShadow(s, posX + i % 9 * 18 + 17 - gui.getFontRenderer().getStringWidth(s), posY + i / 9 * 18 + 9, 0xFFFFFF);
        }
    }

    public GlyphIdentifier getGlyphIdentifier()
    {
        return new GlyphIdentifier(selectedGlyphs);
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (disabled)
        {
            return false;
        }
        
        for (int i = 0; i < elements.size(); i++)
        {
            if (elements.get(i).intersectsWith(x, y))
            {
                if (mouseButton == 0)
                {
                    if (selectedGlyphs.size() == 9)
                    {
                        return true;
                    }

                    addGlyph(i);
                }
                else
                {
                    if (glyphCount[i] == 0)
                    {
                        return true;
                    }
                    else
                    {
                        for (int j = selectedGlyphs.size() - 1; j >= 0; j--)
                        {
                            if (selectedGlyphs.get(j) == i)
                            {
                                removeGlyphAt(j);
                                break;
                            }
                        }
                    }
                }

                gui.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                return true;
            }
        }

        return false;
    }

    public void removeGlyphAt(int i)
    {
        glyphCount[selectedGlyphs.get(i)]--;
        selectedGlyphs.remove(i);
    }

    public void reset()
    {
        selectedGlyphs.clear();
        glyphCount = new int[27];
    }

    public void setIdentifierTo(GlyphIdentifier identifier)
    {
        reset();

        if (identifier != null && !identifier.isEmpty())
        {
            selectedGlyphs = new ArrayList<Integer>(identifier.getGlyphs());

            for (int i : selectedGlyphs)
            {
                glyphCount[i]++;
            }
        }
    }
}
