package uk.co.shadeddimensions.ep3.portal;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet43Experience;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import cpw.mods.fml.common.registry.GameRegistry;

public class EntityManager
{
    static final int PLAYER_COOLDOWN_RATE = 10;

    private static ChunkCoordinates getActualExitLocation(Entity entity, TilePortalController controller)
    {
        int entityHeight = Math.round(entity.height), entityWidth = Math.round(entity.width);
        boolean horizontal = controller.portalType == 3;

        forloop:
        for (ChunkCoordinates c : controller.blockManager.getPortals())
        {
            for (int i = 0; i < (horizontal ? Math.round(entityWidth / 2) : entityHeight); i++)
            {
                if (horizontal)
                {
                    if (controller.worldObj.getBlockId(c.posX + i, c.posY, c.posZ) != CommonProxy.blockPortal.blockID || controller.worldObj.getBlockId(c.posX - i, c.posY, c.posZ) != CommonProxy.blockPortal.blockID || controller.worldObj.getBlockId(c.posX, c.posY, c.posZ + i) != CommonProxy.blockPortal.blockID || controller.worldObj.getBlockId(c.posX, c.posY, c.posZ + i) != CommonProxy.blockPortal.blockID)
                    {
                        continue forloop;
                    }
                }
                else
                {
                    if (controller.worldObj.getBlockId(c.posX, c.posY + i, c.posZ) != CommonProxy.blockPortal.blockID && !controller.worldObj.isAirBlock(c.posX, c.posY + i, c.posZ))
                    {
                        continue forloop;
                    }
                }
            }

            return c;
        }

        return null;
    }

    private static void handleMomentum(Entity entity, int touchedPortalType, int exitPortalType, float exitYaw, boolean keepMomentum)
    {
        if (!keepMomentum)
        {
            entity.motionX = entity.motionY = entity.motionZ = 0;
            return;
        }
        else if (touchedPortalType == 1)
        {
            if (exitPortalType == 2)
            {
                double temp = entity.motionZ;
                entity.motionZ = entity.motionX;
                entity.motionX = exitYaw == -90 ? -temp : temp;
            }
            else if (exitPortalType == 3)
            {
                double temp = entity.motionZ;
                entity.motionZ = entity.motionY;
                entity.motionY = temp;
            }
            else if (exitPortalType == 4)
            {
                double temp = entity.motionZ;
                entity.motionZ = entity.motionY;
                entity.motionY = -temp;
            }
        }
        else if (touchedPortalType == 2)
        {
            if (exitPortalType == 1)
            {
                double temp = entity.motionZ;
                entity.motionZ = entity.motionX;
                entity.motionX = exitYaw == 0 ? -temp : temp;
            }
            else if (exitPortalType == 3)
            {
                double temp = entity.motionX;
                entity.motionX = entity.motionY;
                entity.motionY = temp;
            }
            else if (exitPortalType == 4)
            {
                double temp = entity.motionX;
                entity.motionX = entity.motionY;
                entity.motionY = -temp;
            }
        }
        else if (touchedPortalType == 3 || touchedPortalType == 4)
        {
            if (exitPortalType == 1)
            {
                double temp = entity.motionY;
                entity.motionY = entity.motionZ;
                entity.motionZ = exitYaw == 0 ? -temp : temp;
            }
            else if (exitPortalType == 2)
            {
                double temp = entity.motionY;
                entity.motionY = entity.motionX;
                entity.motionX = exitYaw == -90 ? -temp : temp;
            }
            else if (exitPortalType == 3)
            {
                entity.motionY = touchedPortalType == 3 ? -entity.motionY : entity.motionY;
            }
        }

        entity.velocityChanged = true;
    }

    private static float getRotation(Entity entity, TilePortalController controller, ChunkCoordinates loc)
    {
        if (controller.portalType == 1)
        {
            if (controller.worldObj.isBlockOpaqueCube(loc.posX, loc.posY, loc.posZ + 1))
            {
                return 180f; // 2
            }

            return 0f; // 0
        }
        else if (controller.portalType == 2)
        {
            if (controller.worldObj.isBlockOpaqueCube(loc.posX - 1, loc.posY, loc.posZ))
            {
                return -90f; // 3
            }

            return 90f; // 1
        }

        return entity.rotationYaw;
    }

    public static boolean isEntityFitForTravel(Entity entity)
    {
        return entity != null && entity.timeUntilPortal == 0;
    }

