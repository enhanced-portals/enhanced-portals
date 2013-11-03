package uk.co.shadeddimensions.ep3.network;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
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
import uk.co.shadeddimensions.ep3.item.ItemStabilizer;
import uk.co.shadeddimensions.ep3.item.ItemWrench;
import uk.co.shadeddimensions.ep3.lib.GUIs;
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
    static final int START_BLOCK_ID = 512, START_ITEM_ID = 5000, MAX_BLOCK_ID = 4095, MAX_ITEM_ID = 32000;
    
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
    public static Configuration config;
    
    int portalID, frameID, stabilizerID; // Block IDs 
    int wrenchID, gogglesID, paintbrushID, locationCardID, portalModuleID; // Item IDs
    public static boolean showExtendedRedstoneInformation, customNetherPortals, portalsDestroyBlocks, fasterPortalCooldown, requirePower; // Bools
    public static double buildCraftPowerMultiplier, industrialCraftPowerMultiplier, thermalExpansionPowerMultiplier; // Doubles

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
        blockFrame = new BlockFrame(frameID, "ep3.portalFrame");
        GameRegistry.registerBlock(blockFrame, ItemFrame.class, "ep3.portalFrame");
        
        blockPortal = new BlockPortal(portalID, "ep3.portal");
        GameRegistry.registerBlock(blockPortal, "ep3.portal");
        
        blockStabilizer = new BlockStabilizer(stabilizerID, "ep3.stabilizer");
        GameRegistry.registerBlock(blockStabilizer, ItemStabilizer.class, "ep3.stabilizer");
}

    public void registerItems()
    {
        itemWrench = new ItemWrench(wrenchID, "ep3.wrench");
        GameRegistry.registerItem(itemWrench, "ep3.wrench");
        
        itemPaintbrush = new ItemPaintbrush(paintbrushID, "ep3.paintbrush");
        GameRegistry.registerItem(itemPaintbrush, "ep3.paintbrush");
        
        itemGoggles = new ItemGoggles(gogglesID, "ep3.goggles");
        GameRegistry.registerItem(itemGoggles, "ep3.goggles");
        
        itemLocationCard = new ItemLocationCard(locationCardID, "ep3.locationCard");
        GameRegistry.registerItem(itemLocationCard, "ep3.locationCard");
        
        itemUpgrade = new ItemPortalModule(portalModuleID, "ep3.portalModule");
        GameRegistry.registerItem(itemUpgrade, "ep3.portalModule");
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
        config.load();
        
        ////////////// BLOCK IDS /////////////////
        portalID = resolveFreeBlockID("Portal");
        frameID = resolveFreeBlockID("Frame");
        stabilizerID = resolveFreeBlockID("DimensionalBridgeStabilizer");
        
        ////////////// ITEM IDS /////////////////
        wrenchID = resolveFreeItemID("Wrench");
        gogglesID = resolveFreeItemID("Glasses");
        paintbrushID = resolveFreeItemID("Paintbrush");
        locationCardID  = resolveFreeItemID("LocationCard");
        portalModuleID = resolveFreeItemID("PortalModule");
        
        ///////////////// REDSTONE INTERFACE /////////////////
        showExtendedRedstoneInformation = config.get("Redstone Interface", "ShowExtendedRedstoneInformation", false, "If enabled, shows a description of what the specific redstone mode does").getBoolean(true);
        
        ///////////////// VANILLA OVERRIDES /////////////////
        customNetherPortals = config.get("Vanilla Overrides", "CustomNetherPortals", false, "If enabled, overwrites the Nether portals mechanics to allow any shape/size/horizontal portals").getBoolean(false);
        
        ///////////////// PORTAL /////////////////
        portalsDestroyBlocks = config.get("Portal", "PortalsDestroyBlocks", true, "Portals will destroy blocks that are in their way, if this is enabled. Only applies to blocks placed inside the frame AFTER the portal has been initialized").getBoolean(true);
        fasterPortalCooldown = config.get("Portal", "FasterPortalCooldown", false, "Sets every entities portal cooldown period to 10 ticks (The same as a player). Boats, Minecarts and Horses will always be forced to 10 ticks regardless of this setting. Only affects mobs using EP portals").getBoolean(false);
        
        ///////////////// POWER /////////////////
        requirePower = config.get("Power", "RequirePower", true).getBoolean(true);
        buildCraftPowerMultiplier = config.get("Power", "BuildCraftPowerMultiplier", 1.0).getDouble(1.0);
        industrialCraftPowerMultiplier = config.get("Power", "IndustrialCraftPowerMultiplier", 1.0).getDouble(1.0);
        thermalExpansionPowerMultiplier = config.get("Power", "ThermalExpansionPowerMultiplier", 1.0).getDouble(1.0);
        
        ///////////////// CATEGORY COMMENTS /////////////////
        config.addCustomCategoryComment("Power", "All power multipliers must be 0.0 or higher.");
        
        config.save();
    }
    
    int resolveFreeBlockID(String entry)
    {
        if (!config.hasKey("Block", entry))
        {
            for (int i = START_BLOCK_ID; i < MAX_BLOCK_ID; i++)
            {
                if (Block.blocksList[i] == null && !hasUsed(0, i))
                {
                    setUsed(0, i);
                    return i;
                }
            }
        }
        else
        {
            return config.getCategory("Block").get(entry).getInt();
        }
        
        return -1;
    }
    
    int resolveFreeItemID(String entry)
    {
        if (!config.hasKey("Item", entry))
        {
            for (int i = START_ITEM_ID; i < MAX_ITEM_ID; i++)
            {
                if (Item.itemsList[i] == null && !hasUsed(1, i))
                {
                    setUsed(1, i);
                    return i;
                }
            }
        }
        else
        {
            return config.getCategory("Item").get(entry).getInt();
        }
        
        return -1;
    }
    
    ArrayList<Integer> usedBlocks = new ArrayList<Integer>();
    ArrayList<Integer> usedItems = new ArrayList<Integer>();
    
    boolean hasUsed(int i, int j)
    {
        if (i == 0)
        {
            return usedBlocks.contains(j);
        }
        else if (i == 1)
        {
            return usedItems.contains(j);
        }
                
        return false;
    }
    
    void setUsed(int i, int j)
    {
        if (i == 0)
        {
            usedBlocks.add(j);
        }
        else if (i == 1)
        {
            usedItems.add(j);
        }
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
    
    public static void openGui(EntityPlayer player, GUIs gui, TileEnhancedPortals tile)
    {
        openGui(player, gui.ordinal(), tile);
    }
    
    public static void openGui(EntityPlayer player, int id, TileEnhancedPortals tile)
    {        
        player.openGui(EnhancedPortals.instance, id, tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
    }
}
