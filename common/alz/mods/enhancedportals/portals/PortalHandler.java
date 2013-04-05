package alz.mods.enhancedportals.portals;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import alz.mods.enhancedportals.common.WorldLocation;
import alz.mods.enhancedportals.helpers.PortalHelper.PortalShape;
import alz.mods.enhancedportals.helpers.WorldHelper;
import alz.mods.enhancedportals.reference.Localizations;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;
import alz.mods.enhancedportals.tileentity.TileEntityNetherPortal;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;

public class PortalHandler
{
	public static boolean isBlockRemovable(int ID)
	{
		return Reference.Settings.RemovableBlocks.contains(ID);
	}

	public static boolean isBlockFrame(int ID, boolean includeSelf)
	{
		if (includeSelf && ID == Reference.BlockIDs.NetherPortal)
			return true;

		return Reference.Settings.BorderBlocks.contains(ID);
	}
	
	private static Queue<WorldLocation> updateQueue(Queue<WorldLocation> queue, WorldLocation location, PortalShape shape)
	{
		if (shape == PortalShape.X)
		{
			queue.add(new WorldLocation(location.xCoord, location.yCoord - 1, location.zCoord));
			queue.add(new WorldLocation(location.xCoord, location.yCoord + 1, location.zCoord));
			queue.add(new WorldLocation(location.xCoord - 1, location.yCoord, location.zCoord));
			queue.add(new WorldLocation(location.xCoord + 1, location.yCoord, location.zCoord));
		}
		else if (shape == PortalShape.Z)
		{
			queue.add(new WorldLocation(location.xCoord, location.yCoord - 1, location.zCoord));
			queue.add(new WorldLocation(location.xCoord, location.yCoord + 1, location.zCoord));
			queue.add(new WorldLocation(location.xCoord, location.yCoord, location.zCoord - 1));
			queue.add(new WorldLocation(location.xCoord, location.yCoord, location.zCoord + 1));
		}
		else if (shape == PortalShape.HORIZONTAL)
		{
			queue.add(new WorldLocation(location.xCoord - 1, location.yCoord, location.zCoord));
			queue.add(new WorldLocation(location.xCoord + 1, location.yCoord, location.zCoord));
			queue.add(new WorldLocation(location.xCoord, location.yCoord, location.zCoord - 1));
			queue.add(new WorldLocation(location.xCoord, location.yCoord, location.zCoord + 1));
		}
		else if (shape == PortalShape.INVALID) // used for deconstructing portals
		{
			queue.add(new WorldLocation(location.xCoord, location.yCoord - 1, location.zCoord));
			queue.add(new WorldLocation(location.xCoord, location.yCoord + 1, location.zCoord));
			queue.add(new WorldLocation(location.xCoord - 1, location.yCoord, location.zCoord));
			queue.add(new WorldLocation(location.xCoord + 1, location.yCoord, location.zCoord));
			queue.add(new WorldLocation(location.xCoord, location.yCoord, location.zCoord - 1));
			queue.add(new WorldLocation(location.xCoord, location.yCoord, location.zCoord + 1));
		}
		
		return queue;
	}
	
	public static class Create
	{
		static final int MAX_CHANCES = 10;
		
