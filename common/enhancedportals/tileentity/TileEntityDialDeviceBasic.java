package enhancedportals.tileentity;

import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.world.WorldLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Strings;
import enhancedportals.network.packet.PacketBasicDialDeviceUpdate;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketRequestData;
import enhancedportals.world.WorldHelper;

public class TileEntityDialDeviceBasic extends TileEntityEnhancedPortals
{
    public boolean active;
    WorldLocation modifierLocation;
    Ticket chunkTicket;
    final int TICK_DELAY = 50;
    int timer, ticksToGo;

    public TileEntityDialDeviceBasic()
    {
        active = false;
    }

    @Override
    public void invalidate()
    {
        ForgeChunkManager.releaseTicket(chunkTicket);

        super.invalidate();
    }

    private void loadChunk()
    {
        if (chunkTicket == null)
        {
            chunkTicket = ForgeChunkManager.requestTicket(EnhancedPortals.instance, worldObj, Type.NORMAL);
        }

        if (chunkTicket == null)
        {
            Reference.log.log(Level.WARNING, String.format("The Basic Dialling Device at %s, %s, %s may not automatically close the portals due to no chunkloaders available."));
            return;
        }

        chunkTicket.getModData().setInteger("basicDialX", xCoord);
        chunkTicket.getModData().setInteger("basicDialY", yCoord);
        chunkTicket.getModData().setInteger("basicDialZ", zCoord);

        loadChunk(chunkTicket);
    }

    public void loadChunk(Ticket ticket)
    {
        if (chunkTicket == null)
        {
            chunkTicket = ticket;
        }

        ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4));
    }

    public void processDiallingRequest(String network, EntityPlayer player)
    {
        if (worldObj.isRemote || active)
        {
            return;
        }

        modifierLocation = WorldHelper.findPortalModifier(modifierLocation, worldObj, xCoord, yCoord, zCoord);

        if (modifierLocation == null)
        {
            sendChatToPlayer(Strings.ChatNoModifiers.toString(), player);
            return;
        }

        TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity(), exitModifier = null;

        if (modifier.dialDeviceNetwork.equals(network))
        {
            sendChatToPlayer(Strings.ChatInvalidDestination.toString(), player);
            return;
        }

        if (EnhancedPortals.proxy.DialDeviceNetwork.hasNetwork(network))
        {
            exitModifier = (TileEntityPortalModifier) EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(network).get(0).getTileEntity();

            if (exitModifier.isActive())
            {
                sendChatToPlayer(Strings.ChatDialActivePortal.toString(), player);
                return;
            }

            if (!modifier.createPortalFromDialDevice() || !exitModifier.createPortalFromDialDevice())
            {
                modifier.removePortal();
                exitModifier.removePortal();

                sendChatToPlayer(Strings.ChatNoConnection.toString(), player);
            }
            else
            {
                sendChatToPlayer(Strings.ChatDialSuccess.toString(), player);
                sendChatToPlayer(String.format(Strings.ChatDialSuccess2.toString(), "38"), player);

                modifier.tempDialDeviceNetwork = network;
                exitModifier.tempDialDeviceNetwork = modifier.dialDeviceNetwork;
                active = true;
                ticksToGo = 760;

                worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, BlockIds.DialHomeDeviceBasic, TICK_DELAY);
                loadChunk();

                PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketBasicDialDeviceUpdate(this)));
            }
        }
        else
        {
            sendChatToPlayer(Strings.ChatNoConnection.toString(), player);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        active = tagCompound.getBoolean("Active");

        if (active)
        {
            modifierLocation = new WorldLocation(tagCompound.getInteger("ModifierX"), tagCompound.getInteger("ModifierY"), tagCompound.getInteger("ModifierZ"), tagCompound.getInteger("ModifierD"));
            ticksToGo = tagCompound.getInteger("TicksToGo");
        }
    }

    public void scheduledBlockUpdate()
    {
        if (modifierLocation == null || !(modifierLocation.getTileEntity() instanceof TileEntityPortalModifier))
        {
            active = false;
            unloadChunk();
            PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketBasicDialDeviceUpdate(this)));
            return;
        }

        TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity();
        TileEntityPortalModifier exitModifier = (TileEntityPortalModifier) EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(modifier.tempDialDeviceNetwork).get(0).getTileEntity();

        if (ticksToGo > 0 && active)
        {
            int time = TICK_DELAY;

            if (time > ticksToGo)
            {
                time = ticksToGo;
            }

            ticksToGo -= time;

            if (!modifier.isAnyActive() || !exitModifier.isAnyActive())
            {
                ticksToGo = 0;
                time = 0;
            }

            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, BlockIds.DialHomeDeviceBasic, time);
        }
        else if (ticksToGo == 0 && active)
        {
            modifier.removePortal();
            exitModifier.removePortal();
            modifier.tempDialDeviceNetwork = "";
            exitModifier.tempDialDeviceNetwork = "";
            active = false;
            unloadChunk();
        }
    }

    private void sendChatToPlayer(String str, EntityPlayer player)
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

    private void unloadChunk()
    {
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

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setBoolean("Active", active);

        if (active)
        {
            tagCompound.setInteger("ModifierX", modifierLocation.xCoord);
            tagCompound.setInteger("ModifierY", modifierLocation.yCoord);
            tagCompound.setInteger("ModifierZ", modifierLocation.zCoord);
            tagCompound.setInteger("ModifierD", modifierLocation.dimension);
            tagCompound.setInteger("TicksToGo", ticksToGo);
        }
    }
}
