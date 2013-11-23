package uk.co.shadeddimensions.ep3.network;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityEggInfo;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.block.BlockDecoration;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.block.BlockPortal;
import uk.co.shadeddimensions.ep3.block.BlockStabilizer;
import uk.co.shadeddimensions.ep3.entity.mob.MobCreeper;
import uk.co.shadeddimensions.ep3.entity.mob.MobEnderman;
import uk.co.shadeddimensions.ep3.item.ItemDecoration;
import uk.co.shadeddimensions.ep3.item.ItemEntityCard;
import uk.co.shadeddimensions.ep3.item.ItemFrame;
import uk.co.shadeddimensions.ep3.item.ItemGoggles;
import uk.co.shadeddimensions.ep3.item.ItemHandheldScanner;
import uk.co.shadeddimensions.ep3.item.ItemLocationCard;
import uk.co.shadeddimensions.ep3.item.ItemMisc;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.item.ItemPortalModule;
import uk.co.shadeddimensions.ep3.item.ItemStabilizer;
import uk.co.shadeddimensions.ep3.item.ItemSynchronizer;
import uk.co.shadeddimensions.ep3.item.ItemUpgrade;
import uk.co.shadeddimensions.ep3.item.ItemWrench;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.packet.PacketTileUpdate;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileFrame;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import cofh.util.ConfigHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public static final int REDSTONE_FLUX_COST = 10000;
    public static final int REDSTONE_FLUX_TIMER = 20;

    public static BlockFrame blockFrame;
    public static BlockPortal blockPortal;
    public static BlockStabilizer blockStabilizer;
    public static BlockDecoration blockDecoration;

    public static ItemWrench itemWrench;
    public static ItemPaintbrush itemPaintbrush;
    public static ItemGoggles itemGoggles;
    public static ItemPortalModule itemPortalModule;
    public static ItemLocationCard itemLocationCard;
    public static ItemSynchronizer itemSynchronizer;
    public static ItemEntityCard itemEntityCard;
    public static ItemHandheldScanner itemScanner;
    public static ItemUpgrade itemInPlaceUpgrade;
    public static ItemMisc itemMisc;

    public static NetworkManager networkManager;

    public static final Logger logger = Logger.getLogger(Reference.NAME);
    public static ConfigHandler configuration;

    public static boolean useAlternateGlyphs, customNetherPortals, portalsDestroyBlocks, fasterPortalCooldown, disableVanillaRecipes, disableTERecipes;
    public static int redstoneFluxPowerMultiplier;

    public static int MobEnderman, MobCreeper;
    public static int Dimension, WastelandID;

    public static void sendPacketToAllAroundTiny(TileEnhancedPortals tile)
    {
        PacketDispatcher.sendPacketToAllAround(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, 15, tile.worldObj.provider.dimensionId, new PacketTileUpdate(tile).getPacket());
    }

    public static void sendUpdatePacketToPlayer(TileEnhancedPortals tile, EntityPlayer player)
    {
        PacketDispatcher.sendPacketToPlayer(new PacketTileUpdate(tile).getPacket(), (Player) player);
    }

    public static void sendUpdatePacketToAllAround(TileEnhancedPortals tile)
    {
        sendPacketToAllAround(tile, new PacketTileUpdate(tile).getPacket());
    }

    public static void sendPacketToAllAround(TileEntity tile, Packet250CustomPayload packet)
    {
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
        blockFrame = new BlockFrame(configuration.getBlockId("Frame"), "ep3.portalFrame");
        GameRegistry.registerBlock(blockFrame, ItemFrame.class, "ep3.portalFrame");

        blockPortal = new BlockPortal(configuration.getBlockId("Portal"), "ep3.portal");
        GameRegistry.registerBlock(blockPortal, "ep3.portal");

        blockStabilizer = new BlockStabilizer(configuration.getBlockId("DimensionalBridgeStabilizer"), "ep3.stabilizer");
        GameRegistry.registerBlock(blockStabilizer, ItemStabilizer.class, "ep3.stabilizer");

        blockDecoration = new BlockDecoration(configuration.getBlockId("Decoration"), "ep3.decoration");
        GameRegistry.registerBlock(blockDecoration, ItemDecoration.class, "ep3.decoration");
    }

    public void registerItems()
    {
        itemWrench = new ItemWrench(configuration.getItemId("Wrench"), "ep3.wrench");
        GameRegistry.registerItem(itemWrench, "ep3.wrench");

        itemPaintbrush = new ItemPaintbrush(configuration.getItemId("Paintbrush"), "ep3.paintbrush");
        GameRegistry.registerItem(itemPaintbrush, "ep3.paintbrush");

        itemGoggles = new ItemGoggles(configuration.getItemId("Glasses"), "ep3.goggles");
        GameRegistry.registerItem(itemGoggles, "ep3.goggles");

        itemLocationCard = new ItemLocationCard(configuration.getItemId("LocationCard"), "ep3.locationCard");
        GameRegistry.registerItem(itemLocationCard, "ep3.locationCard");

        itemPortalModule = new ItemPortalModule(configuration.getItemId("PortalModule"), "ep3.portalModule");
        GameRegistry.registerItem(itemPortalModule, "ep3.portalModule");

        itemSynchronizer = new ItemSynchronizer(configuration.getItemId("Synchronizer"), "ep3.synchronizer");
        GameRegistry.registerItem(itemSynchronizer, "ep3.synchronizer");

        itemEntityCard = new ItemEntityCard(configuration.getItemId("EntityCard"), "ep3.entityCard");
        GameRegistry.registerItem(itemEntityCard, "ep3.entityCard");

        itemScanner = new ItemHandheldScanner(configuration.getItemId("HandheldScanner"), "ep3.handheldScanner");
        GameRegistry.registerItem(itemScanner, "ep3.handheldScanner");

        itemInPlaceUpgrade = new ItemUpgrade(configuration.getItemId("InPlaceUpgrade"), "ep3.inPlaceUpgrade");
        GameRegistry.registerItem(itemInPlaceUpgrade, "ep3.inPlaceUpgrade");

        itemMisc = new ItemMisc(configuration.getItemId("MiscItems"), "ep3.miscItems");
        GameRegistry.registerItem(itemMisc, "ep3.miscItems");
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
        GameRegistry.registerTileEntity(TileStabilizerMain.class, "epStabilizerMain");
    }

    public void registerEntities()
    {
        EntityRegistry.registerModEntity(MobEnderman.class, "enderman", MobEnderman, EnhancedPortals.instance, 64, 1, true);
        registerEntityEgg(MobEnderman.class, 0xFF0000, 0xFF00FF);

        EntityRegistry.registerModEntity(MobCreeper.class, "creeper", MobCreeper, EnhancedPortals.instance, 64, 1, true);
        registerEntityEgg(MobCreeper.class, 0xFF0000, 0x00FF00);
    }

    public static int eggIdCounter = 300;

    static int getUniqueEggId()
    {
        do
        {
            eggIdCounter++;
        }
        while (EntityList.getStringFromID(eggIdCounter) != null);

        return eggIdCounter;
    }

    @SuppressWarnings("unchecked")
    static void registerEntityEgg(Class<?> entity, int c, int c2)
    {
        int id = getUniqueEggId();
        EntityList.IDtoClassMapping.put(id, entity);
        EntityList.entityEggs.put(id, new EntityEggInfo(id, c, c2));
    }

    public void setupConfiguration(Configuration theConfig)
    {
        configuration = new ConfigHandler(Reference.VERSION);
        configuration.setConfiguration(theConfig);

        configuration.addBlockEntry("Portal");
        configuration.addBlockEntry("Frame");
        configuration.addBlockEntry("DimensionalBridgeStabilizer");
        configuration.addBlockEntry("Decoration");

        configuration.addItemEntry("Wrench");
        configuration.addItemEntry("Glasses");
        configuration.addItemEntry("Paintbrush");
        configuration.addItemEntry("LocationCard");
        configuration.addItemEntry("Synchronizer");
        configuration.addItemEntry("EntityCard");
        configuration.addItemEntry("HandheldScanner");
        configuration.addItemEntry("MiscItems");
        configuration.addItemEntry("PortalModule");
        configuration.addItemEntry("InPlaceUpgrade");

        useAlternateGlyphs = configuration.get("Misc", "UseAlternateGlyphs", false);
        customNetherPortals = configuration.get("Overrides", "CustomNetherPortals", false);

        portalsDestroyBlocks = configuration.get("Portal", "PortalsDestroyBlocks", true);
        fasterPortalCooldown = configuration.get("Portal", "FasterPortalCooldown", false);

        redstoneFluxPowerMultiplier = configuration.get("Power", "PowerMultiplier", 1);

        MobEnderman = configuration.get("Mobs", "EndermanID", 0);
        MobCreeper = configuration.get("Mobs", "CreeperID", 1);

        Dimension = configuration.get("World", "DimensionID", -10);
        WastelandID = configuration.get("World", "WastelandID", 50);

        disableVanillaRecipes = configuration.get("Recipes", "DisableVanillaRecipes", false);
        disableTERecipes = configuration.get("Recipes", "DisableTERecipes", false);

        if (redstoneFluxPowerMultiplier < 0)
        {
            redstoneFluxPowerMultiplier = 0;
        }

        configuration.init();
    }

    public void miscSetup()
    {
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(itemPortalModule, 1, 4), 10, 40, 2));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(itemPortalModule, 1, 6), 10, 40, 2));
    }

    public static void openGui(EntityPlayer player, GUIs gui, TileEnhancedPortals tile)
    {
        openGui(player, gui.ordinal(), tile);
    }

    public static void openGui(EntityPlayer player, int id, TileEnhancedPortals tile)
    {
        player.openGui(EnhancedPortals.instance, id, tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public void setupCrafting()
    {
        if (disableVanillaRecipes)
        {
            return;
        }

        // Frames
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFrame, 4, 0), new Object[] { "SDS", "IQI", "SIS", 'S', Block.stone, 'D', Item.diamond, 'Q', Block.blockNetherQuartz, 'I', Item.ingotIron }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFrame, 1, BlockFrame.PORTAL_CONTROLLER), new Object[] { "IDI", " F ", "IEI", 'F', new ItemStack(blockFrame, 1, 0), 'I', Item.ingotIron, 'E', Item.enderPearl, 'D', Item.diamond }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFrame, 1, BlockFrame.REDSTONE_INTERFACE), new Object[] { " R ", "RFR", " R ", 'F', new ItemStack(blockFrame, 1, 0), 'R', Item.redstone }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFrame, 1, BlockFrame.NETWORK_INTERFACE), new Object[] { " Z ", "EFE", " Z ", 'F', new ItemStack(blockFrame, 1, 0), 'Z', Item.blazePowder, 'E', Item.enderPearl }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFrame, 1, BlockFrame.DIALLING_DEVICE), new Object[] { " E ", "DFD", " E ", 'F', new ItemStack(blockFrame, 1, BlockFrame.NETWORK_INTERFACE), 'E', Item.enderPearl, 'D', Item.diamond }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFrame, 1, BlockFrame.BIOMETRIC_IDENTIFIER), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(blockFrame, 1, 0), 'Z', Item.blazePowder, 'P', Item.porkRaw, 'B', Item.beefRaw, 'C', Item.chickenRaw }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFrame, 1, BlockFrame.BIOMETRIC_IDENTIFIER), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(blockFrame, 1, 0), 'Z', Item.blazePowder, 'P', Item.porkCooked, 'B', Item.beefCooked, 'C', Item.chickenCooked }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFrame, 1, BlockFrame.MODULE_MANIPULATOR), new Object[] { " D ", "EFE", " U ", 'F', new ItemStack(blockFrame, 1, 0), 'D', Item.diamond, 'E', Item.emerald, 'U', new ItemStack(itemMisc, 1, 0) }));

        // In-Place Upgrades
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemInPlaceUpgrade, 1, 0), new Object[] { " R ", "RFR", " R ", 'F', new ItemStack(itemMisc, 1, 1), 'R', Item.redstone }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemInPlaceUpgrade, 1, 1), new Object[] { " Z ", "EFE", " Z ", 'F', new ItemStack(itemMisc, 1, 1), 'Z', Item.blazePowder, 'E', Item.enderPearl }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemInPlaceUpgrade, 1, 2), new Object[] { " E ", "DFD", " E ", 'F', new ItemStack(itemInPlaceUpgrade, 1, 1), 'E', Item.enderPearl, 'D', Item.diamond }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemInPlaceUpgrade, 1, 3), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(itemMisc, 1, 1), 'Z', Item.blazePowder, 'P', Item.porkRaw, 'B', Item.beefRaw, 'C', Item.chickenRaw }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemInPlaceUpgrade, 1, 3), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(itemMisc, 1, 1), 'Z', Item.blazePowder, 'P', Item.porkCooked, 'B', Item.beefCooked, 'C', Item.chickenCooked }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemInPlaceUpgrade, 1, 4), new Object[] { " D ", "EFE", " U ", 'F', new ItemStack(itemMisc, 1, 1), 'D', Item.diamond, 'E', Item.emerald, 'U', new ItemStack(itemMisc, 1, 0) }));

        // Stabilizer
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockStabilizer, 6), new Object[] { "QPQ", "EDE", "QPQ", 'D', Block.blockDiamond, 'E', Item.emerald, 'Q', Block.blockIron, 'P', Item.enderPearl }));

        // Wrench
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemWrench), new Object[] { "I I", " Q ", " I ", 'I', Item.ingotIron, 'Q', Item.netherQuartz }));

        // Glasses
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemGoggles), true, new Object[] { "R B", "GLG", "L L", 'R', "dyeRed", 'B', "dyeCyan", 'G', "glass", 'L', Item.leather }));

        // Nanobrush
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemPaintbrush), new Object[] { "WT ", "TS ", "  S", 'W', Block.cloth, 'T', Item.silk, 'S', "stickWood" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemPaintbrush), new Object[] { " TW", " ST", "S  ", 'W', Block.cloth, 'T', Item.silk, 'S', "stickWood" }));

        // Location Card
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemLocationCard, 16), new Object[] { "IPI", "PPP", "IDI", 'I', Item.ingotIron, 'P', Item.paper, 'D', "dyeBlue" }));

        // Synchronizer
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemSynchronizer), new Object[] { "GGG", "IQI", "III", 'G', Item.ingotGold, 'I', Item.ingotIron, 'Q', Item.netherQuartz }));

        // Handheld Scanner
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemScanner), new Object[] { "GRG", "IQI", "IEI", 'G', Item.ingotGold, 'I', Item.ingotIron, 'R', Item.redstone, 'Q', Item.netherQuartz, 'E', itemEntityCard }));

        // Entity Card
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEntityCard, 8), new Object[] { "GPG", "PPP", "GDG", 'G', Item.ingotGold, 'P', Item.paper, 'D', "dyeLime" }));

        // Decoration
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockDecoration, 8, 0), new Object[] { "SQS", "QQQ", "SQS", 'S', Block.stone, 'Q', Block.blockNetherQuartz }));

        // Blank stuff
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemMisc, 1, 0), true, new Object[] { "II ", "II ", "NN ", 'I', Item.ingotIron, 'N', Item.goldNugget })); // Blank Portal Module
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemMisc, 4, 1), new Object[] { "D", "P", "R", 'P', Item.paper, 'D', Item.diamond, 'R', "dyeRed" })); // Blank Upgrade

        // Portal Modules
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemPortalModule, 1, 0), new Object[] { "RXG", 'X', new ItemStack(itemMisc, 1, 0), 'R', Item.redstone, 'G', Item.gunpowder })); // Particle Destroyer
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemPortalModule, 1, 1), new Object[] { "RGB", " X ", "BGR", 'X', new ItemStack(itemMisc, 1, 0), 'R', "dyeRed", 'B', "dyeBlue", 'G', "dyeGreen" })); // Rainbow particles
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemPortalModule, 1, 2), new Object[] { "RXN", 'X', new ItemStack(itemMisc, 1, 0), 'R', Item.redstone, 'N', Block.music })); // Portal Silencer
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemPortalModule, 1, 3), new Object[] { "AXF", 'X', new ItemStack(itemMisc, 1, 0), 'A', Block.anvil, 'F', Item.feather })); // Momentum
        // 4 - Portal Cloaking - does not have a recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemPortalModule, 1, 5), new Object[] { "BXI", 'X', new ItemStack(itemMisc, 1, 0), 'B', "dyeWhite", 'I', "dyeBlack" })); // Particle Shader
        // 6 - Ethereal Frame - does not have a recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemPortalModule, 1, 7), new Object[] { "FFF", "FXF", "FFF", 'X', new ItemStack(itemMisc, 1, 0), 'F', Item.feather })); // Featherfall
    }
}
