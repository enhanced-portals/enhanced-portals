package uk.co.shadeddimensions.library.gui.element;

import java.util.List;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementText extends ElementBase
{
    int colour;
    String displayText, hoverText;
    boolean shadow;

    public ElementText(IGuiBase parent, int x, int y, String text, String hover)
    {
        this(parent, x, y, text, hover, 0x404040, false);
    }

    public ElementText(IGuiBase parent, int x, int y, String text, String hover, int c, boolean s)
    {
        super(parent, x, y, parent.getFontRenderer().getStringWidth(text), parent.getFontRenderer().FONT_HEIGHT);
        displayText = text;
        hoverText = hover;
        colour = c;
        shadow = s;
    }

    @Override
    public void addTooltip(List<String> list)
    {
        if (hoverText != null)
        {
            list.add(hoverText);
        }
    }

    @Override
    public void draw()
    {
        if (shadow)
        {
            gui.getFontRenderer().drawStringWithShadow(displayText, posX, posY, colour);
        }
        else
        {
            gui.getFontRenderer().drawString(displayText, posX, posY, colour);
        }
    }
}
