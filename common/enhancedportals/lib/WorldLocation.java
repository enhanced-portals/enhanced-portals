package enhancedportals.lib;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class WorldLocation
{
    public static boolean equals(WorldLocation location, WorldLocation location2)
    {
        return location2.xCoord == location.xCoord && location2.yCoord == location.yCoord && location2.zCoord == location.zCoord && location2.dimension == location.dimension;
    }

    public int xCoord, yCoord, zCoord, dimension;

    public WorldLocation()
    {

    }

    public WorldLocation(int x, int y, int z)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }

    public WorldLocation(int x, int y, int z, IBlockAccess blockAccess)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = 0; // TODO
    }

    public WorldLocation(int x, int y, int z, World world)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = world.provider.dimensionId;
    }

    public boolean equals(WorldLocation loc)
    {
        return equals(this, loc);
    }

    public int getBlockId()
    {
        return getWorld().getBlockId(xCoord, yCoord, zCoord);
    }

    public Material getMaterial()
    {
        return getWorld().getBlockMaterial(xCoord, yCoord, zCoord);
    }

    public int getMetadata()
    {
        return getWorld().getBlockMetadata(xCoord, yCoord, zCoord);
    }

    public WorldLocation getOffset(ForgeDirection direction)
    {
        WorldLocation newLocation = new WorldLocation();
        newLocation.dimension = dimension;
        newLocation.xCoord = xCoord + direction.offsetX;
        newLocation.yCoord = yCoord + direction.offsetY;
        newLocation.zCoord = zCoord + direction.offsetZ;

        return newLocation;
    }

    public TileEntity getTileEntity()
    {
        return getWorld().getBlockTileEntity(xCoord, yCoord, zCoord);
    }

    public World getWorld()
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimension);
        }
        else
        {
            return FMLClientHandler.instance().getClient().theWorld;
        }
    }

    public boolean isBlockAir()
    {
        return getBlockId() == 0;
    }

    public void markBlockForUpdate()
    {
        getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setBlock(int id)
    {
        getWorld().setBlock(xCoord, yCoord, zCoord, id);
    }

    public void setBlockAndMeta(int id, int meta, int flags)
    {
        getWorld().setBlock(xCoord, yCoord, zCoord, id, meta, flags);
    }

    public void setBlockToAir()
    {
        getWorld().setBlockToAir(xCoord, yCoord, zCoord);
    }
}
