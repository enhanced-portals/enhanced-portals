package uk.co.shadeddimensions.enhancedportals.network;

import uk.co.shadeddimensions.enhancedportals.client.renderer.PortalFrameRenderer;
import uk.co.shadeddimensions.enhancedportals.client.renderer.TilePortalRenderer;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketGuiString;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ClientProxy extends CommonProxy
{
    public static boolean isWearingGoggles = false;
    public static int renderPass = 0;
    
    public static int portalFrameRenderType;

    @Override
    public void registerRenderers()
    {
        portalFrameRenderType = RenderingRegistry.getNextAvailableRenderId();
        
        RenderingRegistry.registerBlockHandler(new PortalFrameRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePortal.class, new TilePortalRenderer());        
    }

    public static void sendGuiPacket(int i, String s)
    {
        PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiString(i, s)));
    }
}
