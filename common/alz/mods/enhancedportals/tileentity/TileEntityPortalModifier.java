package alz.mods.enhancedportals.tileentity;

import alz.mods.enhancedportals.helpers.EntityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPortalModifier extends TileEntity implements IInventory
{
	public int Colour, Frequency;
	public boolean HadPower, TestSynch;
	ItemStack[] inv;
	
	public TileEntityPortalModifier()
	{
		inv = new ItemStack[3];
		TestSynch = false;
	}
	
	public boolean hasUpgrade(int id)
	{
		for (ItemStack stack : inv)
		{
			if (stack != null && stack.getItemDamage() == id)
				return true;
		}
		
		return false;
	}
		
	public boolean hasFreeSpace()
	{
		if (inv[0] != null && inv[1] != null && inv[2] != null)
			return false;
		
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		
		Colour = tag.getInteger("colour");
		Frequency = tag.getInteger("freq");
		HadPower = tag.getBoolean("power");
		
		NBTTagList tagList = tag.getTagList("inventory");
		
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound theTag = (NBTTagCompound) tagList.tagAt(i);			
			byte slot = theTag.getByte("slot");
			
			if (slot >= 0 && slot < inv.length)
				inv[slot] = ItemStack.loadItemStackFromNBT(theTag);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		
		tag.setInteger("colour", Colour);
		tag.setInteger("freq", Frequency);
		tag.setBoolean("power", HadPower);
		
		NBTTagList tagList = new NBTTagList();
		
		for (int i = 0; i < inv.length; i++)
		{
			ItemStack stack = inv[i];
			
			if (stack == null)
				continue;
			
			NBTTagCompound theTag = new NBTTagCompound();
			theTag.setByte("slot", (byte)i);
			stack.writeToNBT(theTag);
			tagList.appendTag(theTag);
		}
		
		tag.setTag("inventory", tagList);
	}

	@Override
	public int getSizeInventory()
	{
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inv[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		ItemStack stack = getStackInSlot(slot);
		
		if (stack != null)
		{
			if (stack.stackSize <= amount)
				setInventorySlotContents(slot, null);
			else
			{
				stack = stack.splitStack(amount);
				
				if (stack.stackSize == 0)
					setInventorySlotContents(slot, null);
			}
		}
		
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		
		if (stack != null)
			setInventorySlotContents(slot, null);
		
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		inv[slot] = stack;
		
		if (stack != null && stack.stackSize > getInventoryStackLimit())
			stack.stackSize = getInventoryStackLimit();
	}

	@Override
	public String getInvName()
	{
		return "alz.ep.modifier";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() { }

	@Override
	public void closeChest() { }

	// ?
	@Override
	public boolean func_94042_c()
	{
		return false;
	}

	@Override
	public boolean func_94041_b(int i, ItemStack itemstack)
	{
		return EntityHelper.canAcceptItemStack(this, itemstack);
	}	
}
