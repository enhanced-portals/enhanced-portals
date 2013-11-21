package uk.co.shadeddimensions.ep3.world;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.world.biome.Biomes;

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
        worldChunkMgr = new WorldChunkManagerHell(Biomes.wasteland, 0.8F, 0.1F);
        dimensionId = CommonProxy.Dimension;
    }

    @Override
    public IChunkProvider createChunkGenerator()
    {
        return new EPChunkProvider(worldObj, worldObj.getSeed());
    }
}
