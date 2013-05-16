package enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;

public class TileEntityDialDeviceBasic extends TileEntityEnhancedPortals
{
    @Override
    public PacketData getPacketData()
    {
        return null;
    }

    @Override
    public void parsePacketData(PacketData data)
    {
        
    }
    
    String oldModifierNetwork;
    boolean oldParticles, oldSounds, active = false;
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
        
        if (EnhancedPortals.proxy.ModifierNetwork.hasNetwork(network))
        {
            if (modifierLocation.getBlockId() == BlockIds.PortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity();
                
                oldModifierNetwork = modifier.network;
                oldParticles = modifier.getParticles();
                oldSounds = modifier.getSounds();
                
                EnhancedPortals.proxy.ModifierNetwork.removeFromAllNetworks(modifierLocation);
                EnhancedPortals.proxy.ModifierNetwork.addToNetwork(network, modifierLocation);
                modifier.network = network;
                
                if (modifier.isAnyActive())
                {
                    modifier.removePortal();
                }
                
                if (modifier.createPortal())
                {
                    player.sendChatToPlayer(EnumChatFormatting.GREEN + "Connection established. 38 seconds remain.");
                    worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, BlockIds.DialHomeDeviceBasic, 760);
                    active = true;
                }
                else
                {
                    player.sendChatToPlayer(EnumChatFormatting.RED + "Could not establish a connection.");
                }
            }
            else
            {
                player.sendChatToPlayer(EnumChatFormatting.RED + "Could not create a portal.");
            }
        }
        else
        {
            player.sendChatToPlayer(EnumChatFormatting.RED + "Could not establish a connection.");
        }
    }
    
    public void scheduledBlockUpdate()
    {
        if (modifierLocation == null)
        {
            active = false;
            return;
        }
        
        if (modifierLocation.getBlockId() == BlockIds.PortalModifier)
        {
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity();
            
            EnhancedPortals.proxy.ModifierNetwork.removeFromNetwork(modifier.network, modifierLocation);
            
            if (!oldModifierNetwork.equals(""))
            {
                EnhancedPortals.proxy.ModifierNetwork.addToNetwork(oldModifierNetwork, modifierLocation);
            }
            
            modifier.removePortal();
            modifier.network = oldModifierNetwork;
            modifier.setSounds(oldSounds);
            modifier.setParticles(oldParticles);
            
            oldModifierNetwork = "";
            oldSounds = true;
            oldParticles = true;
            modifierLocation = null;
            active = false;
        }
    }
}
