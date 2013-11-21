package uk.co.shadeddimensions.ep3.world.biome;

import java.util.Random;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenerator;
import uk.co.shadeddimensions.ep3.entity.mob.MobCreeper;
import uk.co.shadeddimensions.ep3.world.worldgen.WorldGenDeadlands;

public class BiomeGenDeadlands extends BiomeGenBase
{
    @SuppressWarnings("unchecked")
    public BiomeGenDeadlands(int par1)
    {
        super(par1);
        theBiomeDecorator.bigMushroomsPerChunk = 0;
        theBiomeDecorator.cactiPerChunk = 0;
        theBiomeDecorator.clayPerChunk = 0;
        theBiomeDecorator.deadBushPerChunk = 0;
        theBiomeDecorator.flowersPerChunk = 0;
        theBiomeDecorator.grassPerChunk = 0;
        theBiomeDecorator.mushroomsPerChunk = 0;
        theBiomeDecorator.reedsPerChunk = 0;
        theBiomeDecorator.sandPerChunk = 0;
        theBiomeDecorator.sandPerChunk2 = 0;
        theBiomeDecorator.treesPerChunk = -999;
        theBiomeDecorator.waterlilyPerChunk = 0;
        theBiomeDecorator.generateLakes = false;

        spawnableCreatureList.clear();
        spawnableCaveCreatureList.clear();
        spawnableMonsterList.clear();
        spawnableWaterCreatureList.clear();

        spawnableMonsterList.add(new SpawnListEntry(MobCreeper.class, 30, 1, 4));

        setDisableRain();
        setBiomeName("Deadlands");
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random par1Random)
    {
        return new WorldGenDeadlands();
    }

    @Override
    public int getBiomeFoliageColor()
    {
        return 0xAA0000;
    }

    @Override
    public int getBiomeGrassColor()
    {
        return 0x770000;
    }

    @Override
    public int getSkyColorByTemp(float par1)
    {
        return 0x000000;
    }

    @Override
    public int getWaterColorMultiplier()
    {
        return 0xFF0000;
    }
}
