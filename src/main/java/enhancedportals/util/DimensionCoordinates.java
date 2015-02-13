package enhancedportals.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

public class DimensionCoordinates extends ChunkCoordinates
{
    public int posD;

    public DimensionCoordinates(int x, int y, int z, int d)
    {
        posX = x;
        posY = y;
        posZ = z;
        posD = d;
    }

    public DimensionCoordinates(ChunkCoordinates c, int d)
    {
        this(c.posX, c.posY, c.posZ, d);
    }

    public DimensionCoordinates(NBTTagCompound tagCompound)
    {
        this(tagCompound.getInteger("X"), tagCompound.getInteger("Y"), tagCompound.getInteger("Z"), tagCompound.getInteger("D"));
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof DimensionCoordinates))
        {
            return false;
        }
        else
        {
            DimensionCoordinates dimensioncoordinates = (DimensionCoordinates) obj;
            return this.posX == dimensioncoordinates.posX && this.posY == dimensioncoordinates.posY && this.posZ == dimensioncoordinates.posZ && this.posD == dimensioncoordinates.posD;
        }
    }

    public int hashCode()
    {
        return this.posX + this.posZ << 8 + this.posY << 16 + this.posD << 32;
    }

    public String toString()
    {
        return "Pos{x=" + this.posX + ", y=" + this.posY + ", z=" + this.posZ + ", d=" + this.posD + "}";
    }

    public DimensionCoordinates offset(ForgeDirection dir)
    {
        return new DimensionCoordinates(posX + dir.offsetX, posY + dir.offsetY, posZ + dir.offsetZ, posD);
    }

    public NBTTagCompound save(NBTTagCompound tag)
    {
        tag.setInteger("Dimension", posD);
        tag.setInteger("X", posX);
        tag.setInteger("Y", posY);
        tag.setInteger("Z", posZ);
        return tag;
    }
}
