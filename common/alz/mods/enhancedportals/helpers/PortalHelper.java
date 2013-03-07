package alz.mods.enhancedportals.helpers;

import java.util.LinkedList;
import java.util.Queue;

import alz.mods.enhancedportals.common.EnhancedPortals;
import alz.mods.enhancedportals.reference.Logger;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PortalHelper
{
	static int portalBlock;
	
	public static enum PortalShape
	{
		X, Z, HORIZONTAL, INVALID
	}
	
	public static PortalShape getPortalShape(IBlockAccess world, int x, int y, int z)
	{
		if (WorldHelper.isBlockPortalFrame(world.getBlockId(x - 1, y, z), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x + 1, y, z), true) &&
				WorldHelper.isBlockPortalFrame(world.getBlockId(x, y + 1, z), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x, y - 1, z), true))
		{
			return PortalShape.X;
		}
		else if (WorldHelper.isBlockPortalFrame(world.getBlockId(x, y, z - 1), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x, y, z + 1), true) &&
				WorldHelper.isBlockPortalFrame(world.getBlockId(x, y + 1, z), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x, y - 1, z), true))
		{
			return PortalShape.Z;
		}
		else if (WorldHelper.isBlockPortalFrame(world.getBlockId(x - 1, y, z), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x + 1, y, z), true) &&
				WorldHelper.isBlockPortalFrame(world.getBlockId(x, y, z + 1), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x, y, z - 1), true))
		{
			return PortalShape.HORIZONTAL;
		}
		
		return PortalShape.INVALID;
	}
	
	public static PortalShape getPortalShape(World world, int x, int y, int z)
	{
		if (WorldHelper.isBlockPortalFrame(world.getBlockId(x - 1, y, z), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x + 1, y, z), true) &&
				WorldHelper.isBlockPortalFrame(world.getBlockId(x, y + 1, z), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x, y - 1, z), true))
		{
			return PortalShape.X;
		}
		else if (WorldHelper.isBlockPortalFrame(world.getBlockId(x, y, z - 1), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x, y, z + 1), true) &&
				WorldHelper.isBlockPortalFrame(world.getBlockId(x, y + 1, z), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x, y - 1, z), true))
		{
			return PortalShape.Z;
		}
		else if (WorldHelper.isBlockPortalFrame(world.getBlockId(x - 1, y, z), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x + 1, y, z), true) &&
				WorldHelper.isBlockPortalFrame(world.getBlockId(x, y, z + 1), true) && WorldHelper.isBlockPortalFrame(world.getBlockId(x, y, z - 1), true))
		{
			return PortalShape.HORIZONTAL;
		}
		
		return PortalShape.INVALID;
	}
	
	public static boolean createPortalAround(World world, int x, int y, int z)
	{
		return createPortalAround(world, x, y, z, 0, null);
	}
	
	public static boolean createPortalAround(World world, int x, int y, int z, int meta)
	{
		return createPortalAround(world, x, y, z, meta, null);
	}
	
	public static boolean createPortalAround(World world, int x, int y, int z, EntityPlayer player)
	{
		return createPortalAround(world, x, y, z, 0, player);
	}
	
	public static boolean createPortalAround(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
		boolean createdPortal = false;
		
		if (player != null)
		{
			if (player.inventory.mainInventory[player.inventory.currentItem] == null)
				return false;
			else if (player.inventory.mainInventory[player.inventory.currentItem].itemID != Item.flintAndSteel.itemID)
				return false;
		}
		
		if (createPortal(world, x, y + 1, z, meta))
			createdPortal = true;
		
		if (createPortal(world, x, y - 1, z, meta))
			createdPortal = true;
		
		if (createPortal(world, x + 1, y, z, meta))
			createdPortal = true;
		
		if (createPortal(world, x - 1, y, z, meta))
			createdPortal = true;
		
		if (createPortal(world, x, y, z + 1, meta))
			createdPortal = true;
		
		if (createPortal(world, x, y, z - 1, meta))
			createdPortal = true;
		
		if (createdPortal && player != null)
			player.inventory.mainInventory[player.inventory.currentItem].damageItem(1, player);
		
		return createdPortal;
	}
	
	public static boolean createPortal(World world, int x, int y, int z, int meta)
	{
		if (!WorldHelper.isBlockPortalRemovable(world.getBlockId(x, y, z)))
			return false;
		
		if (!createPortal(world, x, y, z, meta, PortalShape.X))
			if (!createPortal(world, x, y, z, meta, PortalShape.Z))
				if (!createPortal(world, x, y, z, meta, PortalShape.HORIZONTAL))
					return false;
		
		return true;
	}
	
	public static boolean createPortal(World world, int x, int y, int z, int meta, PortalShape shape)
	{
		if (shape == PortalShape.INVALID || !WorldHelper.isBlockPortalRemovable(world.getBlockId(x, y, z)))
			return false;
		
		Queue<int[]> queue = new LinkedList<int[]>();
		Queue<int[]> addedBlocks = new LinkedList<int[]>();
		int usedChances = 0, maxChances = 10;
		
		queue.add(new int[] { x, y, z });
		
		while (!queue.isEmpty())
		{
			int[] current = (int[])queue.remove();
			int currentID = world.getBlockId(current[0], current[1], current[2]);
			
			if (WorldHelper.isBlockPortalRemovable(currentID))
			{
				int sides = getSides(world, current[0], current[1], current[2], shape);
				
				if (sides == -1)
				{
					removePortal(world, addedBlocks);
					//Reference.LogData(String.format("Failed to create a portal at %s, %s, %s, shape code: %s. Unexpected block found.", x, y, z, shape), world.isRemote);
					return false;
				}
				
				if ((sides < 2) && (usedChances < maxChances))
				{
					usedChances++;
					sides += 2;
				}
				
				if (sides >= 2)
				{
					addedBlocks.add(new int[] { current[0], current[1], current[2], currentID, world.getBlockMetadata(current[0], current[1], current[2]) });
					world.setBlockAndMetadata(current[0], current[1], current[2], portalBlock, meta);
				
					queue = updateQueue(queue, shape, current[0], current[1], current[2]);
				}
			}
		}
		
		if (validatePortal(world, x, y, z, shape))
		{
			Logger.LogData(String.format("Successfully created a portal of %s blocks at %s, %s, %s.", addedBlocks.size(), x, y, z), world.isRemote);
						
			return true;
		}
		else
		{
			Logger.LogData(String.format("Failed to create a portal of %s blocks at %s, %s, %s. (Failed validation)", addedBlocks.size(), x, y, z), world.isRemote);

			removePortal(world, addedBlocks);
			return false;
		}
	}
	
	private static Queue<int[]> updateQueue(Queue<int[]> queue, PortalShape shape, int x, int y, int z)
	{
		if (shape == PortalShape.X)
		{
			queue.add(new int[] { x, y - 1, z });
            queue.add(new int[] { x, y + 1, z });
            queue.add(new int[] { x - 1, y, z });
            queue.add(new int[] { x + 1, y, z });
		}
		else if (shape == PortalShape.Z)
		{
			queue.add(new int[] { x, y - 1, z });
            queue.add(new int[] { x, y + 1, z });
            queue.add(new int[] { x, y, z - 1 });
            queue.add(new int[] { x, y, z + 1 });
		}
		else if (shape == PortalShape.HORIZONTAL)
		{
			queue.add(new int[] { x - 1, y, z });
            queue.add(new int[] { x + 1, y, z });
			queue.add(new int[] { x, y, z - 1 });
            queue.add(new int[] { x, y, z + 1 });
		}
		else if (shape == PortalShape.INVALID) // used for deconstructing portals
		{
			queue.add(new int[] { x, y - 1, z });
            queue.add(new int[] { x, y + 1, z });
			queue.add(new int[] { x - 1, y, z });
            queue.add(new int[] { x + 1, y, z });
			queue.add(new int[] { x, y, z - 1 });
            queue.add(new int[] { x, y, z + 1 });
		}
		
		return queue;
	}
		
	private static int getSides(World world, int x, int y, int z, PortalShape shape)
	{
		int totalSides = 0;
		
		for (int val : getSideBlocks(world, x, y, z, shape))
		{
			if (WorldHelper.isBlockPortalFrame(val, true))
				totalSides++;
			else if (!WorldHelper.isBlockPortalRemovable(val))
				return -1;
		}
		
		return totalSides;
	}
	
	private static int[] getSideBlocks(World world, int x, int y, int z, PortalShape shape)
	{
		int[] allBlocks = new int[4];
		
		// Add the sides for horizontal portals		
		if (shape == PortalShape.HORIZONTAL)
		{
			allBlocks[0] = world.getBlockId(x - 1, y, z);
			allBlocks[1] = world.getBlockId(x + 1, y, z);
			allBlocks[2] = world.getBlockId(x, y, z + 1);
			allBlocks[3] = world.getBlockId(x, y, z - 1);
		}
		else if (shape == PortalShape.X) // For the X-Axis portals
		{
			allBlocks[0] = world.getBlockId(x, y + 1, z);
			allBlocks[1] = world.getBlockId(x, y - 1, z);
			allBlocks[2] = world.getBlockId(x - 1, y, z);
			allBlocks[3] = world.getBlockId(x + 1, y, z);
		}
		else if (shape == PortalShape.Z) // For the Z-Axis portals
		{
			allBlocks[0] = world.getBlockId(x, y + 1, z);
			allBlocks[1] = world.getBlockId(x, y - 1, z);
			allBlocks[2] = world.getBlockId(x, y, z + 1);
			allBlocks[3] = world.getBlockId(x, y, z - 1);
		}
		
		return allBlocks;
	}
	
	private static boolean validatePortal(World world, int x, int y, int z, PortalShape shape)
	{
		Queue<int[]> queue = new LinkedList<int[]>();
		Queue<String> checkedQueue = new LinkedList<String>();
		queue.add(new int[] { x, y, z });
		
		while (!queue.isEmpty())
		{
			int[] current = (int[])queue.remove();			
			int currentID = world.getBlockId(current[0], current[1], current[2]);
			
			if (currentID == portalBlock && !checkedQueue.contains(current[0] + "," + current[1] + "," + current[2]))
			{
				int sides = getSides(world, current[0], current[1], current[2], shape);
				
				if (sides != 4)
					return false;
				
				queue = updateQueue(queue, shape, current[0], current[1], current[2]);
				checkedQueue.add(current[0] + "," + current[1] + "," + current[2]);
			}			
		}
		
		return true;
	}
	
	public static boolean removePortal(World world, int x, int y, int z)
	{
		return removePortal(world, x, y, z, getPortalShape(world, x, y, z));
	}
	
	public static boolean removePortalAround(World world, int x, int y, int z)
	{
		removePortal(world, x, y + 1, z);
		removePortal(world, x, y - 1, z);
		removePortal(world, x + 1, y, z);
		removePortal(world, x - 1, y, z);
		removePortal(world, x, y, z + 1);
		removePortal(world, x, y, z - 1);
				
		return true;
	}
	
	public static boolean removePortal(World world, int x, int y, int z, PortalShape shape)
	{
		Queue<int[]> queue = new LinkedList<int[]>();
		queue.add(new int[] { x, y, z });
		
		while (!queue.isEmpty())
		{
			int[] current = (int[])queue.remove();
			int currentID = world.getBlockId(current[0], current[1], current[2]);
			
			if (currentID == portalBlock)
			{
				world.setBlock(current[0], current[1], current[2], 0);			
				queue = updateQueue(queue, shape, current[0], current[1], current[2]);
			}
		}
		
		return true;
	}
	
	public static boolean removePortal(World world, Queue<int[]> queue)
	{
		while (!queue.isEmpty())
		{
			int[] current = (int[])queue.remove();
			
			if (current.length == 4)
			{
				if (current[3] == Block.fire.blockID || current[3] == portalBlock)
					current[3] = current[4] = 0;
				
				world.setBlockAndMetadata(current[0], current[1], current[2], current[3], current[4]);
			}
			else
			{
				world.setBlock(current[0], current[1], current[2], 0);
			}
		}
		
		return true;
	}
	
	static
	{
		portalBlock = EnhancedPortals.instance.blockNetherPortal.blockID;
	}
}
