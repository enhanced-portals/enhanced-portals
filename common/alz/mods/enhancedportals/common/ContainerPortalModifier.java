package alz.mods.enhancedportals.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import alz.mods.enhancedportals.client.gui.GuiModifierSlot;
import alz.mods.enhancedportals.helpers.EntityHelper;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;

public class ContainerPortalModifier extends Container
{
    protected TileEntityPortalModifier tileEntity;

    public ContainerPortalModifier(InventoryPlayer inventoryPlayer, TileEntityPortalModifier modifier)
    {
        tileEntity = modifier;

        addSlotToContainer(new GuiModifierSlot(tileEntity, 0, 152, 55));
        addSlotToContainer(new GuiModifierSlot(tileEntity, 1, 152, 35));
        addSlotToContainer(new GuiModifierSlot(tileEntity, 2, 152, 15));

        bindPlayerInventory(inventoryPlayer);
    }

    protected void bindPlayerInventory(InventoryPlayer player)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return tileEntity.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        if (slotObject != null && slotObject.getHasStack())
        {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            if (slot >= 3 && stackInSlot.itemID == Reference.ItemIDs.PortalModifierUpgrade + 256)
            {
                if (!EntityHelper.canAcceptItemStack(tileEntity, stack))
                {
                    return null;
                }

                if (!mergeItemStack(stackInSlot, 0, 3, true))
                {
                    return null;
                }
            }
            else if (slot <= 3)
            {
                if (!mergeItemStack(stackInSlot, 3, 37, true))
                {
                    return null;
                }
            }
            else
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

            slotObject.onPickupFromSlot(player, stackInSlot);
        }

        return stack;
    }
}
