/**
 * Derived from BuildCraft released under the MMPL https://github.com/BuildCraft/BuildCraft http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package uk.co.shadeddimensions.ep3.gui.slots;

import net.minecraft.inventory.IInventory;

public class SlotLimited extends SlotBase
{
    private final int limit;

    public SlotLimited(IInventory iinventory, int slotIndex, int posX, int posY, int limit)
    {
        super(iinventory, slotIndex, posX, posY);
        this.limit = limit;
    }

    @Override
    public int getSlotStackLimit()
    {
        return limit;
    }

}
