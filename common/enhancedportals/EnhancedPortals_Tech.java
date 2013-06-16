package enhancedportals;

import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Textures;
import enhancedportals.lib.Textures.Colour;
import enhancedportals.portal.PortalTexture;

@Mod(modid = Reference.MOD_ID + "_Tech", name = "EP2 Tech", version = "@TECH_VERSION@", dependencies = "required-after:" + Reference.MOD_ID)
public class EnhancedPortals_Tech
{
    Icon fuelTexture;
    boolean hasAdded = false;

    @Instance(Reference.MOD_ID + "_Tech")
    public static EnhancedPortals_Tech instance;

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void registerIcons(TextureStitchEvent.Pre event)
    {
        if (event.map == FMLClientHandler.instance().getClient().renderEngine.textureMapBlocks)
        {
            fuelTexture = event.map.registerIcon("EP2_Tech:fuel");
        }
    }

    @ForgeSubscribe
    public void worldLoad(WorldEvent.Load event)
    {
        if (!hasAdded && event.world.isRemote)
        {
            addBuildcraftTextures();
            addThermalExpansionTextures();
            addTConstructTextures();
            
            hasAdded = true;
        }
    }
    
    private void addTConstructTextures()
    {
        try
        {            
            Item filledBuckets = (Item) Class.forName("mods.tinker.tconstruct.common.TContent").getField("buckets").get(null);
            Block liquidMetal = (Block) Class.forName("mods.tinker.tconstruct.common.TContent").getField("liquidMetalStill").get(null);
            String identifier = "I:" + filledBuckets.itemID + ":";
            
            Textures.portalTextureMap.put(identifier + "0", new PortalTexture(identifier + "0", Block.blocksList[liquidMetal.blockID].getIcon(0, 0), Colour.RED.getModifierTexture(), 0xAA0000));
            Textures.portalTextureMap.put(identifier + "1", new PortalTexture(identifier + "1", Block.blocksList[liquidMetal.blockID].getIcon(0, 1), Colour.YELLOW.getModifierTexture(), 0xDDDD00));
            Textures.portalTextureMap.put(identifier + "2", new PortalTexture(identifier + "2", Block.blocksList[liquidMetal.blockID].getIcon(0, 2), Colour.YELLOW.getModifierTexture(), 0xFFFF00));
            Textures.portalTextureMap.put(identifier + "3", new PortalTexture(identifier + "3", Block.blocksList[liquidMetal.blockID].getIcon(0, 3), Colour.WHITE.getModifierTexture(), 0xFFFFFF));
            Textures.portalTextureMap.put(identifier + "4", new PortalTexture(identifier + "4", Block.blocksList[liquidMetal.blockID].getIcon(0, 4), Colour.PINK.getModifierTexture(), 0xFFC3E1));
            Textures.portalTextureMap.put(identifier + "5", new PortalTexture(identifier + "5", Block.blocksList[liquidMetal.blockID].getIcon(0, 5), Colour.BLUE.getModifierTexture(), 0x0000DD));
            Textures.portalTextureMap.put(identifier + "6", new PortalTexture(identifier + "6", Block.blocksList[liquidMetal.blockID].getIcon(0, 6), Colour.YELLOW.getModifierTexture(), 0xDDDD00));
            Textures.portalTextureMap.put(identifier + "7", new PortalTexture(identifier + "7", Block.blocksList[liquidMetal.blockID].getIcon(0, 7), Colour.BROWN.getModifierTexture(), 0x9A832C));
            
            Textures.portalTextureMap.put(identifier + "9", new PortalTexture(identifier + "9", Block.blocksList[liquidMetal.blockID].getIcon(0, 9), Colour.PURPLE.getModifierTexture(), 0xCA61E3));            
            Textures.portalTextureMap.put(identifier + "10", new PortalTexture(identifier + "10", Block.blocksList[liquidMetal.blockID].getIcon(0, 10), Colour.PINK.getModifierTexture(), 0xE9CBF0));            
            Textures.portalTextureMap.put(identifier + "11", new PortalTexture(identifier + "11", Block.blocksList[liquidMetal.blockID].getIcon(0, 11), Colour.PURPLE.getModifierTexture(), 0x9853A9));            
            Textures.portalTextureMap.put(identifier + "12", new PortalTexture(identifier + "12", Block.blocksList[liquidMetal.blockID].getIcon(0, 12), Colour.LIGHT_GRAY.getModifierTexture(), 0xCCCCCC));
            
            Reference.log.log(Level.INFO, "Loaded TConstruct addon successfully.");
        }
        catch (Exception e)
        {
            Reference.log.log(Level.WARNING, "Couldn't load TConstruct addon.");
        }
    }
    
