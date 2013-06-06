package enhancedportals;

import java.io.File;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedcore.reflection.ReflectionHelper;
import enhancedportals.block.BlockObsidian;
import enhancedportals.command.CommandEP;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Settings;
import enhancedportals.network.CommonProxy;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.PacketHandler;
import enhancedportals.portal.network.DialDeviceNetwork;
import enhancedportals.portal.network.ModifierNetwork;
import enhancedportals.world.DialDeviceChunkCallback;

@Mod(name = Reference.MOD_NAME, modid = Reference.MOD_ID, version = Reference.MOD_VERSION, dependencies = "required-after:EnhancedCore")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class, channels = { Reference.MOD_ID })
public class EnhancedPortals
{
    @Instance(Reference.MOD_ID)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy     proxy;

    @Init
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(this);

        proxy.loadBlocks();
        proxy.loadItems();
        proxy.loadTileEntities();
        proxy.loadRecipes();

        // Add blocks to border list
        Settings.BorderBlocks.add(BlockIds.Obsidian);
        Settings.BorderBlocks.add(BlockIds.ObsidianStairs);
        Settings.BorderBlocks.add(BlockIds.PortalModifier);

        // Add blocks to destroy list
        Settings.DestroyBlocks.add(0);
        Settings.DestroyBlocks.add(Block.fire.blockID);

        // Row 1
        Reference.glyphItems.add(new ItemStack(Item.diamond));
        Reference.glyphItems.add(new ItemStack(Item.emerald));
        Reference.glyphItems.add(new ItemStack(Item.goldNugget));
        Reference.glyphItems.add(new ItemStack(Item.redstone));
        Reference.glyphItems.add(new ItemStack(Item.ingotIron));
        Reference.glyphItems.add(new ItemStack(Item.lightStoneDust));
        Reference.glyphItems.add(new ItemStack(Item.netherQuartz));
        Reference.glyphItems.add(new ItemStack(Item.bucketLava));
        Reference.glyphItems.add(new ItemStack(Item.dyePowder, 1, 4));

        // Row 2
        Reference.glyphItems.add(new ItemStack(Item.appleGold));
        Reference.glyphItems.add(new ItemStack(Item.blazeRod));
        Reference.glyphItems.add(new ItemStack(Item.slimeBall));
        Reference.glyphItems.add(new ItemStack(Item.goldenCarrot));
        Reference.glyphItems.add(new ItemStack(Item.enderPearl));
        Reference.glyphItems.add(new ItemStack(Item.fireballCharge));
        Reference.glyphItems.add(new ItemStack(Item.netherStar));
        Reference.glyphItems.add(new ItemStack(Item.ghastTear));
        Reference.glyphItems.add(new ItemStack(Item.magmaCream));

        // Row 3
        Reference.glyphItems.add(new ItemStack(Item.eyeOfEnder));
        Reference.glyphItems.add(new ItemStack(Item.firework));
        Reference.glyphItems.add(new ItemStack(Item.ingotGold));
        Reference.glyphItems.add(new ItemStack(Item.pickaxeDiamond));
        Reference.glyphItems.add(new ItemStack(Item.gunpowder));
        Reference.glyphItems.add(new ItemStack(Item.pocketSundial));
        Reference.glyphItems.add(new ItemStack(Item.writableBook));
        Reference.glyphItems.add(new ItemStack(Item.potion, 1, 5));
        Reference.glyphItems.add(new ItemStack(Item.cake));
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        ForgeChunkManager.setForcedChunkLoadingCallback(instance, new DialDeviceChunkCallback());
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        Reference.log.setParent(FMLLog.getLogger());
        Block.blocksList[BlockIds.Obsidian] = null;

        if (!ReflectionHelper.replaceBlock(new BlockObsidian(), net.minecraft.block.BlockObsidian.class))
        {
            Block.blocksList[BlockIds.Obsidian] = null;
            Block.blocksList[BlockIds.Obsidian] = new net.minecraft.block.BlockObsidian(49).setHardness(50.0F).setResistance(2000.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("obsidian");
            Reference.log.log(Level.SEVERE, "Unable to modify the Obsidian block. A lot of functionality will be lost.");
        }

        proxy.loadSettings(new Configuration(new File(event.getModConfigurationDirectory(), "EnhancedPortals 2.cfg")));
        Localization.loadLocales();
    }

    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void registerIcons(TextureStitchEvent.Pre event)
    {
        proxy.registerIcons(event);
    }

    @ServerStarting
    private void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandEP());

        proxy.ModifierNetwork = new ModifierNetwork(event.getServer());
        proxy.DialDeviceNetwork = new DialDeviceNetwork(event.getServer());
    }

    @ServerStopping
    private void serverStopping(FMLServerStoppingEvent event)
    {
        proxy.ModifierNetwork.saveData();
        proxy.DialDeviceNetwork.saveData();
    }

    @ForgeSubscribe
    public void worldSave(WorldEvent.Save event)
    {
        if (!event.world.isRemote)
        {
            proxy.ModifierNetwork.saveData();
            proxy.DialDeviceNetwork.saveData();
        }
    }
}
