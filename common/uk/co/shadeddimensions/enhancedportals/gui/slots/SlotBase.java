/**
 * Derived from BuildCraft released under the MMPL
 * https://github.com/BuildCraft/BuildCraft
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package uk.co.shadeddimensions.enhancedportals.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import uk.co.shadeddimensions.enhancedportals.gui.tooltips.ToolTip;

public class SlotBase extends Slot
{
    private ToolTip toolTips;

    public SlotBase(IInventory iinventory, int slotIndex, int posX, int posY)
    {
        super(iinventory, slotIndex, posX, posY);
    }

    public boolean canShift()
    {
        return true;
    }
    
    public ToolTip getToolTip()
    {
        return toolTips;
    }
    
    public void setToolTips(ToolTip toolTips)
    {
        this.toolTips = toolTips;
    }
}
