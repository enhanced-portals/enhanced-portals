package uk.co.shadeddimensions.enhancedportals.network;

import net.minecraftforge.client.MinecraftForgeClient;
import uk.co.shadeddimensions.enhancedportals.client.renderer.PortalFrameItemRenderer;
import uk.co.shadeddimensions.enhancedportals.client.renderer.PortalFrameRenderer;
import uk.co.shadeddimensions.enhancedportals.client.renderer.PortalItemRenderer;
import uk.co.shadeddimensions.enhancedportals.client.renderer.PortalRenderer;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketGuiString;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ClientProxy extends CommonProxy
{
    public static boolean isWearingGoggles = false;
    public static int renderPass = 0;
    
    public static int portalFrameRenderType;
    public static int portalRenderType;

    @Override
    public void registerRenderers()
    {
        portalRenderType = RenderingRegistry.getNextAvailableRenderId();
        portalFrameRenderType = RenderingRegistry.getNextAvailableRenderId();
        
        RenderingRegistry.registerBlockHandler(new PortalRenderer());
        RenderingRegistry.registerBlockHandler(new PortalFrameRenderer());
        
        MinecraftForgeClient.registerItemRenderer(blockPortal.blockID, new PortalItemRenderer());
        MinecraftForgeClient.registerItemRenderer(blockFrame.blockID, new PortalFrameItemRenderer());
    }

    public static void sendGuiPacket(int i, String s)
    {
        PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiString(i, s)));
    }
}
