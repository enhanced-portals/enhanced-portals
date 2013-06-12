package enhancedportals.network;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedcore.util.MathHelper;
import enhancedportals.block.BlockAutomaticDialler;
import enhancedportals.block.BlockDialDevice;
import enhancedportals.block.BlockDialDeviceBasic;
import enhancedportals.block.BlockDummyPortal;
import enhancedportals.block.BlockNetherPortal;
import enhancedportals.block.BlockObsidian;
import enhancedportals.block.BlockObsidianStairs;
import enhancedportals.block.BlockPortalModifier;
import enhancedportals.item.ItemEnhancedFlintSteel;
import enhancedportals.item.ItemMiscellaneous;
import enhancedportals.item.ItemNetworkCard;
import enhancedportals.item.ItemPortalModifierUpgrade;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.ItemIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Settings;
import enhancedportals.portal.network.DialDeviceNetwork;
import enhancedportals.portal.network.ModifierNetwork;
import enhancedportals.tileentity.TileEntityAutomaticDialler;
import enhancedportals.tileentity.TileEntityDialDevice;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;
import enhancedportals.tileentity.TileEntityNetherPortal;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class CommonProxy
{
    public BlockNetherPortal blockNetherPortal;
    public BlockObsidian blockObsidian;
    public BlockObsidianStairs blockObsidianStairs;
    public BlockPortalModifier blockPortalModifier;
    public BlockDummyPortal blockDummyPortal;
    public BlockDialDevice blockDialDevice;
    public BlockDialDeviceBasic blockDialDeviceBasic;
    public BlockAutomaticDialler blockAutomaticDialler;

    public ItemPortalModifierUpgrade itemPortalModifierUpgrade;
    public ItemNetworkCard itemNetworkCard;
    public ItemEnhancedFlintSteel itemEnhancedFlintSteel;
    public ItemMiscellaneous itemMisc;

    public ModifierNetwork ModifierNetwork;
    public DialDeviceNetwork DialDeviceNetwork;
    public boolean isIdentifierTaken = false;

    public World getWorld(int dimension)
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimension);
    }

    public void loadBlocks()
    {
        if (Settings.AllowPortalModifiers)
        {
            blockPortalModifier = new BlockPortalModifier();
            GameRegistry.registerBlock(blockPortalModifier, Localization.PortalModifier_Name);
        }

        if (Settings.AllowDialHomeDevices)
        {
            blockDialDevice = new BlockDialDevice();
            GameRegistry.registerBlock(blockDialDevice, Localization.DialDevice_Name);

            blockDialDeviceBasic = new BlockDialDeviceBasic();
            GameRegistry.registerBlock(blockDialDeviceBasic, Localization.DialDeviceBasic_Name);

            blockAutomaticDialler = new BlockAutomaticDialler();
            GameRegistry.registerBlock(blockAutomaticDialler, Localization.AutomaticDialler_Name);
        }

        if (Settings.AllowObsidianStairs)
        {
            blockObsidianStairs = new BlockObsidianStairs();
            GameRegistry.registerBlock(blockObsidianStairs, Localization.ObsidianStairs_Name);
        }

        blockNetherPortal = new BlockNetherPortal();
        GameRegistry.registerBlock(blockNetherPortal, Localization.NetherPortal_Name);

        blockDummyPortal = new BlockDummyPortal();
        GameRegistry.registerBlock(blockDummyPortal, "dummyPortal");
    }

    public void loadItems()
    {
        if (Settings.AllowPortalModifiers)
        {
            itemPortalModifierUpgrade = new ItemPortalModifierUpgrade();
            GameRegistry.registerItem(itemPortalModifierUpgrade, Localization.PortalModifierUpgrade_Name);

            itemMisc = new ItemMiscellaneous();
            GameRegistry.registerItem(itemMisc, Localization.MiscellaneousItems_Name);

            itemNetworkCard = new ItemNetworkCard();
            GameRegistry.registerItem(itemNetworkCard, Localization.NetworkCard_Name);
        }

        if (Settings.AllowFlintSteel)
        {
            itemEnhancedFlintSteel = new ItemEnhancedFlintSteel();
            GameRegistry.registerItem(itemEnhancedFlintSteel, Localization.EnhancedFlintSteel_Name);
        }
    }

    public void loadRecipes()
    {
        // --- ITEMS
        if (!Settings.DisableModifierRecipe && Settings.AllowPortalModifiers)
        {
            // Upgrade Card
            GameRegistry.addShapedRecipe(new ItemStack(itemMisc, 1, 0), "NDN", " P ", "N N", Character.valueOf('N'), Item.goldNugget, Character.valueOf('D'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('P'), Item.paper);
            GameRegistry.addShapedRecipe(new ItemStack(itemMisc, 1, 0), "N N", " P ", "NDN", Character.valueOf('N'), Item.goldNugget, Character.valueOf('D'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('P'), Item.paper);
            GameRegistry.addShapedRecipe(new ItemStack(itemMisc, 1, 0), "N N", "DP ", "N N", Character.valueOf('N'), Item.goldNugget, Character.valueOf('D'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('P'), Item.paper);
            GameRegistry.addShapedRecipe(new ItemStack(itemMisc, 1, 0), "N N", " PD", "N N", Character.valueOf('N'), Item.goldNugget, Character.valueOf('D'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('P'), Item.paper);

            // Network Card
            GameRegistry.addShapedRecipe(new ItemStack(itemNetworkCard), "IDI", " P ", "I I", Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), new ItemStack(Item.dyePowder, 1, 4), Character.valueOf('P'), Item.paper);
            GameRegistry.addShapedRecipe(new ItemStack(itemNetworkCard), "I I", "DP ", "I I", Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), new ItemStack(Item.dyePowder, 1, 4), Character.valueOf('P'), Item.paper);
            GameRegistry.addShapedRecipe(new ItemStack(itemNetworkCard), "I I", " PD", "I I", Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), new ItemStack(Item.dyePowder, 1, 4), Character.valueOf('P'), Item.paper);
            GameRegistry.addShapedRecipe(new ItemStack(itemNetworkCard), "I I", " P ", "IDI", Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), new ItemStack(Item.dyePowder, 1, 4), Character.valueOf('P'), Item.paper);
        }

        if (Settings.AllowFlintSteel)
        {
            // Flint & Steel
            GameRegistry.addShapelessRecipe(new ItemStack(itemEnhancedFlintSteel), Item.flintAndSteel, Item.lightStoneDust, Item.lightStoneDust);
        }

        if (!Settings.DisableModifierRecipe && Settings.AllowPortalModifiers)
        {
            // Particle Upgrade
            GameRegistry.addShapelessRecipe(new ItemStack(itemPortalModifierUpgrade, 1, 0), new ItemStack(itemMisc, 1, 0), Item.redstone, Item.blazePowder);

            // Sound Upgrade
            GameRegistry.addShapelessRecipe(new ItemStack(itemPortalModifierUpgrade, 1, 1), new ItemStack(itemMisc, 1, 0), Item.redstone, Block.music);

            // Dimensional Upgrade
            GameRegistry.addShapedRecipe(new ItemStack(itemPortalModifierUpgrade, 1, 2), "IEI", "GUG", "IRI", Character.valueOf('U'), new ItemStack(itemMisc, 1, 0), Character.valueOf('R'), Item.redstone, Character.valueOf('G'), Item.ingotGold, Character.valueOf('I'), Item.ingotIron, Character.valueOf('E'), Item.enderPearl);

            // Advanced Dimensional Upgrade
            GameRegistry.addShapedRecipe(new ItemStack(itemPortalModifierUpgrade, 1, 3), "GEG", "DUD", "GIG", Character.valueOf('U'), new ItemStack(itemPortalModifierUpgrade, 1, 2), Character.valueOf('D'), Item.diamond, Character.valueOf('G'), Item.ingotGold, Character.valueOf('I'), Item.ingotIron, Character.valueOf('E'), Item.enderPearl);

            // Momentum
            GameRegistry.addShapelessRecipe(new ItemStack(itemPortalModifierUpgrade, 1, 4), new ItemStack(itemMisc, 1, 0), Block.anvil, Item.feather);

            // Nether Frame Upgrade
            GameRegistry.addShapelessRecipe(new ItemStack(itemPortalModifierUpgrade, 1, 5), new ItemStack(itemMisc, 1, 0), Block.glowStone, Block.netherBrick, Block.blockNetherQuartz);

            // Resourceful Frame Upgrade
            GameRegistry.addShapelessRecipe(new ItemStack(itemPortalModifierUpgrade, 1, 6), new ItemStack(itemMisc, 1, 0), Block.blockIron, Block.blockGold, Item.diamond, Item.diamond, Item.emerald, Item.emerald);

            // Modifier Camouflage
            GameRegistry.addShapelessRecipe(new ItemStack(itemPortalModifierUpgrade, 1, 7), new ItemStack(itemMisc, 1, 0), Item.diamond, Item.redstone, Item.enderPearl);

            // Dialling Upgrade
            GameRegistry.addShapelessRecipe(new ItemStack(itemPortalModifierUpgrade, 1, 8), new ItemStack(itemMisc, 1, 0), Item.redstone, Item.redstone, Item.enderPearl);
        }

        // --- BLOCKS
        if (Settings.AllowObsidianStairs)
        {
            // Obsidian Stairs
            GameRegistry.addShapedRecipe(new ItemStack(blockObsidianStairs, 4), "O  ", "OO ", "OOO", Character.valueOf('O'), Block.obsidian);
            GameRegistry.addShapedRecipe(new ItemStack(blockObsidianStairs, 4), "OOO", " OO", "  O", Character.valueOf('O'), Block.obsidian);
        }

        if (!Settings.DisableModifierRecipe && Settings.AllowPortalModifiers)
        {
            // Portal Modifier
            ItemStack flintSteel = Settings.AllowFlintSteel ? new ItemStack(itemEnhancedFlintSteel) : new ItemStack(Item.flintAndSteel);

            GameRegistry.addShapedRecipe(new ItemStack(blockPortalModifier), "OFO", "DIE", "ORO", Character.valueOf('O'), Block.obsidian, Character.valueOf('F'), flintSteel, Character.valueOf('D'), Item.diamond, Character.valueOf('I'), Item.ingotIron, Character.valueOf('E'), Item.enderPearl, Character.valueOf('R'), Item.redstone);
            GameRegistry.addShapedRecipe(new ItemStack(blockPortalModifier), "OFO", "EID", "ORO", Character.valueOf('O'), Block.obsidian, Character.valueOf('F'), flintSteel, Character.valueOf('D'), Item.diamond, Character.valueOf('I'), Item.ingotIron, Character.valueOf('E'), Item.enderPearl, Character.valueOf('R'), Item.redstone);
            GameRegistry.addShapedRecipe(new ItemStack(blockPortalModifier), "ORO", "DIE", "OFO", Character.valueOf('O'), Block.obsidian, Character.valueOf('F'), flintSteel, Character.valueOf('D'), Item.diamond, Character.valueOf('I'), Item.ingotIron, Character.valueOf('E'), Item.enderPearl, Character.valueOf('R'), Item.redstone);
            GameRegistry.addShapedRecipe(new ItemStack(blockPortalModifier), "ORO", "EID", "OFO", Character.valueOf('O'), Block.obsidian, Character.valueOf('F'), flintSteel, Character.valueOf('D'), Item.diamond, Character.valueOf('I'), Item.ingotIron, Character.valueOf('E'), Item.enderPearl, Character.valueOf('R'), Item.redstone);
        }

        if (!Settings.DisableDHDRecipe && Settings.AllowDialHomeDevices)
        {
            // Basic Dialling Device
            GameRegistry.addShapedRecipe(new ItemStack(blockDialDeviceBasic), "SES", "IDI", "SRS", Character.valueOf('S'), Block.stone, Character.valueOf('E'), Item.enderPearl, Character.valueOf('D'), Item.diamond, Character.valueOf('I'), Item.ingotIron, Character.valueOf('R'), Item.redstone);
            GameRegistry.addShapedRecipe(new ItemStack(blockDialDeviceBasic), "SRS", "IDI", "SES", Character.valueOf('S'), Block.stone, Character.valueOf('E'), Item.enderPearl, Character.valueOf('D'), Item.diamond, Character.valueOf('I'), Item.ingotIron, Character.valueOf('R'), Item.redstone);

            // Dialling Device
            GameRegistry.addShapedRecipe(new ItemStack(blockDialDevice), "OMO", "DED", "ORO", Character.valueOf('O'), Block.obsidian, Character.valueOf('E'), Item.enderPearl, Character.valueOf('D'), Item.diamond, Character.valueOf('M'), Item.emerald, Character.valueOf('R'), Item.redstone);
            GameRegistry.addShapedRecipe(new ItemStack(blockDialDevice), "ORO", "DED", "OMO", Character.valueOf('O'), Block.obsidian, Character.valueOf('E'), Item.enderPearl, Character.valueOf('D'), Item.diamond, Character.valueOf('M'), Item.emerald, Character.valueOf('R'), Item.redstone);

            // Automatic Dialler
            GameRegistry.addShapedRecipe(new ItemStack(blockAutomaticDialler), "SOS", "IRI", "SOS", Character.valueOf('S'), Block.stone, Character.valueOf('O'), Block.obsidian, Character.valueOf('I'), Item.ingotIron, Character.valueOf('R'), Item.redstone);
        }
    }

    public void loadSettings(Configuration config)
    {
        Settings.config = config;

        // Block IDs
        BlockIds.ObsidianStairs = config.getBlock("ObsidianStairs", BlockIds.ObsidianStairs).getInt();
        BlockIds.NetherPortal = config.getBlock("NetherPortal", BlockIds.NetherPortal).getInt();
        BlockIds.PortalModifier = config.getBlock("PortalModifier", BlockIds.PortalModifier).getInt();
        BlockIds.DialHomeDevice = config.getBlock("DialDevice", BlockIds.DialHomeDevice).getInt();
        BlockIds.DialHomeDeviceBasic = config.getBlock("BasicDialDevice", BlockIds.DialHomeDeviceBasic).getInt();
        BlockIds.AutomaticDialler = config.getBlock("AutomaticDialler", BlockIds.AutomaticDialler).getInt();
        BlockIds.DummyPortal = config.getBlock("DummyPortal", BlockIds.DummyPortal).getInt();

        // Item IDs
        ItemIds.PortalModifierUpgrade = config.getItem("PortalModifierUpgrade", ItemIds.PortalModifierUpgrade).getInt();
        ItemIds.NetworkCard = config.getItem("NetworkCard", ItemIds.NetworkCard).getInt();
        ItemIds.MiscellaneousItems = config.getItem("MiscItems", ItemIds.MiscellaneousItems).getInt();
        ItemIds.EnhancedFlintAndSteel = config.getItem("UpgradedFlintAndSteel", ItemIds.EnhancedFlintAndSteel).getInt();

        // Boolean configs
        Settings.AllowDialHomeDevices = config.get("Settings", "AllowDialDevices", Settings.AllowDialHomeDevices).getBoolean(Settings.AllowDialHomeDevices);
        Settings.AllowPortalColours = config.get("Settings", "AllowPortalColours", Settings.AllowPortalColours).getBoolean(Settings.AllowPortalColours);
        Settings.AllowPortalModifiers = config.get("Settings", "AllowPortalModifiers", Settings.AllowPortalModifiers).getBoolean(Settings.AllowPortalModifiers);
        Settings.AllowFlintSteel = config.get("Settings", "AllowUpgradedFlintAndSteel", Settings.AllowFlintSteel).getBoolean(Settings.AllowFlintSteel);
        Settings.AllowObsidianStairs = config.get("Settings", "AllowObsidianStairs", Settings.AllowObsidianStairs).getBoolean(Settings.AllowObsidianStairs);
        Settings.DisableDHDRecipe = config.get("Settings", "DisableDialDeviceRecipe", Settings.DisableDHDRecipe).getBoolean(Settings.DisableDHDRecipe);
        Settings.DisableModifierRecipe = config.get("Settings", "DisableModifierRecipe", Settings.DisableModifierRecipe).getBoolean(Settings.DisableModifierRecipe);
        Settings.RenderPortalEffect = config.get("Effects", "RenderPortalEffect", Settings.RenderPortalEffect, "Renders the swirly effect when you're inside a portal").getBoolean(Settings.RenderPortalEffect);
        Settings.AdventureModeLimitation = config.get("Settings", "RestrictAdventurePlayers", Settings.AdventureModeLimitation, "Stops players in Adventure mode from using certain blocks").getBoolean(Settings.AdventureModeLimitation);

        // Integer configs
        Settings.SoundLevel = MathHelper.clampInt(config.get("Effects", "SoundLevel", 100, "Percentage chance of sounds to play (per block). 0 = disabled, 100 = vanilla").getInt(), 0, 100);
        Settings.ParticleLevel = MathHelper.clampInt(config.get("Effects", "ParticleLevel", 100, "Percentage chance of particles to spawn. 0 = disabled, 100 = vanilla").getInt(), 0, 100);
        Settings.PigmenLevel = MathHelper.clampInt(config.get("Settings", "PigmenLevel", 100, "Percentage chance of pigmen to spawn. 0 = disabled, 100 = vanilla").getInt(), 0, 100);

        // String configs
        Settings.addExcludedBlocks(config.get("Blocks", "ExcludedTextureBlocks", "", "These blocks are excluded from being used as portal textures. Seperate IDs by a single space").getString());
        Settings.addBorderBlocks(config.get("Blocks", "BorderBlocks", "", "These blocks will be valid for portal frames. Seperate IDs by a single space").getString());
        Settings.addDestroyBlocks(config.get("Blocks", "DestroyBlocks", "18 30 31 32 37 38 39 40 78 106 131 132 141 142", "These blocks will get removed when creating a portal. Seperate IDs by a single space").getString());

        config.save();
    }

    public void loadTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityNetherPortal.class, "EPNPortal");
        GameRegistry.registerTileEntity(TileEntityPortalModifier.class, "EPPModifier");
        GameRegistry.registerTileEntity(TileEntityDialDevice.class, "EPDDevice");
        GameRegistry.registerTileEntity(TileEntityDialDeviceBasic.class, "EPDDBasic");
        GameRegistry.registerTileEntity(TileEntityAutomaticDialler.class, "EPADialler");
    }

    public void registerIcons(TextureStitchEvent.Pre event)
    {

    }
}
