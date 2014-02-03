package uk.co.shadeddimensions.library.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Slot which prevents player interaction.
 * 
 * @author King Lemming
 * 
 */
public class SlotLocked extends Slot
{
    public SlotLocked(IInventory inventory, int x, int y, int z)
    {
        super(inventory, x, y, z);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player)
    {
        return false;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }
}
