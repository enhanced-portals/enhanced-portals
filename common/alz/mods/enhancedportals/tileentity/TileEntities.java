package alz.mods.enhancedportals.tileentity;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities
{
	public static void Init()
	{
		SetupTileEntities();
	}

	private static void SetupTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityPortalModifier.class, "epmodif");
		GameRegistry.registerTileEntity(TileEntityPortalModifier.class, "portalModifier");
		GameRegistry.registerTileEntity(TileEntityDialDevice.class, "dialDevice");
	}
}
