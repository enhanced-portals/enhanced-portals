package alz.mods.enhancedportals.helpers;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import alz.mods.enhancedportals.portals.PortalTexture;
import alz.mods.enhancedportals.reference.Localizations;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Reference.BlockIDs;
import alz.mods.enhancedportals.reference.Settings;
import alz.mods.enhancedportals.reference.Strings;
import alz.mods.enhancedportals.teleportation.TeleportData;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;

public class WorldHelper
{
    public static int[] findBestAttachedModifier(World world, int x, int y, int z, int blockSearchingThrough, int blockToFind, int colourToFind)
    {
        Queue<int[]> queue = new LinkedList<int[]>();
        Queue<String> checkedQueue = new LinkedList<String>();
        Queue<int[]> nonIdeal = new LinkedList<int[]>();
        queue.add(new int[] { x, y, z });

        while (!queue.isEmpty())
        {
            int[] current = queue.remove();
            int currentID = world.getBlockId(current[0], current[1], current[2]);

            if (currentID == blockSearchingThrough && !checkedQueue.contains(current[0] + "," + current[1] + "," + current[2]))
            {
                checkedQueue.add(current[0] + "," + current[1] + "," + current[2]);

                queue.add(new int[] { current[0], current[1] - 1, current[2] });
                queue.add(new int[] { current[0], current[1] + 1, current[2] });
                queue.add(new int[] { current[0], current[1], current[2] - 1 });
                queue.add(new int[] { current[0], current[1], current[2] + 1 });
                queue.add(new int[] { current[0] + 1, current[1], current[2] });
                queue.add(new int[] { current[0] - 1, current[1], current[2] });
            }
            else if (currentID == blockToFind && !checkedQueue.contains(current[0] + "," + current[1] + "," + current[2]))
            {
                if (((TileEntityPortalModifier) world.getBlockTileEntity(current[0], current[1], current[2])).getTexture().ordinal() == colourToFind)
                {
                    return current;
                }
                else
                {
                    nonIdeal.add(new int[] { current[0], current[1], current[2] });
                    checkedQueue.add(current[0] + "," + current[1] + "," + current[2]);

                    queue.add(new int[] { current[0], current[1] - 1, current[2] });
                    queue.add(new int[] { current[0], current[1] + 1, current[2] });
                    queue.add(new int[] { current[0], current[1], current[2] - 1 });
                    queue.add(new int[] { current[0], current[1], current[2] + 1 });
                    queue.add(new int[] { current[0] + 1, current[1], current[2] });
                    queue.add(new int[] { current[0] - 1, current[1], current[2] });
                }
            }
        }

        if (nonIdeal.isEmpty())
        {
            return null;
        }

        return nonIdeal.remove();
    }

    public static int[] findFirstAttachedBlock(World world, int x, int y, int z, int blockSearchingThrough, int blockToFind)
    {
        Queue<int[]> queue = new LinkedList<int[]>();
        Queue<String> checkedQueue = new LinkedList<String>();
        queue.add(new int[] { x, y, z });

        while (!queue.isEmpty())
        {
            int[] current = queue.remove();
            int currentID = world.getBlockId(current[0], current[1], current[2]);

            if (currentID == blockSearchingThrough && !checkedQueue.contains(current[0] + "," + current[1] + "," + current[2]))
            {
                int X = current[0];
                int Y = current[1];
                int Z = current[2];

                checkedQueue.add(X + "," + Y + "," + Z);

                queue.add(new int[] { X, Y - 1, Z });
                queue.add(new int[] { X, Y + 1, Z });
                queue.add(new int[] { X, Y, Z - 1 });
                queue.add(new int[] { X, Y, Z + 1 });
                queue.add(new int[] { X + 1, Y, Z });
                queue.add(new int[] { X - 1, Y, Z });
            }
            else if (currentID == blockToFind)
            {
                return current;
            }
        }

        return null;
    }

    public static void floodUpdateMetadata(World world, int x, int y, int z, int blockID, int newMeta)
    {
        floodUpdateMetadata(world, x, y, z, blockID, newMeta, false);
    }

    public static void floodUpdateMetadata(World world, int x, int y, int z, int blockID, int newMeta, boolean affectModifiers)
    {
        Queue<int[]> queue = new LinkedList<int[]>();
        queue.add(new int[] { x, y, z });

        while (!queue.isEmpty())
        {
            int[] current = queue.remove();
            int currentID = world.getBlockId(current[0], current[1], current[2]);

            if (currentID == blockID && world.getBlockMetadata(current[0], current[1], current[2]) != newMeta)
            {
                int X = current[0];
                int Y = current[1];
                int Z = current[2];

                world.setBlock(current[0], current[1], current[2], Reference.BlockIDs.NetherPortal, newMeta, 3);
                world.markBlockForUpdate(X, Y, Z);

                queue.add(new int[] { X, Y - 1, Z });
                queue.add(new int[] { X, Y + 1, Z });
                queue.add(new int[] { X, Y, Z - 1 });
                queue.add(new int[] { X, Y, Z + 1 });
                queue.add(new int[] { X + 1, Y, Z });
                queue.add(new int[] { X - 1, Y, Z });
            }
            if (affectModifiers && currentID == BlockIDs.PortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(current[0], current[1], current[2]);

                if (modifier.getTexture().ordinal() != newMeta)
                {
                    modifier.setTexture(PortalTexture.getPortalTexture(newMeta));

                    if (!world.isRemote)
                    {
                        Reference.ServerHandler.sendUpdatePacketToNearbyClients(modifier);
                    }
                    else
                    {
                        world.markBlockForRenderUpdate(current[0], current[1], current[2]);
                    }
                }
            }
        }
    }

