package uk.co.shadeddimensions.enhancedportals.network;

import uk.co.shadeddimensions.enhancedportals.client.renderer.TilePortalRenderer;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TilePortal.class, new TilePortalRenderer());
    }
}
