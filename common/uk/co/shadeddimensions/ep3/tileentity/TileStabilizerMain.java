package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.EntityManager;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.portal.PortalUtils;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.PortalTextureManager;
import uk.co.shadeddimensions.ep3.util.WorldUtils;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyStorage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileStabilizerMain extends TileEnhancedPortals  implements IInventory, IEnergyHandler
{
    static final int ACTIVE_PORTALS_PER_ROW = 2;

    ArrayList<ChunkCoordinates> blockList;
    HashMap<String, String> activeConnections;
    HashMap<String, String> activeConnectionsReverse;
    int powerState, rows;
    EnergyStorage energyStorage;
    public int instability = 0;
    
    @SideOnly(Side.CLIENT)
    public int intActiveConnections;

    public TileStabilizerMain()
    {
        blockList = new ArrayList<ChunkCoordinates>();
        activeConnections = new HashMap<String, String>();
        activeConnectionsReverse = new HashMap<String, String>();
        energyStorage = new EnergyStorage(32000);
    }

    /***
     * Sets up a new connection between two portals.
     * @return True if connection was successfully established.
     */
    public boolean setupNewConnection(GlyphIdentifier portalA, GlyphIdentifier portalB, PortalTextureManager textureManager)
    {
        if (activeConnections.containsKey(portalA.getGlyphString()) || activeConnections.containsValue(portalB.getGlyphString()) || !hasEnoughPowerToStart() || !canAcceptNewConnection())
        {
            return false;
        }
        else if (!hasEnoughPowerToStart())
        {
            return false;
        }

        TilePortalController cA = CommonProxy.networkManager.getPortalController(portalA), cB = CommonProxy.networkManager.getPortalController(portalB);

        if (cA == null || cB == null)
        {
            return false;
        }
        else if (cA.isPortalActive || cB.isPortalActive) // Make sure both portals are inactive
        {
            return false;
        }
        else if (!cA.hasConfigured || cA.waitingForCard || !cB.hasConfigured || cB.waitingForCard) // Make sure they're set up correctly...
        {
            return false;
        }
        else if (cA.isPortalActive || cB.isPortalActive)
        {
            return false;
        }
        else if (!cA.bridgeStabilizer.equals(cB.bridgeStabilizer)) // And make sure they're on the same DBS
        {
            return false;
        }

        if (textureManager != null)
        {
            cA.swapTextureData(textureManager);
            cB.swapTextureData(textureManager);
        }

        if (!PortalUtils.createPortalFrom(cA))
        {
            cA.revertTextureData();
            cB.revertTextureData();
            return false;
        }
        else if (!PortalUtils.createPortalFrom(cB)) // Make sure both portals can be created
        {
            PortalUtils.removePortalFrom(cA);
            cA.revertTextureData();
            cB.revertTextureData();
            return false;
        }

        activeConnections.put(portalA.getGlyphString(), portalB.getGlyphString());
        activeConnectionsReverse.put(portalB.getGlyphString(), portalA.getGlyphString());

        if (activeConnections.size() == 1)
        {
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, CommonProxy.blockStabilizer.blockID, 1); // Schedule tick for power drain
        }

        return true;
    }

    /***
     * Terminates both portals and removes them from the active connection list.
     */
    public void terminateExistingConnection(GlyphIdentifier portalA, GlyphIdentifier portalB)
    {
        if (portalA == null || portalB == null)
        {
            return;
        }

        TilePortalController cA = CommonProxy.networkManager.getPortalController(portalA), cB = CommonProxy.networkManager.getPortalController(portalB);

        if (cA == null || cB == null)
        {
            if (cA != null)
            {
                PortalUtils.removePortalFrom(cA);
                cA.revertTextureData();
            }

            if (cB != null)
            {
                PortalUtils.removePortalFrom(cB);
                cB.revertTextureData();
            }

            removeExistingConnection(portalA, portalB);
            return;
        }
        else if ((activeConnections.containsKey(portalA.getGlyphString()) && activeConnections.get(portalA.getGlyphString()).equals(portalB.getGlyphString())) ||
                (activeConnectionsReverse.containsKey(portalA.getGlyphString()) && activeConnectionsReverse.get(portalA.getGlyphString()).equals(portalB.getGlyphString())))
        {
            // Make sure we're terminating the correct connection, also don't mind that we're terminating it from the other side that we started it from
            PortalUtils.removePortalFrom(cA);
            PortalUtils.removePortalFrom(cB);
            cA.revertTextureData();
            cB.revertTextureData();

            removeExistingConnection(portalA, portalB);
        }
    }

    /***
     * Terminates both portals and removes them from the active connection list. Used by dialling devices when the exit location is not known by the controller.
     */
    public void terminateExistingConnection(GlyphIdentifier identifier)
    {
        if (identifier == null || identifier.isEmpty())
        {
            return;
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
     * Removes a connection from the active list.
     */
    public void removeExistingConnection(GlyphIdentifier portalA, GlyphIdentifier portalB)
    {
        activeConnections.remove(portalA.getGlyphString());
        activeConnections.remove(portalB.getGlyphString());
        activeConnectionsReverse.remove(portalA.getGlyphString());
        activeConnectionsReverse.remove(portalB.getGlyphString());
    }

    /***
     * Gets whether or not this stabilizer has enough power to keep the portal open for at least one tick.
     */
    boolean hasEnoughPowerToStart()
    {
        int powerRequirement = CommonProxy.redstoneFluxPowerMultiplier * (1 * CommonProxy.REDSTONE_FLUX_COST);
        return extractEnergy(null, (int) (powerRequirement * 0.3), true) == (int) (powerRequirement * 0.3);
    }

    /***
     * Whether or not this stabilizer can create a new connection
     */
    public boolean canAcceptNewConnection()
    {        
        return (activeConnections.size() * 2) + 2 <= ACTIVE_PORTALS_PER_ROW * rows;
    }

    @Override
    public void updateTick(Random random)
    {
        if (activeConnections.size() > 0 && CommonProxy.redstoneFluxPowerMultiplier > 0) // Make sure we're only ticking while we have active connections
        {
            int powerRequirement = CommonProxy.redstoneFluxPowerMultiplier * (activeConnections.size() * CommonProxy.REDSTONE_FLUX_COST);

            if (extractEnergy(null, powerRequirement, true) == powerRequirement) // Simulate the full power requirement
            {
                extractEnergy(null, powerRequirement, false);
                instability = 0;
            }
            else if (extractEnergy(null, (int) (powerRequirement * 0.8), true) == (int) (powerRequirement * 0.8)) // Otherwise, try it at 80%
            {
                extractEnergy(null, (int) (powerRequirement * 0.8), false);
                instability = 20;
            }
            else if (extractEnergy(null, (int) (powerRequirement * 0.5), true) == (int) (powerRequirement * 0.5)) // Otherwise, try it at 50%
            {
                extractEnergy(null, (int) (powerRequirement * 0.5), false);
                instability = 50;
            }
            else if (extractEnergy(null, (int) (powerRequirement * 0.3), true) == (int) (powerRequirement * 0.3)) // Otherwise, try it at 30%
            {
                extractEnergy(null, (int) (powerRequirement * 0.3), false);
                instability = 70;
            }
            else
            {
                for (int i = activeConnections.size() - 1; i > -1; i--)
                {
                    terminateExistingConnection(new GlyphIdentifier(activeConnections.values().toArray(new String[activeConnections.size()])[i]));
                }

                instability = 0;
                return; // Don't schedule another tick
            }

            CommonProxy.sendPacketToAllAroundTiny(this);
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, CommonProxy.blockStabilizer.blockID, CommonProxy.REDSTONE_FLUX_TIMER);
        }
    }

    public void onEntityEnterPortal(GlyphIdentifier uID, Entity entity, TilePortal portal)
    {
        if (EntityManager.isEntityFitForTravel(entity))
        {
            GlyphIdentifier exit = null;

            if (activeConnections.containsKey(uID.getGlyphString()))
            {
                exit = new GlyphIdentifier(activeConnections.get(uID.getGlyphString()));
            }
            else if (activeConnectionsReverse.containsKey(uID.getGlyphString()))
            {
                exit = new GlyphIdentifier(activeConnectionsReverse.get(uID.getGlyphString()));
            }

            if (exit != null)
            {
                EntityManager.teleportEntity(entity, uID, exit, portal);
            }
        }

        EntityManager.setEntityPortalCooldown(entity);
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

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        energyStorage.writeToNBT(tag);
        tag.setInteger("powerState", powerState);
        tag.setInteger("rows", rows);
        WorldUtils.saveChunkCoordList(tag, blockList, "blockList");

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
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        energyStorage.readFromNBT(tag);
        powerState = tag.getInteger("powerState");
        rows = tag.getInteger("rows");
        blockList = WorldUtils.loadChunkCoordList(tag, "blockList");

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
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        stream.writeInt(activeConnections.size());
        stream.writeInt(getEnergyStored(null));
        stream.writeInt(instability);
    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        intActiveConnections = stream.readInt();
        energyStorage.setEnergyStored(stream.readInt());
        instability = stream.readInt();
    }

    public void deconstruct()
    {
        for (ChunkCoordinates c : blockList)
        {
            TileStabilizer t = (TileStabilizer) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
            t.mainBlock = null;
        }

        worldObj.setBlock(xCoord, yCoord, zCoord, CommonProxy.blockStabilizer.blockID, 0, 3);
    }

    public IEnergyStorage getIEnergyStorage()
    {
        return energyStorage;
    }

    /* IEnergyHandler */
    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }
    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        return energyStorage.extractEnergy(maxExtract, simulate);
    }
    @Override
    public boolean canInterface(ForgeDirection from)
    {
        return true;
    }
    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return energyStorage.getEnergyStored();
    }
    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return energyStorage.getMaxEnergyStored();
    }

    /* IInventory */
    @Override
    public int getSizeInventory()
    {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {

    }

    @Override
    public String getInvName()
    {
        return null;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }
    @Override
    public void openChest() { }
    @Override
    public void closeChest() { }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }
}
