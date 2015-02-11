package enhancedportals.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import enhancedportals.util.DimensionCoordinates;

public class TileEP extends TileEntity {
    public boolean onBlockActivated(EntityPlayer player) {
        return false;
    }
    
    public DimensionCoordinates getDimensionCoordinates() {
        return new DimensionCoordinates(xCoord, yCoord, zCoord, worldObj.provider.dimensionId);
    }
    
    public void onPreDestroy() {
        
    }
}
