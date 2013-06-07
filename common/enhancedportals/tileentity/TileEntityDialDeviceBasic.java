package enhancedportals.tileentity;

import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.world.WorldLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketBasicDialDeviceUpdate;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketRequestData;

public class TileEntityDialDeviceBasic extends TileEntityEnhancedPortals
{
    public boolean active;
    WorldLocation  modifierLocation;
    Ticket         chunkTicket;

    public TileEntityDialDeviceBasic()
    {
        active = false;
    }

    private void findPortalModifier()
    {
        if (modifierLocation != null)
        {
            if (modifierLocation.getTileEntity() instanceof TileEntityPortalModifier)
            {
                return;
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
        
                        if (modifier != null && modifier.isRemotelyControlled())
                        {
                            modifierLocation = new WorldLocation(xCoord + i, yCoord + k, zCoord + j, worldObj);
                        }
                    }
                }
            }
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

        findPortalModifier();

        if (modifierLocation == null)
        {
            sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noModifier"), player);
            return;
        }

        TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity(), exitModifier = null;

        if (modifier.dialDeviceNetwork.equals(network))
        {
            sendChatToPlayer(EnumChatFormatting.RED + "Invalid destination", player);
            return;
        }

        if (EnhancedPortals.proxy.DialDeviceNetwork.hasNetwork(network))
        {
            exitModifier = (TileEntityPortalModifier) EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(network).get(0).getTileEntity();

            if (exitModifier.isActive())
            {
                sendChatToPlayer(EnumChatFormatting.RED + "Cannot dial an active portal", player);
                return;
            }

            if (!modifier.createPortalFromDialDevice() || !exitModifier.createPortalFromDialDevice())
            {
                sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noConnection"), player);
            }
            else
            {
                sendChatToPlayer(EnumChatFormatting.GREEN + "Successfully dialled the portal", player);
                sendChatToPlayer(EnumChatFormatting.GREEN + "38 seconds remain until automatic shutdown", player);

                modifier.tempDialDeviceNetwork = network;
                exitModifier.tempDialDeviceNetwork = modifier.dialDeviceNetwork;
                active = true;

                worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, BlockIds.DialHomeDeviceBasic, 760);
                PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketBasicDialDeviceUpdate(this)));
                loadChunk();
            }
        }
        else
        {
            sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noConnection"), player);
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

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        active = tagCompound.getBoolean("Active");

        if (active)
        {
            modifierLocation = new WorldLocation(tagCompound.getInteger("ModifierX"), tagCompound.getInteger("ModifierY"), tagCompound.getInteger("ModifierZ"), tagCompound.getInteger("ModifierD"));
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

        modifier.removePortal();
        exitModifier.removePortal();
        modifier.tempDialDeviceNetwork = "";
        exitModifier.tempDialDeviceNetwork = "";
        active = false;
        unloadChunk();
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
        }
    }
}