    public static void setEntityPortalCooldown(Entity entity)
    {
        if (CommonProxy.fasterPortalCooldown || entity instanceof EntityPlayer || entity instanceof EntityMinecart || entity instanceof EntityBoat || entity instanceof EntityHorse)
        {
            entity.timeUntilPortal = PLAYER_COOLDOWN_RATE;
        }
        else
        {
            entity.timeUntilPortal = 300; // Reduced to 300 ticks from 900.
        }
    }

    public static void transferEntity(Entity entity, GlyphIdentifier entryID, GlyphIdentifier exitID, int portalType)
    {
        TilePortalController controllerEntry = CommonProxy.networkManager.getPortalController(entryID), controllerDest = CommonProxy.networkManager.getPortalController(exitID);

        if (controllerDest == null)
        {
            CommonProxy.logger.fine("Failed to teleport entity - Cannot get TileEntity of exit Portal Controller!");
            return;
        }
        else if (!controllerDest.isPortalActive)
        {
            CommonProxy.logger.fine("Failed to teleport entity - Portal is not active!");
            return;
        }

        TileBiometricIdentifier bio = controllerDest.blockManager.getBiometricIdentifier(controllerDest.worldObj);

        if (bio != null)
        {
            if (!bio.canEntityBeRecieved(entity))
            {
                setEntityPortalCooldown(entity);
                return;
            }
        }

        ChunkCoordinates exit = getActualExitLocation(entity, controllerDest);

        if (exit == null)
        {
            CommonProxy.logger.fine("Failed to teleport entity (" + entity.getEntityName() + ") - Could not find a suitable exit location.");
            return;
        }
        else
        {
            boolean keepMomentum = false;
            TileModuleManipulator manip = controllerEntry.blockManager.getModuleManipulator(controllerEntry.worldObj);

            if (manip != null)
            {
                keepMomentum = manip.shouldKeepMomentumOnTeleport();
            }

            CommonProxy.logger.fine(String.format("Found a suitable exit location for Entity (%s): %s, %s, %s", entity.getEntityName(), exit.posX, exit.posY, exit.posZ));

            while (entity.ridingEntity != null)
            {
                entity = entity.ridingEntity;
            }

            transferEntityWithRider(entity, exit.posX + 0.5, exit.posY, exit.posZ + 0.5, getRotation(entity, controllerDest, exit), (WorldServer) controllerDest.worldObj, portalType, controllerDest.portalType, keepMomentum);
        }
    }

    public static Entity transferEntityWithRider(Entity entity, double x, double y, double z, float yaw, WorldServer world, int touchedPortalType, int exitPortalType, boolean keepMomentum)
    {
        Entity rider = entity.riddenByEntity;

        if (rider != null)
        {
            rider.mountEntity(null);
            rider = transferEntityWithRider(rider, x, y, z, yaw, world, touchedPortalType, exitPortalType, keepMomentum);
        }

        entity = transferEntity(entity, x, y, z, yaw, world, touchedPortalType, exitPortalType, keepMomentum);

        if (rider != null)
        {
            rider.mountEntity(entity);
        }

        return entity;
    }

    public static Entity transferEntity(Entity entity, double x, double y, double z, float yaw, WorldServer world, int touchedPortalType, int exitPortalType, boolean keepMomentum)
    {
        if (entity.worldObj.provider.dimensionId == world.provider.dimensionId)
        {
            return transferEntityWithinDimension(entity, x, y, z, yaw, touchedPortalType, exitPortalType, keepMomentum);
        }
        else
        {
            return transferEntityToDimension(entity, x, y, z, yaw, (WorldServer) entity.worldObj, world, touchedPortalType, exitPortalType, keepMomentum);
        }
    }

