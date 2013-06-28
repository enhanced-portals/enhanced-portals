package enhancedportals.world;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import enhancedportals.lib.BlockIds;

public class RuinsGenerator implements IWorldGenerator
{
    public int GENERATION_CHANCE = 10;
    private String[][][] smallStructures = { 
            { { "49 49 49 49 49" }, { "49 PORTAL:4 PORTAL:5 PORTAL:4 49" }, { "49 PORTAL:4 PORTAL:4 PORTAL:4 49" }, { "49 49 49 49 49" } }
    };
    
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
        if (rand.nextInt(GENERATION_CHANCE) != 0)
        {
            return;
        }
        
        generateStructure(smallStructures[0], world, x, 100, z);
    }
    
    private void generateStructure(String[][] string, World world, int xCoord, int yCoord, int zCoord)
    {        
        for (int y = 0; y < string.length; y++)
        {
            for (int x = 0; x < string[y].length; x++)
            {
                int z = 0, blockID = 0, blockMeta = 0;
                String[] blocks = string[y][x].split(" ");
                
                for (String str : blocks)
                {
                    if (str.contains(":"))
                    {
                        String s = str.split(":")[0];
                        
                        if (s.equals("PORTAL"))
                        {
                            s = "" + BlockIds.NetherPortal;
                        }
                        
                        blockID = Integer.parseInt(s);
                        blockMeta = Integer.parseInt(str.split(":")[1]);
                    }
                    else
                    {
                        blockID = Integer.parseInt(str);
                        blockMeta = 0;
                    }
                    
                    world.setBlock(xCoord + x, yCoord + y, zCoord + z, blockID, blockMeta, 0);
                    z++;
                }
            }
        }
    }
}
