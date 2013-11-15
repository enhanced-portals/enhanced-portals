package uk.co.shadeddimensions.ep3.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenDeadlands extends WorldGenerator
{
    @Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        for (int var6 = 0; var6 < 64; ++var6)
        {
            int var7 = i + random.nextInt(8) - random.nextInt(8);
            int var8 = j + random.nextInt(4) - random.nextInt(4);
            int var9 = k + random.nextInt(8) - random.nextInt(8);

            if (world.isAirBlock(var7, var8, var9) && world.getBlockId(var7, var8 - 1, var9) == Block.blockClay.blockID || world.getBlockId(var7, var8 - 1, var9) == Block.netherrack.blockID)
            {
                world.setBlock(var7, var8, var9, Block.fire.blockID);
            }
        }

        return true;
    }
}