    private void addBuildcraftTextures()
    {
        try
        {
            Item bucketOil = (Item) Class.forName("buildcraft.BuildCraftEnergy").getField("bucketOil").get(null);
            Item bucketFuel = (Item) Class.forName("buildcraft.BuildCraftEnergy").getField("bucketFuel").get(null);
            Block blockOil = (Block) Class.forName("buildcraft.BuildCraftEnergy").getField("oilMoving").get(null);

            Textures.portalTextureMap.put("I:" + bucketFuel.itemID + ":0", new PortalTexture("I:" + bucketFuel.itemID + ":0", fuelTexture, Colour.YELLOW.getModifierTexture(), 0xFFFF00));
            Textures.portalTextureMap.put("I:" + bucketOil.itemID + ":0", new PortalTexture("I:" + bucketOil.itemID + ":0", Block.blocksList[blockOil.blockID].getIcon(2, 0), Colour.BLACK.getModifierTexture(), 0));

            Reference.log.log(Level.INFO, "Loaded BuildCraft addon successfully.");
        }
        catch (Exception e)
        {
            Reference.log.log(Level.WARNING, "Couldn't load BuildCraft addon.");
        }
    }
    
    private void addThermalExpansionTextures()
    {
        try
        {
            Block liquidRedstone = (Block) Class.forName("thermalexpansion.liquid.TELiquids").getField("blockRedstone").get(null);
            Block liquidGlowstone = (Block) Class.forName("thermalexpansion.liquid.TELiquids").getField("blockGlowstone").get(null);
            Block liquidEnder = (Block) Class.forName("thermalexpansion.liquid.TELiquids").getField("blockEnder").get(null);
            
            ItemStack bucketRedstone = (ItemStack) Class.forName("thermalexpansion.liquid.TELiquids").getField("bucketRedstone").get(null);
            ItemStack bucketGlowstone = (ItemStack) Class.forName("thermalexpansion.liquid.TELiquids").getField("bucketGlowstone").get(null);
            ItemStack bucketEnder = (ItemStack) Class.forName("thermalexpansion.liquid.TELiquids").getField("bucketEnder").get(null);
            
            Textures.portalTextureMap.put("I:" + bucketRedstone.itemID + ":0", new PortalTexture("I:" + bucketRedstone.itemID + ":0", liquidRedstone.getBlockTextureFromSide(0), Colour.RED.getModifierTexture(), 0xFF0000));
            Textures.portalTextureMap.put("I:" + bucketGlowstone.itemID + ":1", new PortalTexture("I:" + bucketGlowstone.itemID + ":1", liquidGlowstone.getBlockTextureFromSide(0), Colour.YELLOW.getModifierTexture(), 0xFFFF00));
            Textures.portalTextureMap.put("I:" + bucketEnder.itemID + ":2", new PortalTexture("I:" + bucketEnder.itemID + ":2", liquidEnder.getBlockTextureFromSide(0), Colour.BLUE.getModifierTexture(), 0x0088AA));
            
            Reference.log.log(Level.INFO, "Loaded ThermalExpansion addon successfully.");
        }
        catch (Exception e)
        {
            Reference.log.log(Level.WARNING, "Couldn't load ThermalExpansion addon.");
        }
    }
}
