package enhancedportals.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public class PortalCreationFX extends EntityFX
{
    public PortalCreationFX(World world, double x, double y, double z)
    {
        super(world, x + 0.5, y, z + 0.5);
        setParticleTextureIndex(7);
        motionX = (rand.nextBoolean() ? rand.nextDouble() : -rand.nextDouble()) / 2.0;
        motionY = rand.nextDouble() / 2.0;
        motionZ = (rand.nextBoolean() ? rand.nextDouble() : -rand.nextDouble()) / 2.0;
        noClip = true;
    }
}
