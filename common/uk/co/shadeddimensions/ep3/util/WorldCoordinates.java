package uk.co.shadeddimensions.ep3.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;

public class WorldCoordinates extends ChunkCoordinates
{
    public int dimension;

    public WorldCoordinates()
    {

    }

    public WorldCoordinates(ChunkCoordinates chunkCoordinates, int dim)
    {
        super(chunkCoordinates);
        dimension = dim;
    }

    public WorldCoordinates(int x, int y, int z, int dim)
    {
        super(x, y, z);
        dimension = dim;
    }

    public WorldCoordinates(WorldCoordinates coord)
    {
        super(coord.posX, coord.posY, coord.posZ);
        dimension = coord.dimension;
    }
    
    public WorldCoordinates(NBTTagCompound tag)
    {
        this(tag.getInteger("X"), tag.getInteger("Y"), tag.getInteger("Z"), tag.getInteger("D"));
    }
    
    public int getBlockId()
    {        
        WorldServer world = getWorld();
        
        if (!world.getChunkProvider().chunkExists(posX >> 4, posY >> 4))
        {
            world.getChunkProvider().loadChunk(posX >> 4, posY >> 4);
        }
        
        return world.getBlockId(posX, posY, posZ);
    }
    
    public int getBlockMetadata()
    {        
        WorldServer world = getWorld();
        
        if (!world.getChunkProvider().chunkExists(posX >> 4, posY >> 4))
        {
            world.getChunkProvider().loadChunk(posX >> 4, posY >> 4);
        }
        
        return world.getBlockMetadata(posX, posY, posZ);
    }
    
    public TileEntity getBlockTileEntity()
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
        
        return world.getBlockTileEntity(posX, posY, posZ);
    }
        
    public WorldCoordinates offset(ForgeDirection orientation)
    {
        return new WorldCoordinates(posX + orientation.offsetX, posY + orientation.offsetY, posZ + orientation.offsetZ, dimension);
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