    public static ForgeDirection getBlockDirection(World world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);

        return ForgeDirection.getOrientation(meta);
    }

    public static World getWorld(int DimensionID)
    {
        WorldServer ret = DimensionManager.getWorld(DimensionID);

        if (ret == null)
        {
            DimensionManager.initDimension(DimensionID);
            ret = DimensionManager.getWorld(DimensionID);
        }

        return ret;
    }

    public static boolean isBlockPortalFrame(int ID, boolean includeSelf)
    {
        if (includeSelf && ID == Reference.BlockIDs.NetherPortal)
        {
            return true;
        }

        return Settings.BorderBlocks.contains(ID);
    }

    public static boolean isBlockPortalRemovable(int ID)
    {
        return Settings.RemovableBlocks.contains(ID);
    }

    public static boolean isDimensionLoaded(int DimensionID)
    {
        for (int i : DimensionManager.getIDs())
        {
            if (i == DimensionID)
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isValidExitPortal(World baseWorld, TeleportData selectedExit, TileEntityPortalModifier baseModifier, Entity entity)
    {
        return isValidExitPortal(baseWorld, selectedExit, baseModifier, entity, false);
    }

    public static boolean isValidExitPortal(World baseWorld, TeleportData selectedExit, TileEntityPortalModifier baseModifier, Entity entity, boolean suppressWarnings)
    {
        boolean hasUpgradesRequired = false;
        World theWorld = baseWorld;

        if (selectedExit.getDimension() == baseWorld.provider.dimensionId)
        {
            hasUpgradesRequired = baseModifier.hasUpgrade(1) || baseModifier.hasUpgrade(2);
        }
        else if (selectedExit.getDimension() == 0 && baseWorld.provider.dimensionId == -1 || selectedExit.getDimension() == -1 && baseWorld.provider.dimensionId == 0)
        {
            hasUpgradesRequired = true;
            theWorld = getWorld(selectedExit.getDimension());
        }
        else
        {
            hasUpgradesRequired = baseModifier.hasUpgrade(2);
            theWorld = getWorld(selectedExit.getDimension());
        }

        theWorld.getChunkProvider().loadChunk(selectedExit.getX() >> 4, selectedExit.getY() >> 4);

        if (!hasUpgradesRequired)
        {
            if (!suppressWarnings)
            {
                Reference.LogData(String.format(Localizations.getLocalizedString(Strings.Console_MissingUpgrade), entity.getEntityName()));
                EntityHelper.sendMessage(entity, Localizations.getLocalizedString(Strings.Portal_MissingUpgrade));
            }

            return false;
        }

        if (theWorld.getBlockId(selectedExit.getXOffsetBlock(), selectedExit.getYOffsetBlock(), selectedExit.getZOffsetBlock()) == Reference.BlockIDs.NetherPortal)
        {
            return true;
        }
        else
        {
            boolean createdPortal = PortalHelper.createPortal(theWorld, selectedExit.getXOffsetBlock(), selectedExit.getYOffsetBlock(), selectedExit.getZOffsetBlock(), 0);

            if (createdPortal)
            {
                return true;
            }
        }

        if (!suppressWarnings)
        {
            Reference.LogData(String.format(Localizations.getLocalizedString(Strings.Console_ExitBlocked), entity.getEntityName()));
            EntityHelper.sendMessage(entity, Localizations.getLocalizedString(Strings.Portal_ExitBlocked));
        }

        return false;
    }

    // Returns true if it had to load the dimension
    public static boolean loadDimension(int DimensionID)
    {
        if (isDimensionLoaded(DimensionID))
        {
            return false;
        }

        DimensionManager.initDimension(DimensionID);
        return true;
    }

    public static int[] offsetDirectionBased(World world, int x, int y, int z)
    {
        return offsetDirectionBased(world, x, y, z, WorldHelper.getBlockDirection(world, x, y, z));
    }

    public static int[] offsetDirectionBased(World world, int x, int y, int z, ForgeDirection direction)
    {
        if (direction == ForgeDirection.UNKNOWN)
        {
            return null;
        }

        if (direction == ForgeDirection.NORTH)
        {
            z -= 1;
        }
        else if (direction == ForgeDirection.SOUTH)
        {
            z += 1;
        }
        else if (direction == ForgeDirection.EAST)
        {
            x += 1;
        }
        else if (direction == ForgeDirection.WEST)
        {
            x -= 1;
        }
        else if (direction == ForgeDirection.UP)
        {
            y += 1;
        }
        else if (direction == ForgeDirection.DOWN)
        {
            y -= 1;
        }

        return new int[] { x, y, z };
    }

    public static void unloadDimension(int DimensionID)
    {
        if (!isDimensionLoaded(DimensionID))
        {
            return;
        }

        DimensionManager.setWorld(DimensionID, null);
    }
}
