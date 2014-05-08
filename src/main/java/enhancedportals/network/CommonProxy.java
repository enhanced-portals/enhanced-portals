package enhancedportals.network;

import java.io.File;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.EnhancedPortals;
import enhancedportals.block.BlockFrame;
import enhancedportals.block.BlockPortal;
import enhancedportals.block.BlockStabilizer;
import enhancedportals.block.BlockStabilizerEmpty;
import enhancedportals.block.decoration.BlockDimensionalBridgeStabilizer;
import enhancedportals.block.decoration.BlockPortalFrame;
import enhancedportals.item.ItemBlankPortalModule;
import enhancedportals.item.ItemBlankUpgrade;
import enhancedportals.item.ItemFrame;
import enhancedportals.item.ItemGlasses;
import enhancedportals.item.ItemGuide;
import enhancedportals.item.ItemLocationCard;
import enhancedportals.item.ItemPaintbrush;
import enhancedportals.item.ItemPortalModule;
import enhancedportals.item.ItemStabilizer;
import enhancedportals.item.ItemUpgrade;
import enhancedportals.item.ItemWrench;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.network.packet.PacketRequestGui;
import enhancedportals.network.packet.PacketRerender;
import enhancedportals.network.packet.PacketTextureData;
import enhancedportals.portal.NetworkManager;
import enhancedportals.tileentity.TileStabilizer;
import enhancedportals.tileentity.TileStabilizerMain;
import enhancedportals.tileentity.portal.TileController;
import enhancedportals.tileentity.portal.TileDiallingDevice;
import enhancedportals.tileentity.portal.TileFrameBasic;
import enhancedportals.tileentity.portal.TileModuleManipulator;
import enhancedportals.tileentity.portal.TileNetworkInterface;
import enhancedportals.tileentity.portal.TilePortal;
import enhancedportals.tileentity.portal.TileProgrammableInterface;
import enhancedportals.tileentity.portal.TileRedstoneInterface;
import enhancedportals.tileentity.portal.TileTransferEnergy;
import enhancedportals.tileentity.portal.TileTransferFluid;
import enhancedportals.tileentity.portal.TileTransferItem;

public class CommonProxy
{
    public static final int REDSTONE_FLUX_COST = 10000, REDSTONE_FLUX_TIMER = 20, RF_PER_MJ = 10;
    public int gogglesRenderIndex = 0;
    public NetworkManager networkManager;
    public static boolean forceShowFrameOverlays, disableSounds, disableParticles, portalsDestroyBlocks, fasterPortalCooldown, requirePower;
    public static double powerMultiplier, powerStorageMultiplier;
    public static int activePortalsPerRow = 2;
    static Configuration config;
    static File craftingDir;

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

    public void miscSetup()
    {
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(ItemPortalModule.instance, 1, 4), 1, 1, 2));
    }

    public void registerBlocks()
    {
        GameRegistry.registerBlock(new BlockFrame("frame"), ItemFrame.class, "frame");
        GameRegistry.registerBlock(new BlockPortal("portal"), "portal");
        GameRegistry.registerBlock(new BlockStabilizer("dbs"), ItemStabilizer.class, "dbs");
        GameRegistry.registerBlock(new BlockPortalFrame("decor_frame"), "decor_frame");
        GameRegistry.registerBlock(new BlockDimensionalBridgeStabilizer("decor_dbs"), "decor_dbs");
        GameRegistry.registerBlock(new BlockStabilizerEmpty("dbs_empty"), "dbs_empty");
    }

    public void registerItems()
    {
        GameRegistry.registerItem(new ItemWrench("wrench"), "wrench");
        GameRegistry.registerItem(new ItemPaintbrush("nanobrush"), "nanobrush");
        GameRegistry.registerItem(new ItemGlasses("glasses"), "glasses");
        GameRegistry.registerItem(new ItemLocationCard("location_card"), "location_card");
        GameRegistry.registerItem(new ItemPortalModule("portal_module"), "portal_module");
        GameRegistry.registerItem(new ItemUpgrade("upgrade"), "upgrade");
        GameRegistry.registerItem(new ItemBlankPortalModule("blank_portal_module"), "blank_portal_module");
        GameRegistry.registerItem(new ItemBlankUpgrade("blank_upgrade"), "blank_upgrade");
        GameRegistry.registerItem(new ItemGuide("guide"), "guide");
    }

    public void registerPackets()
    {
        EnhancedPortals.packetPipeline.registerPacket(PacketRequestGui.class);
        EnhancedPortals.packetPipeline.registerPacket(PacketTextureData.class);
        EnhancedPortals.packetPipeline.registerPacket(PacketRerender.class);
        EnhancedPortals.packetPipeline.registerPacket(PacketGuiData.class);
        EnhancedPortals.packetPipeline.registerPacket(PacketGui.class);
    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TilePortal.class, "epP");
        GameRegistry.registerTileEntity(TileFrameBasic.class, "epF");
        GameRegistry.registerTileEntity(TileController.class, "epPC");
        GameRegistry.registerTileEntity(TileRedstoneInterface.class, "epRI");
        GameRegistry.registerTileEntity(TileNetworkInterface.class, "epNI");
        GameRegistry.registerTileEntity(TileDiallingDevice.class, "epDD");
        GameRegistry.registerTileEntity(TileProgrammableInterface.class, "epPI");
        GameRegistry.registerTileEntity(TileModuleManipulator.class, "epMM");
        GameRegistry.registerTileEntity(TileStabilizer.class, "epDBS");
        GameRegistry.registerTileEntity(TileStabilizerMain.class, "epDBSM");
        GameRegistry.registerTileEntity(TileTransferEnergy.class, "epTE");
        GameRegistry.registerTileEntity(TileTransferFluid.class, "epTF");
        GameRegistry.registerTileEntity(TileTransferItem.class, "epTI");
    }

    public void setupConfiguration(File c)
    {
        config = new Configuration(c);
        craftingDir = new File(c.getParentFile(), "crafting");
        craftingDir.mkdirs();
        forceShowFrameOverlays = config.get("Misc", "ForceShowFrameOverlays", false).getBoolean(false);
        disableSounds = config.get("Overrides", "DisableSounds", false).getBoolean(false);
        disableParticles = config.get("Overrides", "DisableParticles", false).getBoolean(false);
        portalsDestroyBlocks = config.get("Portal", "PortalsDestroyBlocks", true).getBoolean(true);
        fasterPortalCooldown = config.get("Portal", "FasterPortalCooldown", false).getBoolean(false);
        requirePower = config.get("Power", "RequirePower", true).getBoolean(true);
        powerMultiplier = config.get("Power", "PowerMultiplier", 1.0).getDouble(1.0);
        powerStorageMultiplier = config.get("Power", "DBSPowerStorageMultiplier", 1.0).getDouble(1.0);
        activePortalsPerRow = config.get("Portal", "ActivePortalsPerRow", 2).getInt(2);
        config.save();

        if (powerMultiplier < 0)
        {
            requirePower = false;
        }
        
        if (powerStorageMultiplier < 0.01)
        {
            powerStorageMultiplier = 0.01;
        }
    }

    public void setupCrafting()
    {
        // TODO
    }
}
