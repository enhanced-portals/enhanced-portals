package uk.co.shadeddimensions.enhancedportals.network;

import net.minecraftforge.client.MinecraftForgeClient;
import uk.co.shadeddimensions.enhancedportals.client.renderer.PortalFrameItemRenderer;
import uk.co.shadeddimensions.enhancedportals.client.renderer.PortalItemRenderer;
import uk.co.shadeddimensions.enhancedportals.client.renderer.TilePortalFrameRenderer;
import uk.co.shadeddimensions.enhancedportals.client.renderer.TilePortalRenderer;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketGuiString;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ClientProxy extends CommonProxy
{
    public static boolean isWearingGoggles = false;
    public static int renderPass = 0;
    
    @Override
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TilePortal.class, new TilePortalRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePortalFrame.class, new TilePortalFrameRenderer());
        
        MinecraftForgeClient.registerItemRenderer(blockPortal.blockID, new PortalItemRenderer());
        MinecraftForgeClient.registerItemRenderer(blockFrame.blockID, new PortalFrameItemRenderer());
    }

    public static void sendGuiPacket(int i, String s)
    {
        PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiString(i, s)));
    }
}