		/***
		 * Creates a portal at the specified location
		 * @param location The WorldLocation to create the portal at
		 * @param shape The shape of the portal
		 * @param texture The texture of the portal
		 * @return
		 */
		public static boolean createPortal(WorldLocation location, PortalShape shape, PortalTexture texture)
		{
			if (shape == PortalShape.INVALID || location.worldObj.isRemote || !isBlockRemovable(location.getBlockId()))
			{
				return false;
			}

			if (texture == PortalTexture.UNKNOWN)
			{
				texture = PortalTexture.PURPLE;
			}
			
			World world = location.worldObj;
			Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
			Queue<WorldLocation> addedBlocks = new LinkedList<WorldLocation>();
			int usedChances = 0;
			
			queue.add(location);
			
			while (!queue.isEmpty())
			{
				WorldLocation current = queue.remove();
				int currentBlockID = world.getBlockId(current.xCoord, current.yCoord, current.zCoord);
				
				if (Reference.Settings.MaximumPortalSize > 0 && addedBlocks.size() >= Reference.Settings.MaximumPortalSize)
				{
					Remove.removePortal(addedBlocks);
					Reference.LogData(String.format(Localizations.getLocalizedString(Strings.Console_sizeFail), location.xCoord, location.yCoord, location.zCoord), world.isRemote);
					return false;
				}
				
				if (isBlockRemovable(currentBlockID))
				{
					int sides = getSides(current, shape);
					
					if (sides == -1)
					{
						Remove.removePortal(addedBlocks);
						return false;
					}
					else if (sides < 2 && usedChances < MAX_CHANCES)
					{
						usedChances++;
						sides += 2;
					}
					
					if (sides >= 2)
					{
						addedBlocks.add(new WorldLocation(world, current.xCoord, current.yCoord, current.zCoord));
						world.setBlock(current.xCoord, current.yCoord, current.zCoord, Reference.BlockIDs.NetherPortal);
						Data.updateTexture(current, texture, PortalTexture.UNKNOWN);
						queue = updateQueue(queue, current, shape);
					}
				}
			}
			
			if (validatePortal(location, shape))
			{
				Reference.LogData(String.format(Localizations.getLocalizedString(Strings.Console_success), addedBlocks.size(), location.xCoord, location.yCoord, location.zCoord), world.isRemote);

				return true;
			}
			else
			{
				Reference.LogData(String.format(Localizations.getLocalizedString(Strings.Console_fail), addedBlocks.size(), location.xCoord, location.yCoord, location.zCoord), world.isRemote);

				Remove.removePortal(addedBlocks);
				return false;
			}
		}
		
		/***
		 * Creates a portal at the specified location
		 * @param location The WorldLocation to create the portal at
		 * @param shape The shape of the portal
		 * @return
		 */
		public static boolean createPortal(WorldLocation location, PortalShape shape)
		{
			return createPortal(location, shape, PortalTexture.PURPLE);
		}
		
		/***
		 * Creates a portal at the specified location
		 * @param location The WorldLocation to create the portal at
		 * @param texture The texture of the portal blocks
		 * @return
		 */
		public static boolean createPortal(WorldLocation location, PortalTexture texture)
		{
			if (createPortal(location, PortalShape.X, texture))
			{
				return true;
			}
			else if (createPortal(location, PortalShape.Z, texture))
			{
				return true;
			}
			else if (createPortal(location, PortalShape.HORIZONTAL, texture))
			{
				return true;
			}			
			
			return false;
		}
		
		/***
		 * Creates a portal at the specified location
		 * @param location The WorldLocation to create the portal at
		 * @return
		 */
		public static boolean createPortal(WorldLocation location)
		{
			return createPortal(location, PortalTexture.PURPLE);
		}
		
		/***
		 * Attempts to create a portal on all six sides of the block
		 * @param location The WorldLocation of the block itself.
		 * @param texture The texture to apply to the portal.
		 * @return
		 */
		public static boolean createPortalAroundBlock(WorldLocation location, PortalTexture texture)
		{
			location.xCoord -= 1;
			
			// Attempt to create a portal at x - 1
			if (createPortal(location, texture))
			{
				return true;
			}
			
			location.xCoord += 2;
			
			// Attempt to create a portal at x + 1
			if (createPortal(location, texture))
			{
				return true;
			}
			
			location.xCoord -= 1;
			location.zCoord -= 1;
			
			// Attempt to create a portal at z - 1
			if (createPortal(location, texture))
			{
				return true;
			}
			
			location.zCoord += 2;
			
			// Attempt to create a portal at z + 1
			if (createPortal(location, texture))
			{
				return true;
			}
			
			location.zCoord -=1;
			location.yCoord -= 1;
			
			// Attempt to create a portal at y - 1
			if (createPortal(location, texture))
			{
				return true;
			}
			
			location.yCoord += 2;
			
			// Attempt to create a portal at y + 1
			if (createPortal(location, texture))
			{
				return true;
			}
			
			return false;
		}
		
