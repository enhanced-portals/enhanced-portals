package enhancedportals.network;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.EnhancedPortals;
import enhancedportals.block.BlockCrafting;
import enhancedportals.block.BlockDecoration;
import enhancedportals.block.BlockFrame;
import enhancedportals.block.BlockPortal;
import enhancedportals.block.BlockStabilizer;
import enhancedportals.crafting.ThermalExpansion;
import enhancedportals.crafting.Vanilla;
import enhancedportals.item.ItemDecoration;
import enhancedportals.item.ItemFrame;
import enhancedportals.item.ItemGoggles;
import enhancedportals.item.ItemGuide;
import enhancedportals.item.ItemLocationCard;
import enhancedportals.item.ItemMisc;
import enhancedportals.item.ItemPaintbrush;
import enhancedportals.item.ItemPortalModule;
import enhancedportals.item.ItemStabilizer;
import enhancedportals.item.ItemUpgrade;
import enhancedportals.item.ItemWrench;
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
import enhancedportals.tileentity.portal.TileRedstoneInterface;
import enhancedportals.tileentity.portal.TileTransferEnergy;
import enhancedportals.tileentity.portal.TileTransferFluid;
import enhancedportals.tileentity.portal.TileTransferItem;

public class CommonProxy
{
	public static final Logger logger = Logger.getLogger(EnhancedPortals.NAME);
    public static final int REDSTONE_FLUX_COST = 10000, REDSTONE_FLUX_TIMER = 20;
    public static final int RF_PER_MJ = 10;
    public int gogglesRenderIndex = 0;
    public NetworkManager networkManager;
    public static boolean useAlternateGlyphs, forceShowFrameOverlays, disableSounds, disableParticles, portalsDestroyBlocks, fasterPortalCooldown, requirePower, disableVanillaRecipes, disableTERecipes;
    public static double powerMultiplier;
    static Configuration config;

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
    
    public void registerPackets()
    {
        EnhancedPortals.packetPipeline.registerPacket(PacketRequestGui.class);
        EnhancedPortals.packetPipeline.registerPacket(PacketTextureData.class);
        EnhancedPortals.packetPipeline.registerPacket(PacketRerender.class);
        EnhancedPortals.packetPipeline.registerPacket(PacketGuiData.class);
    }

    public void miscSetup()
    {
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(ItemPortalModule.instance, 1, 4), 1, 1, 2));
    }

    public void registerBlocks()
    {
        GameRegistry.registerBlock(new BlockFrame(), ItemFrame.class, "frame");
        GameRegistry.registerBlock(new BlockPortal(), "portal");
        GameRegistry.registerBlock(new BlockStabilizer(), ItemStabilizer.class, "stabilizer");
        GameRegistry.registerBlock(new BlockDecoration(), ItemDecoration.class, "decoration");
        GameRegistry.registerBlock(new BlockCrafting(), "crafting");
    }

    public void registerItems()
    {
        GameRegistry.registerItem(new ItemWrench(), "wrench");
        GameRegistry.registerItem(new ItemPaintbrush(), "paintbrush");
        GameRegistry.registerItem(new ItemGoggles(), "goggles");
        GameRegistry.registerItem(new ItemLocationCard(), "location_card");
        GameRegistry.registerItem(new ItemPortalModule(), "portal_module");
        GameRegistry.registerItem(new ItemUpgrade(), "upgrade");
        GameRegistry.registerItem(new ItemMisc(), "misc");
        GameRegistry.registerItem(new ItemGuide(), "guide");
    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TilePortal.class, "epPortal");
        GameRegistry.registerTileEntity(TileFrameBasic.class, "epPortalFrame");
        GameRegistry.registerTileEntity(TileController.class, "epPortalController");
        GameRegistry.registerTileEntity(TileRedstoneInterface.class, "epRedstoneInterface");
        GameRegistry.registerTileEntity(TileNetworkInterface.class, "epNetworkInterface");
        GameRegistry.registerTileEntity(TileDiallingDevice.class, "epDiallingDevice");
        //GameRegistry.registerTileEntity(TileBiometricIdentifier.class, "epBiometricIdentifier"); // TODO
        GameRegistry.registerTileEntity(TileModuleManipulator.class, "epModuleManipulator");
        GameRegistry.registerTileEntity(TileStabilizer.class, "epStabilizer");
        GameRegistry.registerTileEntity(TileStabilizerMain.class, "epStabilizerMain");
        GameRegistry.registerTileEntity(TileTransferEnergy.class, "epTEnergy");
        GameRegistry.registerTileEntity(TileTransferFluid.class, "epTFluid");
        GameRegistry.registerTileEntity(TileTransferItem.class, "epTItem");
    }

    public void setupConfiguration(Configuration theConfig)
    {
        config = theConfig;
        useAlternateGlyphs = config.get("Misc", "UseAlternateGlyphs", false).getBoolean(false);
        forceShowFrameOverlays = config.get("Misc", "ForceShowFrameOverlays", false).getBoolean(false);
        disableSounds = config.get("Overrides", "DisableSounds", false).getBoolean(false);
        disableParticles = config.get("Overrides", "DisableParticles", false).getBoolean(false);
        portalsDestroyBlocks = config.get("Portal", "PortalsDestroyBlocks", true).getBoolean(true);
        fasterPortalCooldown = config.get("Portal", "FasterPortalCooldown", false).getBoolean(false);
        requirePower = config.get("Power", "RequirePower", true).getBoolean(true);
        powerMultiplier = config.get("Power", "PowerMultiplier", 1.0).getDouble(1.0);
        disableVanillaRecipes = config.get("Recipes", "DisableVanillaRecipes", false).getBoolean(false);
        disableTERecipes = config.get("Recipes", "DisableTERecipes", false).getBoolean(false);
        config.save();

        if (powerMultiplier < 0)
        {
            powerMultiplier = 0;
        }
    }
    
    public void setupCrafting()
    {
        Vanilla.registerRecipes();
        
        if (Loader.isModLoaded("ThermalExpansion") && !CommonProxy.disableTERecipes)
        {
            ThermalExpansion.registerRecipes();
            ThermalExpansion.registerMachineRecipes();
        }
    }
}
