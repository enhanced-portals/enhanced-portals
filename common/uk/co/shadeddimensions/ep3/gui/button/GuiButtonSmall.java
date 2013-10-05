/**
 * Derived from BuildCraft released under the MMPL https://github.com/BuildCraft/BuildCraft http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package uk.co.shadeddimensions.ep3.gui.button;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonSmall extends GuiBetterButton
{
    public GuiButtonSmall(int i, int x, int y, int w, String s)
    {
        super(i, x, y, w, StandardButtonTextureSets.SMALL_BUTTON, s);
    }

    public GuiButtonSmall(int i, int x, int y, String s)
    {
        this(i, x, y, 200, s);
    }
}
