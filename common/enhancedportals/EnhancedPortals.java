package enhancedportals;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
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
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Settings;
import enhancedportals.network.ClientPacketHandler;
import enhancedportals.network.CommonProxy;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.ServerPacketHandler;
import enhancedportals.portal.PortalTexture;

@Mod(name = Reference.MOD_NAME, modid = Reference.MOD_ID, version = Reference.MOD_VERSION)
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

        // Map items to textures
        Settings.ItemPortalTextureMap.put(Item.bucketLava.itemID + ":0", new PortalTexture(10, 0));
        Settings.ItemPortalTextureMap.put(Item.bucketLava.itemID + ":0_", new PortalTexture(11, 0));
        Settings.ItemPortalTextureMap.put(Item.bucketWater.itemID + ":0", new PortalTexture(8, 0));
        Settings.ItemPortalTextureMap.put(Item.bucketWater.itemID + ":0_", new PortalTexture(9, 0));

        // Row 1
        Reference.glyphItems.add(new ItemStack(Item.diamond));
        Reference.glyphItems.add(new ItemStack(Item.emerald));
        Reference.glyphItems.add(new ItemStack(Item.goldNugget));
        Reference.glyphItems.add(new ItemStack(Item.redstone));
        Reference.glyphItems.add(new ItemStack(Item.ingotIron));
        Reference.glyphItems.add(new ItemStack(Item.lightStoneDust));
        Reference.glyphItems.add(new ItemStack(Item.netherQuartz));
        Reference.glyphItems.add(new ItemStack(Item.bucketLava));
        Reference.glyphItems.add(new ItemStack(Item.dyePowder, 1, PortalTexture.Colour.BLUE.ordinal()));

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
        Reference.glyphItems.add(new ItemStack(Item.potion));
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

    @PostInit
    private void postInit(FMLPostInitializationEvent event)
    {

    }

    @PreInit
    private void preInit(FMLPreInitializationEvent event)
    {
        proxy.loadSettings(new Configuration(event.getSuggestedConfigurationFile()));
        proxy.loadBlocks();
        proxy.loadItems();
        proxy.loadTileEntities();
        proxy.loadRecipes();

        Localization.loadLocales();
    }

    @ServerStarting
    private void serverStarting(FMLServerStartingEvent event)
    {
        // Load up networks
    }

    @ServerStopping
    private void serverStopping(FMLServerStoppingEvent event)
    {
        // Save networks
    }
}
