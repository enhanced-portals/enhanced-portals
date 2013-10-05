package uk.co.shadeddimensions.ep3.gui.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.api.IPortalModule;
import uk.co.shadeddimensions.ep3.portal.StackHelper;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;

public class SlotPortalModule extends Slot
{
    public SlotPortalModule(TileModuleManipulator tile, int slotIndex, int posX, int posY)
    {
        super(tile, slotIndex, posX, posY);
    }

    @Override
    public boolean canTakeStack(EntityPlayer entityPlayer)
    {
        return getStack() == null ? true : ((IPortalModule) getStack().getItem()).canRemoveUpgrade((TileModuleManipulator) inventory, new IPortalModule[] {}, getStack());
    }

    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        return itemStack == null || StackHelper.isUpgrade(itemStack) && !((TileModuleManipulator) inventory).hasModule(((IPortalModule) itemStack.getItem()).getID(itemStack));
    }

    @Override
    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack stack)
    {
        if (getStack() != null)
        {
            ((IPortalModule) getStack().getItem()).onUpgradeRemoved((TileModuleManipulator) inventory, stack);
        }

        super.onPickupFromSlot(entityPlayer, stack);
    }
}
