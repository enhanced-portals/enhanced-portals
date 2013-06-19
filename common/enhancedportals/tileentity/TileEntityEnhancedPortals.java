package enhancedportals.tileentity;

import net.minecraft.tileentity.TileEntity;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldPosition;

public abstract class TileEntityEnhancedPortals extends TileEntity
{
    public BlockPosition getBlockPosition()
    {
        return new BlockPosition(xCoord, yCoord, zCoord);
    }

    public WorldPosition getWorldPosition()
    {
        return new WorldPosition(xCoord, yCoord, zCoord, worldObj);
    }
}
