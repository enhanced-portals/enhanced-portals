package enhancedportals.world;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class RuinsGenerator implements IWorldGenerator
{
    public RuinsGenerator()
    {

    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.dimensionId == -1)
        {
            generateNether(world, random, chunkX * 16, chunkZ * 16);
        }
        else if (world.provider.dimensionId == 0)
        {
            generateOverworld(world, random, chunkX * 16, chunkZ * 16);
        }
        else if (world.provider.dimensionId > 1)
        {
            generateOverworld(world, random, chunkX * 16, chunkZ * 16);
        }
    }

    private void generateNether(World world, Random rand, int x, int z)
    {

    }

    private void generateOverworld(World world, Random rand, int x, int z)
    {

    }
}
