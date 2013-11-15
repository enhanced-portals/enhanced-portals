package uk.co.shadeddimensions.ep3.world.biome;

import uk.co.shadeddimensions.ep3.network.CommonProxy;

public class Biomes
{
    public static BiomeGenDeadlands wasteland;
    
    public static void initBiomes()
    {
        wasteland = new BiomeGenDeadlands(CommonProxy.WastelandID);
    }
}
