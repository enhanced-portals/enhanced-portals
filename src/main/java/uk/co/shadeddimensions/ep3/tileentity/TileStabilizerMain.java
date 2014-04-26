package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.block.BlockStabilizer;
import uk.co.shadeddimensions.ep3.item.ItemLocationCard;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.portal.PortalException;
import uk.co.shadeddimensions.ep3.portal.PortalTextureManager;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import uk.co.shadeddimensions.ep3.util.WorldUtils;
import uk.co.shadeddimensions.library.util.ItemHelper;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.EnhancedPortals;

public class TileStabilizerMain extends TileEP implements IInventory, IEnergyHandler, IPowerReceptor
{
	static final int ACTIVE_PORTALS_PER_ROW = 2, ENERGY_STORAGE_PER_ROW = CommonProxy.REDSTONE_FLUX_COST + CommonProxy.REDSTONE_FLUX_COST / 2;

	ArrayList<ChunkCoordinates> blockList;

	HashMap<String, String> activeConnections;
	HashMap<String, String> activeConnectionsReverse;

	ItemStack inventory;
	int rows, tickTimer;
	EnergyStorage energyStorage;
	public int powerState, instability = 0;
	Random rand = new Random();
	public boolean is3x3 = false;
	private final PowerHandler mjHandler;

	@SideOnly(Side.CLIENT)
	public int intActiveConnections;

	public TileStabilizerMain()
	{
		blockList = new ArrayList<ChunkCoordinates>();
		activeConnections = new HashMap<String, String>();
		activeConnectionsReverse = new HashMap<String, String>();
		energyStorage = new EnergyStorage(0);
		
		float energyUsage = CommonProxy.REDSTONE_FLUX_COST / CommonProxy.RF_PER_MJ;
		mjHandler = new PowerHandler(this, Type.MACHINE);
		mjHandler.configure(2.0f, energyUsage, energyUsage * 0.2f, 1500);
		mjHandler.configurePowerPerdition(0, 0);
	}

	public boolean activate(EntityPlayer player)
	{
		ItemStack stack = player.inventory.getCurrentItem();

		if (stack != null && stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlockID() == BlockStabilizer.ID)
		{
			return false;
		}

		GuiHandler.openGui(player, this, GuiHandler.DIMENSIONAL_BRIDGE_STABILIZER);
		return true;
	}

	void addHighInstabilityEffects(Entity entity)
	{
		if (entity instanceof EntityLivingBase)
		{
			PotionEffect blindness = new PotionEffect(Potion.blindness.id, 600, 1);
			PotionEffect hunger = new PotionEffect(Potion.hunger.id, 600, 1);
			PotionEffect poison = new PotionEffect(Potion.poison.id, 600, 1);

			blindness.setCurativeItems(new ArrayList<ItemStack>());
			hunger.setCurativeItems(new ArrayList<ItemStack>());
			poison.setCurativeItems(new ArrayList<ItemStack>());

			((EntityLivingBase) entity).addPotionEffect(blindness);
			((EntityLivingBase) entity).addPotionEffect(hunger);
			((EntityLivingBase) entity).addPotionEffect(poison);
		}
	}

	void addLowInstabilityEffects(Entity entity)
	{
		if (entity instanceof EntityLivingBase)
		{
			PotionEffect blindness = new PotionEffect(Potion.blindness.id, 200, 1);
			PotionEffect hunger = new PotionEffect(Potion.hunger.id, 200, 1);
			PotionEffect poison = new PotionEffect(Potion.poison.id, 200, 1);

			blindness.setCurativeItems(new ArrayList<ItemStack>());
			hunger.setCurativeItems(new ArrayList<ItemStack>());
			poison.setCurativeItems(new ArrayList<ItemStack>());

			int effect = rand.nextInt(3);
			((EntityLivingBase) entity).addPotionEffect(effect == 0 ? blindness : effect == 1 ? hunger : poison);
		}
	}

