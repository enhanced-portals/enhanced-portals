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
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class EntityManager
{
    static final int PLAYER_COOLDOWN_RATE = 10;

    private static ChunkCoordinates getActualExitLocation(Entity entity, TilePortalController controller)
    {
        int entityHeight = Math.round(entity.height), entityWidth = Math.round(entity.width);
        boolean horizontal = controller.portalType == 3;

        forloop:
            for (WorldCoordinates c : controller.getAllPortalBlocks())
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

    private static void handleMomentum(Entity entity, TilePortal portalTouched, int exitPortalType, float exitYaw)
    {
        // 1 - X (Use rotation for direction)
        // 2 - Z (Use rotation for direction)
        // 3 - Horizontal (UP)
        // 4 - Horizontal (DOWN)
        int touchedPortalType = portalTouched.getBlockMetadata();

        if (touchedPortalType == 1)
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

        entity.fallDistance = 0f;
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

    private static void removeEntityFromWorld(Entity entity, WorldServer world)
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
            world.onEntityRemoved(entity);
        }

        entity.isDead = false;
    }

    public static boolean isEntityFitForTravel(Entity entity)
    {
        return entity != null && entity.timeUntilPortal == 0;
    }

    public static void setEntityPortalCooldown(Entity entity)
    {
        if (CommonProxy.fasterPortalCooldown || (entity instanceof EntityPlayer || entity instanceof EntityMinecart || entity instanceof EntityBoat || entity instanceof EntityHorse))
        {
            entity.timeUntilPortal = PLAYER_COOLDOWN_RATE;
        }
        else
        {
            entity.timeUntilPortal = 300; // Reduced to 300 ticks from 900.
        }
    }

    public static void teleportEntity(Entity entity, GlyphIdentifier entryID, GlyphIdentifier exitID, TilePortal portal)
    {
        TilePortalController /*controllerEntry = CommonProxy.networkManager.getPortalController(entryID),*/ controllerDest = CommonProxy.networkManager.getPortalController(exitID);

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

        TileBiometricIdentifier bio = controllerDest.getBiometricIdentifier();

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
            CommonProxy.logger.fine("Failed to teleport entity - Could not find a suitable exit location.");
            return;
        }
        else
        {
            CommonProxy.logger.fine(String.format("Found a suitable exit location for Entity (%s): %s, %s, %s", entity.getEntityName(), exit.posX, exit.posY, exit.posZ));
            teleportEntity(entity, exit, (WorldServer) controllerDest.worldObj, getRotation(entity, controllerDest, exit), controllerDest.portalType, portal);
        }
    }

    public static void teleportEntity(Entity entity, WorldCoordinates location)
    {
        teleportEntityBasic(entity, new ChunkCoordinates(location.posX, location.posY, location.posZ), location.getWorld());
    }

    @SuppressWarnings("rawtypes")
    public static Entity teleportEntityBasic(Entity entity, ChunkCoordinates location, WorldServer world)
    {
        double offsetY = entity instanceof EntityMinecart ? 0.4 : 0;
        boolean dimensionalTravel = entity.worldObj.provider.dimensionId != world.provider.dimensionId;
        Entity mount = entity.ridingEntity;

        if (mount != null) // If the entity is riding another entity
        {
            entity.mountEntity(null); // Dismount
            mount = teleportEntityBasic(mount, location, world); // Then send the mounted entity first. Store it for later use
        }

        entity.worldObj.updateEntityWithOptionalForce(entity, false);

        if (entity instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;            
            player.closeScreen(); // Close any open GUI screen.

            if (dimensionalTravel)
            {
                player.dimension = world.provider.dimensionId; // Update the player's dimension
                player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte) world.difficultySetting, world.provider.terrainType, world.provider.getHeight(), player.theItemInWorldManager.getGameType())); // Send a respawn packet to the player
                ((WorldServer) player.worldObj).getPlayerManager().removePlayer(player); // Remove the player from the world
            }
        }

        if (dimensionalTravel)
        {
            removeEntityFromWorld(entity, (WorldServer) entity.worldObj); // Remove the entity from the world
        }

        entity.motionX = entity.motionY = entity.motionZ = 0;

        world.getChunkProvider().loadChunk(location.posX >> 4, location.posZ >> 4); // Make sure the chunk is loaded
        entity.setPositionAndRotation(location.posX + 0.5, location.posY + offsetY, location.posZ + 0.5, entity.rotationYaw, entity.rotationPitch);

        if (dimensionalTravel)
        {
            if (!(entity instanceof EntityPlayer))
            {
                NBTTagCompound nbt = new NBTTagCompound();
                entity.isDead = false;
                entity.writeToNBTOptional(nbt); // Save all entity data to NBT, including it's ID
                entity.isDead = true;

                entity = EntityList.createEntityFromNBT(nbt, world); // Make a new entity from the NBT data in the new world

                if (entity == null)
                {
                    return null; // If we failed, quit
                }

                entity.dimension = world.provider.dimensionId; // Update it's dimension
            }

            world.spawnEntityInWorld(entity); // Spawn it in the new world
            entity.setWorld(world); // Set the entities world to the new one
        }

        entity.setPositionAndRotation(location.posX + 0.5, location.posY + offsetY, location.posZ + 0.5, entity.rotationYaw, entity.rotationPitch);
        world.updateEntityWithOptionalForce(entity, false);
        entity.setPositionAndRotation(location.posX + 0.5, location.posY + offsetY, location.posZ + 0.5, entity.rotationYaw, entity.rotationPitch);

        if (entity instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;

            if (dimensionalTravel)
            {
                player.mcServer.getConfigurationManager().func_72375_a(player, world); // ??
            }

            player.playerNetServerHandler.setPlayerLocation(location.posX + 0.5, location.posY + offsetY, location.posZ + 0.5, entity.rotationYaw, entity.rotationPitch); // Update the players location -- make sure the client gets this data too
        }

        world.updateEntityWithOptionalForce(entity, false);

        if (entity instanceof EntityPlayerMP && dimensionalTravel)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;

            player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, world); // Make sure the client has the correct time & weather
            player.mcServer.getConfigurationManager().syncPlayerInventory(player); // And make sure their inventory isn't out of sync

            Iterator iterator = player.getActivePotionEffects().iterator();

            while (iterator.hasNext()) // Sync up any potion effects the player may have
            {
                PotionEffect effect = (PotionEffect) iterator.next();
                player.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(player.entityId, effect));
            }

            player.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(player.experience, player.experienceTotal, player.experienceLevel)); // Sync up their xp
        }

        entity.setPositionAndRotation(location.posX + 0.5, location.posY + offsetY, location.posZ + 0.5, entity.rotationYaw, entity.rotationPitch);

        if (entity instanceof EntityMinecart) // Stops the minecart from derping about. TODO: Figure out a solution which isn't this.
        {
            entity.motionX = 0;
            entity.motionY = 0;
            entity.motionZ = 0;
        }

        if (mount != null) // Remount any mounted entities
        {
            if (!(entity instanceof EntityPlayerMP)) // Player re-mounting is derpy.
            {
                entity.mountEntity(mount);
            }
        }

        return entity;
    }

    @SuppressWarnings("rawtypes")
    public static Entity teleportEntity(Entity entity, ChunkCoordinates location, WorldServer world, float exitYaw, int exitPortalType, TilePortal portal)
    {
        double offsetY = entity instanceof EntityMinecart ? 0.4 : 0;
        boolean dimensionalTravel = entity.worldObj.provider.dimensionId != world.provider.dimensionId;
        Entity mount = entity.ridingEntity;

        if (mount != null) // If the entity is riding another entity
        {
            entity.mountEntity(null); // Dismount
            mount = teleportEntity(mount, location, world, exitYaw, exitPortalType, portal); // Then send the mounted entity first. Store it for later use
        }

        entity.worldObj.updateEntityWithOptionalForce(entity, false);

        if (entity instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;            
            player.closeScreen(); // Close any open GUI screen.

            if (dimensionalTravel)
            {
                player.dimension = world.provider.dimensionId; // Update the player's dimension
                player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte) world.difficultySetting, world.provider.terrainType, world.provider.getHeight(), player.theItemInWorldManager.getGameType())); // Send a respawn packet to the player
                ((WorldServer) player.worldObj).getPlayerManager().removePlayer(player); // Remove the player from the world
            }
        }

        if (dimensionalTravel)
        {
            removeEntityFromWorld(entity, (WorldServer) entity.worldObj); // Remove the entity from the world
        }

        handleMomentum(entity, portal, exitPortalType == 3 && world.isAirBlock(location.posX, location.posY - 1, location.posZ) ? 4 : exitPortalType, exitYaw);

        world.getChunkProvider().loadChunk(location.posX >> 4, location.posZ >> 4); // Make sure the chunk is loaded
        entity.setPositionAndRotation(location.posX + 0.5, location.posY + offsetY, location.posZ + 0.5, exitYaw, entity.rotationPitch);

        if (dimensionalTravel)
        {
            if (!(entity instanceof EntityPlayer))
            {
                NBTTagCompound nbt = new NBTTagCompound();
                entity.isDead = false;
                entity.writeToNBTOptional(nbt); // Save all entity data to NBT, including it's ID
                entity.isDead = true;

                entity = EntityList.createEntityFromNBT(nbt, world); // Make a new entity from the NBT data in the new world

                if (entity == null)
                {
                    return null; // If we failed, quit
                }

                entity.dimension = world.provider.dimensionId; // Update it's dimension
            }

            world.spawnEntityInWorld(entity); // Spawn it in the new world
            entity.setWorld(world); // Set the entities world to the new one
        }

        entity.setPositionAndRotation(location.posX + 0.5, location.posY + offsetY, location.posZ + 0.5, exitYaw, entity.rotationPitch);
        world.updateEntityWithOptionalForce(entity, false);
        entity.setPositionAndRotation(location.posX + 0.5, location.posY + offsetY, location.posZ + 0.5, exitYaw, entity.rotationPitch);

        if (entity instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;

            if (dimensionalTravel)
            {
                player.mcServer.getConfigurationManager().func_72375_a(player, world); // ??
            }

            player.playerNetServerHandler.setPlayerLocation(location.posX + 0.5, location.posY + offsetY, location.posZ + 0.5, exitYaw, entity.rotationPitch); // Update the players location -- make sure the client gets this data too
        }

        world.updateEntityWithOptionalForce(entity, false);

        if (entity instanceof EntityPlayerMP && dimensionalTravel)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;

            player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, world); // Make sure the client has the correct time & weather
            player.mcServer.getConfigurationManager().syncPlayerInventory(player); // And make sure their inventory isn't out of sync

            Iterator iterator = player.getActivePotionEffects().iterator();

            while (iterator.hasNext()) // Sync up any potion effects the player may have
            {
                PotionEffect effect = (PotionEffect) iterator.next();
                player.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(player.entityId, effect));
            }

            player.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(player.experience, player.experienceTotal, player.experienceLevel)); // Sync up their xp
        }

        entity.setPositionAndRotation(location.posX + 0.5, location.posY + offsetY, location.posZ + 0.5, exitYaw, entity.rotationPitch);

        if (entity instanceof EntityMinecart) // Stops the minecart from derping about. TODO: Figure out a solution which isn't this.
        {
            entity.motionX = 0;
            entity.motionY = 0;
            entity.motionZ = 0;
        }

        if (mount != null) // Remount any mounted entities
        {
            if (!(entity instanceof EntityPlayerMP)) // Player re-mounting is derpy.
            {
                entity.mountEntity(mount);
            }
        }

        return entity;
    }

    public static void teleportEntityToDimension(Entity par1Entity)
    {
        WorldCoordinates location = new WorldCoordinates(0, 0, 0, CommonProxy.Dimension);
        location.posY = location.getWorld().getTopSolidOrLiquidBlock(location.posX, location.posZ);
        
        teleportEntity(par1Entity, location);
    }
}
