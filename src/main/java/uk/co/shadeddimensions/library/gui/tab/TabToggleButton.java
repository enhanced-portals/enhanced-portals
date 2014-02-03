package uk.co.shadeddimensions.library.gui.tab;

import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.library.gui.IGuiBase;

public class TabToggleButton extends TabBase
{
    int closedColour = 0x666666, openColour = 0xBBBBBB;

    public TabToggleButton(IGuiBase gui, int side, ItemStack item)
    {
        this(gui, side, item.getUnlocalizedName(), item.getDisplayName(), item);
    }

    public TabToggleButton(IGuiBase gui, int side, String ID, String name, ItemStack item)
    {
        super(gui, side);
        this.name = name;
        maxWidth = Math.min(maxWidth, gui.getFontRenderer().getStringWidth(name) + 30);
        backgroundColor = closedColour;
        titleColour = 0xFFFFFF;
        stack = item;
        this.ID = ID;

        if (stack == null)
        {
            maxWidth -= 20;
        }
    }

    public TabToggleButton(IGuiBase gui, ItemStack item)
    {
        this(gui, 0, item.getUnlocalizedName(), item.getDisplayName(), item);
    }

    public TabToggleButton(IGuiBase gui, String ID, String name, ItemStack item)
    {
        this(gui, 0, ID, name, item);
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (isFullyOpened())
        {
            return true;
        }

        return false;
    }

    @Override
    public void setFullyOpen()
    {
        super.setFullyOpen();
        backgroundColor = openColour;
    }

    @Override
    public void toggleOpen()
    {
        if (open)
        {
            open = false;
            backgroundColor = closedColour;
        }
        else
        {
            open = true;
            gui.handleElementButtonClick(ID, 0);
        }
    }
}
