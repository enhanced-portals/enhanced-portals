package uk.co.shadeddimensions.ep3.client.gui.elements;

import uk.co.shadeddimensions.library.gui.GuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementBaseContainer;

public class ElementGlyphIdentifier extends ElementBaseContainer
{
    ElementGlyphSelector selector;

    public ElementGlyphIdentifier(GuiBase parent, int x, int y, ElementGlyphSelector sel)
    {
        super(parent, x, y, 162, 18);
        selector = sel;
    }

    @Override
    public void update()
    {
        elements.clear();

        int j = 0;            
        for (int i : selector.selectedGlyphs)
        {
            addElement(new ElementGlyph(gui, j, 0, i));
            j += 18;
        }
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        for (int i = 0; i < elements.size(); i++)
        {
            if (elements.get(i).intersectsWith(x, y))
            {
                selector.removeGlyphAt(i);
                gui.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);

                return true;
            }
        }

        return false;
    }
}
