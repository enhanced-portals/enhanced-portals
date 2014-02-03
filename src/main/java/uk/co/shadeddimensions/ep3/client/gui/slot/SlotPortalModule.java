package uk.co.shadeddimensions.ep3.client.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.item.ItemPortalModule;

public class SlotPortalModule extends Slot
{
    public SlotPortalModule(IInventory par1iInventory, int par2, int par3, int par4)
    {
        super(par1iInventory, par2, par3, par4);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack == null || stack.getItem() instanceof ItemPortalModule;
    }
}
