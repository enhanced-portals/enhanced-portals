package alz.mods.enhancedportals.common;

import java.util.Locale.Category;

import alz.mods.enhancedportals.block.BlockFire;
import alz.mods.enhancedportals.block.BlockNetherPortal;
import alz.mods.enhancedportals.block.BlockObsidian;
import alz.mods.enhancedportals.block.BlockPortalModifier;
import alz.mods.enhancedportals.block.BlockStairsObsidian;
import alz.mods.enhancedportals.item.ItemUpgrade;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
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
import cpw.mods.fml.relauncher.Side;

@Mod(modid=Reference.modID, name=Reference.modName, version=Reference.modVersion)
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={Reference.modID}, packetHandler=PacketHandler.class)
public class EnhancedPortals
{
	@SidedProxy(clientSide="alz.mods.enhancedportals.client.ClientProxy", serverSide="alz.mods.enhancedportals.common.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance(Reference.modID)
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
		Reference.ConfigFile = new Configuration(event.getSuggestedConfigurationFile());
		Reference.Logger = event.getModLog();
		
		Reference.allowTeleporting = Reference.GetFromConfig("AllowTeleporting", true);
		Reference.canDyePortals = Reference.GetFromConfig("CanDyePortals", true);
		Reference.doesDyingCost = Reference.GetFromConfig("DoesDyingCost", true);
		Reference.allowModifiers = Reference.GetFromConfig("AllowModifiers", true);
		Reference.allowObsidianStairs = Reference.GetFromConfig("AllowObsidianStairs", true);
		Reference.printPortalMessages = Reference.GetFromConfig("PrintPortalCreationMessages", true);
		
		Reference.pigmenSpawnChance = Reference.GetFromConfig("PigmenSpawnChance", 100, 0, 100);
		Reference.soundLevel = Reference.GetFromConfig("SoundLevel", 100, 0, 100);
		Reference.particleLevel = Reference.GetFromConfig("ParticleLevel", 100, 0, 100);
		
		Reference.IDObsidianStairs = Reference.GetFromConfig("ObsidianStairsID", 512, true);
		Reference.IDPortalModifier = Reference.GetFromConfig("PortalModifierID", 513, true);
		Reference.IDPortalModifierUpgrade = Reference.GetFromConfig("PortalModifierUpgradeID", 6000, false);
		
		Reference.STR_NoUpgrade = Reference.GetFromConfig("TEXT_NoUpgrade", "You do not have the required upgrade.");
		Reference.STR_ExitBlocked = Reference.GetFromConfig("TEXT_ExitBlocked", "The exit portal is blocked or invalid.");
		Reference.STR_NoExit = Reference.GetFromConfig("TEXT_NoExit", "Couldn't find a valid exit.");
		
		Reference.STR_Frequency = Reference.GetFromConfig("TEXT_Frequency", "Frequency");
		Reference.STR_Upgrades = Reference.GetFromConfig("TEXT_Upgrades", "Upgrades");
		Reference.STR_PortalModifierTitle = Reference.GetFromConfig("TEXT_PortalModifier", "Portal Modifier");
	
		Reference.AddToBorderBlocks(Reference.GetFromConfig("CustomBorderBlocks", ""));
		Reference.AddToDestroyBlocks(Reference.GetFromConfig("CustomDestroyBlocks", "8,9,10,11,6,18,30,31,32,37,38,39,40,78,104,105,106,111,115"));
		
		Reference.ConfigFile.save();
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
		
		if (Reference.allowModifiers)
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
		
			Reference.borderBlocks.add(Reference.IDPortalModifier);
		}
		
		if (Reference.allowObsidianStairs)
		{
			blockStairsObsidian = new BlockStairsObsidian();
			
			GameRegistry.registerBlock(blockStairsObsidian, "blockStairsObsidian");
			LanguageRegistry.addName(blockStairsObsidian, "Obsidian Stairs");
			GameRegistry.addRecipe(new ItemStack(blockStairsObsidian, 4), "X  ", "XX ", "XXX", Character.valueOf('X'), Block.obsidian);
			Reference.borderBlocks.add(Reference.IDObsidianStairs);
		}
		
		proxy.setupRenderers();

		// Events etc
		MinecraftForge.EVENT_BUS.register(new EventHooks());
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		
		// Add stuff to the removable & border blocks.
		Reference.removableBlocks.add(0);
		Reference.removableBlocks.add(Block.fire.blockID);
		Reference.borderBlocks.add(EnhancedPortals.instance.blockObsidian.blockID);
	}
}
