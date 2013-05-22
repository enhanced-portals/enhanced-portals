package enhancedportals;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import enhancedportals.block.BlockObsidian;
import enhancedportals.command.CommandEP;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Settings;
import enhancedportals.network.ClientPacketHandler;
import enhancedportals.network.CommonProxy;
import enhancedportals.network.EventHooks;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.ServerPacketHandler;
import enhancedportals.portal.network.ModifierNetwork;

@Mod(name = Reference.MOD_NAME, modid = Reference.MOD_ID, version = Reference.MOD_VERSION, acceptedMinecraftVersions = Reference.MC_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @SidedPacketHandler(channels = { Reference.MOD_ID }, packetHandler = ClientPacketHandler.class), serverPacketHandlerSpec = @SidedPacketHandler(channels = { Reference.MOD_ID }, packetHandler = ServerPacketHandler.class))
public class EnhancedPortals
{
    @Instance(Reference.MOD_ID)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy proxy;

    @Init
    private void init(FMLInitializationEvent event)
    {
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new EventHooks());

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

        Reference.glyphValues.add("diamond");
        Reference.glyphValues.add("emerald");
        Reference.glyphValues.add("goldNugget");
        Reference.glyphValues.add("redstone");
        Reference.glyphValues.add("ironIngot");
        Reference.glyphValues.add("glowstoneDust");
        Reference.glyphValues.add("netherQuartz");
        Reference.glyphValues.add("lavaBucket");
        Reference.glyphValues.add("lapisLazuli");

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

        Reference.glyphValues.add("goldenApple");
        Reference.glyphValues.add("blazeRod");
        Reference.glyphValues.add("slimeBall");
        Reference.glyphValues.add("goldenCarrot");
        Reference.glyphValues.add("enderPearl");
        Reference.glyphValues.add("fireCharge");
        Reference.glyphValues.add("netherStar");
        Reference.glyphValues.add("ghastTear");
        Reference.glyphValues.add("magmaCream");

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

        Reference.glyphValues.add("eyeOfEnder");
        Reference.glyphValues.add("firework");
        Reference.glyphValues.add("goldIngot");
        Reference.glyphValues.add("diamondPickaxe");
        Reference.glyphValues.add("gunpowder");
        Reference.glyphValues.add("clock");
        Reference.glyphValues.add("bookAndQuill");
        Reference.glyphValues.add("potion");
        Reference.glyphValues.add("cake");
    }

    @PreInit
    private void preInit(FMLPreInitializationEvent event)
    {
        Reference.log.setParent(FMLLog.getLogger());
        Block.blocksList[BlockIds.Obsidian] = null;

        if (!setField(Block.class, new BlockObsidian(), net.minecraft.block.BlockObsidian.class))
        {
            Block.blocksList[BlockIds.Obsidian] = null;
            Block.blocksList[BlockIds.Obsidian] = new net.minecraft.block.BlockObsidian(49).setHardness(50.0F).setResistance(2000.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("obsidian");
            Reference.log.log(Level.SEVERE, "Unable to modify the Obsidian block.");
        }

        proxy.loadSettings(new Configuration(new File(event.getModConfigurationDirectory(), "EnhancedPortals 2.cfg")));
        Localization.loadLocales();
    }

    @ServerStarting
    private void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandEP());

        proxy.ModifierNetwork = new ModifierNetwork(event.getServer());
    }

    @ServerStopping
    private void serverStopping(FMLServerStoppingEvent event)
    {
        proxy.ModifierNetwork.saveData();
    }

    private boolean setField(Class<?> cls, Object value, Class<?> clas)
    {
        Field field = null;

        for (Field f : cls.getDeclaredFields())
        {
            if (f.getType() == net.minecraft.block.Block.class)
            {
                try
                {
                    if ((Block) f.get(null) instanceof net.minecraft.block.BlockObsidian)
                    {
                        field = f;
                    }

                }
                catch (Exception e)
                {
                    return false;
                }
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
                e.printStackTrace();
                return false;
            }
        }

        try
        {
            field.set(cls, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
