package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.lib.GuiIds;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TilePortalFrameNetworkInterface extends TilePortalFrame
{
    public String NetworkIdentifier;

    @SideOnly(Side.CLIENT)
    public int connectedPortals = 0;
    
    public TilePortalFrameNetworkInterface()
    {
        NetworkIdentifier = "";
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
        if (id == 0)
        {
            if (!NetworkIdentifier.equals(""))
            {
                CommonProxy.networkManager.removePortalFromNetwork(getControllerValidated().UniqueIdentifier, NetworkIdentifier);
            }
            
            CommonProxy.networkManager.addPortalToNetwork(getControllerValidated().UniqueIdentifier, string);
            NetworkIdentifier = string;            
        }
        
        CommonProxy.sendUpdatePacketToAllAround(this);
    }
}
