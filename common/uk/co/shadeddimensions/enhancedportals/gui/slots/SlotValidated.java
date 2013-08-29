/**
 * Derived from BuildCraft released under the MMPL
 * https://github.com/BuildCraft/BuildCraft
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package uk.co.shadeddimensions.enhancedportals.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotValidated extends Slot
{
    public SlotValidated(IInventory inv, int id, int x, int y)
    {
        super(inv, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        return inventory.isItemValidForSlot(getSlotIndex(), itemStack);
    }
}
