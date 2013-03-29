package alz.mods.enhancedportals.tileentity;

import java.util.ArrayList;
import java.util.List;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import alz.mods.enhancedportals.client.ClientProxy;
import alz.mods.enhancedportals.helpers.EntityHelper;
import alz.mods.enhancedportals.helpers.PortalHelper;
import alz.mods.enhancedportals.helpers.TeleportData;
import alz.mods.enhancedportals.helpers.WorldHelper;
import alz.mods.enhancedportals.networking.ITileEntityNetworking;
import alz.mods.enhancedportals.networking.PacketTileUpdate;
import alz.mods.enhancedportals.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityPortalModifier extends TileEntity implements IInventory, ITileEntityNetworking, IPeripheral
{
	private int Colour, Frequency;
	public boolean HadPower;
	private ItemStack[] inv;
	private List<IComputerAccess> computerAccess;
	
	public TileEntityPortalModifier()
	{
		inv = new ItemStack[3];
		computerAccess = new ArrayList<IComputerAccess>();
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
	
	public int getColour()
	{
		return Colour;
	}
	
	public int getFrequency()
	{
		return Frequency;
	}
	
	public void setColour(int colour)
	{
		setColour(colour, false);
	}
	
	public void setColour(int colour, boolean swapBlackPurple)
	{
		if (swapBlackPurple)
		{
			if (colour == 0)
				colour = 5;
			else if (colour == 5)
				colour = 0;
		}
		
		Colour = colour;
		
		for (IComputerAccess computer : computerAccess)
		{
			computer.queueEvent("enhancedPortals_colourChanged", new Object[] { colour });
		}
	}
	
	public void setFrequency(int frequency)
	{
		Frequency = frequency;
		
		for (IComputerAccess computer : computerAccess)
		{
			computer.queueEvent("enhancedPortals_frequencyChanged", new Object[] { frequency });
		}
	}
	
	public void setState(boolean state)
	{
		for (IComputerAccess computer : computerAccess)
		{
			computer.queueEvent("enhancedPortals_portalChanged", new Object[] { state });
		}
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
	
	@Override
	public PacketTileUpdate getUpdatePacket()
	{		
		return new PacketTileUpdate(xCoord, yCoord, zCoord, worldObj.provider.dimensionId, new int[] { Frequency, Colour });
	}

	@Override
	public void parseUpdatePacket(PacketTileUpdate packet)
	{
		if (packet.data == null)
			return;
		
		Frequency = packet.data[0];
		Colour = packet.data[1];
		
		if (worldObj.isRemote)
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void validate()
	{
		super.validate();
		
		if (worldObj.isRemote)
		{
			ClientProxy.RequestTileData(this);
		}
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack)
	{
		return EntityHelper.canAcceptItemStack(this, itemstack);
	}

	/* COMPUTERCRAFT */
	
	@Override
	public String getType()
	{
		return "Portal Modifier";
	}

	@Override
	public String[] getMethodNames()
	{
		return new String[] { "getFrequency", "setFrequency", "getColour", "setColour", "turnOn", "createPortal", "create", "turnOff", "removePortal", "remove" };
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception
	{
		if (method == 0)
		{
			return new Object[] { Frequency };
		}
		else if (method == 1)
		{
			if (arguments == null || arguments.length != 1)
				return new Object[] { "Usage: setFrequency(<frequency>)" };
			
			try
			{
				Frequency = (int)Double.parseDouble(arguments[0].toString());
				
				if (Reference.LinkData != null)
					Reference.LinkData.AddToFrequency(Frequency, new TeleportData(xCoord, yCoord, zCoord, worldObj.provider.dimensionId));
				
				return new Object[] { true };
			}
			catch (NumberFormatException e)
			{
				return new Object[] { false };
			}
		}
		else if (method == 2)
		{
			return new Object[] { Colour };
		}
		else if (method == 3)
		{
			if (arguments == null || arguments.length != 1)
				return new Object[] { "Usage:", "setColour(<0 - 15>)", "setColour(<colour name>)", "Valid colour names:", "black, red, green, brown, blue, purple, cyan, silver, gray, pink, lime, yellow, lightBlue, magenta, orange, white" };
			
			try
			{
				int col = (int)Double.parseDouble(arguments[0].toString());
				
				if (col < 0)
					col = 0;
				
				if (col > 15)
					col = 15;
								
				setColour(col, true);
				
				if (Reference.LinkData != null)
					Reference.LinkData.sendUpdatePacketToNearbyClients(this);
				
				int[] offset = WorldHelper.offsetDirectionBased(worldObj, xCoord, yCoord, zCoord);
				WorldHelper.floodUpdateMetadata(worldObj, offset[0], offset[1], offset[2], 90, col);
				
				return new Object[] { true };
			}
			catch (NumberFormatException e)
			{
				int count = 0;
				
				for (String str : ItemDye.dyeColorNames)
				{
					if (arguments[0].toString().toLowerCase().equals((str.toLowerCase())))
					{						
						setColour(count, true);
						
						if (Reference.LinkData != null)
							Reference.LinkData.sendUpdatePacketToNearbyClients(this);
						
						int[] offset = WorldHelper.offsetDirectionBased(worldObj, xCoord, yCoord, zCoord);
						WorldHelper.floodUpdateMetadata(worldObj, offset[0], offset[1], offset[2], 90, Colour);
						
						return new Object[] { true };
					}
					
					count++;
				}
				
				return new Object[] { false };
			}
		}
		else if (method == 4 || method == 5 || method == 6)
		{
			return computerCreatePortal();
		}
		else if (method == 7 || method == 8 || method == 9)
		{
			return computerRemovePortal();
		}
		
		return null;
	}
	
	private Object[] computerCreatePortal()
	{
		ForgeDirection direction = WorldHelper.getBlockDirection(worldObj, xCoord, yCoord, zCoord);
		int[] blockToTest = WorldHelper.offsetDirectionBased(worldObj, xCoord, yCoord, zCoord, direction);
		int blockID = worldObj.getBlockId(blockToTest[0], blockToTest[1], blockToTest[2]);
		boolean createdPortal = false;
		
		if (hasUpgrade(0))
			createdPortal = PortalHelper.createPortalAround(worldObj, xCoord, yCoord, zCoord, Colour);
		else
			if (WorldHelper.isBlockPortalRemovable(blockID))
				createdPortal = PortalHelper.createPortal(worldObj, blockToTest[0], blockToTest[1], blockToTest[2], Colour);
		
		setState(createdPortal);
		
		return new Object[] { createdPortal };
	}
	
	private Object[] computerRemovePortal()
	{
		ForgeDirection direction = WorldHelper.getBlockDirection(worldObj, xCoord, yCoord, zCoord);
		int[] blockToTest = WorldHelper.offsetDirectionBased(worldObj, xCoord, yCoord, zCoord, direction);
		int blockID = worldObj.getBlockId(blockToTest[0], blockToTest[1], blockToTest[2]), meta = worldObj.getBlockMetadata(blockToTest[0], blockToTest[1], blockToTest[2]);
		
		if (hasUpgrade(0))
			PortalHelper.removePortalAround(worldObj, xCoord, yCoord, zCoord);
		else
			if (blockID == Reference.BlockIDs.NetherPortal && meta == Colour)
				PortalHelper.removePortal(worldObj, blockToTest[0], blockToTest[1], blockToTest[2]);
		
		return new Object[] { false };
	}

	@Override
	public boolean canAttachToSide(int side)
	{
		return hasUpgrade(4);
	}

	@Override
	public void attach(IComputerAccess computer)
	{
		computerAccess.add(computer);
	}

	@Override
	public void detach(IComputerAccess computer)
	{
		computerAccess.remove(computer);
	}
}
