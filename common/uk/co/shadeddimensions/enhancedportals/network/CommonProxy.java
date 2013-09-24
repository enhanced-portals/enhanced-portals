package uk.co.shadeddimensions.enhancedportals.network;

import java.io.File;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.DimensionManager;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.block.BlockPortal;
import uk.co.shadeddimensions.enhancedportals.item.ItemPortalFrame;
import uk.co.shadeddimensions.enhancedportals.item.ItemWrench;
import uk.co.shadeddimensions.enhancedportals.lib.Reference;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketNetworkInterfaceData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalControllerData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalFrameData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketRedstoneInterfaceData;
import uk.co.shadeddimensions.enhancedportals.portal.NetworkManager;
import uk.co.shadeddimensions.enhancedportals.tileentity.TileEP;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileRedstoneInterface;
import cpw.mods.fml.common.FMLCommonHandler;
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
        blockFrame = (BlockFrame) EnhancedPortals.config.registerBlock(BlockFrame.class, ItemPortalFrame.class, Reference.SHORT_ID + ".portalFrame");
        blockPortal = (BlockPortal) EnhancedPortals.config.registerBlock(BlockPortal.class, Reference.SHORT_ID + ".portal");
    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TilePortal.class, "epPortal");
        GameRegistry.registerTileEntity(TilePortalFrame.class, "epPortalFrame");
        GameRegistry.registerTileEntity(TilePortalController.class, "epPortalController");
        GameRegistry.registerTileEntity(TileRedstoneInterface.class, "epRedstoneInterface");
        GameRegistry.registerTileEntity(TileNetworkInterface.class, "epNetworkInterface");
        GameRegistry.registerTileEntity(TileDiallingDevice.class, "epDiallingDevice");
        GameRegistry.registerTileEntity(TileBiometricIdentifier.class, "epBiometricIdentifier");
        GameRegistry.registerTileEntity(TileModuleManipulator.class, "epModuleManipulator");
    }

    public void registerItems()
    {
        itemWrench = (ItemWrench) EnhancedPortals.config.registerItem(ItemWrench.class, Reference.SHORT_ID + ".wrench");
    }

    public void registerRenderers()
    {

    }

    public static void sendUpdatePacketToAllAround(TileEP tile)
    {
        Packet250CustomPayload packet = null;

        if (tile instanceof TilePortalController)
        {
            packet = MainPacket.makePacket(new PacketPortalControllerData((TilePortalController) tile));
        }
        else if (tile instanceof TileRedstoneInterface)
        {
            packet = MainPacket.makePacket(new PacketRedstoneInterfaceData((TileRedstoneInterface) tile));
        }
        else if (tile instanceof TileNetworkInterface)
        {
            packet = MainPacket.makePacket(new PacketNetworkInterfaceData((TileNetworkInterface) tile));
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
        EnhancedPortals.config.addBlock(Reference.SHORT_ID + ".portal");
        EnhancedPortals.config.addBlock(Reference.SHORT_ID + ".portalFrame");

        EnhancedPortals.config.addItem(Reference.SHORT_ID + ".wrench");
        
        EnhancedPortals.config.addBoolean("showExtendedRedstoneInformation", false).addComment("[Redstone Interface] If enabled, shows a description of what the specific redstone mode does.");
        EnhancedPortals.config.addBoolean("randomTeleportMode", false).addComment("[Network Interface] If enabled, changes the default sequential teleport mode to random.");
        EnhancedPortals.config.addBoolean("customNetherPortals", false).addComment("If enabled, overwrites the Nether portals mechanics to allow any shape/size.");
        
        EnhancedPortals.config.fillConfigFile(); // Must be last.
    }
    
    public File getBaseDir()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getFile(".");
    }
    
    public File getWorldDir()
    {
        return new File(getBaseDir(), DimensionManager.getWorld(0).getSaveHandler().getWorldDirectoryName());
    }
}
