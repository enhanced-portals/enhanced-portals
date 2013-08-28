package uk.co.shadeddimensions.enhancedportals.multipart;

import java.util.Iterator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

public class MultipartUtils
{
    public static PortalPart getPortalPart(TileMultipart tile)
    {
        Iterator<TMultiPart> iterator = tile.jPartList().iterator();

        while (iterator.hasNext())
        {
            TMultiPart part = iterator.next();

            if (part.getType().equals(PortalPart.Type))
            {
                return (PortalPart) part;
            }
        }

        return null;
    }

    public static boolean doesMultipartContainPortal(TileMultipart tile)
    {
        return getPortalPart(tile) != null;
    }

    public static boolean doesMultipartContainPortal(World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileMultipart)
        {
            return doesMultipartContainPortal((TileMultipart) tile);
        }

        return false;
    }

    public static boolean removePortalFromPart(World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileMultipart)
        {
            return removePortalFromPart((TileMultipart) tile);
        }

        return false;
    }

    public static boolean removePortalFromPart(TileMultipart tile)
    {
        if (!doesMultipartContainPortal(tile))
        {
            return true;
        }

        PortalPart part = getPortalPart(tile);

        if (part == null)
        {
            return false;
        }
        else
        {
            tile.remPart(part);
            return true;
        }
    }

    public static boolean addPortalToPart(World world, int x, int y, int z, int meta)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileMultipart)
        {
            return addPortalToPart((TileMultipart) tile, meta);
        }

        return false;
    }

    public static boolean addPortalToPart(TileMultipart tile, int meta)
    {
        TileMultipart.addPart(tile.worldObj, new BlockCoord(tile), new PortalPart(meta));

        return false;
    }
}
