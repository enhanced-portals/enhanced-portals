package uk.co.shadeddimensions.ep3.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class TileStabilizer extends TileEnhancedPortals
{
    public boolean hasConfigured;
    
    public TileStabilizer()
    {
        hasConfigured = false;
    }
    
    @Override
    public boolean activate(EntityPlayer player)
    {
        if (super.activate(player))
        {
            return true;
        }
        else if (CommonProxy.isClient() || hasConfigured)
        {
            return false;
        }
        
        WorldCoordinates topLeft = getWorldCoordinates();
        
        while (topLeft.offset(ForgeDirection.WEST).getBlockId() == CommonProxy.blockStabilizer.blockID)
        {
            topLeft = topLeft.offset(ForgeDirection.WEST);
        }
        
        while (topLeft.offset(ForgeDirection.NORTH).getBlockId() == CommonProxy.blockStabilizer.blockID)
        {
            topLeft = topLeft.offset(ForgeDirection.NORTH);
        }
        
        while (topLeft.offset(ForgeDirection.UP).getBlockId() == CommonProxy.blockStabilizer.blockID)
        {
            topLeft = topLeft.offset(ForgeDirection.UP);
        }
        
        System.out.println(checkShape(topLeft, true) + ", " + checkShape(topLeft, false));
        return false;
    }
    
    boolean checkShape(WorldCoordinates topLeft, boolean isX)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                for (int k = 0; k < 2; k++)
                {
                    if (worldObj.getBlockId(topLeft.posX + (isX ? i : j), topLeft.posY - k, topLeft.posZ + (!isX ? i : j)) != CommonProxy.blockStabilizer.blockID)
                    {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
}
