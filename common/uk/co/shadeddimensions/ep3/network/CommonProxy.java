package uk.co.shadeddimensions.ep3.network;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.block.BlockPortal;
import uk.co.shadeddimensions.ep3.block.material.FrameMaterial;
import uk.co.shadeddimensions.ep3.item.ItemGoggles;
import uk.co.shadeddimensions.ep3.item.ItemPortalFrame;
import uk.co.shadeddimensions.ep3.item.ItemPortalModule;
import uk.co.shadeddimensions.ep3.item.ItemWrench;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.packet.PacketTileUpdate;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;
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
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public static BlockFrame blockFrame;
    public static BlockPortal blockPortal;

    public static ItemWrench itemWrench;
    public static ItemGoggles itemGoggles;
    public static ItemPortalModule itemUpgrade;

    public static NetworkManager networkManager;
    
    public static FrameMaterial frameMaterial = new FrameMaterial();

    public static final Logger logger = Logger.getLogger(Reference.NAME);

    public static void sendUpdatePacketToPlayer(TileEnhancedPortals tile, EntityPlayer player)
    {
        PacketDispatcher.sendPacketToPlayer(new PacketTileUpdate(tile).getPacket(), (Player) player);
    }
    
    public static void sendUpdatePacketToAllAround(TileEnhancedPortals tile)
    {
        PacketDispatcher.sendPacketToAllAround(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, 128, tile.worldObj.provider.dimensionId, new PacketTileUpdate(tile).getPacket());
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

        EnhancedPortals.config.addInteger("powerCostMultiplier", 1).addComment("Multiplies all power requirements by this value");
        
        EnhancedPortals.config.fillConfigFile(); // Must be last.
    }
    
    public static boolean isClient()
    {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }
    
    public static boolean isServer()
    {
        return !isClient();
    }
}
