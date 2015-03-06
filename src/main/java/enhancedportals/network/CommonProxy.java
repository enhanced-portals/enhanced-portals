package enhancedportals.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import enhancedportals.EnhancedPortals;
import enhancedportals.block.BlockDecorBorderedQuartz;
import enhancedportals.block.BlockDecorEnderInfusedMetal;
import enhancedportals.block.BlockFrame;
import enhancedportals.block.BlockPortal;
import enhancedportals.block.BlockStabilizer;
import enhancedportals.block.BlockStabilizerEmpty;
import enhancedportals.crafting.ThermalExpansion;
import enhancedportals.crafting.Vanilla;
import enhancedportals.item.ItemBlankPortalModule;
import enhancedportals.item.ItemBlankUpgrade;
import enhancedportals.item.ItemFrame;
import enhancedportals.item.ItemGlasses;
import enhancedportals.item.ItemLocationCard;
import enhancedportals.item.ItemManual;
import enhancedportals.item.ItemNanobrush;
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
import enhancedportals.tile.TileController;
import enhancedportals.tile.TileDialingDevice;
import enhancedportals.tile.TileFrameBasic;
import enhancedportals.tile.TileNetworkInterface;
import enhancedportals.tile.TilePortal;
import enhancedportals.tile.TilePortalManipulator;
import enhancedportals.tile.TileRedstoneInterface;
import enhancedportals.tile.TileStabilizer;
import enhancedportals.tile.TileStabilizerMain;
import enhancedportals.tile.TileTransferEnergy;
import enhancedportals.tile.TileTransferFluid;
import enhancedportals.tile.TileTransferItem;

public class CommonProxy
{
    public static final int REDSTONE_FLUX_COST = 10000, REDSTONE_FLUX_TIMER = 20;
    public int gogglesRenderIndex = 0;
    public NetworkManager networkManager;
    public static boolean forceShowFrameOverlays, disableSounds, disableParticles, portalsDestroyBlocks, fasterPortalCooldown, requirePower, updateNotifier, vanillaRecipes, teRecipes;
    public static double powerMultiplier, powerStorageMultiplier;
    public static int activePortalsPerRow = 2;
    static Configuration config;
    static File craftingDir;
    public static String lateVers;

    public void waitForController(ChunkCoordinates controller, ChunkCoordinates frame)
    {

    }

    public ArrayList<ChunkCoordinates> getControllerList(ChunkCoordinates controller)
    {
        return null;
    }

    public void clearControllerList(ChunkCoordinates controller)
    {

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

    public void miscSetup()
    {
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(ItemPortalModule.instance, 1, 4), 1, 1, 2));
    }

    public void registerBlocks()
    {
        GameRegistry.registerBlock(new BlockFrame("frame"), ItemFrame.class, "frame");
        GameRegistry.registerBlock(new BlockPortal("portal"), "portal");
        GameRegistry.registerBlock(new BlockStabilizer("dbs"), ItemStabilizer.class, "dbs");
        GameRegistry.registerBlock(new BlockDecorBorderedQuartz("decor_frame"), "decor_frame");
        GameRegistry.registerBlock(new BlockDecorEnderInfusedMetal("decor_dbs"), "decor_dbs");
        GameRegistry.registerBlock(new BlockStabilizerEmpty("dbs_empty"), "dbs_empty");
    }

    public void registerItems()
    {
        GameRegistry.registerItem(new ItemWrench("wrench"), "wrench");
        GameRegistry.registerItem(new ItemNanobrush("nanobrush"), "nanobrush");
        GameRegistry.registerItem(new ItemGlasses("glasses"), "glasses");
        GameRegistry.registerItem(new ItemLocationCard("location_card"), "location_card");
        GameRegistry.registerItem(new ItemPortalModule("portal_module"), "portal_module");
        GameRegistry.registerItem(new ItemUpgrade("upgrade"), "upgrade");
        GameRegistry.registerItem(new ItemBlankPortalModule("blank_portal_module"), "blank_portal_module");
        GameRegistry.registerItem(new ItemBlankUpgrade("blank_upgrade"), "blank_upgrade");
        GameRegistry.registerItem(new ItemManual("manual"), "manual");
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
        GameRegistry.registerTileEntity(TileDialingDevice.class, "epDD");
        GameRegistry.registerTileEntity(TilePortalManipulator.class, "epMM");
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
        forceShowFrameOverlays = config.get("Misc", "ForceShowFrameOverlays", false).getBoolean(false);
        disableSounds = config.get("Overrides", "DisableSounds", false).getBoolean(false);
        disableParticles = config.get("Overrides", "DisableParticles", false).getBoolean(false);
        portalsDestroyBlocks = config.get("Portal", "PortalsDestroyBlocks", true).getBoolean(true);
        fasterPortalCooldown = config.get("Portal", "FasterPortalCooldown", false).getBoolean(false);
        requirePower = config.get("Power", "RequirePower", true).getBoolean(true);
        powerMultiplier = config.get("Power", "PowerMultiplier", 1.0).getDouble(1.0);
        powerStorageMultiplier = config.get("Power", "DBSPowerStorageMultiplier", 1.0).getDouble(1.0);
        activePortalsPerRow = config.get("Portal", "ActivePortalsPerRow", 2).getInt(2);
        updateNotifier = config.get("Misc", "NotifyOfUpdates", true).getBoolean(true);
        vanillaRecipes = config.get("Crafting", "Vanilla", true).getBoolean(true);
        teRecipes = config.get("Crafting", "ThermalExpansion", true).getBoolean(true);

        config.save();

        if (powerMultiplier < 0)
        {
            requirePower = false;
        }

        if (powerStorageMultiplier < 0.01)
        {
            powerStorageMultiplier = 0.01;
        }

        try
        {
            URL versionIn = new URL(EnhancedPortals.UPDATE_URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(versionIn.openStream()));
            lateVers = in.readLine();

            if (FMLCommonHandler.instance().getSide() == Side.SERVER && !lateVers.equals(EnhancedPortals.VERSION))
            {
                EnhancedPortals.logger.info("You're using an outdated version (v" + EnhancedPortals.VERSION + "). The newest version is: " + lateVers);
            }
        }
        catch (Exception e)
        {
            EnhancedPortals.logger.warn("Unable to get the latest version information");
            lateVers = EnhancedPortals.VERSION;
        }
    }

    public static boolean Notify(EntityPlayer player, String lateVers)
    {
        if (updateNotifier == true)
        {
            player.addChatMessage(new ChatComponentText("Enhanced Portals has been updated to v" + lateVers + " :: You are running v" + EnhancedPortals.VERSION));
            return true;
        }
        else
        {
            EnhancedPortals.logger.info("You're using an outdated version (v" + EnhancedPortals.VERSION + ")");
            return false;
        }
    }

    public void setupCrafting()
    {
        if (vanillaRecipes) Vanilla.registerRecipes();
        if (teRecipes && Loader.isModLoaded(EnhancedPortals.MODID_THERMALEXPANSION)) ThermalExpansion.registerRecipes();
    }
}
