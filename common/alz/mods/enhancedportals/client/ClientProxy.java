package alz.mods.enhancedportals.client;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import alz.mods.enhancedportals.common.CommonProxy;
import alz.mods.enhancedportals.reference.IO;

public class ClientProxy extends CommonProxy
{
	@Override
	public void setupRenderers()
	{
		MinecraftForgeClient.preloadTexture(IO.TerrainPath);
		MinecraftForgeClient.preloadTexture(IO.ItemsPath);
		
		for (int i = 0; i < 16; i++)
		{
			FMLClientHandler.instance().getClient().renderEngine.registerTextureFX(new TextureNetherPortalFX(i));
		}
	}
}
