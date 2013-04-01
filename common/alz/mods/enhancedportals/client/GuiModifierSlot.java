package alz.mods.enhancedportals.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import alz.mods.enhancedportals.helpers.EntityHelper;

public class GuiModifierSlot extends Slot
{
	public GuiModifierSlot(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer)
	{
		return true;
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack)
	{
		return EntityHelper.canAcceptItemStack(inventory, itemStack);
	}
}
