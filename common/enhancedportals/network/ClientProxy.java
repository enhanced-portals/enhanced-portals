package enhancedportals.network;

import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
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

    @Override
    public void loadTileEntities()
    {
        super.loadTileEntities();

        //MinecraftForgeClient.registerItemRenderer(BlockIds.DialHomeDevice, new ItemDialDeviceRenderer());
        //MinecraftForgeClient.registerItemRenderer(BlockIds.DialHomeDeviceBasic, new ItemDialDeviceRenderer());
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDialDeviceBasic.class, new TileEntityDialDeviceRenderer());
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDialDevice.class, new TileEntityDialDeviceRenderer());
    }
}
