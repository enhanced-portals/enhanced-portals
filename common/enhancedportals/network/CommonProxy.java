package enhancedportals.network;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.block.BlockAutomaticDialler;
import enhancedportals.block.BlockDialDevice;
import enhancedportals.block.BlockDialDeviceBasic;
import enhancedportals.block.BlockDummyPortal;
import enhancedportals.block.BlockNetherPortal;
import enhancedportals.block.BlockObsidian;
import enhancedportals.block.BlockObsidianStairs;
import enhancedportals.block.BlockPortalModifier;
import enhancedportals.item.ItemEnhancedFlintSteel;
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
    public BlockNetherPortal         blockNetherPortal;
    public BlockObsidian             blockObsidian;
    public BlockObsidianStairs       blockObsidianStairs;
    public BlockPortalModifier       blockPortalModifier;
    public BlockDummyPortal          blockDummyPortal;
    public BlockDialDevice           blockDialDevice;
    public BlockDialDeviceBasic      blockDialDeviceBasic;
    public BlockAutomaticDialler     blockAutomaticDialler;

    public ItemPortalModifierUpgrade portalModifierUpgrade;
    public ItemNetworkCard           networkCard;
    public ItemEnhancedFlintSteel    enhancedFlintSteel;

    public ModifierNetwork           ModifierNetwork;
    public DialDeviceNetwork         DialDeviceNetwork;
    public boolean                   isIdentifierTaken = false;

    public World getWorld(int dimension)
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimension);
    }

    public void loadBlocks()
    {
        blockNetherPortal = new BlockNetherPortal();
        blockPortalModifier = new BlockPortalModifier();
        blockObsidianStairs = new BlockObsidianStairs();
        blockDialDevice = new BlockDialDevice();
        blockDialDeviceBasic = new BlockDialDeviceBasic();
        blockAutomaticDialler = new BlockAutomaticDialler();
        blockDummyPortal = new BlockDummyPortal();

        GameRegistry.registerBlock(blockNetherPortal, Localization.NetherPortal_Name);
        GameRegistry.registerBlock(blockPortalModifier, Localization.PortalModifier_Name);
        GameRegistry.registerBlock(blockObsidianStairs, Localization.ObsidianStairs_Name);
        GameRegistry.registerBlock(blockDummyPortal, "dummyPortal");
        GameRegistry.registerBlock(blockDialDevice, Localization.DialDevice_Name);
        GameRegistry.registerBlock(blockDialDeviceBasic, Localization.DialDeviceBasic_Name);
        GameRegistry.registerBlock(blockAutomaticDialler, Localization.AutomaticDialler_Name);
    }

    public void loadItems()
    {
        portalModifierUpgrade = new ItemPortalModifierUpgrade();
        networkCard = new ItemNetworkCard();
        enhancedFlintSteel = new ItemEnhancedFlintSteel();

        GameRegistry.registerItem(portalModifierUpgrade, Localization.PortalModifierUpgrade_Name);
        GameRegistry.registerItem(networkCard, Localization.NetworkCard_Name);
        GameRegistry.registerItem(enhancedFlintSteel, Localization.EnhancedFlintSteel_Name);
    }

    public void loadRecipes()
    {

    }

    public void loadSettings(Configuration config)
    {
        Settings.config = config;

        // Block IDs
        BlockIds.ObsidianStairs = config.getBlock("ObsidianStairs", BlockIds.ObsidianStairs).getInt();
        BlockIds.NetherPortal = config.getBlock("NetherPortal", BlockIds.NetherPortal).getInt();
        BlockIds.PortalModifier = config.getBlock("PortalModifier", BlockIds.PortalModifier).getInt();
        BlockIds.DialHomeDevice = config.getBlock("DialDevice", BlockIds.DialHomeDevice).getInt();

        // Item IDs
        ItemIds.PortalModifierUpgrade = config.getItem("PortalModifierUpgrade", ItemIds.PortalModifierUpgrade).getInt();
        ItemIds.NetworkCard = config.getItem("NetworkCard", ItemIds.NetworkCard).getInt();

        // Boolean configs
        Settings.AllowDialHomeDevices = config.get("Settings", "AllowDialDevices", Settings.AllowDialHomeDevices).getBoolean(Settings.AllowDialHomeDevices);
        Settings.AllowPortalColours = config.get("Settings", "AllowPortalColours", Settings.AllowPortalColours).getBoolean(Settings.AllowPortalColours);
        Settings.AllowPortalModifiers = config.get("Settings", "AllowPortalModifiers", Settings.AllowPortalModifiers).getBoolean(Settings.AllowPortalModifiers);
        Settings.DisableDHDRecipe = config.get("Settings", "DisableDialDeviceRecipe", Settings.DisableDHDRecipe).getBoolean(Settings.DisableDHDRecipe);
        Settings.DisableModifierRecipe = config.get("Settings", "DisableModifierRecipe", Settings.DisableModifierRecipe).getBoolean(Settings.DisableModifierRecipe);
        Settings.RenderPortalEffect = config.get("Effects", "RenderPortalEffect", Settings.RenderPortalEffect).getBoolean(Settings.RenderPortalEffect);

        // Integer configs
        Settings.SoundLevel = MathHelper.clamp_int(config.get("Effects", "SoundLevel", 100).getInt(), 0, 100);
        Settings.ParticleLevel = MathHelper.clamp_int(config.get("Effects", "ParticleLevel", 100).getInt(), 0, 100);
        Settings.PigmenLevel = MathHelper.clamp_int(config.get("Settings", "PigmenLevel", 100).getInt(), 0, 100);

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
