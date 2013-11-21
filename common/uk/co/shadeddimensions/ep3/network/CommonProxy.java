package uk.co.shadeddimensions.ep3.network;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityEggInfo;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
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

    public static NetworkManager networkManager;

    public static final Logger logger = Logger.getLogger(Reference.NAME);
    public static ConfigHandler configuration;

    public static boolean useAlternateGlyphs, customNetherPortals, portalsDestroyBlocks, fasterPortalCooldown;
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

        if (redstoneFluxPowerMultiplier < 0)
        {
            redstoneFluxPowerMultiplier = 0;
        }

        configuration.init();
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

    public void setupCrafting()
    {
        // Frames
        CraftingManager.getInstance().addRecipe(new ItemStack(blockFrame, 4, 0), new Object[] { "SDS", "IQI", "SIS", 'S', Block.stone, 'D', Item.diamond, 'Q', Block.blockNetherQuartz, 'I', Item.ingotIron });
        CraftingManager.getInstance().addRecipe(new ItemStack(blockFrame, 4, 0), new Object[] { " D ", "IQI", " I ", 'D', Item.diamond, 'Q', new ItemStack(blockDecoration, 1, 0), 'I', Item.ingotIron });
        CraftingManager.getInstance().addRecipe(new ItemStack(blockFrame, 1, BlockFrame.PORTAL_CONTROLLER), new Object[] { "IDI", " F ", "IEI", 'F', new ItemStack(blockFrame, 1, 0), 'I', Item.ingotIron, 'E', Item.enderPearl, 'D', Item.diamond });
        CraftingManager.getInstance().addRecipe(new ItemStack(blockFrame, 1, BlockFrame.REDSTONE_INTERFACE), new Object[] { " R ", "RFR", " R ", 'F', new ItemStack(blockFrame, 1, 0), 'R', Item.redstone });
        CraftingManager.getInstance().addRecipe(new ItemStack(blockFrame, 1, BlockFrame.NETWORK_INTERFACE), new Object[] { " Z ", "EFE", " Z ", 'F', new ItemStack(blockFrame, 1, 0), 'Z', Item.blazePowder, 'E', Item.enderPearl });
        CraftingManager.getInstance().addRecipe(new ItemStack(blockFrame, 1, BlockFrame.DIALLING_DEVICE), new Object[] { " E ", "DFD", " E ", 'F', new ItemStack(blockFrame, 1, BlockFrame.NETWORK_INTERFACE), 'E', Item.enderPearl, 'D', Item.diamond });
        CraftingManager.getInstance().addRecipe(new ItemStack(blockFrame, 1, BlockFrame.BIOMETRIC_IDENTIFIER), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(blockFrame, 1, 0), 'Z', Item.blazePowder, 'P', Item.porkRaw, 'B', Item.beefRaw, 'C', Item.chickenRaw });
        CraftingManager.getInstance().addRecipe(new ItemStack(blockFrame, 1, BlockFrame.BIOMETRIC_IDENTIFIER), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(blockFrame, 1, 0), 'Z', Item.blazePowder, 'P', Item.porkCooked, 'B', Item.beefCooked, 'C', Item.chickenCooked });
        CraftingManager.getInstance().addRecipe(new ItemStack(blockFrame, 1, BlockFrame.MODULE_MANIPULATOR), new Object[] { " D ", "EFE", " U ", 'F', new ItemStack(blockFrame, 1, 0), 'D', Item.diamond, 'E', Item.emerald, 'U', itemPortalModule });

        // In-Place Upgrades
        CraftingManager.getInstance().addRecipe(new ItemStack(itemInPlaceUpgrade, 1, 0), new Object[] { " R ", "RFR", " R ", 'F', Item.paper, 'R', Item.redstone });
        CraftingManager.getInstance().addRecipe(new ItemStack(itemInPlaceUpgrade, 1, 1), new Object[] { " Z ", "EFE", " Z ", 'F', Item.paper, 'Z', Item.blazePowder, 'E', Item.enderPearl });
        CraftingManager.getInstance().addRecipe(new ItemStack(itemInPlaceUpgrade, 1, 2), new Object[] { " E ", "DFD", " E ", 'F', new ItemStack(itemInPlaceUpgrade, 1, 1), 'E', Item.enderPearl, 'D', Item.diamond });
        CraftingManager.getInstance().addRecipe(new ItemStack(itemInPlaceUpgrade, 1, 3), new Object[] { "PBC", "ZFZ", 'F', Item.paper, 'Z', Item.blazePowder, 'P', Item.porkRaw, 'B', Item.beefRaw, 'C', Item.chickenRaw });
        CraftingManager.getInstance().addRecipe(new ItemStack(itemInPlaceUpgrade, 1, 3), new Object[] { "PBC", "ZFZ", 'F', Item.paper, 'Z', Item.blazePowder, 'P', Item.porkCooked, 'B', Item.beefCooked, 'C', Item.chickenCooked });
        CraftingManager.getInstance().addRecipe(new ItemStack(itemInPlaceUpgrade, 1, 4), new Object[] { " D ", "EFE", " U ", 'F', Item.paper, 'D', Item.diamond, 'E', Item.emerald, 'U', itemPortalModule });

        // Stabilizer
        CraftingManager.getInstance().addRecipe(new ItemStack(blockStabilizer, 6), new Object[] { "QPQ", "EDE", "QPQ", 'D', Block.blockDiamond, 'E', Item.emerald, 'Q', Block.blockNetherQuartz, 'P', Item.enderPearl });

        // Wrench
        CraftingManager.getInstance().addRecipe(new ItemStack(itemWrench), new Object[] { "Q Q", "III", " I ", 'I', Item.ingotIron, 'Q', Item.netherQuartz });

        // Location Card
        CraftingManager.getInstance().addRecipe(new ItemStack(itemLocationCard, 16), new Object[] { "GPG", "PPP", "GPG", 'G', Item.ingotGold, 'P', Item.paper });

        // Decoration
        CraftingManager.getInstance().addRecipe(new ItemStack(blockDecoration, 8, 0), new Object[] { "SQS", "QQQ", "SQS", 'S', Block.stone, 'Q', Block.blockNetherQuartz });
    }
}
