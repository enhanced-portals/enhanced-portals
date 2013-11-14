package uk.co.shadeddimensions.ep3.world;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import uk.co.shadeddimensions.ep3.network.CommonProxy;

public class EPWorldProvider extends WorldProvider
{
    @Override
    public String getDimensionName()
    {
        return "EP Dimension Test";
    }

    @Override
    protected void registerWorldChunkManager()
    {
        worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.8F, 0.1F);
        dimensionId = CommonProxy.Dimension;
    }

    @Override
    public IChunkProvider createChunkGenerator()
    {
        return new EPChunkProvider(worldObj, worldObj.getSeed(), false);
    }
}