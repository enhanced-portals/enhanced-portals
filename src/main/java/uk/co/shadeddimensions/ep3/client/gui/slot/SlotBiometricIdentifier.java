package uk.co.shadeddimensions.ep3.client.gui.slot;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.item.ItemEntityCard;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileBiometricIdentifier;

public class SlotBiometricIdentifier extends Slot
{
    TileBiometricIdentifier biometric;

    public SlotBiometricIdentifier(TileBiometricIdentifier bio, int par2, int par3, int par4)
    {
        super(bio, par2, par3, par4);
        biometric = bio;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack == null || stack.itemID == ItemEntityCard.ID && stack.hasTagCompound() && stack.getTagCompound().hasKey("entities");
    }

    @Override
    public void onSlotChanged()
    {
        super.onSlotChanged();
        ItemStack s = getStack();

        if (s != null)
        {
            biometric.applyBiometricFilters(getSlotIndex(), s);
            biometric.setInventorySlotContents(getSlotIndex(), null);
        }
    }
}
