package enhancedportals.network;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.block.BlockDummyPortal;
import enhancedportals.block.BlockNetherPortal;
import enhancedportals.block.BlockObsidian;
import enhancedportals.block.BlockObsidianStairs;
import enhancedportals.block.BlockPortalModifier;
import enhancedportals.item.ItemPortalModifierUpgrade;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Settings;
import enhancedportals.portal.network.ModifierNetwork;
import enhancedportals.tileentity.TileEntityNetherPortal;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class CommonProxy
{
    public BlockNetherPortal blockNetherPortal;
    public BlockObsidian blockObsidian;
    public BlockObsidianStairs blockObsidianStairs;
    public BlockPortalModifier blockPortalModifier;
    public BlockDummyPortal blockDummyPortal;

    public ItemPortalModifierUpgrade portalModifierUpgrade;
    
    public ModifierNetwork ModifierNetwork;
    
    public World getWorld(int dimension)
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimension);
    }

    public void loadBlocks()
    {
        Block.blocksList[BlockIds.Obsidian] = null;

        blockNetherPortal = new BlockNetherPortal();
        blockPortalModifier = new BlockPortalModifier();
        blockObsidian = new BlockObsidian();
        blockObsidianStairs = new BlockObsidianStairs();
        blockDummyPortal = new BlockDummyPortal();

        GameRegistry.registerBlock(blockNetherPortal, Localization.NetherPortal_Name);
        GameRegistry.registerBlock(blockPortalModifier, Localization.PortalModifier_Name);
        GameRegistry.registerBlock(blockObsidian, Localization.Obsidian_Name);
        GameRegistry.registerBlock(blockObsidianStairs, Localization.ObsidianStairs_Name);
        GameRegistry.registerBlock(blockDummyPortal, "Dummy Portal");
    }

    public void loadItems()
    {
        portalModifierUpgrade = new ItemPortalModifierUpgrade();
        
        GameRegistry.registerItem(portalModifierUpgrade, Localization.PortalModifierUpgrade_Name);
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

        // Boolean configs
        Settings.AllowDialHomeDevices = config.get("Settings", "AllowDialDevices", Settings.AllowDialHomeDevices).getBoolean(Settings.AllowDialHomeDevices);
        Settings.AllowPortalColours = config.get("Settings", "AllowPortalColours", Settings.AllowPortalColours).getBoolean(Settings.AllowPortalColours);
        Settings.AllowPortalModifiers = config.get("Settings", "AllowPortalModifiers", Settings.AllowPortalModifiers).getBoolean(Settings.AllowPortalModifiers);
        Settings.DisableDHDRecipe = config.get("Settings", "DisableDialDeviceRecipe", Settings.DisableDHDRecipe).getBoolean(Settings.DisableDHDRecipe);
        Settings.DisableModifierRecipe = config.get("Settings", "DisableModifierRecipe", Settings.DisableModifierRecipe).getBoolean(Settings.DisableModifierRecipe);
        Settings.RenderPortalEffect = config.get("Settings", "RenderPortalEffect", Settings.RenderPortalEffect).getBoolean(Settings.RenderPortalEffect);

        // Integer configs
        Settings.SoundLevel = MathHelper.clamp_int(config.get("Settings", "SoundLevel", 100).getInt(), 0, 100);
        Settings.ParticleLevel = MathHelper.clamp_int(config.get("Settings", "ParticleLevel", 100).getInt(), 0, 100);
        Settings.PigmenLevel = MathHelper.clamp_int(config.get("Settings", "PigmenLevel", 100).getInt(), 0, 100);

        config.save();
    }

    public void loadTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityNetherPortal.class, "EPNPortal");
        GameRegistry.registerTileEntity(TileEntityPortalModifier.class, "EPPModifier");
    }
}
