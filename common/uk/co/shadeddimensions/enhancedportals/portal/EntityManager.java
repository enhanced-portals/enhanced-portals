package uk.co.shadeddimensions.enhancedportals.portal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TilePortalController;

public class EntityManager
{
    static final int PLAYER_COOLDOWN_RATE = 10;
    
    public static boolean isEntityFitForTravel(Entity entity)
    {
        return entity != null && entity.timeUntilPortal == 0;
    }
    
    public static void setEntityPortalCooldown(Entity entity)
    {
        if (entity instanceof EntityMinecart || entity instanceof EntityBoat || entity instanceof EntityHorse)
        {
            entity.timeUntilPortal = PLAYER_COOLDOWN_RATE;
        }
        else
        {
            entity.timeUntilPortal = entity.getPortalCooldown();
        }
    }
    
    public static void teleportEntity(Entity entity, String UIDFrom, String UIDTo)
    {
        TilePortalController controllerDest = CommonProxy.networkManager.getPortalLocationController(UIDTo);
        
        if (controllerDest == null)
        {
            CommonProxy.logger.fine("Failed to teleport entity - Cannot get TileEntity of exit Portal Controller!");
            return;
        }
        
        boolean isDimensionalPortal = entity.worldObj.provider.dimensionId != controllerDest.worldObj.provider.dimensionId;
        //WorldServer destinationWorld = (WorldServer) controllerDest.worldObj;
        
        if (!controllerDest.portalActive)
        {
            controllerDest.createPortal();
            
            if (!controllerDest.portalActive)
            {
                CommonProxy.logger.fine("Failed to teleport entity - Could not create an exit portal!");
                return;
            }
        }
        
        ChunkCoordinates exit = getActualExitLocation(entity, controllerDest);
        
        if (exit == null)
        {
            CommonProxy.logger.fine("Failed to teleport entity - Could not find a suitable exit location. Entity is too big for the portal!");
            return;
        }
        
        CommonProxy.logger.fine(String.format("Found a suitable exit location for Entity (%s): %s, %s, %s", entity.getEntityName(), exit.posX, exit.posY, exit.posZ));
        
        if (isDimensionalPortal)
        {
            System.out.println("TODO."); // TODO
            return;
        }
        
        float entityRotation = getRotation(entity, controllerDest, exit);
                
        if (entity instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            
            player.playerNetServerHandler.setPlayerLocation(exit.posX + 0.5, exit.posY, exit.posZ + 0.5, entityRotation, player.rotationPitch);
        }
        
        entity.setLocationAndAngles(exit.posX + 0.5, exit.posY, exit.posZ + 0.5, entityRotation, entity.rotationPitch);
    }
    
    private static float getRotation(Entity entity, TilePortalController controller, ChunkCoordinates loc)
    {
        int portalOrientation = controller.PortalType;
        
        if (portalOrientation == 1)
        {
            if (controller.worldObj.isBlockOpaqueCube(loc.posX, loc.posY, loc.posZ + 1))
            {
                return 180f;
            }
            
            return 0f;
        }
        else if (portalOrientation == 2)
        {
            if (controller.worldObj.isBlockOpaqueCube(loc.posX - 1, loc.posY, loc.posZ))
            {
                return -90f;
            }
            
            return 90f;
        }

        return entity.rotationYaw;
    }
        
    private static ChunkCoordinates getActualExitLocation(Entity entity, TilePortalController controller)
    {
        int entityHeight = Math.round(entity.height), entityWidth = Math.round(entity.width);
        boolean horizontal = controller.PortalType == 3;
        
        forloop:
        for (ChunkCoordinates c : controller.blockManager.getPortalsCoord())
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
}
