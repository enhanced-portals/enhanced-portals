package uk.co.shadeddimensions.library.gui.element;

import java.util.List;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementTextBox extends ElementText
{
    List<String> textStrings;

    public ElementTextBox(IGuiBase parent, int x, int y, String text, int width)
    {
        this(parent, x, y, text, width, 0x404040, false);
    }

    @SuppressWarnings("unchecked")
    public ElementTextBox(IGuiBase parent, int x, int y, String text, int width, int c, boolean s)
    {
        super(parent, x, y, text, null, c, s);

        textStrings = parent.getFontRenderer().listFormattedStringToWidth(text, width);
        sizeX = width;
        sizeY = textStrings.size() * parent.getFontRenderer().FONT_HEIGHT;
    }

    @Override
    public void draw()
    {
        int y = 0;

        for (String s : textStrings)
        {
            if (shadow)
            {
                gui.getFontRenderer().drawStringWithShadow(s, posX, posY + y, colour);
            }
            else
            {
                gui.getFontRenderer().drawString(s, posX, posY + y, colour);
            }

            y += gui.getFontRenderer().FONT_HEIGHT;
        }
    }
}
