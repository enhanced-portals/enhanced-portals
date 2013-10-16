package uk.co.shadeddimensions.ep3.network;

import java.io.File;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.DimensionManager;
import uk.co.shadeddimensions.ep3.client.renderer.PortalFrameItemRenderer;
import uk.co.shadeddimensions.ep3.client.renderer.PortalItemRenderer;
import uk.co.shadeddimensions.ep3.client.renderer.TilePortalFrameRenderer;
import uk.co.shadeddimensions.ep3.network.packet.PacketGuiData;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ClientProxy extends CommonProxy
{
    public static boolean isWearingGoggles = false;

    @Override
    public File getWorldDir()
    {
        return new File(getBaseDir(), "saves/" + DimensionManager.getWorld(0).getSaveHandler().getWorldDirectoryName());
    }

    @Override
    public void registerRenderers()
    {
        //ClientRegistry.bindTileEntitySpecialRenderer(TilePortal.class, new TilePortalRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePortalFrame.class, new TilePortalFrameRenderer());

        MinecraftForgeClient.registerItemRenderer(blockPortal.blockID, new PortalItemRenderer());
        MinecraftForgeClient.registerItemRenderer(blockFrame.blockID, new PortalFrameItemRenderer());
    }

    public static void sendGuiPacket(GuiPayload payload)
    {
        PacketDispatcher.sendPacketToServer(new PacketGuiData(payload).getPacket());
    }
}
