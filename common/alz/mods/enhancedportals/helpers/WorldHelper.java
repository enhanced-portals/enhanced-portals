package alz.mods.enhancedportals.helpers;

import java.util.LinkedList;
import java.util.Queue;

import alz.mods.enhancedportals.reference.BlockID;
import alz.mods.enhancedportals.reference.Language;
import alz.mods.enhancedportals.reference.Logger;
import alz.mods.enhancedportals.reference.Settings;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;

public class WorldHelper
{	
	public static boolean isDimensionLoaded(int DimensionID)
	{
		for (int i : DimensionManager.getIDs())
			if (i == DimensionID)
				return true;
		
		return false;
	}
	
	// Returns true if it had to load the dimension
	public static boolean loadDimension(int DimensionID)
	{
		if (isDimensionLoaded(DimensionID))
			return false;
		
		DimensionManager.initDimension(DimensionID);
		return true;
	}
	
	public static void unloadDimension(int DimensionID)
	{
		if (!isDimensionLoaded(DimensionID))
			return;
		
		DimensionManager.setWorld(DimensionID, null);
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
	
	public static int[] findFirstAttachedBlock(World world, int x, int y, int z, int blockSearchingThrough, int blockToFind)
	{
		Queue<int[]> queue = new LinkedList<int[]>();
		Queue<String>checkedQueue = new LinkedList<String>();
	    queue.add(new int[] { x, y, z });
		
		while (!queue.isEmpty())
	    {
	    	int[] current = (int[])queue.remove();
	    	int currentID = world.getBlockId(current[0], current[1], current[2]);

	    	if (currentID == blockSearchingThrough && !checkedQueue.contains(current[0] + "," + current[1] + "," + current[2]))
	    	{
	    		int X = current[0]; int Y = current[1]; int Z = current[2];

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
	
	public static int[] findBestAttachedModifier(World world, int x, int y, int z, int blockSearchingThrough, int blockToFind, int colourToFind)
	{
		Queue<int[]> queue = new LinkedList<int[]>();
		Queue<String> checkedQueue = new LinkedList<String>();
	    queue.add(new int[] { x, y, z });
		
		while (!queue.isEmpty())
	    {
	    	int[] current = (int[])queue.remove();
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
	    	else if (currentID == blockToFind)
	    	{
	    		if (((TileEntityPortalModifier)world.getBlockTileEntity(current[0], current[1], current[2])).Colour == colourToFind)
	    			return current;
	    		else
	    		{
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
		
		return null;
	}
	
	public static void floodUpdateMetadata(World world, int x, int y, int z, int blockID, int newMeta)
	{
		Queue<int[]> queue = new LinkedList<int[]>();
	    queue.add(new int[] { x, y, z });

	    while (!queue.isEmpty())
	    {
	    	int[] current = (int[])queue.remove();
	    	int currentID = world.getBlockId(current[0], current[1], current[2]);

	    	if (currentID == blockID && world.getBlockMetadata(current[0], current[1], current[2]) != newMeta)
	    	{
	    		int X = current[0]; int Y = current[1]; int Z = current[2];

	    		world.setBlockAndMetadataWithNotify(X, Y, Z, BlockID.NetherPortal, newMeta, 3);
	    		world.markBlockForUpdate(X, Y, Z);
	    		
	    		queue.add(new int[] { X, Y - 1, Z });
	    		queue.add(new int[] { X, Y + 1, Z });
	    		queue.add(new int[] { X, Y, Z - 1 });
	    		queue.add(new int[] { X, Y, Z + 1 });
	        	queue.add(new int[] { X + 1, Y, Z });
	        	queue.add(new int[] { X - 1, Y, Z });
	    	}
	    }
	}
	
	public static boolean isBlockPortalRemovable(int ID)
	{
		return Settings.RemovableBlocks.contains(ID);
	}
	
	public static boolean isBlockPortalFrame(int ID, boolean includeSelf)
	{
		if (includeSelf && ID == BlockID.NetherPortal)
			return true;
		
		return Settings.BorderBlocks.contains(ID);
	}
	
	public static ForgeDirection getBlockDirection(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
				
		return ForgeDirection.getOrientation(meta);
	}
	
	public static int[] offsetDirectionBased(World world, int x, int y, int z)
	{
		return offsetDirectionBased(world, x, y, z, WorldHelper.getBlockDirection(world, x, y, z));
	}
	
	public static int[] offsetDirectionBased(World world, int x, int y, int z, ForgeDirection direction)
	{
		if (direction == ForgeDirection.UNKNOWN)
			return null;
		
		if (direction == ForgeDirection.NORTH)
			z -= 1;
		else if (direction == ForgeDirection.SOUTH)
			z += 1;
		else if (direction == ForgeDirection.EAST)
			x += 1;
		else if (direction == ForgeDirection.WEST)
			x -= 1;
		else if (direction == ForgeDirection.UP)
			y += 1;
		else if (direction == ForgeDirection.DOWN)
			y -= 1;
		
		return new int[] { x, y, z };
	}
	
	public static boolean isValidExitPortal(World baseWorld, int[] selectedExit, TileEntityPortalModifier baseModifier, Entity entity)
	{
		boolean hasUpgradesRequired = false;
		World theWorld = baseWorld;
		
		if (selectedExit[3] == baseWorld.provider.dimensionId)
		{
			hasUpgradesRequired = baseModifier.hasUpgrade(1) || baseModifier.hasUpgrade(2);
		}
		else if ((selectedExit[3] == 0 && baseWorld.provider.dimensionId == -1) || (selectedExit[3] == -1 && baseWorld.provider.dimensionId == 0))
		{
			hasUpgradesRequired = true;
			theWorld = getWorld(selectedExit[3]);
		}
		else
		{
			hasUpgradesRequired = baseModifier.hasUpgrade(2);
			theWorld = getWorld(selectedExit[3]);
		}
		
		if (baseWorld.provider.dimensionId == 1 && selectedExit[3] != 1)
		{
			Logger.LogData(String.format("Couldn't teleport entity (%s) - Teleportation from The End to another dimension is currently broken.", entity.getEntityName()));
			return false;
		}
		
		if (!hasUpgradesRequired)
		{
			Logger.LogData(String.format("Couldn't teleport entity (%s) - Modifier does not have the required upgrade.", entity.getEntityName()));
			EntityHelper.sendMessage(entity, Language.NoUpgrade);
			return false;
		}
		
		int[] offset = offsetDirectionBased(theWorld, selectedExit[0], selectedExit[1], selectedExit[2]);
		
		if (theWorld.getBlockId(offset[0], offset[1], offset[2]) == BlockID.NetherPortal)
		{
			return true;
		}
		else
		{
			boolean createdPortal =  PortalHelper.createPortal(theWorld, offset[0], offset[1], offset[2], 0);
			
			if (createdPortal)
				return true;
		}
		
		Logger.LogData(String.format("Couldn't teleport entity (%s) - Exit is blocked.", entity.getEntityName()));
		EntityHelper.sendMessage(entity, Language.ExitBlocked);		
		return false;
	}
}
