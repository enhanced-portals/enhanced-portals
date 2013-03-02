package alz.mods.enhancedportals.common;

import alz.mods.enhancedportals.client.GuiModifierSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPortalModifier extends Container
{
	protected TileEntityPortalModifier tileEntity;
	
	public ContainerPortalModifier(InventoryPlayer inventoryPlayer, TileEntityPortalModifier modifier)
	{
		tileEntity = modifier;
		
		addSlotToContainer(new GuiModifierSlot(tileEntity, 0, 8, 46));
		addSlotToContainer(new GuiModifierSlot(tileEntity, 1, 26, 46));
		addSlotToContainer(new GuiModifierSlot(tileEntity, 2, 44, 46));
		
		bindPlayerInventory(inventoryPlayer);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileEntity.isUseableByPlayer(player);
	}

	protected void bindPlayerInventory(InventoryPlayer player)
	{
		for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
            	addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
    
	    for (int i = 0; i < 9; i++)
	            addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
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
            
            if (slot >= 3 && stackInSlot.itemID == EnhancedPortals.instance.itemUpgrade.itemID)
            {
            	if (!this.mergeItemStack(stackInSlot, 0, 3, true))
            		return null;
            }
            else if (slot < 3 && stackInSlot.itemID == EnhancedPortals.instance.itemUpgrade.itemID)
            {
            	if (!this.mergeItemStack(stackInSlot, 3, 37, true))
            		return null;
            }
            else
            	return null;
            
			if (stackInSlot.stackSize == 0)
                slotObject.putStack(null);
	        else
	        	slotObject.onSlotChanged();
	        	        
	        slotObject.onPickupFromSlot(player, stackInSlot);
		}
		
		return stack;
	}
}