		/***
		 * Attempts to create a portal on all six sides of the block
		 * @param location The WorldLocation of the block itself.
		 * @return
		 */
		public static boolean createPortalAroundBlock(WorldLocation location)
		{
			return createPortalAroundBlock(location, PortalTexture.PURPLE);
		}
		
		/***
		 * Creates a portal on the active face of the Portal Modifier.
		 * @param modifier The Portal Modifier to create a portal on.
		 * @return
		 */
		public static boolean createPortalFromModifier(TileEntityPortalModifier modifier)
		{
			int[] offset = WorldHelper.offsetDirectionBased(modifier.worldObj, modifier.xCoord, modifier.yCoord, modifier.zCoord);
			
			return createPortal(new WorldLocation(modifier.worldObj, offset[0], offset[1], offset[2]), modifier.PortalData.Texture);
		}
		
		private static int getSides(WorldLocation location, PortalShape shape)
		{
			int totalSides = 0;
			
			int[] allBlocks = new int[4];

			// Add the sides for horizontal portals		
			if (shape == PortalShape.HORIZONTAL)
			{
				allBlocks[0] = location.worldObj.getBlockId(location.xCoord - 1, location.yCoord, location.zCoord);
				allBlocks[1] = location.worldObj.getBlockId(location.xCoord + 1, location.yCoord, location.zCoord);
				allBlocks[2] = location.worldObj.getBlockId(location.xCoord, location.yCoord, location.zCoord + 1);
				allBlocks[3] = location.worldObj.getBlockId(location.xCoord, location.yCoord, location.zCoord - 1);
			}
			else if (shape == PortalShape.X) // For the X-Axis portals
			{
				allBlocks[0] = location.worldObj.getBlockId(location.xCoord, location.yCoord + 1, location.zCoord);
				allBlocks[1] = location.worldObj.getBlockId(location.xCoord, location.yCoord - 1, location.zCoord);
				allBlocks[2] = location.worldObj.getBlockId(location.xCoord - 1, location.yCoord, location.zCoord);
				allBlocks[3] = location.worldObj.getBlockId(location.xCoord + 1, location.yCoord, location.zCoord);
			}
			else if (shape == PortalShape.Z) // For the Z-Axis portals
			{
				allBlocks[0] = location.worldObj.getBlockId(location.xCoord, location.yCoord + 1, location.zCoord);
				allBlocks[1] = location.worldObj.getBlockId(location.xCoord, location.yCoord - 1, location.zCoord);
				allBlocks[2] = location.worldObj.getBlockId(location.xCoord, location.yCoord, location.zCoord + 1);
				allBlocks[3] = location.worldObj.getBlockId(location.xCoord, location.yCoord, location.zCoord - 1);
			}
			
			for (int val : allBlocks)
			{
				if (isBlockFrame(val, true))
				{
					totalSides++;
				}
				else if (isBlockRemovable(val))
				{
					return -1;
				}
			}
			
			return totalSides;
		}
		
		private static boolean validatePortal(WorldLocation location, PortalShape shape)
		{
			if (location.worldObj.isRemote)
			{
				return true;
			}
			
			Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
			Queue<WorldLocation> checkedQueue = new LinkedList<WorldLocation>();
			queue.add(location);
			
			while (!queue.isEmpty())
			{
				WorldLocation current = queue.remove();
				int currentBlockID = location.getBlockId();
				
				if (currentBlockID == Reference.BlockIDs.NetherPortal && !checkedQueue.contains(current))
				{				
					int sides = getSides(current, shape);
					
					if (sides != 4)
					{
						return false;
					}
					
					queue = updateQueue(queue, current, shape);
					checkedQueue.add(current);
				}
			}
			
			return true;
		}
	}
	
