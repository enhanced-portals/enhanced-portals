package uk.co.shadeddimensions.library.gui.element;

import net.minecraft.client.gui.Gui;
import uk.co.shadeddimensions.library.gui.IGuiBase;

/***
 * Should be used behind standard GUI elements that appear on tabs, that aren't part of the module system.
 * @author Alz454
 */
public class ElementClickBlocker extends ElementBase
{
    int colour;
    
    public ElementClickBlocker(IGuiBase gui, int posX, int posY, int sizeX, int sizeY)
    {
        super(gui, posX, posY, sizeX, sizeY);
        this.colour = 0x66000000;
    }
    
    public ElementClickBlocker(IGuiBase gui, int posX, int posY, int sizeX, int sizeY, int colour)
    {
        super(gui, posX, posY, sizeX, sizeY);
        this.colour = colour;
    }

    @Override
    public void draw()
    {
        Gui.drawRect(posX, posY, posX + sizeX, posY + sizeY, colour);
    }
    
    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        return true;
    }
}
