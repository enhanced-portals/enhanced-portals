package enhancedportals.tileentity;

import java.util.ArrayList;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
import enhancedportals.network.packet.PacketDialDeviceUpdate;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.portal.network.DialDeviceNetworkObject;
import enhancedportals.world.WorldHelper;

public class TileEntityDialDevice extends TileEntityEnhancedPortals
{
    public ArrayList<DialDeviceNetworkObject> destinationList;
    public int                                selectedDestination, tickTimer, timer;
    public boolean                            active;

    WorldLocation                             modifierLocation;
    Ticket         chunkTicket;

    public TileEntityDialDevice()
    {
        destinationList = new ArrayList<DialDeviceNetworkObject>();
        selectedDestination = -1;
        tickTimer = 760;
        active = false;
    }

    public boolean hasDestination(DialDeviceNetworkObject obj2)
    {
        for (DialDeviceNetworkObject obj : destinationList)
        {
            if (obj.displayName.equals(obj2.displayName) && obj.texture.equals(obj2.texture) && obj.network.equals(obj2.network) && obj.thickness == obj2.thickness)
            {
                return true;
            }
        }

        return false;
    }

    public void processDiallingRequest(int id, EntityPlayer player)
    {
        if (worldObj.isRemote || active)
        {
            return;
        }

        modifierLocation = WorldHelper.findPortalModifier(modifierLocation, worldObj, xCoord, yCoord, zCoord);
        DialDeviceNetworkObject obj = destinationList.get(id);

        if (modifierLocation == null)
        {
            sendChatToPlayer(Strings.ChatNoModifiers.toString(), player);
            return;
        }

        TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity(), exitModifier = null;

        if (modifier.dialDeviceNetwork.equals(obj.network))
        {
            sendChatToPlayer(Strings.ChatInvalidDestination.toString(), player);
            return;
        }

        if (EnhancedPortals.proxy.DialDeviceNetwork.hasNetwork(obj.network))
        {
            exitModifier = (TileEntityPortalModifier) EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(obj.network).get(0).getTileEntity();

            if (exitModifier.isActive())
            {
                sendChatToPlayer(Strings.ChatDialActivePortal.toString(), player);
                return;
            }

            if (!modifier.createPortalFromDialDevice(obj.texture, obj.thickness) || !exitModifier.createPortalFromDialDevice(obj.texture, obj.thickness))
            {
                modifier.removePortal();
                exitModifier.removePortal();

                sendChatToPlayer(Strings.ChatNoConnection.toString(), player);
            }
            else
            {
                sendChatToPlayer(Strings.ChatDialSuccess.toString(), player);
                sendChatToPlayer(Strings.ChatDialSuccess2.toString(), player);

                modifier.tempDialDeviceNetwork = obj.network;
                exitModifier.tempDialDeviceNetwork = modifier.dialDeviceNetwork;
                active = true;

                worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, BlockIds.DialHomeDevice, tickTimer);
                PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketDialDeviceUpdate(this)));
                loadChunk();
            }
        }
        else
        {
            sendChatToPlayer(Strings.ChatNoConnection.toString(), player);
        }
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
            Reference.log.log(Level.WARNING, String.format("The Dialling Device at %s, %s, %s may not automatically close the portals due to no chunkloaders available."));
            return;
        }

        chunkTicket.getModData().setInteger("dialX", xCoord);
        chunkTicket.getModData().setInteger("dialY", yCoord);
        chunkTicket.getModData().setInteger("dialZ", zCoord);

        loadChunk(chunkTicket);
    }
    
    private void unloadChunk()
    {
        ForgeChunkManager.unforceChunk(chunkTicket, new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4));
    }

    public void loadChunk(Ticket ticket)
    {
        if (chunkTicket == null)
        {
            chunkTicket = ticket;
        }

        ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4));
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
    
    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        tickTimer = tagCompound.getInteger("TickTimer");

        if (tagCompound.hasKey("mX"))
        {
            active = true;
            modifierLocation = new WorldLocation(tagCompound.getInteger("mX"), tagCompound.getInteger("mY"), tagCompound.getInteger("mZ"), tagCompound.getInteger("mD"));
            timer = tagCompound.getInteger("Timer");
        }

        NBTTagList list = tagCompound.getTagList("Entries");

        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);

            String name = tag.getString("Name"), network = tag.getString("Network");
            byte thickness = tag.getByte("Thickness");
            String texture = tag.getString("Texture");

            destinationList.add(new DialDeviceNetworkObject(name, network, texture, thickness));
        }
    }

    public boolean removeDestination(DialDeviceNetworkObject obj2)
    {
        for (int i = 0; i < destinationList.size(); i++)
        {
            DialDeviceNetworkObject obj = destinationList.get(i);

            if (obj.displayName.equals(obj2.displayName) && obj.texture.equals(obj2.texture) && obj.network.equals(obj2.network) && obj.thickness == obj2.thickness)
            {
                destinationList.remove(i);
                return true;
            }
        }

        return false;
    }

    public void scheduledBlockUpdate()
    {
        if (modifierLocation == null || !(modifierLocation.getTileEntity() instanceof TileEntityPortalModifier))
        {
            active = false;
            unloadChunk();
            PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketDialDeviceUpdate(this)));
            return;
        }

        TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity();
        TileEntityPortalModifier exitModifier = (TileEntityPortalModifier) EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(modifier.tempDialDeviceNetwork).get(0).getTileEntity();

        modifier.removePortal();
        exitModifier.removePortal();
        modifier.tempDialDeviceNetwork = "";
        exitModifier.tempDialDeviceNetwork = "";
        active = false;
        unloadChunk();
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        if (active)
        {
            tagCompound.setInteger("mX", modifierLocation.xCoord);
            tagCompound.setInteger("mY", modifierLocation.yCoord);
            tagCompound.setInteger("mZ", modifierLocation.zCoord);
            tagCompound.setInteger("mD", modifierLocation.dimension);
            tagCompound.setInteger("Timer", timer);
        }

        tagCompound.setInteger("TickTimer", tickTimer);

        NBTTagList list = new NBTTagList();

        for (int i = 0; i < destinationList.size(); i++)
        {
            NBTTagCompound tag = new NBTTagCompound();
            DialDeviceNetworkObject obj = destinationList.get(i);

            if (obj == null)
            {
                continue;
            }

            tag.setString("Name", obj.displayName);
            tag.setString("Network", obj.network);
            tag.setByte("Thickness", obj.thickness);
            tag.setString("Texture", obj.texture);

            list.appendTag(tag);
        }

        tagCompound.setTag("Entries", list);
    }
}
