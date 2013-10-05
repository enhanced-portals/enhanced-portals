package uk.co.shadeddimensions.ep3.network;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.DimensionManager;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.block.BlockPortal;
import uk.co.shadeddimensions.ep3.item.ItemGoggles;
import uk.co.shadeddimensions.ep3.item.ItemPortalFrame;
import uk.co.shadeddimensions.ep3.item.ItemPortalModule;
import uk.co.shadeddimensions.ep3.item.ItemWrench;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.packet.MainPacket;
import uk.co.shadeddimensions.ep3.network.packet.PacketModuleManipulator;
import uk.co.shadeddimensions.ep3.network.packet.PacketNetworkInterfaceData;
import uk.co.shadeddimensions.ep3.network.packet.PacketPortalControllerData;
import uk.co.shadeddimensions.ep3.network.packet.PacketPortalData;
import uk.co.shadeddimensions.ep3.network.packet.PacketPortalFrameData;
import uk.co.shadeddimensions.ep3.network.packet.PacketRedstoneInterfaceData;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.tileentity.TileEP;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public static BlockFrame blockFrame;
    public static BlockPortal blockPortal;

    public static ItemWrench itemWrench;
    public static ItemGoggles itemGoggles;
    public static ItemPortalModule itemUpgrade;

    public static NetworkManager networkManager;

    public static final Logger logger = Logger.getLogger(Reference.NAME);

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
        else if (tile instanceof TileDiallingDevice)
        {

        }
        else if (tile instanceof TileModuleManipulator)
        {
            packet = MainPacket.makePacket(new PacketModuleManipulator((TileModuleManipulator) tile));
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

    public File getBaseDir()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getFile(".");
    }

    public File getWorldDir()
    {
        return new File(getBaseDir(), DimensionManager.getWorld(0).getSaveHandler().getWorldDirectoryName());
    }

    public void registerBlocks()
    {
        blockFrame = (BlockFrame) EnhancedPortals.config.registerBlock(BlockFrame.class, ItemPortalFrame.class, Reference.SHORT_ID + ".portalFrame");
        blockPortal = (BlockPortal) EnhancedPortals.config.registerBlock(BlockPortal.class, Reference.SHORT_ID + ".portal");
    }

    public void registerItems()
    {
        itemWrench = (ItemWrench) EnhancedPortals.config.registerItem(ItemWrench.class, Reference.SHORT_ID + ".wrench");
        itemGoggles = (ItemGoggles) EnhancedPortals.config.registerItem(ItemGoggles.class, Reference.SHORT_ID + ".goggles");
        itemUpgrade = (ItemPortalModule) EnhancedPortals.config.registerItem(ItemPortalModule.class, Reference.SHORT_ID + ".portalModule");
    }

    public void registerRenderers()
    {

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

    public void setupConfiguration()
    {
        EnhancedPortals.config.addBlock(Reference.SHORT_ID + ".portal");
        EnhancedPortals.config.addBlock(Reference.SHORT_ID + ".portalFrame");

        EnhancedPortals.config.addItem(Reference.SHORT_ID + ".wrench");
        EnhancedPortals.config.addItem(Reference.SHORT_ID + ".goggles");
        EnhancedPortals.config.addItem(Reference.SHORT_ID + ".portalModule");

        EnhancedPortals.config.addBoolean("showExtendedRedstoneInformation", false).addComment("[Redstone Interface] If enabled, shows a description of what the specific redstone mode does.");
        EnhancedPortals.config.addBoolean("randomTeleportMode", false).addComment("[Network Interface] If enabled, changes the default sequential teleport mode to random.");
        EnhancedPortals.config.addBoolean("customNetherPortals", false).addComment("[NI] If enabled, overwrites the Nether portals mechanics to allow any shape/size.");
        EnhancedPortals.config.addBoolean("portalsDestroyBlocks", true).addComment("[NI - Forced ON] Portals will destroy blocks that are in their way, if this is enabled. Only applies to blocks placed inside the frame AFTER the portal has been initialized.");

        EnhancedPortals.config.fillConfigFile(); // Must be last.
    }
}
