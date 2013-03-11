package alz.mods.enhancedportals.helpers;

import alz.mods.enhancedportals.reference.ItemID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class EntityHelper
{
	public static void sendMessage(Entity entity, String message)
	{
		if (entity instanceof EntityPlayer)
			((EntityPlayer)entity).sendChatToPlayer(message);
	}
	
	public static boolean canEntityTravel(Entity Entity)
	{
		return Entity.timeUntilPortal == 0 && isValidEntityForTravel(Entity);
	}
	
	public static boolean isValidEntityForTravel(Entity Entity)
	{
		return true;
	}
	
	public static void setCanEntityTravel(Entity Entity, boolean State)
	{
		if (State)
			Entity.timeUntilPortal = 0;
		else
			Entity.timeUntilPortal = Entity.getPortalCooldown();
	}
	
	public static void setEntityLocation(Entity entity, double x, double y, double z, boolean keepVelocity)
	{
		double[] offset = offsetDirectionBased(entity.worldObj, (int)x, (int)y, (int)z);
		
		if (entity instanceof EntityPlayerMP)
			((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(offset[0], offset[1], offset[2], entity.rotationYaw, entity.rotationPitch);
		else
		{
			//if (entity instanceof EntityMinecart)
			//	y += 0.5;
			
			entity.setPositionAndRotation(offset[0], offset[1], offset[2], entity.rotationYaw, entity.rotationPitch);
		}
		
		if (!keepVelocity)
			entity.setVelocity(0, 0, 0);
	}
	
	public static void sendEntityToDimension(Entity Entity, int Dimension)
	{
		if (Dimension != Entity.dimension)
			Entity.travelToDimension(Dimension);
	}
		
	public static void sendEntityToDimensionAndLocation(Entity entity, int dimension, double x, double y, double z, boolean keepVelocity)
	{		
		sendEntityToDimension(entity, dimension);
		setEntityLocation(entity, x, y, z, keepVelocity);
	}
	
	public static double[] offsetDirectionBased(World world, int x, int y, int z)
	{
		return offsetDirectionBased(world, (double)x, (double)y, (double)z, WorldHelper.getBlockDirection(world, x, y, z));
	}
	
	public static double[] offsetDirectionBased(World world, double x, double y, double z, ForgeDirection direction)
	{
		if (direction == ForgeDirection.UNKNOWN)
			return null;
		
		if (direction == ForgeDirection.NORTH)
		{
			z -= 0.5;
			x += 0.5;
		}
		else if (direction == ForgeDirection.SOUTH)
		{
			z += 1.5;
			x += 0.5;
		}
		else if (direction == ForgeDirection.EAST)
		{
			x += 1.5;
			z += 0.5;
		}
		else if (direction == ForgeDirection.WEST)
		{
			x -= 0.5;
			z += 0.5;
		}
		else if (direction == ForgeDirection.UP)
		{
			y += 1;
			z += 0.5;
			x += 0.5;
		}
		else if (direction == ForgeDirection.DOWN)
		{
			y -= 2;
			z += 0.5;
			x += 0.5;
		}
		
		return new double[] { x, y, z };
	}
	
	public static boolean canAcceptItemStack(IInventory inventory, ItemStack stack)
	{
		if (stack.itemID != ItemID.ModifierUpgrade + 256)
			return false;
		
		ItemStack firstSlot = inventory.getStackInSlot(0), secondSlot = inventory.getStackInSlot(1), thirdSlot = inventory.getStackInSlot(2);
				
		if (firstSlot != null)
		{
			if (firstSlot.getItemDamage() == stack.getItemDamage())
				return false;
		}
		else if (secondSlot != null)
		{
			if (secondSlot.getItemDamage() == stack.getItemDamage())
				return false;
		}
		else if (thirdSlot != null)
		{
			if (thirdSlot.getItemDamage() == stack.getItemDamage())
				return false;
		}
		
		return true;
	}
}
