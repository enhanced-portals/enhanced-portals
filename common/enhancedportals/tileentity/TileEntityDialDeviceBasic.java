package enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;
import enhancedportals.network.packet.PacketRequestSync;
import enhancedportals.network.packet.PacketTEUpdate;

public class TileEntityDialDeviceBasic extends TileEntityEnhancedPortals
{
    @Override
    public PacketData getPacketData()
    {
        return new PacketData(new int[0], new byte[] { (byte) (active ? 1 : 0) }, new String[0]);
    }

    @Override
    public void parsePacketData(PacketData data)
    {
        if (data.byteData.length != 1)
        {
            return;
        }
        
        active = data.byteData[0] == 1;
    }
    
    String oldModifierNetwork;
    public boolean active = false;
    WorldLocation modifierLocation;
    
    public void processDiallingRequest(String network, EntityPlayer player)
    {
        if (worldObj.isRemote || active)
        {
            return;
        }
                
        outerloop:
        for (int x = -5; x < 5; x++)
        {
            for (int z = -5; z < 5; z++)
            {
                for (int y = -5; y < 5; y++)
                {
                    if (worldObj.getBlockId(xCoord + x, yCoord + y, zCoord + z) == BlockIds.PortalModifier)
                    {
                        modifierLocation = new WorldLocation(xCoord + x, yCoord + y, zCoord + z, worldObj);
                        break outerloop;
                    }
                }
            }
        }
        
        if (modifierLocation == null)
        {
            if (player != null)
            {
                player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noModifier"));
            }
            
            return;
        }
        
        if (EnhancedPortals.proxy.ModifierNetwork.hasNetwork(network))
        {
            if (modifierLocation.getBlockId() == BlockIds.PortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity();
                
                oldModifierNetwork = modifier.network;                
                EnhancedPortals.proxy.ModifierNetwork.removeFromAllNetworks(modifierLocation);
                EnhancedPortals.proxy.ModifierNetwork.addToNetwork(network, modifierLocation);
                modifier.network = network;
                
                if (modifier.isAnyActive())
                {
                    modifier.removePortal();
                }
                
                if (modifier.createPortal())
                {
                    if (player != null)
                    {
                        player.sendChatToPlayer(EnumChatFormatting.GREEN + Localization.localizeString("chat.dialSuccess"));
                    }
                    
                    worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, BlockIds.DialHomeDeviceBasic, 760);
                    active = true;
                    PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
                }
                else
                {
                    modifier.network = oldModifierNetwork;
                    
                    if (player != null)
                    {
                        player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noPortal"));
                    }
                }
            }
            else if (player != null)
            {
                player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noConnection"));
            }
        }
        else if (player != null)
        {
            player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noConnection"));
        }
    }
    
    public void scheduledBlockUpdate()
    {        
        if (modifierLocation == null)
        {
            active = false;
            PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
            return;
        }
        
        if (modifierLocation.getBlockId() == BlockIds.PortalModifier)
        {
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity();
            
            EnhancedPortals.proxy.ModifierNetwork.removeFromNetwork(modifier.network, modifierLocation);
            
            if (oldModifierNetwork != null && !oldModifierNetwork.equals(""))
            {
                EnhancedPortals.proxy.ModifierNetwork.addToNetwork(oldModifierNetwork, modifierLocation);
            }
            
            modifier.removePortal();
            modifier.network = oldModifierNetwork;
            
            oldModifierNetwork = "";
            modifierLocation = null;
            active = false;
            PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
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
            oldModifierNetwork = tagCompound.getString("ModifierNetwork");        
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
            tagCompound.setString("ModifierNetwork", oldModifierNetwork);
        }
    }
    
    @Override
    public void validate()
    {
        super.validate();
        
        if (worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToServer(new PacketRequestSync(this).getPacket());
        }
    }
}
