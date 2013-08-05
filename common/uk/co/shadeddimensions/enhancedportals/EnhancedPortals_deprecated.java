package uk.co.shadeddimensions.enhancedportals;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedcore.reflection.ReflectionHelper;
import enhancedportals.block.BlockObsidian;
import enhancedportals.command.CommandEP;
import enhancedportals.computercraft.block.BlockDialDeviceBasic_cc;
import enhancedportals.computercraft.block.BlockDialDevice_cc;
import enhancedportals.computercraft.block.BlockPortalModifier_cc;
import enhancedportals.computercraft.tileentity.TileEntityDialDeviceBasic_cc;
import enhancedportals.computercraft.tileentity.TileEntityDialDevice_cc;
import enhancedportals.computercraft.tileentity.TileEntityPortalModifier_cc;
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
import enhancedportals.world.EPTeleporter;

@Mod(name = Reference.MOD_NAME + " (Deprecation Support)", modid = Reference.MOD_ID, version = Reference.MOD_VERSION, dependencies = "required-after:EnhancedCore@[1.1.1,);required-after:Forge@[9.10.0.780,)", acceptedMinecraftVersions = "[1.6.2,)")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class, channels = { Reference.MOD_ID })
public class EnhancedPortals_deprecated
{
    @Instance(Reference.MOD_ID)
    public static EnhancedPortals_deprecated instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy proxy;

    @EventHandler
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
        Settings.DestroyBlocks.add(Block.fire.blockID);

        // Row 1
        Reference.glyphItems.add(new ItemStack(Item.diamond));
        Reference.glyphItems.add(new ItemStack(Item.emerald));
        Reference.glyphItems.add(new ItemStack(Item.goldNugget));
        Reference.glyphItems.add(new ItemStack(Item.redstone));
        Reference.glyphItems.add(new ItemStack(Item.ingotIron));
        Reference.glyphItems.add(new ItemStack(Item.glowstone));
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
        
        if (Loader.isModLoaded("ComputerCraft"))
        {
            GameRegistry.registerTileEntity(TileEntityPortalModifier_cc.class, "EPPModifierCC");
            GameRegistry.registerTileEntity(TileEntityDialDeviceBasic_cc.class, "EPPBDialCC");
            GameRegistry.registerTileEntity(TileEntityDialDevice_cc.class, "EPPDialCC");
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ForgeChunkManager.setForcedChunkLoadingCallback(instance, new DialDeviceChunkCallback());
        
        if (Loader.isModLoaded("ComputerCraft"))
        {
            Block.blocksList[BlockIds.PortalModifier] = null;
            Block.blocksList[BlockIds.DialDeviceBasic] = null;
            Block.blocksList[BlockIds.DialDevice] = null;

            EnhancedPortals_deprecated.proxy.blockPortalModifier = null;
            EnhancedPortals_deprecated.proxy.blockDialDeviceBasic = null;
            EnhancedPortals_deprecated.proxy.blockDialDevice = null;

            EnhancedPortals_deprecated.proxy.blockPortalModifier = new BlockPortalModifier_cc();
            EnhancedPortals_deprecated.proxy.blockDialDeviceBasic = new BlockDialDeviceBasic_cc();
            EnhancedPortals_deprecated.proxy.blockDialDevice = new BlockDialDevice_cc();
        }
    }

    @EventHandler
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

    private boolean reflectTeleporter(WorldServer world, EPTeleporter teleporter)
    {
        Field field = null;

        for (Field f : net.minecraft.world.WorldServer.class.getDeclaredFields())
        {
            if (f.getType() == net.minecraft.world.Teleporter.class)
            {
                field = f;
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
            field.set(world, teleporter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void registerIcons(TextureStitchEvent.Pre event)
    {
        proxy.registerIcons(event);
    }

    @EventHandler
    private void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandEP());

        proxy.ModifierNetwork = new ModifierNetwork(event.getServer());
        proxy.DialDeviceNetwork = new DialDeviceNetwork(event.getServer());
    }

    @EventHandler
    private void serverStopping(FMLServerStoppingEvent event)
    {
        proxy.ModifierNetwork.saveData();
        proxy.DialDeviceNetwork.saveData();
    }

    @ForgeSubscribe
    public void worldLoad(WorldEvent.Load event)
    {
        if (!event.world.isRemote)
        {
            WorldServer serverWorld = (WorldServer) event.world;

            if (!(serverWorld.field_85177_Q instanceof EPTeleporter))
            {
                if (!reflectTeleporter(serverWorld, new EPTeleporter(serverWorld)))
                {
                    Reference.log.log(Level.SEVERE, "Could not overwrite the default Teleporter. Minecraft will create new portals instead of using custom ones!");
                }
            }
        }
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
