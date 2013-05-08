package enhancedportals.network;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy
{
    @Override
    public void loadSettings()
    {
        super.loadSettings();
    }

    @Override
    public void loadTileEntities()
    {
        super.loadTileEntities();
    }
    
    @Override
    public World getWorld(int dimension)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimension);
        }
        else
        {
            return FMLClientHandler.instance().getClient().theWorld;
        }
    }
}
