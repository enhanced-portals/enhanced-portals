package alz.mods.enhancedportals.client;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import alz.mods.enhancedportals.common.CommonProxy;
import alz.mods.enhancedportals.common.LinkData;
import alz.mods.enhancedportals.common.Reference;

public class ClientProxy extends CommonProxy
{
	@Override
	public void setupRenderers()
	{
		MinecraftForgeClient.preloadTexture(Reference.textureLocation);
		MinecraftForgeClient.preloadTexture(Reference.textureItemLocation);
		
		for (int i = 0; i < 16; i++)
		{
			FMLClientHandler.instance().getClient().renderEngine.registerTextureFX(new TextureNetherPortalFX(i));
		}
	}
}
