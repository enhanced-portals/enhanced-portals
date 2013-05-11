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

        Settings.ItemPortalTextureMap.put(Item.bucketLava.itemID + ":0", new PortalTexture(10, 0));
        Settings.ItemPortalTextureMap.put(Item.bucketLava.itemID + ":0_", new PortalTexture(11, 0));
        Settings.ItemPortalTextureMap.put(Item.bucketWater.itemID + ":0", new PortalTexture(8, 0));
        Settings.ItemPortalTextureMap.put(Item.bucketWater.itemID + ":0_", new PortalTexture(9, 0));

        // Row 1
        Reference.glyphItems.add(new ItemStack(Item.diamond));
        Reference.glyphItems.add(new ItemStack(Item.redstone));
        Reference.glyphItems.add(new ItemStack(Item.flint));
        Reference.glyphItems.add(new ItemStack(Item.arrow));
        Reference.glyphItems.add(new ItemStack(Item.boat));
        Reference.glyphItems.add(new ItemStack(Item.beefCooked));
        Reference.glyphItems.add(new ItemStack(Item.bone));
        Reference.glyphItems.add(new ItemStack(Item.brick));
        Reference.glyphItems.add(new ItemStack(Item.blazeRod));

        Reference.glyphValues.add("diamond");
        Reference.glyphValues.add("redstone");
        Reference.glyphValues.add("flint");
        Reference.glyphValues.add("arrow");
        Reference.glyphValues.add("boat");
        Reference.glyphValues.add("steak");
        Reference.glyphValues.add("bone");
        Reference.glyphValues.add("brick");
        Reference.glyphValues.add("blazeRod");

        // Row 2
        Reference.glyphItems.add(new ItemStack(Item.bread));
        Reference.glyphItems.add(new ItemStack(Item.gunpowder));
        Reference.glyphItems.add(new ItemStack(Item.goldNugget));
        Reference.glyphItems.add(new ItemStack(Item.paper));
        Reference.glyphItems.add(new ItemStack(Item.porkCooked));
        Reference.glyphItems.add(new ItemStack(Item.coal));
        Reference.glyphItems.add(new ItemStack(Item.bucketEmpty));
        Reference.glyphItems.add(new ItemStack(Item.egg));
        Reference.glyphItems.add(new ItemStack(Item.feather));

        Reference.glyphValues.add("bread");
        Reference.glyphValues.add("gunpowder");
        Reference.glyphValues.add("goldNugget");
        Reference.glyphValues.add("paper");
        Reference.glyphValues.add("porkchop");
        Reference.glyphValues.add("coal");
        Reference.glyphValues.add("bucket");
        Reference.glyphValues.add("egg");
        Reference.glyphValues.add("feather");

        // Row 3
        Reference.glyphItems.add(new ItemStack(Item.appleRed));
        Reference.glyphItems.add(new ItemStack(Item.leather));
        Reference.glyphItems.add(new ItemStack(Item.ghastTear));
        Reference.glyphItems.add(new ItemStack(Item.bow));
        Reference.glyphItems.add(new ItemStack(Item.fishingRod));
        Reference.glyphItems.add(new ItemStack(Item.potato));
        Reference.glyphItems.add(new ItemStack(Item.book));
        Reference.glyphItems.add(new ItemStack(Item.shears));
        Reference.glyphItems.add(new ItemStack(Item.slimeBall));

        Reference.glyphValues.add("apple");
        Reference.glyphValues.add("leather");
        Reference.glyphValues.add("ghastTear");
        Reference.glyphValues.add("bow");
        Reference.glyphValues.add("fishingRod");
        Reference.glyphValues.add("potato");
        Reference.glyphValues.add("book");
        Reference.glyphValues.add("shears");
        Reference.glyphValues.add("slimeBall");
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