	public static class Remove
	{
		/***
		 * Removes a portal at the specified location
		 * @param location The location of the portal
		 * @return
		 */
		public static void removePortal(WorldLocation location, PortalShape shape)
		{
			if (location.getBlockId() != Reference.BlockIDs.NetherPortal)
			{
				return;
			}
			
			Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
			queue.add(location);
			
			while (!queue.isEmpty())
			{
				WorldLocation current = queue.remove();
				int currentBlockID = current.getBlockId();
				
				if (currentBlockID == Reference.BlockIDs.NetherPortal)
				{
					current.setBlock(0);
					queue = updateQueue(queue, current, shape);
				}
			}
		}
		
		/***
		 * Removes a portal at the specified location
		 * @param location The location of the portal
		 * @return
		 */
		public static void removePortal(WorldLocation location)
		{
			removePortal(location, PortalShape.INVALID);
		}
		
		/***
		 * Removes a portal at the specified location
		 * @param location The location of the portal
		 * @return
		 */
		public static void removePortal(Queue<WorldLocation> addedBlocks)
		{
			while (!addedBlocks.isEmpty())
			{
				WorldLocation current = addedBlocks.remove();
				current.setBlock(0);
			}
		}
		
		/***
		 * Removes a portal from a portal modifier
		 * @param modifier The portal modifier to remove the attached portal from
		 * @return
		 */
		public static void removePortalModifier(TileEntityPortalModifier modifier)
		{
			int[] offset = WorldHelper.offsetDirectionBased(modifier.worldObj, modifier.xCoord, modifier.yCoord, modifier.zCoord);
			
			removePortal(new WorldLocation(modifier.worldObj, offset[0], offset[1], offset[2]));
		}
	}
	
	public static class Data
	{
		/***
		 * Updates all the portal block's textures to the new one
		 * @param location The location of the portal
		 * @param newTexture The new texture to add
		 * @param oldTexture The texture to replace, leave null/PortalTexture.UNKNOWN for all.
		 * @param updateModifiers Should Portal Modifiers be updated too? (Only if oldTexture is set, will only update it if the modifier's texture equals oldTexture)
		 * @return
		 */
		public static boolean floodUpdateTexture(WorldLocation location, PortalTexture newTexture, PortalTexture oldTexture, boolean updateModifiers)
		{
			if (location.getBlockId() != Reference.BlockIDs.NetherPortal)
			{
				return false;
			}
			
			Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
			queue.add(location);
			
			while (!queue.isEmpty())
			{
				WorldLocation current = queue.remove();
				int currentBlockID = current.getBlockId();
				PortalTexture texture = ((TileEntityNetherPortal)current.getBlockTileEntity()).Texture;
				
				if (currentBlockID == Reference.BlockIDs.NetherPortal && texture != newTexture)
				{
					updateTexture(current, newTexture, oldTexture, false);
					queue = updateQueue(queue, current, PortalShape.INVALID);
				}
				else if (updateModifiers && currentBlockID == Reference.BlockIDs.PortalModifier)
				{
					TileEntityPortalModifier modifier = (TileEntityPortalModifier) current.getBlockTileEntity();
					
					if (modifier.PortalData.Texture == oldTexture) // This should only affect modifiers if they're the same texture as what's being replaced
					{
						modifier.PortalData.Texture = newTexture;
						
						if (current.worldObj.isRemote)
						{
							current.markBlockForUpdate();
						}
						else
						{
							Reference.LinkData.sendUpdatePacketToNearbyClients(modifier);
						}
					}
				}
			}
			
			// TODO send client floodfill packet from main location
			
			return true;
		}
		
		public static boolean updateTexture(WorldLocation location, PortalTexture newTexture, PortalTexture oldTexture, boolean sendPacket)
		{
			if (location.getBlockId() != Reference.BlockIDs.NetherPortal)
			{
				return false;
			}
			
			TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) location.getBlockTileEntity();
			
			if ((oldTexture != null && oldTexture != PortalTexture.UNKNOWN) && netherPortal.Texture != oldTexture)
			{
				return false;
			}
			
