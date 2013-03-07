package alz.mods.enhancedportals.common;

import alz.mods.enhancedportals.block.BlockFire;
import alz.mods.enhancedportals.block.BlockNetherPortal;
import alz.mods.enhancedportals.block.BlockObsidian;
import alz.mods.enhancedportals.block.BlockPortalModifier;
import alz.mods.enhancedportals.block.BlockStairsObsidian;
import alz.mods.enhancedportals.item.ItemUpgrade;
import alz.mods.enhancedportals.reference.BlockID;
import alz.mods.enhancedportals.reference.ItemID;
import alz.mods.enhancedportals.reference.Language;
import alz.mods.enhancedportals.reference.Logger;
import alz.mods.enhancedportals.reference.ModData;
import alz.mods.enhancedportals.reference.Settings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid=ModData.ID, name=ModData.Name, version=ModData.Version)
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={ModData.ID}, packetHandler=PacketHandler.class)
public class EnhancedPortals
{
	@SidedProxy(clientSide="alz.mods.enhancedportals.client.ClientProxy", serverSide="alz.mods.enhancedportals.common.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance(ModData.ID)
	public static EnhancedPortals instance;
	
	public BlockNetherPortal blockNetherPortal;
	public BlockFire blockFire;
	//public BlockEndPortal blockEndPortal;
	//public BlockEndFrame blockEndFrame;
	public BlockObsidian blockObsidian;	
	public BlockPortalModifier blockPortalModifier;	
	public BlockStairsObsidian blockStairsObsidian;
	
	public ItemUpgrade itemUpgrade;
	
	@PreInit
	public void preLoad(FMLPreInitializationEvent event)
	{
		Settings.ConfigFile = new Configuration(event.getSuggestedConfigurationFile());
		Logger.Logger = event.getModLog();
		
		Settings.AllowTeleporting = Settings.GetFromConfig("AllowTeleporting", true);
		Settings.CanDyePortals = Settings.GetFromConfig("CanDyePortals", true);
		Settings.DoesDyingCost = Settings.GetFromConfig("DoesDyingCost", true);
		Settings.AllowModifiers = Settings.GetFromConfig("AllowModifiers", true);
		Settings.AllowObsidianStairs = Settings.GetFromConfig("AllowObsidianStairs", true);
		Settings.PrintPortalMessages = Settings.GetFromConfig("PrintPortalCreationMessages", true);
		
		Settings.PigmenSpawnChance = Settings.GetFromConfig("PigmenSpawnChance", 100, 0, 100);
		Settings.SoundLevel = Settings.GetFromConfig("SoundLevel", 100, 0, 100);
		Settings.ParticleLevel = Settings.GetFromConfig("ParticleLevel", 100, 0, 100);
		
		BlockID.ObsidianStairs = Settings.GetFromConfig("ObsidianStairsID", 512, true);
		BlockID.PortalModifier = Settings.GetFromConfig("PortalModifierID", 513, true);
		ItemID.ModifierUpgrade = Settings.GetFromConfig("PortalModifierUpgradeID", 6000, false);
		
		Language.NoUpgrade = Settings.GetFromConfig("TEXT_NoUpgrade", "You do not have the required upgrade.");
		Language.ExitBlocked = Settings.GetFromConfig("TEXT_ExitBlocked", "The exit portal is blocked or invalid.");
		Language.NoExit = Settings.GetFromConfig("TEXT_NoExit", "Couldn't find a valid exit.");
		
		Language.Frequency = Settings.GetFromConfig("TEXT_Frequency", "Frequency");
		Language.Upgrades = Settings.GetFromConfig("TEXT_Upgrades", "Upgrades");
		Language.PortalModifierTitle = Settings.GetFromConfig("TEXT_PortalModifier", "Portal Modifier");
	
		Settings.AddToBorderBlocks(Settings.GetFromConfig("CustomBorderBlocks", ""));
		Settings.AddToDestroyBlocks(Settings.GetFromConfig("CustomDestroyBlocks", "8,9,10,11,6,18,30,31,32,37,38,39,40,78,104,105,106,111,115"));
		
		Settings.ConfigFile.save();
	}
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		Block.blocksList[49] = null;
		Block.blocksList[51] = null;
		Block.blocksList[90] = null;
		//Block.blocksList[119] = null;
		
		blockNetherPortal = new BlockNetherPortal();
		blockFire = new BlockFire();
		//blockEndPortal = new BlockEndPortal();
		blockObsidian = new BlockObsidian();
		
		if (Settings.AllowModifiers)
		{
			itemUpgrade = new ItemUpgrade();
			blockPortalModifier = new BlockPortalModifier();
		
			GameRegistry.registerBlock(blockPortalModifier, "blockPortalModifier");
			GameRegistry.registerTileEntity(TileEntityPortalModifier.class, "epmodif");
		
			LanguageRegistry.addName(blockPortalModifier, "Portal Modifier");
			LanguageRegistry.addName(itemUpgrade, "Portal Modifier Upgrade");
		
			GameRegistry.addRecipe(new ItemStack(blockPortalModifier), "OFO", "IDI", "ORO", Character.valueOf('O'), Block.obsidian, Character.valueOf('F'), Item.flintAndSteel, Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), Item.diamond, Character.valueOf('R'), Item.redstone);
			GameRegistry.addRecipe(new ItemStack(itemUpgrade, 1), "XYX", "IKI", "XRX", Character.valueOf('X'), Item.goldNugget, Character.valueOf('Y'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('K'), Item.paper, Character.valueOf('I'), Item.ingotIron, Character.valueOf('R'), Item.redstone);
			GameRegistry.addRecipe(new ItemStack(itemUpgrade, 1, 1), "XYX", "IKI", "XRX", Character.valueOf('X'), Item.goldNugget, Character.valueOf('Y'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('K'), Item.paper, Character.valueOf('I'), Item.ingotGold, Character.valueOf('R'), Item.redstone);
			GameRegistry.addRecipe(new ItemStack(itemUpgrade, 1, 2), "GIG", "IUI", "GDG", Character.valueOf('G'), Item.ingotGold, Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), Item.diamond, Character.valueOf('U'), new ItemStack(itemUpgrade, 1, 1));
		
			Settings.BorderBlocks.add(BlockID.PortalModifier);
		}
		
		if (Settings.AllowObsidianStairs)
		{
			blockStairsObsidian = new BlockStairsObsidian();
			
			GameRegistry.registerBlock(blockStairsObsidian, "blockStairsObsidian");
			LanguageRegistry.addName(blockStairsObsidian, "Obsidian Stairs");
			GameRegistry.addRecipe(new ItemStack(blockStairsObsidian, 4), "X  ", "XX ", "XXX", Character.valueOf('X'), Block.obsidian);
			Settings.BorderBlocks.add(BlockID.ObsidianStairs);
		}
		
		proxy.setupRenderers();

		// Events etc
		MinecraftForge.EVENT_BUS.register(new EventHooks());
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		
		// Add stuff to the removable & border blocks.
		Settings.RemovableBlocks.add(0);
		Settings.RemovableBlocks.add(Block.fire.blockID);
		Settings.BorderBlocks.add(blockObsidian.blockID);
	}
}
