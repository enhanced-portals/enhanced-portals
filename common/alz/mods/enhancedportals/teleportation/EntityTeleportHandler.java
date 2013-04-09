package alz.mods.enhancedportals.teleportation;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet43Experience;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import alz.mods.enhancedportals.reference.Settings;
import cpw.mods.fml.common.registry.GameRegistry;

public class EntityTeleportHandler
{
    /***
     * Can the entity travel dimensions?
     */
    public static boolean canEntityTravel(Entity entity)
    {
        if (entity == null)
        {
            return false;
        }

        return entity.timeUntilPortal == 0;
    }

    private static EntityPlayerMP handlePlayerRespawn(Entity entity, EntityPlayerMP player, WorldServer world, boolean dimensionalTeleport)
    {
        player.closeScreen();

        if (dimensionalTeleport)
        {
            player.dimension = world.provider.dimensionId;
            player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte) player.worldObj.difficultySetting, world.getWorldInfo().getTerrainType(), world.getHeight(), player.theItemInWorldManager.getGameType()));

            ((WorldServer) entity.worldObj).getPlayerManager().removePlayer(player);
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
            {
                return null;
            }
        }

        world.spawnEntityInWorld(entity);
        entity.setWorld(world);

        return entity;
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

    /***
     * Sets the entities ability to travel dimensions
     */
    public static void setCanEntityTravel(Entity entity, boolean state)
    {
        if (state)
        {
            entity.timeUntilPortal = 0;
        }
        else
        {
            entity.timeUntilPortal = entity.getPortalCooldown();
        }
    }

    @SuppressWarnings("rawtypes")
    private static void syncPlayer(EntityPlayerMP player, WorldServer world)
    {
        player.theItemInWorldManager.setWorld(world);
        player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, world);
        player.mcServer.getConfigurationManager().syncPlayerInventory(player);

        Iterator iter = player.getActivePotionEffects().iterator();

        while (iter.hasNext())
        {
            PotionEffect effect = (PotionEffect) iter.next();
            player.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(player.entityId, effect));
        }

        player.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(player.experience, player.experienceTotal, player.experienceLevel));
        GameRegistry.onPlayerChangedDimension(player);
    }

    /***
     * Teleports an entity to the specified location & dimension
     */
    public static void teleportEntity(Entity entity, TeleportData teleportData)
    {
        if (entity.worldObj.isRemote)
        {
            return;
        }

        WorldServer world = MinecraftServer.getServer().worldServerForDimension(teleportData.getDimension());

        teleportEntity(world, entity, teleportData);
    }

    private static Entity teleportEntity(WorldServer world, Entity entity, TeleportData teleportData)
    {
        if (!Settings.AllowTeleporting || !canEntityTravel(entity))
        {
            return entity;
        }

        boolean dimensionalTeleport = entity.worldObj.provider.dimensionId != world.provider.dimensionId;

        if (entity.riddenByEntity != null)
        {
            entity.riddenByEntity.mountEntity(null);
        }

        if (entity.ridingEntity != null)
        {
            entity.mountEntity(null);
        }

        entity.worldObj.updateEntityWithOptionalForce(entity, false);
        EntityPlayerMP player;

        if (entity instanceof EntityPlayerMP)
        {
            player = (EntityPlayerMP) entity;

            handlePlayerRespawn(entity, player, world, dimensionalTeleport);
        }

        if (dimensionalTeleport)
        {
            removeEntityFromWorld(entity.worldObj, entity);
        }

        entity.setLocationAndAngles(teleportData.getXOffsetEntity(), teleportData.getYOffsetEntity(), teleportData.getZOffsetEntity(), entity.rotationYaw, entity.rotationPitch);
        world.theChunkProviderServer.loadChunk(teleportData.getX() >> 4, teleportData.getZ() >> 4);

        if (dimensionalTeleport)
        {
            entity = recreateEntity(entity, world);

            if (entity == null)
            {
                return null;
            }
        }

        if (entity instanceof EntityPlayerMP)
        {
            player = (EntityPlayerMP) entity;

            if (dimensionalTeleport)
            {
                player.mcServer.getConfigurationManager().func_72375_a(player, world);
            }

            player.playerNetServerHandler.setPlayerLocation(teleportData.getXOffsetEntity(), teleportData.getYOffsetEntity(), teleportData.getZOffsetEntity(), entity.rotationYaw, entity.rotationPitch);
        }

        world.updateEntityWithOptionalForce(entity, false);

        if (entity instanceof EntityPlayerMP && dimensionalTeleport)
        {
            player = (EntityPlayerMP) entity;

            syncPlayer(player, world);
        }

        entity.setLocationAndAngles(teleportData.getXOffsetEntity(), teleportData.getYOffsetEntity(), teleportData.getZOffsetEntity(), entity.rotationYaw, entity.rotationPitch);

        setCanEntityTravel(entity, false);
        return entity;
    }
}
