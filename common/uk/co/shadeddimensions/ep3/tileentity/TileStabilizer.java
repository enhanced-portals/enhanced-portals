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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.api.IPowerStorage;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.EntityManager;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.portal.PortalUtils;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.ChunkCoordinateUtils;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class TileStabilizer extends TileEnhancedPortals implements IPowerStorage
{
    static final int MAX_ACTIVE_PORTALS_PER_ROW = 2;

    int rows = 2;
    public boolean hasConfigured;
    ArrayList<ChunkCoordinates> blockList;
    ChunkCoordinates mainBlock;

    HashMap<String, String> activeConnections;
    HashMap<String, String> activeConnectionsReverse;

    public TileStabilizer()
    {
        mainBlock = null;
        hasConfigured = false;
        blockList = new ArrayList<ChunkCoordinates>();
        activeConnections = new HashMap<String, String>();
        activeConnectionsReverse = new HashMap<String, String>();
    }

    /***
     * Whether or not this stabilizer can create a new connection
     */
    public boolean canAcceptNewConnection()
    {        
        System.out.println(String.format("Active Connections: %s, Max Connections: %s", activeConnections.size() * 2, MAX_ACTIVE_PORTALS_PER_ROW * rows));
        return (activeConnections.size() * 2) + 2 <= MAX_ACTIVE_PORTALS_PER_ROW * rows;
    }

    /***
     * Sets up a new connection between two portals.
     * @return True if connection was successfully established.
     */
    public boolean setupNewConnection(GlyphIdentifier portalA, GlyphIdentifier portalB)
    {
        if (activeConnections.containsKey(portalA) || activeConnections.containsValue(portalB) || !hasEnoughPowerToStart() || !canAcceptNewConnection())
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

        if (!PortalUtils.createPortalFrom(cA))
        {
            return false;
        }
        else if (!PortalUtils.createPortalFrom(cB)) // Make sure both portals can be created
        {
            PortalUtils.removePortalFrom(cA);
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
        TilePortalController cA = CommonProxy.networkManager.getPortalController(portalA), cB = CommonProxy.networkManager.getPortalController(portalB);
        
        if (cA == null || cB == null)
        {
            removeExistingConnection(portalA, portalB);
            return;
        }
        else if ((activeConnections.containsKey(portalA.getGlyphString()) && activeConnections.get(portalA.getGlyphString()).equals(portalB.getGlyphString())) ||
                 (activeConnectionsReverse.containsKey(portalA.getGlyphString()) && activeConnectionsReverse.get(portalA.getGlyphString()).equals(portalB.getGlyphString())))
        {
            // Make sure we're terminating the correct connection, also don't mind that we're terminating it from the other side that we started it from
            PortalUtils.removePortalFrom(cA);
            PortalUtils.removePortalFrom(cB);

            removeExistingConnection(portalA, portalB);
        }
    }

    /***
     * Removes a connection from the active list.
     */
    public void removeExistingConnection(GlyphIdentifier portalA, GlyphIdentifier portalB)
    {
        activeConnections.remove(portalA.getGlyphString());
        activeConnectionsReverse.remove(portalB.getGlyphString());
    }

    /***
     * Gets whether or not this stabilizer has enough power to keep the portal open for at least one tick.
     */
    boolean hasEnoughPowerToStart()
    {
        // TODO

        return true;
    }

    /***
     * Gets the block that does all the processing for this multiblock.
     * If that block is self, will return self.
     */
    public TileStabilizer getMainBlock()
    {
        if (mainBlock != null)
        {
            TileEntity tile = worldObj.getBlockTileEntity(mainBlock.posX, mainBlock.posY, mainBlock.posZ);

            if (tile != null && tile instanceof TileStabilizer)
            {
                return (TileStabilizer) tile;
            }
        }

        return null;
    }

    @Override
    public boolean activate(EntityPlayer player)
    {
        if (CommonProxy.isClient() || hasConfigured || player.inventory.getCurrentItem() == null || player.inventory.getCurrentItem().getItem().itemID != CommonProxy.itemWrench.itemID)
        {
            return false;
        }

        WorldCoordinates topLeft = getWorldCoordinates();

        while (topLeft.offset(ForgeDirection.WEST).getBlockId() == CommonProxy.blockStabilizer.blockID)  // Get the westernmost block
        {
            topLeft = topLeft.offset(ForgeDirection.WEST);
        }

        while (topLeft.offset(ForgeDirection.NORTH).getBlockId() == CommonProxy.blockStabilizer.blockID) // Get the northenmost block
        {
            topLeft = topLeft.offset(ForgeDirection.NORTH);
        }

        while (topLeft.offset(ForgeDirection.UP).getBlockId() == CommonProxy.blockStabilizer.blockID) // Get the highest block
        {
            topLeft = topLeft.offset(ForgeDirection.UP);
        }

        ArrayList<ChunkCoordinates> blocks = checkShape(topLeft, true); // Try the X axis

        if (blocks.isEmpty())
        {
            blocks = checkShape(topLeft, false); // Try the Z axis before failing
        }

        if (!blocks.isEmpty()) // success
        {
            for (ChunkCoordinates c : blocks)
            {
                TileStabilizer t = (TileStabilizer) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
                t.hasConfigured = true;
                t.mainBlock = topLeft;
                t.rows = rows;
                CommonProxy.sendUpdatePacketToAllAround(t);
            }

            getMainBlock().blockList = blocks;
        }

        return true;
    }

    ArrayList<ChunkCoordinates> checkShape(WorldCoordinates topLeft, boolean isX)
    {
        ArrayList<ChunkCoordinates> blocks = new ArrayList<ChunkCoordinates>();

        int tempHeight = 0;
        ChunkCoordinates heightChecker = new ChunkCoordinates(topLeft);

        while (worldObj.getBlockId(heightChecker.posX, heightChecker.posY, heightChecker.posZ) == CommonProxy.blockStabilizer.blockID)
        {
            heightChecker.posY--;
            tempHeight++;
        }

        if (tempHeight < 2)
        {
            return new ArrayList<ChunkCoordinates>();
        }

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                for (int k = 0; k < tempHeight; k++)
                {
                    if (worldObj.getBlockId(topLeft.posX + (isX ? i : j), topLeft.posY - k, topLeft.posZ + (!isX ? i : j)) != CommonProxy.blockStabilizer.blockID)
                    {
                        return new ArrayList<ChunkCoordinates>();
                    }

                    blocks.add(new ChunkCoordinates(topLeft.posX + (isX ? i : j), topLeft.posY - k, topLeft.posZ + (!isX ? i : j)));
                }
            }
        }

        rows = tempHeight;
        return blocks;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setBoolean("hasConfigured", hasConfigured);
        tag.setInteger("rows", rows);
        ChunkCoordinateUtils.saveChunkCoord(tag, mainBlock, "mainBlock");

        if (!blockList.isEmpty())
        {
            ChunkCoordinateUtils.saveChunkCoordList(tag, blockList, "blockList");
        }
        
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

        hasConfigured = tag.getBoolean("hasConfigured");
        rows = tag.getInteger("rows");
        mainBlock = ChunkCoordinateUtils.loadChunkCoord(tag, "mainBlock");

        if (tag.hasKey("blockList"))
        {
            blockList = ChunkCoordinateUtils.loadChunkCoordList(tag, "blockList");
        }
        
        if (tag.hasKey("activeConnections"))
        {
            NBTTagList c = tag.getTagList("activeConnections");
            
            for (Object obj : c.tagList)
            {
                NBTTagCompound t = (NBTTagCompound) obj;
                
                String A = t.getString("Key"), B = t.getString("Value");
                
                activeConnections.put(A, B);
                activeConnectionsReverse.put(B, A);
            }
        }
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);

        stream.writeBoolean(hasConfigured);

        if (mainBlock != null)
        {
            stream.writeInt(mainBlock.posX);
            stream.writeInt(mainBlock.posY);
            stream.writeInt(mainBlock.posZ);
        }
        else
        {
            stream.writeInt(0);
            stream.writeInt(-1);
            stream.writeInt(0);
        }
    }

    public void deconstruct()
    {        
        for (ChunkCoordinates c : blockList)
        {
            TileStabilizer t = (TileStabilizer) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
            t.hasConfigured = false;
            t.mainBlock = null;
            CommonProxy.sendUpdatePacketToAllAround(t);
        }
    }

    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        if (hasConfigured)
        {
            TileStabilizer main = getMainBlock();

            if (main == null)
            {
                return;
            }

            main.deconstruct();
        }
    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);

        hasConfigured = stream.readBoolean();        
        ChunkCoordinates c = new ChunkCoordinates(stream.readInt(), stream.readInt(), stream.readInt());

        if (c.posX == -1)
        {
            c = null;
        }

        mainBlock = c;

        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void updateTick(Random random)
    {
        if (activeConnections.size() > 0) // Make sure we're only ticking while we have active connections
        {
            // if has enough for stable drain, do it
            // else if has enough for unstable drain, do it -- 75% with 20% risk, 50% with 50% risk, 40% with 75% risk 25% with 90% risk
            // else if not enough, terminate all connections

            //System.out.println(String.format("Draining %s power", activeConnections.size() * 100));

            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, CommonProxy.blockStabilizer.blockID, 1);
        }
    }

    public void onEntityEnterPortal(GlyphIdentifier uID, Entity entity)
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
                EntityManager.teleportEntity(entity, uID, exit);
            }
        }
        
        EntityManager.setEntityPortalCooldown(entity);
    }
}
