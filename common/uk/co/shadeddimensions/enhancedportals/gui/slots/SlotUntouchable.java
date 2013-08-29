/**
 * Derived from BuildCraft released under the MMPL
 * https://github.com/BuildCraft/BuildCraft
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package uk.co.shadeddimensions.enhancedportals.gui.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotUntouchable extends SlotBase implements IPhantomSlot
{
    public SlotUntouchable(IInventory contents, int id, int x, int y)
    {
        super(contents, id, x, y);
    }

    @Override
    public boolean canAdjust()
    {
        return false;
    }

    @Override
    public boolean canShift()
    {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return false;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }
}
