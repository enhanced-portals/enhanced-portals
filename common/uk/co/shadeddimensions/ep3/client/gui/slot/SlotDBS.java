package uk.co.shadeddimensions.ep3.client.gui.slot;

import uk.co.shadeddimensions.ep3.network.CommonProxy;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import cofh.gui.slot.SlotEnergy;

public class SlotDBS extends SlotEnergy
{
    public SlotDBS(IInventory inventory, int x, int y, int z)
    {
        super(inventory, x, y, z);
    }
    
    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return super.isItemValid(stack) || stack.itemID == CommonProxy.itemLocationCard.itemID;
    }
}
