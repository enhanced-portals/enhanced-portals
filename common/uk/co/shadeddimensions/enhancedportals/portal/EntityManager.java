package uk.co.shadeddimensions.enhancedportals.portal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;

public class EntityManager
{
    static final int PLAYER_COOLDOWN_RATE = 10;
    
    public static boolean isEntityFitForTravel(Entity entity)
    {
        return entity.timeUntilPortal == 0;
    }
    
    public static void setEntityPortalCooldown(Entity entity)
    {
        if (entity instanceof EntityMinecart || entity instanceof EntityBoat)
        {
            entity.timeUntilPortal = PLAYER_COOLDOWN_RATE;
        }
        else
        {
            entity.timeUntilPortal = entity.getPortalCooldown();
        }
    }
}
