package uk.co.shadeddimensions.ep3.world.biome;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenWasteland extends BiomeGenBase
{    
    public BiomeGenWasteland(int par1)
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
        theBiomeDecorator.treesPerChunk = 0;
        theBiomeDecorator.waterlilyPerChunk = 0;
        
        setDisableRain();
        setBiomeName("Wasteland");
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
