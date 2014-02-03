package uk.co.shadeddimensions.library.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container
{
    public Object object;

    public ContainerBase()
    {

    }

    public ContainerBase(Object obj)
    {
        object = obj;
    }

    public ContainerBase addPlayerInventorySlots(EntityPlayer player)
    {
        // TODO
        return this;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return object instanceof IInventory ? ((IInventory) object).isUseableByPlayer(entityplayer) : true;
    }

    public String getUnlocalizedName()
    {
        return "container.empty";
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startPos, int endPos, boolean reverse)
    {
        boolean flag1 = false;
        int k = startPos;

        if (reverse)
        {
            k = endPos - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (stack.isStackable())
        {
            while (stack.stackSize > 0 && (!reverse && k < endPos || reverse && k >= startPos))
            {
                slot = (Slot) inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (!slot.isItemValid(stack))
                {
                    if (reverse)
                    {
                        --k;
                    }
                    else
                    {
                        ++k;
                    }

                    continue;
                }

                if (itemstack1 != null && itemstack1.itemID == stack.itemID && (!stack.getHasSubtypes() || stack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, itemstack1))
                {
                    int l = itemstack1.stackSize + stack.stackSize;

                    if (l <= stack.getMaxStackSize())
                    {
                        stack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < stack.getMaxStackSize())
                    {
                        stack.stackSize -= stack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = stack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (reverse)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        if (stack.stackSize > 0)
        {
            if (reverse)
            {
                k = endPos - 1;
            }
            else
            {
                k = startPos;
            }

            while (!reverse && k < endPos || reverse && k >= startPos)
            {
                slot = (Slot) inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (!slot.isItemValid(stack))
                {
                    if (reverse)
                    {
                        --k;
                    }
                    else
                    {
                        ++k;
                    }

                    continue;
                }

                if (itemstack1 == null)
                {
                    slot.putStack(stack.copy());
                    slot.onSlotChanged();
                    stack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                if (reverse)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        return flag1;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        if (object instanceof IInventory)
        {
            int containerSlotCount = ((IInventory) object).getSizeInventory();
            ItemStack stack = null;
            Slot slotObject = (Slot) inventorySlots.get(slot);

            if (slotObject != null && slotObject.getHasStack())
            {
                ItemStack stackInSlot = slotObject.getStack();
                stack = stackInSlot.copy();

                if (slot < containerSlotCount)
                {
                    if (!mergeItemStack(stackInSlot, 0, 35, true))
                    {
                        return null;
                    }
                }
                else if (!mergeItemStack(stackInSlot, 0, containerSlotCount, false))
                {
                    return null;
                }

                if (stackInSlot.stackSize == 0)
                {
                    slotObject.putStack(null);
                }
                else
                {
                    slotObject.onSlotChanged();
                }

                if (stackInSlot.stackSize == stack.stackSize)
                {
                    return null;
                }

                slotObject.onPickupFromSlot(player, stackInSlot);
            }

            return stack;
        }

        return null;
    }
}
