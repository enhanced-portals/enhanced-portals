package enhancedportals.world;

import net.minecraft.world.World;
import enhancedcore.world.WorldLocation;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class WorldHelper
{
    public static WorldLocation findPortalModifier(WorldLocation modifierLocation, World worldObj, int xCoord, int yCoord, int zCoord)
    {
        if (modifierLocation != null)
        {
            if (modifierLocation.getTileEntity() instanceof TileEntityPortalModifier)
            {
                return modifierLocation;
            }
            else
            {
                modifierLocation = null;
            }
        }

        for (int i = -5; i < 6; i++)
        {
            for (int j = -5; j < 6; j++)
            {
                for (int k = -5; k < 6; k++)
                {
                    if (worldObj.blockHasTileEntity(xCoord + i, yCoord + k, zCoord + j) && worldObj.getBlockTileEntity(xCoord + i, yCoord + k, zCoord + j) instanceof TileEntityPortalModifier)
                    {
                        TileEntityPortalModifier modifier = (TileEntityPortalModifier) worldObj.getBlockTileEntity(xCoord + i, yCoord + k, zCoord + j);

                        if (modifier != null && modifier.isRemotelyControlled() && !modifier.isAnyActive())
                        {
                            modifierLocation = new WorldLocation(xCoord + i, yCoord + k, zCoord + j, worldObj);
                        }
                    }
                }
            }
        }

        return modifierLocation;
    }
}
