/**
 * Derived from BuildCraft released under the MMPL https://github.com/BuildCraft/BuildCraft http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package uk.co.shadeddimensions.ep3.client.gui.button;

public enum StandardButtonTextureSets implements IButtonTextureSet
{
    LARGE_BUTTON(0, 0, 20, 200), SMALL_BUTTON(0, 80, 15, 200), BACK_BUTTON(224, 96, 16, 16), FORWARD_BUTTON(240, 96, 16, 16);
    private final int x, y, height, width;

    private StandardButtonTextureSets(int x, int y, int height, int width)
    {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
    }
}
