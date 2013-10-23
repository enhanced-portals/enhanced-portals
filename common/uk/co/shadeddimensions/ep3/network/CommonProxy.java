package uk.co.shadeddimensions.ep3.network;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.block.BlockPortal;
import uk.co.shadeddimensions.ep3.block.BlockStabilizer;
import uk.co.shadeddimensions.ep3.item.ItemFrame;
import uk.co.shadeddimensions.ep3.item.ItemGoggles;
import uk.co.shadeddimensions.ep3.item.ItemLocationCard;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.item.ItemPortalModule;
import uk.co.shadeddimensions.ep3.item.ItemWrench;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.packet.PacketTileUpdate;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;
import uk.co.shadeddimensions.ep3.tileentity.TileFrame;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
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
    public static BlockStabilizer blockStabilizer;

    public static ItemWrench itemWrench;
    public static ItemPaintbrush itemPaintbrush;    
    public static ItemGoggles itemGoggles;
    public static ItemPortalModule itemUpgrade;
    public static ItemLocationCard itemLocationCard;

    public static NetworkManager networkManager;

    public static final Logger logger = Logger.getLogger(Reference.NAME);

    public static void sendUpdatePacketToPlayer(TileEnhancedPortals tile, EntityPlayer player)
    {
        if (CommonProxy.isClient())
        {
            return;
        }
        
        PacketDispatcher.sendPacketToPlayer(new PacketTileUpdate(tile).getPacket(), (Player) player);
    }
    
    public static void sendUpdatePacketToAllAround(TileEnhancedPortals tile)
    {
        if (CommonProxy.isClient())
        {
            return;
        }
        
        sendPacketToAllAround(tile, new PacketTileUpdate(tile).getPacket());
    }
    
    public static void sendPacketToAllAround(TileEntity tile, Packet250CustomPayload packet)
    {
        if (CommonProxy.isClient())
        {
            return;
        }
        
        PacketDispatcher.sendPacketToAllAround(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, 128, tile.worldObj.provider.dimensionId, packet);
    }

    public File getBaseDir()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getFile(".");
    }

    public File getResourcePacksDir()
    {
        return new File(getBaseDir(), "resourcepacks");
    }
    
    public File getWorldDir()
    {
        return new File(getBaseDir(), DimensionManager.getWorld(0).getSaveHandler().getWorldDirectoryName());
    }

    public void registerBlocks()
    {
        blockFrame = (BlockFrame) EnhancedPortals.config.registerBlock(BlockFrame.class, ItemFrame.class, Reference.SHORT_ID + ".portalFrame");
        blockPortal = (BlockPortal) EnhancedPortals.config.registerBlock(BlockPortal.class, Reference.SHORT_ID + ".portal");
        blockStabilizer = (BlockStabilizer) EnhancedPortals.config.registerBlock(BlockStabilizer.class, Reference.SHORT_ID + ".stabilizer");
    }

    public void registerItems()
    {
        itemWrench = (ItemWrench) EnhancedPortals.config.registerItem(ItemWrench.class, Reference.SHORT_ID + ".wrench");
        itemPaintbrush = (ItemPaintbrush) EnhancedPortals.config.registerItem(ItemPaintbrush.class, Reference.SHORT_ID + ".paintbrush");
        itemGoggles = (ItemGoggles) EnhancedPortals.config.registerItem(ItemGoggles.class, Reference.SHORT_ID + ".goggles");
        itemLocationCard = (ItemLocationCard) EnhancedPortals.config.registerItem(ItemLocationCard.class, Reference.SHORT_ID + ".locationCard");
        itemUpgrade = (ItemPortalModule) EnhancedPortals.config.registerItem(ItemPortalModule.class, Reference.SHORT_ID + ".portalModule");
    }

    public void registerRenderers()
    {

    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TilePortal.class, "epPortal");
        GameRegistry.registerTileEntity(TileFrame.class, "epPortalFrame");
        GameRegistry.registerTileEntity(TilePortalController.class, "epPortalController");
        GameRegistry.registerTileEntity(TileRedstoneInterface.class, "epRedstoneInterface");
        GameRegistry.registerTileEntity(TileNetworkInterface.class, "epNetworkInterface");
        GameRegistry.registerTileEntity(TileDiallingDevice.class, "epDiallingDevice");
        GameRegistry.registerTileEntity(TileBiometricIdentifier.class, "epBiometricIdentifier");
        GameRegistry.registerTileEntity(TileModuleManipulator.class, "epModuleManipulator");
        GameRegistry.registerTileEntity(TileStabilizer.class, "epStabilizer");
    }

    public void setupConfiguration()
    {
        EnhancedPortals.config.addBlock(Reference.SHORT_ID + ".portal");
        EnhancedPortals.config.addBlock(Reference.SHORT_ID + ".portalFrame");
        EnhancedPortals.config.addBlock(Reference.SHORT_ID + ".stabilizer");

        EnhancedPortals.config.addItem(Reference.SHORT_ID + ".wrench");
        EnhancedPortals.config.addItem(Reference.SHORT_ID + ".goggles");
        EnhancedPortals.config.addItem(Reference.SHORT_ID + ".paintbrush");
        EnhancedPortals.config.addItem(Reference.SHORT_ID + ".locationCard");
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

    public void miscSetup()
    {
        
    }
}
