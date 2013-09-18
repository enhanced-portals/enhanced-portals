package uk.co.shadeddimensions.enhancedportals.tileentity;

import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.lib.GuiIds;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TilePortalFrameNetworkInterface extends TilePortalFrame
{
    public String NetworkIdentifier;

    public TilePortalFrameNetworkInterface()
    {
        NetworkIdentifier = "NOT_SET";
    }

    @Override
    public boolean activate(EntityPlayer player)
    {
        if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == CommonProxy.itemWrench.itemID)
        {
            TilePortalFrameController control = getControllerValidated();

            if (control != null)
            {
                player.openGui(EnhancedPortals.instance, GuiIds.NETWORK_INTERFACE, worldObj, xCoord, yCoord, zCoord);
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        
        tagCompound.setString("NetworkIdentifier", NetworkIdentifier);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        
        NetworkIdentifier = tagCompound.getString("NetworkIdentifier");
    }
    
    @Override
    public void actionPerformed(int id, String string, EntityPlayer player)
    {
        System.out.println(id);
        
        if (id == 0)
        {
            // TODO: NETWORKING
            NetworkIdentifier = string;
            System.out.println(NetworkIdentifier);
        }
        
        CommonProxy.sendUpdatePacketToAllAround(this);
    }
}
