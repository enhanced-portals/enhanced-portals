package uk.co.shadeddimensions.enhancedportals.gui.slots;

import uk.co.shadeddimensions.enhancedportals.portal.StackHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotPortalTexture extends SlotPhantom
{
    public SlotPortalTexture(IInventory iinventory, int slotIndex, int posX, int posY)
    {
        super(iinventory, slotIndex, posX, posY);
    }
    
    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return StackHelper.isItemStackValidForPortalTexture(stack) || stack == null;
    }
}