    public static Entity transferEntityWithinDimension(Entity entity, double x, double y, double z, float yaw, int touchedPortalType, int exitPortalType, boolean keepMomentum)
    {
        if (entity == null)
        {
            return null;
        }
        else if (!isEntityFitForTravel(entity))
        {
            return entity;
        }
        else if (entity instanceof EntityPlayer)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            player.rotationYaw = yaw;
            player.setPositionAndUpdate(x, y, z);
            handleMomentum(player, touchedPortalType, exitPortalType, yaw, keepMomentum);
            player.worldObj.updateEntityWithOptionalForce(player, false);

            setEntityPortalCooldown(player);
            return player;
        }
        else
        {
            WorldServer world = (WorldServer) entity.worldObj;
            NBTTagCompound tag = new NBTTagCompound();
            entity.writeToNBTOptional(tag);

            int chunkX = entity.chunkCoordX;
            int chunkZ = entity.chunkCoordZ;

            if (entity.addedToChunk && world.getChunkProvider().chunkExists(chunkX, chunkZ))
            {
                world.getChunkFromChunkCoords(chunkX, chunkZ).removeEntity(entity);
            }

            world.loadedEntityList.remove(entity);
            world.onEntityRemoved(entity);

            Entity newEntity = EntityList.createEntityFromNBT(tag, world);

            if (newEntity != null)
            {
                handleMomentum(newEntity, touchedPortalType, exitPortalType, yaw, keepMomentum);
                newEntity.setLocationAndAngles(x, y, z, yaw, entity.rotationPitch);
                newEntity.forceSpawn = true;
                world.spawnEntityInWorld(newEntity);
                newEntity.setWorld(world);
                setEntityPortalCooldown(newEntity);
            }

            world.resetUpdateEntityTick();
            return newEntity;
        }
    }

    @SuppressWarnings("rawtypes")
    public static Entity transferEntityToDimension(Entity entity, double x, double y, double z, float yaw, WorldServer exitingWorld, WorldServer enteringWorld, int touchedPortalType, int exitPortalType, boolean keepMomentum)
    {
        if (entity == null)
        {
            return null;
        }
        else if (!isEntityFitForTravel(entity))
        {
            return entity;
        }
        else if (entity instanceof EntityPlayer)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            MinecraftServer server = player.mcServer;
            ServerConfigurationManager config = server.getConfigurationManager();

            player.closeScreen();
            player.dimension = enteringWorld.provider.dimensionId;
            player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte) player.worldObj.difficultySetting, enteringWorld.getWorldInfo().getTerrainType(), enteringWorld.getHeight(), player.theItemInWorldManager.getGameType()));

            exitingWorld.removePlayerEntityDangerously(player);
            player.isDead = false;
            player.setLocationAndAngles(x, y, z, yaw, player.rotationPitch);
            handleMomentum(player, touchedPortalType, exitPortalType, yaw, keepMomentum);

            enteringWorld.spawnEntityInWorld(player);
            player.setWorld(enteringWorld);

            config.func_72375_a(player, exitingWorld);
            player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, entity.rotationPitch);
            player.theItemInWorldManager.setWorld(enteringWorld);

            config.updateTimeAndWeatherForPlayer(player, enteringWorld);
            config.syncPlayerInventory(player);

            Iterator potion = player.getActivePotionEffects().iterator();
            while (potion.hasNext())
            {
                player.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(player.entityId, (PotionEffect) potion.next()));
            }

            player.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(player.experience, player.experienceTotal, player.experienceLevel));
            GameRegistry.onPlayerChangedDimension(player);

            setEntityPortalCooldown(player);
            return player;
        }
        else
        {
            NBTTagCompound tag = new NBTTagCompound();
            entity.writeToNBTOptional(tag);

            int chunkX = entity.chunkCoordX;
            int chunkZ = entity.chunkCoordZ;

            if (entity.addedToChunk && exitingWorld.getChunkProvider().chunkExists(chunkX, chunkZ))
            {
                exitingWorld.getChunkFromChunkCoords(chunkX, chunkZ).removeEntity(entity);
            }

            exitingWorld.loadedEntityList.remove(entity);
            exitingWorld.onEntityRemoved(entity);

            Entity newEntity = EntityList.createEntityFromNBT(tag, enteringWorld);

            if (newEntity != null)
            {
                handleMomentum(newEntity, touchedPortalType, exitPortalType, yaw, keepMomentum);
                newEntity.setLocationAndAngles(x, y, z, yaw, entity.rotationPitch);
                newEntity.forceSpawn = true;
                enteringWorld.spawnEntityInWorld(newEntity);
                newEntity.setWorld(enteringWorld);
                setEntityPortalCooldown(newEntity);
            }

            exitingWorld.resetUpdateEntityTick();
            enteringWorld.resetUpdateEntityTick();

            return newEntity;
        }
    }

    public static void teleportEntityToDimension(Entity par1Entity)
    {
        // TODO 

        // For now, just teleport them to the spawn point of the dimension they're in
        ChunkCoordinates spawn = par1Entity.worldObj.getSpawnPoint();
        transferEntityWithinDimension(par1Entity, spawn.posX, par1Entity.worldObj.getTopSolidOrLiquidBlock(spawn.posX, spawn.posZ), spawn.posZ, 0f, 0, 0, false);
    }
}
