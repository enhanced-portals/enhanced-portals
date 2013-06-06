package enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.world.WorldLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.network.packet.PacketBasicDialDeviceUpdate;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketRequestData;

public class TileEntityDialDeviceBasic extends TileEntityEnhancedPortals
{
    public boolean active;
    WorldLocation  modifierLocation;

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

                        TileEntityPortalModifier modifier = (TileEntityPortalModifier) worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord);
                        
                        if (modifier != null && modifier.isRemotelyControlled())
                        {
                            modifierLocation = new WorldLocation(xCoord - 1, yCoord, zCoord, worldObj);
                        }
    }

    public void processDiallingRequest(String network, EntityPlayer player)
    {
        System.out.println("Recieved dialling request for: " + network + ". From: " + player.getEntityName());
        
        if (worldObj.isRemote || active)
        {
            return;
        }

        findPortalModifier();

        if (modifierLocation == null)
        {
            if (player != null)
            {
                player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noModifier"));
            }
            
            System.out.println("Couldn't find a portal modifier!");
            return;
        }
        
        TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity(), exitModifier = null;
        
        if (modifier.dialDeviceNetwork.equals(network))
        {
            if (player != null)
            {
                player.sendChatToPlayer(EnumChatFormatting.RED + "Invalid destination");
            }
            
            return;
        }

        if (EnhancedPortals.proxy.DialDeviceNetwork.hasNetwork(network))
        {
            exitModifier = (TileEntityPortalModifier) EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(network).get(0).getTileEntity();
            
            if (exitModifier.isActive())
            {
                player.sendChatToPlayer(EnumChatFormatting.RED + "Cannot dial an active portal");
                return;
            }
            
            if (!modifier.createPortalFromDialDevice() || !exitModifier.createPortalFromDialDevice())
            {
                player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noConnection"));
            }
            else
            {
                player.sendChatToPlayer(EnumChatFormatting.GREEN + "Successfully dialled the portal");
                player.sendChatToPlayer(EnumChatFormatting.GREEN + "38 seconds remain until automatic shutdown");

                modifier.tempDialDeviceNetwork = network;
                exitModifier.tempDialDeviceNetwork = modifier.dialDeviceNetwork;
                active = true;
                
                worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, BlockIds.DialHomeDeviceBasic, 760);
                PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketBasicDialDeviceUpdate(this)));
            }
        }
        else if (player != null)
        {
            player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noConnection"));
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
            PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketBasicDialDeviceUpdate(this)));
            return;
        }

        TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity(),
                                 exitModifier = (TileEntityPortalModifier) EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(modifier.tempDialDeviceNetwork).get(0).getTileEntity();;
        modifier.removePortal();
        exitModifier.removePortal();
        modifier.tempDialDeviceNetwork = "";
        exitModifier.tempDialDeviceNetwork = "";
        active = false;
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