			netherPortal.Texture = newTexture;
			
			if (!location.worldObj.isRemote && sendPacket)
			{
				// TODO send packet
			}
			else if (location.worldObj.isRemote)
			{
				location.markBlockForUpdate();
			}
			
			return true;
		}
		
		public static boolean updateTexture(WorldLocation location, PortalTexture newTexture, PortalTexture oldTexture)
		{
			return updateTexture(location, newTexture, oldTexture, true);
		}
		
		/***
		 * Updates the portals texture from a modifier
		 * @param modifier The modifier to update
		 * @param newTexture The new texture to apply
		 * @param updateSelf Should the portal modifier update itself?
		 * @return
		 */
		public static boolean floodUpdateTextureModifier(TileEntityPortalModifier modifier, PortalTexture newTexture, boolean updateSelf)
		{
			int[] offset = WorldHelper.offsetDirectionBased(modifier.worldObj, modifier.xCoord, modifier.yCoord, modifier.zCoord);
			
			if (updateSelf)
			{
				modifier.PortalData.Texture = newTexture;
			}
			
			return floodUpdateTexture(new WorldLocation(modifier.worldObj, offset[0], offset[1], offset[2]), newTexture, modifier.PortalData.Texture, false);
		}
	}
	
	public static class Shape
	{
		/***
		 * Gets a portal's shape
		 * @param world
		 * @param x
		 * @param y
		 * @param z
		 * @return
		 */
		public static PortalShape getPortalShape(IBlockAccess world, int x, int y, int z)
		{
			if (isBlockFrame(world.getBlockId(x - 1, y, z), true) && isBlockFrame(world.getBlockId(x + 1, y, z), true)
					&& isBlockFrame(world.getBlockId(x, y + 1, z), true) && isBlockFrame(world.getBlockId(x, y - 1, z), true))
			{
				return PortalShape.X;
			}
			else if (isBlockFrame(world.getBlockId(x, y, z - 1), true) && isBlockFrame(world.getBlockId(x, y, z + 1), true)
					&& isBlockFrame(world.getBlockId(x, y + 1, z), true) && isBlockFrame(world.getBlockId(x, y - 1, z), true))
			{
				return PortalShape.Z;
			}
			else if (isBlockFrame(world.getBlockId(x - 1, y, z), true) && isBlockFrame(world.getBlockId(x + 1, y, z), true)
					&& isBlockFrame(world.getBlockId(x, y, z + 1), true) && isBlockFrame(world.getBlockId(x, y, z - 1), true))
			{
				return PortalShape.HORIZONTAL;
			}

			return PortalShape.INVALID;
		}

		/***
		 * Gets a portal's shape
		 * @param world
		 * @param x
		 * @param y
		 * @param z
		 * @return
		 */
		public static PortalShape getPortalShape(World world, int x, int y, int z)
		{
			if (isBlockFrame(world.getBlockId(x - 1, y, z), true) && isBlockFrame(world.getBlockId(x + 1, y, z), true)
					&& isBlockFrame(world.getBlockId(x, y + 1, z), true) && isBlockFrame(world.getBlockId(x, y - 1, z), true))
			{
				return PortalShape.X;
			}
			else if (isBlockFrame(world.getBlockId(x, y, z - 1), true) && isBlockFrame(world.getBlockId(x, y, z + 1), true)
					&& isBlockFrame(world.getBlockId(x, y + 1, z), true) && isBlockFrame(world.getBlockId(x, y - 1, z), true))
			{
				return PortalShape.Z;
			}
			else if (isBlockFrame(world.getBlockId(x - 1, y, z), true) && isBlockFrame(world.getBlockId(x + 1, y, z), true)
					&& isBlockFrame(world.getBlockId(x, y, z + 1), true) && isBlockFrame(world.getBlockId(x, y, z - 1), true))
			{
				return PortalShape.HORIZONTAL;
			}

			return PortalShape.INVALID;
		}
	}
}
