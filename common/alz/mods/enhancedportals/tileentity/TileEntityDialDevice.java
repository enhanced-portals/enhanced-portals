package alz.mods.enhancedportals.tileentity;

import java.util.ArrayList;
import java.util.List;

import alz.mods.enhancedportals.portals.PortalData;
import alz.mods.enhancedportals.portals.TeleportData;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDialDevice extends TileEntity implements IInventory
{
	public List<PortalData> PortalDataList;
	public ItemStack[] inventory;
	public int SelectedEntry;
	
	public TileEntityDialDevice()
	{
		PortalDataList = new ArrayList<PortalData>();
		SelectedEntry = 0;
		
		PortalDataList.add(new PortalData("Testing :)", (byte)5, new TeleportData(10, 10, 10, 1)));
		PortalDataList.add(new PortalData("Testing :)", (byte)1, new TeleportData(10, 10, 10, -1)));
		PortalDataList.add(new PortalData("Testing :)", (byte)16, new TeleportData(10, 10, 10, 0)));
		PortalDataList.add(new PortalData("Testing :)", (byte)17, new TeleportData(10, 10, 10, 0)));

		inventory = new ItemStack[2];
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return inventory[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		ItemStack stack = inventory[i];		
		stack.stackSize -= j;
		
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return inventory[i];
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{		
		inventory[i] = itemstack;
	}

	@Override
	public String getInvName()
	{
		return Strings.DialDevice_Name;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void openChest()
	{ }

	@Override
	public void closeChest()
	{ }

	@Override
	public boolean isStackValidForSlot(int ID, ItemStack itemStack)
	{
		if (ID == 0)
		{
			if (itemStack.itemID == Reference.ItemIDs.ItemScroll + 256)
			{
				if (itemStack.getItemDamage() == 1)
				{
					return true;
				}
			}
		}
		else if (ID == 1)
		{
			if (itemStack.itemID == Item.bucketLava.itemID || itemStack.itemID == Item.bucketWater.itemID || itemStack.itemID == Item.dyePowder.itemID)
			{
				return true;
			}
		}
		
		return false;
	}
}
