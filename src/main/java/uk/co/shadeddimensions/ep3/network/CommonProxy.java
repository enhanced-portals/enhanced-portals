package uk.co.shadeddimensions.ep3.network;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.Property;
import uk.co.shadeddimensions.ep3.block.BlockCrafting;
import uk.co.shadeddimensions.ep3.block.BlockDecoration;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.block.BlockNetherPortal;
import uk.co.shadeddimensions.ep3.block.BlockPortal;
import uk.co.shadeddimensions.ep3.block.BlockStabilizer;
import uk.co.shadeddimensions.ep3.crafting.ThermalExpansion;
import uk.co.shadeddimensions.ep3.crafting.Vanilla;
import uk.co.shadeddimensions.ep3.item.ItemEntityCard;
import uk.co.shadeddimensions.ep3.item.ItemGoggles;
import uk.co.shadeddimensions.ep3.item.ItemGuide;
import uk.co.shadeddimensions.ep3.item.ItemHandheldScanner;
import uk.co.shadeddimensions.ep3.item.ItemLocationCard;
import uk.co.shadeddimensions.ep3.item.ItemMisc;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.item.ItemPortalModule;
import uk.co.shadeddimensions.ep3.item.ItemUpgrade;
import uk.co.shadeddimensions.ep3.item.ItemWrench;
import uk.co.shadeddimensions.ep3.item.block.ItemDecoration;
import uk.co.shadeddimensions.ep3.item.block.ItemFrame;
import uk.co.shadeddimensions.ep3.item.block.ItemStabilizer;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileFrameBasic;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.portal.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileRedstoneInterface;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferEnergy;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferFluid;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferItem;
import uk.co.shadeddimensions.ep3.util.ConfigHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.EnhancedPortals;

public class CommonProxy
{
    public int gogglesRenderIndex = 0;

    public NetworkManager networkManager;

    public static final Logger logger = Logger.getLogger(EnhancedPortals.NAME);
    public static ConfigHandler configuration;

    public static class Properties
    {
        public static Property useAlternateGlyphs, forceShowFrameOverlays, disableSounds, disableParticles, portalsDestroyBlocks, fasterPortalCooldown, requirePower, powerMultiplier;
    }

    public static final int REDSTONE_FLUX_COST = 10000, REDSTONE_FLUX_TIMER = 20;
    public static final int RF_PER_MJ = 10;
    
    public int glassesRenderIndex;
    
    public static boolean customNetherPortals, useAlternateGlyphs, forceShowFrameOverlays, disableSounds, disableParticles, portalsDestroyBlocks, fasterPortalCooldown, requirePower, disableVanillaRecipes, disableTERecipes;
    public static int powerMultiplier;
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

    boolean reflectBlock(Block block, Class<? extends Block> clazz)
    {
        Field field = null;

        for (Field f : net.minecraft.block.Block.class.getDeclaredFields())
        {
            if (f.getType() == clazz)
            {
                field = f;
                break;
            }
        }

        if (field == null)
        {
            return false;
        }

        field.setAccessible(true);

        if ((field.getModifiers() & Modifier.FINAL) != 0)
        {
            try
            {
                Field modifiers = Field.class.getDeclaredField("modifiers");
                modifiers.setAccessible(true);
                modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            }
            catch (Exception e)
            {
                return false;
            }
        }

        try
        {
            field.set(null, block);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public void miscSetup()
    {
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(ItemPortalModule.instance, 1, 4), 1, 1, 2));

        if (customNetherPortals)
        {
            BlockNetherPortal.ID = Block.portal.blockID;
            Block.blocksList[BlockNetherPortal.ID] = null;

            if (!reflectBlock(new BlockNetherPortal(), net.minecraft.block.BlockPortal.class))
            {
                Block.blocksList[BlockNetherPortal.ID] = null;
                Block.blocksList[BlockNetherPortal.ID] = new net.minecraft.block.BlockPortal(BlockNetherPortal.ID);
                logger.warning("Unable to modify BlockPortal. Custom Nether Portals have been disabled.");
            }
        }
    }

    public void registerBlocks()
    {
        BlockFrame.ID = configuration.getBlockId("Frame");
        BlockPortal.ID = configuration.getBlockId("Portal");
        BlockStabilizer.ID = configuration.getBlockId("DimensionalBridgeStabilizer");
        BlockDecoration.ID = configuration.getBlockId("Decoration");
        BlockCrafting.ID = configuration.getBlockId("Crafting");
        
        GameRegistry.registerBlock(new BlockFrame(), ItemFrame.class, "portalFrame");
        GameRegistry.registerBlock(new BlockPortal(), "portal");
        GameRegistry.registerBlock(new BlockStabilizer(), ItemStabilizer.class, "stabilizer");
        GameRegistry.registerBlock(new BlockDecoration(), ItemDecoration.class, "ep3.decoration");
        GameRegistry.registerBlock(new BlockCrafting(), "crafting");
    }

