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
        
        if ( controllerDest == null)
        {
            System.out.println("Failed to teleport entity - Exit point is null! Exit (" + controllerDest + ")");
            return;
        }
        
        boolean isDimensionalPortal = entity.worldObj.provider.dimensionId != controllerDest.worldObj.provider.dimensionId;
        WorldServer destinationWorld = (WorldServer) controllerDest.worldObj;
        
        if (isDimensionalPortal)
        {
            System.out.println("TODO."); // TODO
            return;
        }
        
        if (!controllerDest.portalActive)
        {
            controllerDest.createPortal();
            
            if (!controllerDest.portalActive)
            {
                System.out.println("Failed to teleport entity - Could not create an exit portal!");
                return;
            }
        }
        
        ChunkCoordinates exit = getActualExitLocation(entity, controllerDest);
        
        if (exit == null)
        {
            System.out.println("Failed to teleport entity - Could not find a suitable exit location. Entity is too big for the portal!");
            return;
        }
        
        if (entity instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            
            player.playerNetServerHandler.setPlayerLocation(exit.posX + 0.5, exit.posY, exit.posZ + 0.5, player.rotationYaw, player.rotationPitch);
        }
        else
        {
            entity.setLocationAndAngles(exit.posX + 0.5, exit.posY, exit.posZ + 0.5, entity.rotationYaw, entity.rotationPitch);
        }
        
        System.out.println(String.format("Found a suitable exit location: %s, %s, %s", exit.posX, exit.posY, exit.posZ));
    }
        
    private static ChunkCoordinates getActualExitLocation(Entity entity, TilePortalController controller)
    {
        int entityHeight = Math.round(entity.height);
        
        forloop:
        for (ChunkCoordinates c : controller.blockManager.getPortalsCoord())
        {            
            for (int i = 0; i < entityHeight; i++)
            {
                if (controller.worldObj.getBlockId(c.posX, c.posY + i, c.posZ) != CommonProxy.blockPortal.blockID && !controller.worldObj.isAirBlock(c.posX, c.posY + i, c.posZ))
                {
                    continue forloop;
                }
            }
            
            return c;
        }
        
        return null;
    }
}
