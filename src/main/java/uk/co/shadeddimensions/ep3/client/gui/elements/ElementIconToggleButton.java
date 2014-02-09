package uk.co.shadeddimensions.ep3.client.gui.elements;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.library.gui.IGuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementButton;
import uk.co.shadeddimensions.library.util.GuiUtils;

public class ElementIconToggleButton extends ElementButton
{
    Icon icon;
    boolean selected;

    public ElementIconToggleButton(IGuiBase parent, int x, int y, String id, Icon icon)
    {
        super(parent, x, y, 0, id, null, "");
        this.icon = icon;
        this.sizeY = 18;
        this.sizeX = 18;
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
        
        GuiUtils.drawIcon(gui, icon, posX + 1, posY + 1, 0);
    }
}
