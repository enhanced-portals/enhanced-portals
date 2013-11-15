package uk.co.shadeddimensions.ep3.world.biome;

import uk.co.shadeddimensions.ep3.network.CommonProxy;

public class Biomes
{
    public static BiomeGenWasteland wasteland;
    
    public static void initBiomes()
    {
        wasteland = new BiomeGenWasteland(CommonProxy.WastelandID);
    }
}
