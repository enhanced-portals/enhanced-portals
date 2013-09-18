package uk.co.shadeddimensions.enhancedportals.network;

import net.minecraft.network.packet.Packet250CustomPayload;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.block.BlockPortal;
import uk.co.shadeddimensions.enhancedportals.item.ItemPortalFrame;
import uk.co.shadeddimensions.enhancedportals.item.ItemWrench;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketNetworkInterfaceData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalFrameControllerData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalFrameData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalFrameRedstoneData;
import uk.co.shadeddimensions.enhancedportals.portal.networking.NetworkManager;
import uk.co.shadeddimensions.enhancedportals.tileentity.TileEP;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameBiometric;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameDialDevice;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public static BlockFrame blockFrame;
    public static BlockPortal blockPortal;

    public static ItemWrench itemWrench;
    
    public static NetworkManager networkManager;

    public void registerBlocks()
    {
        blockFrame = (BlockFrame) EnhancedPortals.config.registerBlock(BlockFrame.class, ItemPortalFrame.class, "ep2.portalFrame");
        blockPortal = (BlockPortal) EnhancedPortals.config.registerBlock(BlockPortal.class, "ep2.portal");
    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TilePortal.class, "epPortal");
        GameRegistry.registerTileEntity(TilePortalFrame.class, "epPortalFrame");
        GameRegistry.registerTileEntity(TilePortalFrameController.class, "epPortalController");
        GameRegistry.registerTileEntity(TilePortalFrameRedstone.class, "epPortalRedstone");
        GameRegistry.registerTileEntity(TilePortalFrameNetworkInterface.class, "epPortalNI");
        GameRegistry.registerTileEntity(TilePortalFrameDialDevice.class, "epPortalDD");
        GameRegistry.registerTileEntity(TilePortalFrameBiometric.class, "epPortalBiometric");        
    }

    public void registerItems()
    {
        itemWrench = (ItemWrench) EnhancedPortals.config.registerItem(ItemWrench.class, "ep2.wrench");
    }

    public void registerRenderers()
    {

    }

    public static void sendUpdatePacketToAllAround(TileEP tile)
    {
        Packet250CustomPayload packet = null;

        if (tile instanceof TilePortalFrameController)
        {
            packet = MainPacket.makePacket(new PacketPortalFrameControllerData((TilePortalFrameController) tile));
        }
        else if (tile instanceof TilePortalFrameRedstone)
        {
            packet = MainPacket.makePacket(new PacketPortalFrameRedstoneData((TilePortalFrameRedstone) tile));
        }
        else if (tile instanceof TilePortalFrameNetworkInterface)
        {
            packet = MainPacket.makePacket(new PacketNetworkInterfaceData((TilePortalFrameNetworkInterface) tile));
        }
        else if (tile instanceof TilePortalFrame)
        {
            packet = MainPacket.makePacket(new PacketPortalFrameData((TilePortalFrame) tile));
        }
        else if (tile instanceof TilePortal)
        {
            packet = MainPacket.makePacket(new PacketPortalData((TilePortal) tile));
        }

        if (packet != null)
        {
            PacketDispatcher.sendPacketToAllAround(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, 128, tile.worldObj.provider.dimensionId, packet);
        }
    }

    public void setupConfiguration()
    {
        EnhancedPortals.config.addBlock("ep2.portal");
        EnhancedPortals.config.addBlock("ep2.portalFrame");

        EnhancedPortals.config.addItem("ep2.wrench");
        
        EnhancedPortals.config.addBoolean("showExtendedRedstoneInformation", false).addComment("[Redstone Interface] If enabled, shows a description of what the specific redstone mode does.");
        
        EnhancedPortals.config.fillConfigFile(); // Must be last.
    }
}
