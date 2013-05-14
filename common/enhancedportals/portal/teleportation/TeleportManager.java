package enhancedportals.portal.teleportation;

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
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Settings;
import enhancedportals.lib.WorldLocation;
import enhancedportals.portal.Portal;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class TeleportManager
{
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

    public static boolean teleportEntity(Entity entity, WorldLocation teleportData, TileEntityPortalModifier originModifier, boolean supressMessages)
    {
        if (entity.worldObj.isRemote)
        {
            return false;
        }

        World world = EnhancedPortals.proxy.getWorld(teleportData.dimension);
        ((WorldServer) world).theChunkProviderServer.loadChunk(teleportData.xCoord >> 4, teleportData.zCoord >> 4);

        TileEntityPortalModifier outModifier = (TileEntityPortalModifier) teleportData.getTileEntity();
        int outModifierMeta = world.getBlockMetadata(outModifier.xCoord, outModifier.yCoord, outModifier.zCoord);
        WorldLocation outModifierOffset = teleportData.getOffset(ForgeDirection.getOrientation(outModifierMeta));
        boolean teleportEntity = false;

        if (outModifierOffset.getBlockId() == 0)
        {
            if (new Portal(outModifierOffset.xCoord, outModifierOffset.yCoord, outModifierOffset.zCoord, world, outModifier).createPortal())
            {
                teleportEntity = true;
            }
            else
            {
                if (!supressMessages && entity instanceof EntityPlayer)
                {
                    ((EntityPlayer) entity).sendChatToPlayer("A portal could not be created at the exit location.");
                }
            }
        }
        else if (outModifierOffset.getBlockId() == BlockIds.NetherPortal)
        {
            teleportEntity = true;
        }

        if (teleportEntity)
        {
            teleportEntity((WorldServer) world, entity, teleportData, outModifierOffset, outModifierMeta);
        }

        return teleportEntity;
    }

    private static Entity teleportEntity(WorldServer world, Entity entity, WorldLocation teleportData, WorldLocation teleportDataOffset, int metaDirection)
    {
        if (!Settings.AllowTeleporting || !canEntityTravel(entity))
        {
            return entity;
        }

        boolean dimensionalTeleport = entity.worldObj.provider.dimensionId != world.provider.dimensionId;
        float rotationYaw = 0f;

        if (teleportDataOffset.getMetadata() == 4 || teleportDataOffset.getMetadata() == 5)
        {
            if (!teleportDataOffset.getOffset(ForgeDirection.EAST).isBlockAir())
            {
                rotationYaw = 90F;
            }
            else
            {
                rotationYaw = -90F;
            }
        }
        else if (teleportDataOffset.getMetadata() == 2 || teleportDataOffset.getMetadata() == 3)
        {
            if (teleportDataOffset.getOffset(ForgeDirection.NORTH).isBlockAir())
            {
                rotationYaw = 180F;
            }
            else
            {
                rotationYaw = 0F;
            }
        }
        else
        {
            rotationYaw = entity.rotationYaw;
        }
        
        if (metaDirection == 0)
        {
            teleportDataOffset.yCoord -= 1;
        }

        Entity mountedEntity = null;

        if (entity.ridingEntity != null)
        {
            mountedEntity = teleportEntity(world, entity.ridingEntity, teleportData, teleportDataOffset, metaDirection);
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

        entity.setLocationAndAngles(teleportDataOffset.xCoord + 0.5, teleportDataOffset.yCoord, teleportDataOffset.zCoord + 0.5, rotationYaw, entity.rotationPitch);
        world.theChunkProviderServer.loadChunk(teleportData.xCoord >> 4, teleportData.zCoord >> 4);

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

            player.playerNetServerHandler.setPlayerLocation(teleportDataOffset.xCoord + 0.5, teleportDataOffset.yCoord, teleportDataOffset.zCoord + 0.5, rotationYaw, entity.rotationPitch);
        }

        world.updateEntityWithOptionalForce(entity, false);

        if (entity instanceof EntityPlayerMP && dimensionalTeleport)
        {
            player = (EntityPlayerMP) entity;

            syncPlayer(player, world);
        }

        entity.setLocationAndAngles(teleportDataOffset.xCoord + 0.5, teleportDataOffset.yCoord, teleportDataOffset.zCoord + 0.5, rotationYaw, entity.rotationPitch);

        if (mountedEntity != null)
        {
            entity.mountEntity(mountedEntity);
        }

        setCanEntityTravel(entity, false);
        return entity;
    }
}
