package alz.mods.enhancedportals.helpers;

import java.util.Iterator;

import cpw.mods.fml.common.registry.GameRegistry;
import alz.mods.enhancedportals.reference.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet43Experience;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
		if (Entity == null)
			return false;
		
		return Entity.timeUntilPortal == 0;
	}
		
	public static void setCanEntityTravel(Entity Entity, boolean State)
	{
		if (State)
			Entity.timeUntilPortal = 0;
		else
			Entity.timeUntilPortal = Entity.getPortalCooldown();
	}
	
	public static void teleportEntity(Entity entity, TeleportData data)
	{
		World world = MinecraftServer.getServer().worldServerForDimension(data.GetDimension());
		
		teleportEntity(world, entity, data);		
	}
	
	private static Entity teleportEntity(World world, Entity entity, TeleportData teleportData)
	{
		if (!Reference.Settings.AllowTeleporting && canEntityTravel(entity))
			return entity;
		
		if (entity.riddenByEntity != null)
			entity.riddenByEntity.mountEntity(null);
		
		if (entity.ridingEntity != null)
			entity.mountEntity(null);
		
		boolean dimensionalTeleport = entity.worldObj != world;
				
		entity.worldObj.updateEntityWithOptionalForce(entity, false);
		
		EntityPlayerMP player;
		
		if (entity instanceof EntityPlayerMP)
		{
			player = (EntityPlayerMP) entity;
			
			handlePlayerRespawn(entity, player, world, dimensionalTeleport);
		}
		
		if (dimensionalTeleport)
			removeEntityFromWorld(entity.worldObj, entity);
		
		entity.setLocationAndAngles(teleportData.GetXOffsetEntity(), teleportData.GetYOffsetEntity(), teleportData.GetZOffsetEntity(), entity.rotationYaw, entity.rotationPitch);
		((WorldServer)world).theChunkProviderServer.loadChunk(teleportData.GetX() >> 4, teleportData.GetZ() >> 4);
		
		if (dimensionalTeleport)
		{
			entity = recreateEntity(entity, world);
			
			if (entity == null)
				return null;
		}
		
		if (entity instanceof EntityPlayerMP)
		{
			player = (EntityPlayerMP) entity;
			
			if (dimensionalTeleport)
				player.mcServer.getConfigurationManager().func_72375_a(player, (WorldServer)world);
			
			player.playerNetServerHandler.setPlayerLocation(teleportData.GetXOffsetEntity(), teleportData.GetYOffsetEntity(), teleportData.GetZOffsetEntity(), entity.rotationYaw, entity.rotationPitch);
		}
		
		world.updateEntityWithOptionalForce(entity, false);
		
		if (entity instanceof EntityPlayerMP && dimensionalTeleport)
		{
			player = (EntityPlayerMP) entity;
			
			syncPlayer(player, world);
		}
		
		entity.setLocationAndAngles(teleportData.GetXOffsetEntity(), teleportData.GetYOffsetEntity(), teleportData.GetZOffsetEntity(), entity.rotationYaw, entity.rotationPitch);
				
		setCanEntityTravel(entity, false);
		return entity;
	}
	
	private static EntityPlayerMP handlePlayerRespawn(Entity entity, EntityPlayerMP player, World world, boolean dimensionalTeleport)
	{
		player.closeScreen();
		
		if (dimensionalTeleport)
		{
			player.dimension = world.provider.dimensionId;
			player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte)player.worldObj.difficultySetting, world.getWorldInfo().getTerrainType(), world.getHeight(), player.theItemInWorldManager.getGameType()));
		
			((WorldServer)entity.worldObj).getPlayerManager().removePlayer(player);
		}
		
		return player;
	}
	
	private static Entity recreateEntity(Entity entity, World world)	
	{
		if (!(entity instanceof EntityPlayer))
		{
			NBTTagCompound nbt = new NBTTagCompound();
			entity.isDead = false;
			entity.riddenByEntity = null;
			entity.addEntityID(nbt);
			entity.isDead = true;
			
			entity = EntityList.createEntityFromNBT(nbt, world);
			
			if (entity == null)
				return null;
		}
		
		world.spawnEntityInWorld(entity);
		entity.setWorld(world);
		
		return entity;
	}
	
	@SuppressWarnings("rawtypes")
	private static void syncPlayer(EntityPlayerMP player, World world)
	{
		player.theItemInWorldManager.setWorld((WorldServer)world);
		player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, (WorldServer)world);
		player.mcServer.getConfigurationManager().syncPlayerInventory(player);
		
		Iterator iter = player.getActivePotionEffects().iterator();
		
		while (iter.hasNext())
		{
			PotionEffect effect = (PotionEffect)iter.next();
			player.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(player.entityId, effect));
		}
		
		player.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(player.experience, player.experienceTotal, player.experienceLevel));			
		GameRegistry.onPlayerChangedDimension(player);
	}
	
	private static void removeEntityFromWorld(World world, Entity entity)
	{
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			
			player.closeScreen();
			world.playerEntities.remove(player);
			world.updateAllPlayersSleepingFlag();
			
			int chunkX = entity.chunkCoordX, chunkZ = entity.chunkCoordZ;
			
			if (entity.addedToChunk && world.getChunkProvider().chunkExists(chunkX, chunkZ))
			{
				world.getChunkFromChunkCoords(chunkX, chunkZ).removeEntity(entity);
				world.getChunkFromChunkCoords(chunkX, chunkZ).isModified = true;
			}
			
			world.loadedEntityList.remove(entity);
			world.releaseEntitySkin(entity);
		}
		
		entity.isDead = false;
	}
	
	public static double[] offsetDirectionBased(World world, int x, int y, int z)
	{
		return offsetDirectionBased(world, (double)x, (double)y, (double)z, WorldHelper.getBlockDirection(world, x, y, z));
	}
	
	public static double[] offsetDirectionBased(World world, double x, double y, double z, ForgeDirection direction)
	{
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
			y += 1.0;
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
		if (stack.itemID != Reference.ItemIDs.PortalModifierUpgrade + 256)
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
