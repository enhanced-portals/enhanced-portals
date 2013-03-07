package alz.mods.enhancedportals.helpers;

import java.util.LinkedList;
import java.util.Queue;

import alz.mods.enhancedportals.common.EnhancedPortals;
import alz.mods.enhancedportals.common.TileEntityPortalModifier;
import alz.mods.enhancedportals.reference.Settings;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

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

	    		world.setBlockMetadata(X, Y, Z, newMeta);
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
		if (includeSelf && ID == EnhancedPortals.instance.blockNetherPortal.blockID)
			return true;
		
		return Settings.BorderBlocks.contains(ID);
	}
}
