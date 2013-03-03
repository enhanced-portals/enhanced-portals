package alz.mods.enhancedportals.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

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
		if (entity instanceof EntityPlayerMP)
			((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(x, y, z, entity.rotationYaw, entity.rotationPitch);
		else
		{
			if (entity instanceof EntityMinecart)
				y += 0.5;
			
			entity.setPositionAndRotation(x, y, z, entity.rotationYaw, entity.rotationPitch);
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
}
