package uk.co.shadeddimensions.library.gui.element;

import java.util.ArrayList;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementTextClickable extends ElementButton
{
	public ElementTextClickable(IGuiBase parent, int x, int y, String id, String text)
    {
	    super(parent, x, y, parent.getFontRenderer().getStringWidth(text), parent.getFontRenderer().FONT_HEIGHT, id, text, "");
    }

    public ElementTextClickable(IGuiBase parent, int x, int y, String id, String text, ArrayList<String> hover)
    {
        super(parent, x, y, parent.getFontRenderer().getStringWidth(text), parent.getFontRenderer().FONT_HEIGHT, id, text, hover);
    }

    public ElementTextClickable(IGuiBase parent, int x, int y, String id, String text, String hover)
    {
        super(parent, x, y, parent.getFontRenderer().getStringWidth(text), parent.getFontRenderer().FONT_HEIGHT, id, text, hover);
    }
    
    @Override
    public void draw()
    {
    	gui.getFontRenderer().drawStringWithShadow(displayText, posX, posY, !isDisabled() ? intersectsWith(gui.getMouseX(), gui.getMouseY()) ? 16777120 : 14737632 : -6250336);
    }
}
