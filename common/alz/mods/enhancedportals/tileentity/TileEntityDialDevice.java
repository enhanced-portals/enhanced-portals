package alz.mods.enhancedportals.tileentity;

import java.util.ArrayList;
import java.util.List;

import alz.mods.enhancedportals.portals.PortalData;
import alz.mods.enhancedportals.portals.PortalTexture;
import alz.mods.enhancedportals.portals.TeleportData;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDialDevice extends TileEntity implements IInventory
{
	public List<PortalData> PortalDataList;
	public ItemStack[] inventory;
	public int SelectedEntry;
	
	public TileEntityDialDevice()
	{
		PortalDataList = new ArrayList<PortalData>();
		
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
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		SelectedEntry = tagCompound.getInteger("Selected");
		
		NBTTagList tagList = tagCompound.getTagList("PortalDataList");
		
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound portalTag = (NBTTagCompound) tagList.tagAt(i);
			
			String name = portalTag.getString("Name");
			PortalTexture texture = PortalTexture.getPortalTexture(portalTag.getInteger("Texture"));
			int freq = -1;
			TeleportData data = null;
			
			if (portalTag.getBoolean("TeleportData"))
			{
				int dim = portalTag.getInteger("Dimension"),
					x = portalTag.getInteger("X"),
					y = portalTag.getInteger("Y"),
					z = portalTag.getInteger("X");
				
				data = new TeleportData(x, y, z, dim);
			}
			else
			{
				freq = portalTag.getInteger("Frequency");
			}
			
			PortalData portalData = new PortalData();
			portalData.DisplayName = name;
			portalData.Texture = texture;
			
			if (freq == -1 && data != null)
			{
				portalData.TeleportData = data;
				portalData.Frequency = 0;
			}
			else if (freq != -1 && data == null)
			{
				portalData.TeleportData = null;
				portalData.Frequency = freq;
			}
			else
			{
				portalData.TeleportData = null;
				portalData.Frequency = 0;
				
				Reference.LogData("WARNING: Tile entity data has been corrupted. Resetting to default values.");
			}
			
			PortalDataList.add(portalData);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("Selected", SelectedEntry);
		
		NBTTagList tagList = new NBTTagList();
		
		for (PortalData portalData : PortalDataList)
		{
			NBTTagCompound portalTag = new NBTTagCompound();
			
			portalTag.setString("Name", portalData.DisplayName);
			portalTag.setInteger("Texture", portalData.Texture.ordinal());
			
			if (portalData.TeleportData != null)
			{
				portalTag.setBoolean("TeleportData", true);
				portalTag.setInteger("Dimension", portalData.TeleportData.GetDimension());
				portalTag.setInteger("X", portalData.TeleportData.GetX());
				portalTag.setInteger("Y", portalData.TeleportData.GetY());
				portalTag.setInteger("Z", portalData.TeleportData.GetZ());
			}
			else
			{
				portalTag.setBoolean("TeleportData", false);
				portalTag.setInteger("Frequency", portalData.Frequency);
			}
			
			tagList.appendTag(portalTag);
		}
		
		tagCompound.setTag("PortalDataList", tagList);
	}
}
