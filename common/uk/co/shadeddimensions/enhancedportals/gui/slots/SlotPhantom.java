/**
 * Derived from BuildCraft released under the MMPL
 * https://github.com/BuildCraft/BuildCraft
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package uk.co.shadeddimensions.enhancedportals.gui.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class SlotPhantom extends SlotBase implements IPhantomSlot
{
    public SlotPhantom(IInventory iinventory, int slotIndex, int posX, int posY)
    {
        super(iinventory, slotIndex, posX, posY);
    }

    @Override
    public boolean canAdjust()
    {
        return true;
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return false;
    }
}
