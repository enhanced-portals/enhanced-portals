/**
 * Derived from BuildCraft released under the MMPL
 * https://github.com/BuildCraft/BuildCraft
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package uk.co.shadeddimensions.enhancedportals.gui.tooltips;

public class ToolTipLine
{
    public String text;
    public final int color;
    public int spacing;

    public ToolTipLine()
    {
        this("", -1);
    }

    public ToolTipLine(String text)
    {
        this(text, -1);
    }

    public ToolTipLine(String text, int color)
    {
        this.text = text;
        this.color = color;
    }

    public int getSpacing()
    {
        return spacing;
    }

    public void setSpacing(int spacing)
    {
        this.spacing = spacing;
    }
}
