package alz.mods.enhancedportals.teleportation;

import net.minecraft.server.MinecraftServer;
import alz.mods.enhancedportals.common.WorldLocation;
import alz.mods.enhancedportals.helpers.EntityHelper;
import alz.mods.enhancedportals.helpers.WorldHelper;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;

public class TeleportData extends WorldLocation
{
    private boolean linksToModifier;
    private int dimension;

    private int[] blockOffsetLocation;
    private double[] entityOffsetLocation;

    public TeleportData(int x, int y, int z, int dimension)
    {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.dimension = dimension;
        linksToModifier = false;
    }

    public TeleportData(int x, int y, int z, int dimension, boolean modifier)
    {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.dimension = dimension;
        linksToModifier = modifier;
    }

    public TeleportData(TileEntityPortalModifier modifier)
    {
        this.xCoord = modifier.xCoord;
        this.yCoord = modifier.yCoord;
        this.zCoord = modifier.zCoord;
        this.dimension = modifier.worldObj.provider.dimensionId;
        linksToModifier = true;
    }

    public boolean equals(TeleportData data)
    {
        if (data.getX() == getX() && data.getY() == getY() && data.getZ() == getZ() && data.getDimension() == getDimension() && data.linksToModifier == data.linksToModifier)
        {
            return true;
        }

        return false;
    }

    public int getDimension()
    {
        return dimension;
    }

    public String getDimensionAsString()
    {
        switch (dimension)
        {
            case -1:
                return "The Nether";
            case 0:
                return "Overworld";
            case 1:
                return "The End";
            default:
                return "Unknown";
        }
    }

    public int getX()
    {
        return xCoord;
    }

    public int getXOffsetBlock()
    {
        setupBlockOffset();

        return blockOffsetLocation[0];
    }

    public double getXOffsetEntity()
    {
        setupEntityOffset();

        return entityOffsetLocation[0];
    }

    public int getY()
    {
        return yCoord;
    }

    public int getYOffsetBlock()
    {
        setupBlockOffset();

        return blockOffsetLocation[1];
    }

    public double getYOffsetEntity()
    {
        setupEntityOffset();

        return entityOffsetLocation[1];
    }

    public int getZ()
    {
        return zCoord;
    }

    public int getZOffsetBlock()
    {
        setupBlockOffset();

        return blockOffsetLocation[2];
    }

    public double getZOffsetEntity()
    {
        setupEntityOffset();

        return entityOffsetLocation[2];
    }

    public boolean linksToModifier()
    {
        return linksToModifier;
    }

    private void setupBlockOffset()
    {
        if (blockOffsetLocation == null)
        {
            blockOffsetLocation = WorldHelper.offsetDirectionBased(MinecraftServer.getServer().worldServerForDimension(dimension), getX(), getY(), getZ());
        }
    }

    private void setupEntityOffset()
    {
        if (entityOffsetLocation == null)
        {
            entityOffsetLocation = EntityHelper.offsetDirectionBased(MinecraftServer.getServer().worldServerForDimension(dimension), getX(), getY(), getZ());
        }
    }
}
