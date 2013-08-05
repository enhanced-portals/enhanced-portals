package uk.co.shadeddimensions.enhancedportals.lib;

import net.minecraftforge.common.ForgeDirection;

public class Coordinate
{
    public int x, y, z;

    public Coordinate(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Coordinate)
        {
            Coordinate coord = (Coordinate) obj;

            return coord.x == x && coord.y == y && coord.z == z;
        }

        return false;
    }

    public Coordinate offset(ForgeDirection dir)
    {
        return new Coordinate(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
    }

    @Override
    public String toString()
    {
        return "Coordinate (" + x + ", " + y + ", " + z + ")";
    }
}
