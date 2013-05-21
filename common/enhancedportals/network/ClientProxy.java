package enhancedportals.network;

import net.minecraft.item.ItemDye;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import enhancedportals.client.renderer.ItemDialDeviceRenderer;
import enhancedportals.client.renderer.TileEntityDialDeviceRenderer;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Textures;
import enhancedportals.portal.PortalTexture;
import enhancedportals.tileentity.TileEntityDialDevice;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;

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

        MinecraftForgeClient.registerItemRenderer(BlockIds.DialHomeDevice, new ItemDialDeviceRenderer());
        MinecraftForgeClient.registerItemRenderer(BlockIds.DialHomeDeviceBasic, new ItemDialDeviceRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDialDeviceBasic.class, new TileEntityDialDeviceRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDialDevice.class, new TileEntityDialDeviceRenderer());
    }
    
    @Override
    public void registerIcons(TextureStitchEvent.Pre event)
    {        
        if (event.map == FMLClientHandler.instance().getClient().renderEngine.textureMapBlocks)
        {
            for (int i = 0; i < 16; i++)
            {
                Textures.portalTextureMap.put("C:" + i, new PortalTexture("C:" + i, event.map.registerIcon(Reference.MOD_ID + ":netherPortal_" + i), event.map.registerIcon(Reference.MOD_ID + ":portalModifier_active_" + i), ItemDye.dyeColors[i]));
            }
        }
    }
}
