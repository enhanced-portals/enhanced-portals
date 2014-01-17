package uk.co.shadeddimensions.ep3.client.gui.elements;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.library.gui.GuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementButton;

public class ElementIconToggleButton extends ElementButton
{
    Icon icon;
    boolean selected;

    public ElementIconToggleButton(GuiBase parent, int x, int y, String id, Icon icon)
    {
        super(parent, x, y, 0, id, null, "");
        this.icon = icon;
        this.sizeY = icon.getIconHeight() + 2;
        this.sizeX = icon.getIconWidth() + 2;
    }
    
    public void setSelected(boolean b)
    {
        selected = b;
    }
    
    @Override
    public void draw()
    {
        if (selected)
        {
            Gui.drawRect(posX, posY, posX + sizeX, posY + sizeY, 0xFFACACAC);
        }
        
        gui.drawIcon(icon, posX + 1, posY + 1, 0);
    }
}
