package enhancedportals.tileentity;

import java.util.List;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedcore.world.WorldPosition;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Strings;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketRequestData;

public class TileEntityDialDeviceBase extends TileEntityEnhancedPortals
{
    public boolean active;
    protected BlockPosition modifierLocation;
    protected String dialledNetwork;
    protected Ticket chunkTicket;
    protected final int TICK_DELAY = 50;
    protected int timer, ticksToGo;

    public TileEntityDialDeviceBase()
    {
        active = false;
    }

    protected BlockPosition findPortalModifier()
    {
        if (modifierLocation != null)
        {
            if (WorldHelper.getTileEntity(worldObj, modifierLocation) instanceof TileEntityPortalModifier)
            {
                return modifierLocation;
            }
            else
            {
                modifierLocation = null;
            }
        }

        for (int i = -5; i < 6; i++)
        {
            for (int j = -5; j < 6; j++)
            {
                for (int k = -5; k < 6; k++)
                {
                    if (worldObj.blockHasTileEntity(xCoord + i, yCoord + k, zCoord + j) && worldObj.getBlockTileEntity(xCoord + i, yCoord + k, zCoord + j) instanceof TileEntityPortalModifier)
                    {
                        TileEntityPortalModifier modifier = (TileEntityPortalModifier) worldObj.getBlockTileEntity(xCoord + i, yCoord + k, zCoord + j);

                        if (modifier != null && modifier.isRemotelyControlled() && !modifier.isAnyActive())
                        {
                            modifierLocation = new WorldPosition(xCoord + i, yCoord + k, zCoord + j, worldObj);
                        }
                    }
                }
            }
        }

        return modifierLocation;
    }

    protected int getBlockID()
    {
        return 0;
    }

    @Override
    public void invalidate()
    {
        ForgeChunkManager.releaseTicket(chunkTicket);
        super.invalidate();
    }

    protected void loadChunk()
    {
        if (chunkTicket == null)
        {
            chunkTicket = ForgeChunkManager.requestTicket(EnhancedPortals.instance, worldObj, Type.NORMAL);
        }

        if (chunkTicket == null)
        {
            Reference.log.log(Level.WARNING, String.format("The Dialling Device at %s, %s, %s may not automatically close the portals due to no chunkloaders available."));
            return;
        }

        chunkTicket.getModData().setInteger("dialX", xCoord);
        chunkTicket.getModData().setInteger("dialY", yCoord);
        chunkTicket.getModData().setInteger("dialZ", zCoord);
        chunkTicket.getModData().setInteger("modifierX", modifierLocation.getX());
        chunkTicket.getModData().setInteger("modifierY", modifierLocation.getY());
        chunkTicket.getModData().setInteger("modifierZ", modifierLocation.getZ());

        loadChunk(chunkTicket);
    }

