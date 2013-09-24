package uk.co.shadeddimensions.enhancedportals.tileentity.frame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileNetworkInterface extends TilePortalFrame
{
    public String NetworkIdentifier;

    @SideOnly(Side.CLIENT)
    public int connectedPortals = 0;
    
    public TileNetworkInterface()
    {
        NetworkIdentifier = "";
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
            else if (!string.equals(""))
            {
                CommonProxy.networkManager.addPortalToNetwork(getControllerValidated().UniqueIdentifier, string);
            }
            
            NetworkIdentifier = string;
        }
        
        CommonProxy.sendUpdatePacketToAllAround(this);
    }
}
