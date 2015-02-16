package enhancedportals.utility;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

public class DimensionCoordinates extends ChunkCoordinates
{
    public int dimension;

    public DimensionCoordinates()
    {

    }

    public DimensionCoordinates(ChunkCoordinates chunkCoordinates, int dim)
    {
        super(chunkCoordinates);
        dimension = dim;
    }

    public DimensionCoordinates(int x, int y, int z, int dim)
    {
        super(x, y, z);
        dimension = dim;
    }

    public DimensionCoordinates(NBTTagCompound tag)
    {
        this(tag.getInteger("X"), tag.getInteger("Y"), tag.getInteger("Z"), tag.getInteger("D"));
    }

    public DimensionCoordinates(DimensionCoordinates coord)
    {
        super(coord.posX, coord.posY, coord.posZ);
        dimension = coord.dimension;
    }

    public Block getBlock()
    {
        WorldServer world = getWorld();

        if (!world.getChunkProvider().chunkExists(posX >> 4, posY >> 4))
        {
            world.getChunkProvider().loadChunk(posX >> 4, posY >> 4);
        }

        return world.getBlock(posX, posY, posZ);
    }

    public int getMetadata()
    {
        WorldServer world = getWorld();

        if (!world.getChunkProvider().chunkExists(posX >> 4, posY >> 4))
        {
            world.getChunkProvider().loadChunk(posX >> 4, posY >> 4);
        }

        return world.getBlockMetadata(posX, posY, posZ);
    }

    public TileEntity getTileEntity()
    {
        WorldServer world = getWorld();
        
        if (world == null)
        {
            DimensionManager.initDimension(dimension);
            world = DimensionManager.getWorld(dimension);

            if (world == null)
            {
                return null; // How?
            }
        }

        if (!world.getChunkProvider().chunkExists(posX >> 4, posY >> 4))
        {
            world.getChunkProvider().loadChunk(posX >> 4, posY >> 4);
        }

        return world.getTileEntity(posX, posY, posZ);
    }

    public WorldServer getWorld()
    {
        WorldServer world = DimensionManager.getWorld(dimension);

        if (world == null)
        {
            DimensionManager.initDimension(dimension);
            world = DimensionManager.getWorld(dimension);

            if (world == null)
            {
                return null; // How?
            }
        }

        return world;
    }

    public DimensionCoordinates offset(ForgeDirection orientation)
    {
        return new DimensionCoordinates(posX + orientation.offsetX, posY + orientation.offsetY, posZ + orientation.offsetZ, dimension);
    }

    @Override
    public String toString()
    {
        return String.format("WorldCoordinates (X %s, Y %s, Z %s, D %s)", posX, posY, posZ, dimension);
    }

    public void writeToNBT(NBTTagCompound t)
    {
        t.setInteger("X", posX);
        t.setInteger("Y", posY);
        t.setInteger("Z", posZ);
        t.setInteger("D", dimension);
    }
}
