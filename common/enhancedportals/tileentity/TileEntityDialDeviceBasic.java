package enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
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
    
    public void processDiallingRequest(String network, EntityPlayer player)
    {
        if (worldObj.isRemote)
        {
            return;
        }
        
        // TODO
        // - Add wireless connection to portal modifier
        // - Localization
        // - Check for modifier upgrades
        
        if (EnhancedPortals.proxy.ModifierNetwork.hasNetwork(network))
        {
            if (worldObj.getBlockId(xCoord, yCoord, zCoord - 1) == BlockIds.PortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1);
                
                modifier.network = network;
                
                if (modifier.isAnyActive())
                {
                    modifier.removePortal();
                }
                
                if (modifier.createPortal())
                {
                    player.sendChatToPlayer(EnumChatFormatting.GREEN + "Connection established.");
                }
                else
                {
                    player.sendChatToPlayer(EnumChatFormatting.RED + "Could not establish a connection to the network.");
                }
            }
            else
            {
                player.sendChatToPlayer(EnumChatFormatting.RED + "Could not create a portal.");
            }
        }
        else
        {
            player.sendChatToPlayer(EnumChatFormatting.RED + "Could not establish a connection to the network.");
        }
    }
}
