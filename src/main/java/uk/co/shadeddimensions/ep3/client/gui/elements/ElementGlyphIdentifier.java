package uk.co.shadeddimensions.ep3.client.gui.elements;

import uk.co.shadeddimensions.library.gui.IGuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementBaseContainer;

public class ElementGlyphIdentifier extends ElementBaseContainer
{
    ElementGlyphSelector selector;

    public ElementGlyphIdentifier(IGuiBase parent, int x, int y, ElementGlyphSelector sel)
    {
        super(parent, x, y, 162, 18);
        selector = sel;
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (selector.isDisabled())
        {
            return false;
        }
        
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
}