    public void registerItems()
    {
        ItemWrench.ID = configuration.getItemId("Wrench");
        ItemPaintbrush.ID = configuration.getItemId("Paintbrush");
        ItemGoggles.ID = configuration.getItemId("Glasses");
        ItemLocationCard.ID = configuration.getItemId("LocationCard");
        ItemPortalModule.ID = configuration.getItemId("PortalModule");
        ItemEntityCard.ID = configuration.getItemId("EntityCard");
        ItemHandheldScanner.ID = configuration.getItemId("HandheldScanner");
        ItemUpgrade.ID = configuration.getItemId("InPlaceUpgrade");
        ItemMisc.ID = configuration.getItemId("MiscItems");
        ItemGuide.ID = configuration.getItemId("Manual");
        
        GameRegistry.registerItem(new ItemWrench(), "wrench");
        GameRegistry.registerItem(new ItemPaintbrush(), "paintbrush");
        GameRegistry.registerItem(new ItemGoggles(), "goggles");
        GameRegistry.registerItem(new ItemLocationCard(), "locationCard");
        GameRegistry.registerItem(new ItemPortalModule(), "portalModule");
        GameRegistry.registerItem(new ItemEntityCard(), "entityCard");
        GameRegistry.registerItem(new ItemHandheldScanner(), "handheldScanner");
        GameRegistry.registerItem(new ItemUpgrade(), "inPlaceUpgrade");
        GameRegistry.registerItem(new ItemMisc(), "miscItems");
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
        GameRegistry.registerTileEntity(TileBiometricIdentifier.class, "epBiometricIdentifier");
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
        configuration = new ConfigHandler(EnhancedPortals.VERSION);
        configuration.setConfiguration(theConfig);

        configuration.addBlockEntry("Portal");
        configuration.addBlockEntry("Frame");
        configuration.addBlockEntry("DimensionalBridgeStabilizer");
        configuration.addBlockEntry("Decoration");
        configuration.addBlockEntry("Crafting");

        configuration.addItemEntry("Wrench");
        configuration.addItemEntry("Glasses");
        configuration.addItemEntry("Paintbrush");
        configuration.addItemEntry("LocationCard");
        configuration.addItemEntry("EntityCard");
        configuration.addItemEntry("HandheldScanner");
        configuration.addItemEntry("MiscItems");
        configuration.addItemEntry("PortalModule");
        configuration.addItemEntry("InPlaceUpgrade");
        configuration.addItemEntry("Manual");

        Properties.useAlternateGlyphs = config.get("Misc", "UseAlternateGlyphs", false);
        Properties.forceShowFrameOverlays = config.get("Misc", "ForceShowFrameOverlays", false);
        Properties.disableSounds = config.get("Overrides", "DisableSounds", false);
        Properties.disableParticles = config.get("Overrides", "DisableParticles", false);
        Properties.portalsDestroyBlocks = config.get("Portal", "PortalsDestroyBlocks", true);
        Properties.fasterPortalCooldown = config.get("Portal", "FasterPortalCooldown", false);
        Properties.requirePower = config.get("Power", "RequirePower", true);
        Properties.powerMultiplier = config.get("Power", "PowerMultiplier", 1);

        useAlternateGlyphs = Properties.useAlternateGlyphs.getBoolean(false);
        forceShowFrameOverlays = Properties.forceShowFrameOverlays.getBoolean(false);
        disableSounds = Properties.disableSounds.getBoolean(false);
        disableParticles = Properties.disableParticles.getBoolean(false);
        portalsDestroyBlocks = Properties.portalsDestroyBlocks.getBoolean(true);
        fasterPortalCooldown = Properties.fasterPortalCooldown.getBoolean(false);
        requirePower = Properties.requirePower.getBoolean(true);
        powerMultiplier = Properties.powerMultiplier.getInt(1);
        disableVanillaRecipes = config.get("Recipes", "DisableVanillaRecipes", false).getBoolean(false);
        disableTERecipes = config.get("Recipes", "DisableTERecipes", false).getBoolean(false);
        config.save();

        if (powerMultiplier < 0)
        {
            powerMultiplier = 0;
        }

        configuration.init();
    }
    
    public static void saveConfigs()
    {
        Properties.useAlternateGlyphs.set(useAlternateGlyphs);
        Properties.forceShowFrameOverlays.set(forceShowFrameOverlays);
        Properties.disableParticles.set(disableParticles);
        Properties.disableSounds.set(disableSounds);
        Properties.portalsDestroyBlocks.set(portalsDestroyBlocks);
        Properties.fasterPortalCooldown.set(fasterPortalCooldown);
        Properties.requirePower.set(requirePower);
        Properties.powerMultiplier.set(powerMultiplier);
        config.save();
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