    public void loadChunk(Ticket ticket)
    {
        if (chunkTicket == null)
        {
            chunkTicket = ticket;
        }

        if (chunkTicket.getModData().hasKey("modifierX"))
        {
            ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(chunkTicket.getModData().getInteger("modifierX") >> 4, chunkTicket.getModData().getInteger("modifierZ") >> 4));
        }

        ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4));
    }

    protected void processDiallingRequest(String network, EntityPlayer player, String texture, int thickness, int tickTimer)
    {
        if (worldObj.isRemote)
        {
            return;
        }
        else if (active)
        {
            ticksToGo = 0;
            scheduledBlockUpdate();
            return;
        }

        modifierLocation = findPortalModifier();

        if (modifierLocation == null)
        {
            sendChatToPlayer(Strings.ChatNoModifiers.toString(), player);
            return;
        }

        TileEntityPortalModifier entryModifier = (TileEntityPortalModifier) WorldHelper.getTileEntity(worldObj, modifierLocation), exitModifier = null;

        if (entryModifier.dialDeviceNetwork.equals(network))
        {
            sendChatToPlayer(Strings.ChatInvalidDestination.toString(), player);
            return;
        }

        if (EnhancedPortals.proxy.DialDeviceNetwork.hasNetwork(network))
        {
            if (EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(network).isEmpty())
            {
                EnhancedPortals.proxy.DialDeviceNetwork.removeNetwork(network);
                sendChatToPlayer(Strings.ChatNoConnection.toString(), player);
                return;
            }

            WorldPosition exitLocation = EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(network).get(0);
            ((WorldServer) WorldHelper.getWorld(exitLocation.getDimension())).getChunkProvider().loadChunk(exitLocation.getX() >> 4, exitLocation.getZ() >> 4);
            exitModifier = (TileEntityPortalModifier) exitLocation.getTileEntity();

            if (exitModifier == null)
            {
                Reference.log.log(Level.WARNING, "This is unexpected. Tried to dial a network but could not find an exit location!");
                return;
            }

            if (exitModifier.isActive())
            {
                sendChatToPlayer(Strings.ChatDialActivePortal.toString(), player);
                return;
            }

            boolean createdEntryPortal = false, createdExitPortal = false;

            if (texture == null && thickness == -1)
            {
                createdEntryPortal = entryModifier.createPortalFromDialDevice();
                createdExitPortal = exitModifier.createPortalFromDialDevice();
            }
            else
            {
                createdEntryPortal = entryModifier.createPortalFromDialDevice(texture, (byte) thickness);
                createdExitPortal = exitModifier.createPortalFromDialDevice(texture, (byte) thickness);
            }

            if (!createdEntryPortal || !createdExitPortal)
            {
                entryModifier.removePortal();
                exitModifier.removePortal();

                sendChatToPlayer(Strings.ChatNoConnection.toString(), player);
            }
            else
            {
                dialledNetwork = network;
                entryModifier.tempDialDeviceNetwork = network;
                exitModifier.tempDialDeviceNetwork = entryModifier.dialDeviceNetwork;
                active = true;
                ticksToGo = tickTimer == -1 ? 760 : tickTimer;

                loadChunk();
                sendUpdatePacket();

                sendChatToPlayer(Strings.ChatDialSuccess.toString(), player);

                if (ticksToGo / 20 > 0)
                {
                    worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, getBlockID(), TICK_DELAY);
                    sendChatToPlayer(String.format(Strings.ChatDialSuccess2.toString(), ticksToGo / 20), player);
                }
            }
        }
        else
        {
            sendChatToPlayer(Strings.ChatNoConnection.toString(), player);
        }
    }

    public void scheduledBlockUpdate()
    {        
        if (!active || worldObj.isRemote)
        {
            return;
        }
        else if (modifierLocation == null || !(WorldHelper.getTileEntity(worldObj, modifierLocation) instanceof TileEntityPortalModifier))
        {
            unloadChunk();
            sendUpdatePacket();
            
            List<WorldPosition> positionList = EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(dialledNetwork);
            WorldPosition exitLocation = null;
            
            if (!positionList.isEmpty())
            {
                exitLocation = positionList.get(0);
            }
            
            if (exitLocation != null)
            {
                ((WorldServer) WorldHelper.getWorld(exitLocation.getDimension())).getChunkProvider().loadChunk(exitLocation.getX() >> 4, exitLocation.getZ() >> 4);
                TileEntityPortalModifier exitModifier = (TileEntityPortalModifier) exitLocation.getTileEntity();
                
                if (exitModifier != null)
                {
                    exitModifier.removePortal();
                    exitModifier.tempDialDeviceNetwork = "";
                }
            }
            
            active = false;
            return;
        }

        TileEntityPortalModifier modifier = (TileEntityPortalModifier) WorldHelper.getTileEntity(worldObj, modifierLocation);
        List<WorldPosition> positionList = EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(dialledNetwork);
        WorldPosition exitLocation = null;
        
        if (!positionList.isEmpty())
        {
            exitLocation = positionList.get(0);
        }

        if (exitLocation == null)
        {
            active = false;
            return;
        }
        else
        {
            ((WorldServer) WorldHelper.getWorld(exitLocation.getDimension())).getChunkProvider().loadChunk(exitLocation.getX() >> 4, exitLocation.getZ() >> 4);
        }

        TileEntityPortalModifier exitModifier = (TileEntityPortalModifier) exitLocation.getTileEntity();

        if (exitModifier == null)
        {
            ticksToGo = 0;
            scheduledBlockUpdate();
            return;
        }

        if (ticksToGo > 0)
        {
            int time = TICK_DELAY;
            ticksToGo -= TICK_DELAY;

            if (!modifier.isAnyActive() || !exitModifier.isAnyActive())
            {
                ticksToGo = 0;
                time = 0;
            }

            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, getBlockID(), time);
        }
        else if (ticksToGo <= 0)
        {
            modifier.removePortal();
            modifier.tempDialDeviceNetwork = "";

            if (exitModifier != null)
            {
                exitModifier.removePortal();
                exitModifier.tempDialDeviceNetwork = "";
            }

            active = false;
            unloadChunk();
            sendUpdatePacket();
        }
    }

    protected void sendChatToPlayer(String str, EntityPlayer player)
    {
        if (player != null)
        {
            player.sendChatToPlayer(str);
        }
        else
        {
            Reference.log.log(Level.INFO, str);
        }
    }

    protected void sendUpdatePacket()
    {

    }

    protected void unloadChunk()
    {
        if (chunkTicket == null)
        {
            return;
        }
        else if (chunkTicket.getModData().hasKey("modifierX"))
        {
            ForgeChunkManager.unforceChunk(chunkTicket, new ChunkCoordIntPair(chunkTicket.getModData().getInteger("modifierX") >> 4, chunkTicket.getModData().getInteger("modifierZ") >> 4));
        }

        ForgeChunkManager.unforceChunk(chunkTicket, new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4));
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketRequestData(this)));
        }
    }
}