	void addMediumInstabilityEffects(Entity entity)
	{
		if (entity instanceof EntityLivingBase)
		{
			PotionEffect blindness = new PotionEffect(Potion.blindness.id, 400, 1);
			PotionEffect hunger = new PotionEffect(Potion.hunger.id, 400, 1);
			PotionEffect poison = new PotionEffect(Potion.poison.id, 400, 1);

			blindness.setCurativeItems(new ArrayList<ItemStack>());
			hunger.setCurativeItems(new ArrayList<ItemStack>());
			poison.setCurativeItems(new ArrayList<ItemStack>());

			int effect = rand.nextInt(3);

			if (effect == 0)
			{
				((EntityLivingBase) entity).addPotionEffect(blindness);
				((EntityLivingBase) entity).addPotionEffect(hunger);
			}
			else if (effect == 1)
			{
				((EntityLivingBase) entity).addPotionEffect(blindness);
				((EntityLivingBase) entity).addPotionEffect(poison);
			}
			else
			{
				((EntityLivingBase) entity).addPotionEffect(poison);
				((EntityLivingBase) entity).addPotionEffect(hunger);
			}
		}
	}

	public void breakBlock(int oldBlockID, int oldMetadata)
	{
		for (int i = activeConnections.size() - 1; i > -1; i--) // Go backwards so we don't get messed up by connections getting removed from this list
		{
			try
			{
				terminateExistingConnection(new GlyphIdentifier(activeConnections.values().toArray(new String[activeConnections.size()])[i]));
			}
			catch (Exception e)
			{

			}
		}

		for (ChunkCoordinates c : blockList)
		{
			TileEntity tile = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);

			if (tile instanceof TileStabilizer)
			{
				TileStabilizer t = (TileStabilizer) tile;
				t.mainBlock = null;
				WorldUtils.markForUpdate(t);
			}
		}
	}

	/***
	 * Whether or not this stabilizer can create a new connection
	 */
	public boolean canAcceptNewConnection()
	{
		return activeConnections.size() * 2 + 2 <= ACTIVE_PORTALS_PER_ROW * rows;
	}

	@Override
	public boolean canInterface(ForgeDirection from)
	{
		return true;
	}

	@Override
	public boolean canUpdate()
	{
		return true;
	}

	@Override
	public void closeChest()
	{
	}

	public void deconstruct()
	{
		breakBlock(0, 0);
		worldObj.setBlock(xCoord, yCoord, zCoord, BlockStabilizer.ID, 0, 3);
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		ItemStack stack = getStackInSlot(i);

		if (stack != null)
		{
			if (stack.stackSize <= j)
			{
				setInventorySlotContents(i, null);
			}
			else
			{
				stack = stack.splitStack(j);

				if (stack.stackSize == 0)
				{
					setInventorySlotContents(i, null);
				}
			}
		}

		return stack;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		return energyStorage.extractEnergy(maxExtract, simulate);
	}

	public int getActiveConnections()
	{
		return activeConnections != null ? activeConnections.size() : -1;
	}

	public GlyphIdentifier getConnectedPortal(GlyphIdentifier uniqueIdentifier)
	{
		if (activeConnections.containsKey(uniqueIdentifier.getGlyphString()))
		{
			return new GlyphIdentifier(activeConnections.get(uniqueIdentifier.getGlyphString()));
		}
		else if (activeConnectionsReverse.containsKey(uniqueIdentifier.getGlyphString()))
		{
			return new GlyphIdentifier(activeConnectionsReverse.get(uniqueIdentifier.getGlyphString()));
		}

		return null;
	}

	public EnergyStorage getEnergyStorage()
	{
		return energyStorage;
	}

	public int getEnergyStoragePerRow()
	{
		return is3x3 ? ENERGY_STORAGE_PER_ROW + ENERGY_STORAGE_PER_ROW / 2 : ENERGY_STORAGE_PER_ROW;
	}

	@Override
	public int getEnergyStored(ForgeDirection from)
	{
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public String getInvName()
	{
		return "tile.ep3.stabilizer.name";
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from)
	{
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return inventory;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return inventory;
	}

	/***
	 * Gets whether or not this stabilizer has enough power to keep the portal open for at least one second.
	 */
	public boolean hasEnoughPowerToStart()
	{
		if (!GeneralUtils.hasEnergyCost())
		{
			return true;
		}

		int powerRequirement = GeneralUtils.getPowerMultiplier() * 1 * CommonProxy.REDSTONE_FLUX_COST;
		return extractEnergy(null, (int) (powerRequirement * 0.3), true) == (int) (powerRequirement * 0.3);
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return ItemHelper.isEnergyContainerItem(itemstack);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void packetGui(NBTTagCompound tag, EntityPlayer player)
	{
		
	}

	@Override
	public void packetGuiFill(DataOutputStream stream) throws IOException
	{
		stream.writeInt(activeConnections.size());
		stream.writeInt(powerState);
		stream.writeInt(instability);
		stream.writeInt(energyStorage.getMaxEnergyStored());
		stream.writeInt(energyStorage.getEnergyStored());
	}

	@Override
	public void packetGuiUse(DataInputStream stream) throws IOException
	{
		if (stream.available() == 1)
		{
			return; // Stops EOF exceptions from old invalid packets
		}

		intActiveConnections = stream.readInt();
		powerState = stream.readInt();
		instability = stream.readInt();
		energyStorage.setCapacity(stream.readInt());
		energyStorage.setEnergyStored(stream.readInt());
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		powerState = tag.getInteger("powerState");
		is3x3 = tag.getBoolean("is3x3");
		rows = tag.getInteger("rows");
		energyStorage = new EnergyStorage(rows * getEnergyStoragePerRow());
		blockList = GeneralUtils.loadChunkCoordList(tag, "blockList");
		energyStorage.readFromNBT(tag);

		if (tag.hasKey("activeConnections"))
		{
			NBTTagList c = tag.getTagList("activeConnections");

			for (int i = 0; i < c.tagCount(); i++)
			{
				NBTTagCompound t = (NBTTagCompound) c.tagAt(i);
				String A = t.getString("Key"), B = t.getString("Value");

				activeConnections.put(A, B);
				activeConnectionsReverse.put(B, A);
			}
		}

		if (tag.hasKey("inventory"))
		{
			inventory = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("inventory"));
		}
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
		return energyStorage.receiveEnergy(maxReceive, simulate);
	}

	/***
	 * Removes a connection from the active list.
	 */
	public void removeExistingConnection(GlyphIdentifier portalA, GlyphIdentifier portalB)
	{
		activeConnections.remove(portalA.getGlyphString());
		activeConnections.remove(portalB.getGlyphString());
		activeConnectionsReverse.remove(portalA.getGlyphString());
		activeConnectionsReverse.remove(portalB.getGlyphString());

		if (activeConnections.size() == 0 && powerState == 0 && instability > 0)
		{
			setInstability(0);
		}
	}

	public void setData(ArrayList<ChunkCoordinates> blocks, int rows2, boolean is3)
	{
		is3x3 = is3;
		rows = rows2;
		blockList = blocks;
		energyStorage = new EnergyStorage(rows * getEnergyStoragePerRow());
		WorldUtils.markForUpdate(this);
	}

	void setInstability(int newInstability)
	{
		if (newInstability == instability)
		{
			return;
		}

		instability = newInstability;

		for (Entry<String, String> pair : activeConnections.entrySet())
		{
			TileController controllerA = EnhancedPortals.proxy.networkManager.getPortalController(new GlyphIdentifier(pair.getKey()));
			TileController controllerB = EnhancedPortals.proxy.networkManager.getPortalController(new GlyphIdentifier(pair.getValue()));

			if (controllerA != null)
			{
				controllerA.setInstability(newInstability);
			}

			if (controllerB != null)
			{
				controllerB.setInstability(newInstability);
			}
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		inventory = itemstack;
	}

	/***
	 * Sets up a new connection between two portals.
	 * 
	 * @return True if connection was successfully established.
	 */
	public boolean setupNewConnection(GlyphIdentifier portalA, GlyphIdentifier portalB, PortalTextureManager textureManager) throws PortalException
	{
		if (activeConnections.containsKey(portalA.getGlyphString()))
		{
			throw new PortalException("diallingPortalAlreadyActive");
		}
		else if (activeConnections.containsValue(portalB.getGlyphString()))
		{
			throw new PortalException("receivingPortalAlreadyActive");
		}
		else if (!hasEnoughPowerToStart())
		{
			throw new PortalException("notEnoughPowerToStart");
		}
		else if (!canAcceptNewConnection())
		{
			throw new PortalException("maxedConnectionLimit");
		}
		else if (portalA.equals(portalB))
		{
			throw new PortalException("cannotDialDiallingPortal");
		}
		else if (!EnhancedPortals.proxy.networkManager.portalIdentifierExists(portalA))
		{
			throw new PortalException("noPortalWithThatIdentifierSending");
		}
		else if (!EnhancedPortals.proxy.networkManager.portalIdentifierExists(portalB))
		{
			throw new PortalException("noPortalWithThatIdentifierReceiving");
		}

		TileController cA = EnhancedPortals.proxy.networkManager.getPortalController(portalA), cB = EnhancedPortals.proxy.networkManager.getPortalController(portalB);

		if (cA == null)
		{
			throw new PortalException("diallingPortalNotFound");
		}
		else if (cB == null)
		{
			throw new PortalException("receivingPortalNotFound");
		}
		else if (cA.isPortalActive())
		{
			throw new PortalException("diallingPortalAlreadyActive");
		}
		else if (cB.isPortalActive()) // Make sure both portals are inactive
		{
			throw new PortalException("receivingPortalAlreadyActive");
		}
		else if (!cA.isFinalized())
		{
			throw new PortalException("sendingPortalNotInitialized");
		}
		else if (!cB.isFinalized()) // Make sure they're set up correctly...
		{
			throw new PortalException("receivingPortalNotInitialized");
		}
		//else if (!cA.getDimensionalBridgeStabilizer().getWorldCoordinates().equals(cB.getDimensionalBridgeStabilizer().getWorldCoordinates())) // And make sure they're on the same DBS. We're getting the tile instead of the worldcoordinates to make sure the DBS hasn't expanded
		//{
		//	throw new PortalException("notOnSameStabilizer");
		//}
		else if (cA.getDiallingDeviceCount() > 0 && cB.getNetworkInterfaceCount() > 0)
		{
			throw new PortalException("receivingPortalNoDialler");
		}
		else if (cA.getNetworkInterfaceCount() > 0 && cB.getDiallingDeviceCount() > 0)
		{
			throw new PortalException("sendingPortalNoDialler"); // Should never happen but it doesn't hurt to check.
		}
		
		TileStabilizerMain sA = cA.getDimensionalBridgeStabilizer(), sB = cB.getDimensionalBridgeStabilizer();
        
        if (!sA.getWorldCoordinates().equals(sB.getWorldCoordinates()))
        {
            if (cB.isPublic)
            {
                cB.setupTemporaryDBS(sA);
            }
            else
            {
                throw new PortalException("notOnSameStabilizer");
            }
        }

		if (textureManager != null)
		{
			cA.swapTextureData(textureManager);
			cB.swapTextureData(textureManager);
		}

		try
		{
			cA.setInstability(instability);
			cA.portalCreate();
			cA.cacheDestination(portalB, cB.getWorldCoordinates());
		}
		catch (PortalException e)
		{
			cA.revertTextureData();
			cB.revertTextureData();
			throw new PortalException("sendingPortalFailedToCreatePortal");
		}

		try
		{
			cB.setInstability(instability);
			cB.portalCreate();
			cB.cacheDestination(portalA, cA.getWorldCoordinates());
		}
		catch (PortalException e)
		{
			cA.portalRemove();
			cA.cacheDestination(null, null);
			cA.revertTextureData();
			cB.revertTextureData();
			throw new PortalException("receivingPortalFailedToCreatePortal");
		}

		activeConnections.put(portalA.getGlyphString(), portalB.getGlyphString());
		activeConnectionsReverse.put(portalB.getGlyphString(), portalA.getGlyphString());
		return true;
	}

	/***
	 * Terminates both portals and removes them from the active connection list. Used by dialling devices when the exit location is not known by the controller.
	 */
	public void terminateExistingConnection(GlyphIdentifier identifier) throws PortalException
	{
		if (identifier == null || identifier.isEmpty())
		{
			throw new PortalException("No identifier found for first portal");
		}

		GlyphIdentifier portalA = new GlyphIdentifier(identifier), portalB = null;

		if (activeConnections.containsKey(identifier.getGlyphString()))
		{
			portalB = new GlyphIdentifier(activeConnections.get(identifier.getGlyphString()));
		}
		else if (activeConnectionsReverse.containsKey(identifier.getGlyphString()))
		{
			portalB = new GlyphIdentifier(activeConnectionsReverse.get(identifier.getGlyphString()));
		}

		terminateExistingConnection(portalA, portalB);
	}

	/***
	 * Terminates both portals and removes them from the active connection list.
	 */
	public void terminateExistingConnection(GlyphIdentifier portalA, GlyphIdentifier portalB) throws PortalException
	{
		if (portalA == null)
		{
			throw new PortalException("No identifier found for first portal");
		}
		else if (portalB == null)
		{
			throw new PortalException("No identifier found for second portal");
		}

		TileController cA = EnhancedPortals.proxy.networkManager.getPortalController(portalA), cB = EnhancedPortals.proxy.networkManager.getPortalController(portalB);

		if (cA == null || cB == null)
		{
			if (cA == null)
			{
				throw new PortalException("No identifier found for first portal");
			}
			else if (cB == null)
			{
				throw new PortalException("No identifier found for second portal");
			}

			if (cA != null)
			{
				cA.portalRemove();
				cA.revertTextureData();
				cA.cacheDestination(null, null);
			}

			if (cB != null)
			{
				cB.portalRemove();
				cB.revertTextureData();
				cB.cacheDestination(null, null);
			}

			removeExistingConnection(portalA, portalB);
		}
		else if (activeConnections.containsKey(portalA.getGlyphString()) && activeConnections.get(portalA.getGlyphString()).equals(portalB.getGlyphString()) || activeConnectionsReverse.containsKey(portalA.getGlyphString()) && activeConnectionsReverse.get(portalA.getGlyphString()).equals(portalB.getGlyphString()))
		{
			// Make sure we're terminating the correct connection, also don't mind that we're terminating it from the other side that we started it from
			cA.portalRemove();
			cB.portalRemove();
			cA.cacheDestination(null, null);
			cB.cacheDestination(null, null);
			cA.revertTextureData();
			cB.revertTextureData();

			removeExistingConnection(portalA, portalB);
		}
		else
		{
			throw new PortalException("Could not find both portals in the active connection list.");
		}
	}

	@Override
	public void updateEntity()
	{
		if (activeConnections.size() > 0 && GeneralUtils.hasEnergyCost() && tickTimer >= CommonProxy.REDSTONE_FLUX_TIMER)
		{
			int powerRequirement = GeneralUtils.getPowerMultiplier() * activeConnections.size() * CommonProxy.REDSTONE_FLUX_COST;

			if (powerState == 0 && extractEnergy(null, powerRequirement, true) == powerRequirement) // Simulate the full power requirement
			{
				extractEnergy(null, powerRequirement, false);
				setInstability(0);
			}
			else if ((powerState == 1 || powerState == 0) && extractEnergy(null, (int) (powerRequirement * 0.8), true) == (int) (powerRequirement * 0.8)) // Otherwise, try it at 80%
			{
				extractEnergy(null, (int) (powerRequirement * 0.8), false);
				setInstability(20);
			}
			else if ((powerState == 2 || powerState == 0) && extractEnergy(null, (int) (powerRequirement * 0.5), true) == (int) (powerRequirement * 0.5)) // Otherwise, try it at 50%
			{
				extractEnergy(null, (int) (powerRequirement * 0.5), false);
				setInstability(50);
			}
			else if ((powerState == 3 || powerState == 0) && extractEnergy(null, (int) (powerRequirement * 0.3), true) == (int) (powerRequirement * 0.3)) // Otherwise, try it at 30%
			{
				extractEnergy(null, (int) (powerRequirement * 0.3), false);
				setInstability(70);
			}
			else
			{
				for (int i = activeConnections.size() - 1; i > -1; i--) // Go backwards so we don't get messed up by connections getting removed from this list
				{
					try
					{
						terminateExistingConnection(new GlyphIdentifier(activeConnections.values().toArray(new String[activeConnections.size()])[i]));
					}
					catch (PortalException e)
					{
						CommonProxy.logger.warning(e.getMessage());
					}
				}

				setInstability(0);
			}

			tickTimer = -1;
		}

		if (inventory != null)
		{
			if (inventory.getItem() instanceof IEnergyContainerItem)
			{
				int requiredEnergy = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();

				if (requiredEnergy > 0 && ((IEnergyContainerItem) inventory.getItem()).getEnergyStored(inventory) > 0)
				{
					energyStorage.receiveEnergy(((IEnergyContainerItem) inventory.getItem()).extractEnergy(inventory, Math.min(requiredEnergy, 10000), false), false);
				}
			}
			else if (inventory.itemID == ItemLocationCard.ID)
			{
				if (!ItemLocationCard.hasDBSLocation(inventory) && !worldObj.isRemote)
				{
					ItemLocationCard.setDBSLocation(inventory, getWorldCoordinates());
				}
			}
		}

		tickTimer++;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		energyStorage.writeToNBT(tag);
		tag.setBoolean("is3x3", is3x3);
		tag.setInteger("powerState", powerState);
		tag.setInteger("rows", rows);
		GeneralUtils.saveChunkCoordList(tag, blockList, "blockList");

		if (!activeConnections.isEmpty())
		{
			NBTTagList c = new NBTTagList();

			for (Entry<String, String> entry : activeConnections.entrySet())
			{
				NBTTagCompound t = new NBTTagCompound();
				t.setString("Key", entry.getKey());
				t.setString("Value", entry.getValue());
				c.appendTag(t);
			}

			tag.setTag("activeConnections", c);
		}

		if (inventory != null)
		{
			NBTTagCompound t = new NBTTagCompound();
			inventory.writeToNBT(t);
			tag.setTag("inventory", t);
		}
	}

    @Override
    public PowerReceiver getPowerReceiver(ForgeDirection side)
    {
        return mjHandler.getPowerReceiver();
    }

    @Override
    public void doWork(PowerHandler workProvider)
    {
        int acceptedEnergy = receiveEnergy(null, (int)(mjHandler.useEnergy(1.0F, CommonProxy.REDSTONE_FLUX_COST / CommonProxy.RF_PER_MJ, false) * CommonProxy.RF_PER_MJ), false);
        mjHandler.useEnergy(1.0F, acceptedEnergy / CommonProxy.RF_PER_MJ, true);
    }

    @Override
    public World getWorld()
    {
        return this.worldObj;
    }
}
