package uk.co.shadeddimensions.ep3.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.portal.EntityManager;

public class MobEnderman extends EntityEnderman
{
    public MobEnderman(World par1World)
    {
        super(par1World);
    }
    
    @Override
    protected Entity findPlayerToAttack()
    {
        return this.worldObj.getClosestVulnerablePlayerToEntity(this, 64.0D);
    }
    
    @Override
    protected void attackEntity(Entity par1Entity, float par2)
    {
        EntityManager.teleportEntityToDimension(par1Entity);
        teleportRandomly();
    }
}
