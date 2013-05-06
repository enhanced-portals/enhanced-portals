package enhancedportals.lib;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class WorldLocationBA
{
    public int xCoord, yCoord, zCoord;
    public IBlockAccess blockAccess;
    
    public static boolean equals(WorldLocationBA location, WorldLocationBA location2)
    {
        return location2.xCoord == location.xCoord && location2.yCoord == location.yCoord && location2.zCoord == location.zCoord && location2.blockAccess == location.blockAccess;
    }
    
    public boolean equals(WorldLocationBA loc)
    {
        return equals(this, loc);
    }
    
    public WorldLocationBA(int x, int y, int z, IBlockAccess blockaccess)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        blockAccess = blockaccess;
    }
        
    public int getBlockId()
    {
        return blockAccess.getBlockId(xCoord, yCoord, zCoord);
    }
    
    public int getMetadata()
    {
        return blockAccess.getBlockMetadata(xCoord, yCoord, zCoord);
    }
    
    public TileEntity getTileEntity()
    {
        return blockAccess.getBlockTileEntity(xCoord, yCoord, zCoord);
    }
    
    public Material getMaterial()
    {
        return blockAccess.getBlockMaterial(xCoord, yCoord, zCoord);
    }

    public WorldLocationBA getOffset(ForgeDirection direction)
    {
        return new WorldLocationBA(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, blockAccess);
    }
}
