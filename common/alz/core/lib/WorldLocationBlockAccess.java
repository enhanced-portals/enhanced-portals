package alz.core.lib;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class WorldLocationBlockAccess extends WorldLocation
{
    public static boolean isEqual(WorldLocationBlockAccess location, WorldLocationBlockAccess location2)
    {
        return location2.xCoord == location.xCoord && location2.yCoord == location.yCoord && location2.zCoord == location.zCoord && location2.dimension == location.dimension;
    }

    public IBlockAccess blockAccess;

    public WorldLocationBlockAccess()
    {

    }

    public WorldLocationBlockAccess(int x, int y, int z, IBlockAccess blockaccess)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        blockAccess = blockaccess;
    }

    @Override
    public int getBlockId()
    {
        return blockAccess.getBlockId(xCoord, yCoord, zCoord);
    }

    @Override
    public Material getMaterial()
    {
        return blockAccess.getBlockMaterial(xCoord, yCoord, zCoord);
    }

    @Override
    public int getMetadata()
    {
        return blockAccess.getBlockMetadata(xCoord, yCoord, zCoord);
    }

    @Override
    public WorldLocation getOffset(ForgeDirection direction)
    {
        WorldLocation newLocation = new WorldLocation();
        newLocation.dimension = dimension;
        newLocation.xCoord = xCoord + direction.offsetX;
        newLocation.yCoord = yCoord + direction.offsetY;
        newLocation.zCoord = zCoord + direction.offsetZ;

        return newLocation;
    }

    @Override
    public TileEntity getTileEntity()
    {
        return blockAccess.getBlockTileEntity(xCoord, yCoord, zCoord);
    }

    @Override
    public boolean isBlockAir()
    {
        return blockAccess.isAirBlock(xCoord, yCoord, zCoord);
    }

    public boolean isEqual(WorldLocationBlockAccess loc)
    {
        return isEqual(this, loc);
    }

    @Override
    @Deprecated
    public void markBlockForUpdate()
    {

    }

    @Override
    @Deprecated
    public void setBlock(int id)
    {

    }

    @Override
    @Deprecated
    public void setBlockAndMeta(int id, int meta, int flags)
    {

    }

    @Override
    @Deprecated
    public void setBlockToAir()
    {

    }
}
