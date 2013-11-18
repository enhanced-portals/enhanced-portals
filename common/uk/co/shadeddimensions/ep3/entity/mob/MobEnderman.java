package uk.co.shadeddimensions.ep3.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.portal.EntityManager;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class MobEnderman extends EntityEnderman
{
    final int teleportLimit = 600; // 30 seconds in ticks
    int teleportTimer = -1;

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
    public void onUpdate()
    {
        super.onUpdate();
        
        if (teleportTimer > -1)
        {
            teleportTimer++;

            if (teleportTimer >= teleportLimit)
            {
                teleportTimer = -1;
            }
        }
    }

    @Override
    protected void attackEntity(Entity par1Entity, float par2)
    {
        if (teleportTimer == -1)
        {
            if (rand.nextInt(100) <= 9)
            {
                EntityManager.teleportEntityToDimension(par1Entity);
                teleportRandomly();
            }
            else
            {
                int x = (int) (posX + (rand.nextBoolean() ? -rand.nextInt(32) : rand.nextInt(32))), y = 0, z = (int) (posZ + (rand.nextBoolean() ? -rand.nextInt(32) : rand.nextInt(32)));                
                y = worldObj.getTopSolidOrLiquidBlock(x, z);                
                EntityManager.teleportEntity(par1Entity, new WorldCoordinates(x, y, z, worldObj.provider.dimensionId));
                teleportTo(x + (rand.nextBoolean() ? -rand.nextInt(2) : rand.nextInt(2)), y, z + (rand.nextBoolean() ? -rand.nextInt(2) : rand.nextInt(2)));
            }

            teleportTimer++;
        }
        else
        {
            super.attackEntity(par1Entity, par2);
            teleportRandomly();
        }
    }
}
