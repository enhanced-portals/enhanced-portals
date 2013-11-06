package uk.co.shadeddimensions.ep3.client.gui.elements;

import uk.co.shadeddimensions.ep3.client.gui.GuiBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import net.minecraft.client.gui.Gui;

public class GuiEntitySelectionList extends Gui
{
    int x, y;
    GuiBiometricIdentifier parent;
    TileBiometricIdentifier biometric;
    boolean type;
    
    float scrolled;
    
    public GuiEntitySelectionList(int X, int Y, GuiBiometricIdentifier gui, TileBiometricIdentifier tile, boolean t)
    {
        x = X;
        y = Y;
        parent = gui;
        biometric = tile;
        type = t;
    }
    
    public void drawBackground(int mX, int mY)
    {
        if (!biometric.hasSeperateLists && !type)
        {
            mX = mY = -100;
        }
        
        parent.drawRectangle(x, y, 100, 150, 0xFFFFFFFF, true);
        
        if (!biometric.hasSeperateLists && !type)
        {
            parent.drawRectangle(x, y, 100, 150, 0xAA000000, true);
        }
    }
    
    public void drawForeground(int mX, int mY)
    {
        
    }
    
    public void mouseClicked(int mX, int mY, int mouseButton)
    {
        
    }
}
