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
import enhancedportals.portal.PortalTexture;

@Mod(modid = Reference.MOD_ID + "_Tech", name = "EP2 Tech", version = Reference.MOD_VERSION, dependencies = "required-after:" + Reference.MOD_ID)
public class EnhancedPortals_Tech
{
    Icon fuelTexture;
    boolean hasAdded = false;

    @Instance(Reference.MOD_ID + "_BC")
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
            fuelTexture = event.map.registerIcon("EP2_BC:fuel");
        }
    }

    @ForgeSubscribe
    public void worldLoad(WorldEvent.Load event)
    {
        if (!hasAdded && event.world.isRemote)
        {
            addBuildcraftTextures();
            addThermalExpansionTextures();

            hasAdded = true;
        }
    }
    
    private void addBuildcraftTextures()
    {
        try
        {
            Item bucketOil = (Item) Class.forName("buildcraft.BuildCraftEnergy").getField("bucketOil").get(null);
            Item bucketFuel = (Item) Class.forName("buildcraft.BuildCraftEnergy").getField("bucketFuel").get(null);
            Block blockOil = (Block) Class.forName("buildcraft.BuildCraftEnergy").getField("oilMoving").get(null);

            Textures.portalTextureMap.put("I:" + bucketFuel.itemID + ":0", new PortalTexture("I:" + bucketFuel.itemID + ":0", fuelTexture, Textures.getTexture("C:11").getModifierTexture(), 0xFFFF00));
            Textures.portalTextureMap.put("I:" + bucketOil.itemID + ":0", new PortalTexture("I:" + bucketOil.itemID + ":0", Block.blocksList[blockOil.blockID].getIcon(2, 0), Textures.getTexture("C:0").getModifierTexture(), 0));

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
            
            Textures.portalTextureMap.put("I:" + bucketRedstone.itemID + ":0", new PortalTexture("I:" + bucketRedstone.itemID + ":0", liquidRedstone.getBlockTextureFromSide(0), Textures.getTexture("C:1").getModifierTexture(), 0xFF0000));
            Textures.portalTextureMap.put("I:" + bucketGlowstone.itemID + ":1", new PortalTexture("I:" + bucketGlowstone.itemID + ":1", liquidGlowstone.getBlockTextureFromSide(0), Textures.getTexture("C:11").getModifierTexture(), 0xFFFF00));
            Textures.portalTextureMap.put("I:" + bucketEnder.itemID + ":2", new PortalTexture("I:" + bucketEnder.itemID + ":2", liquidEnder.getBlockTextureFromSide(0), Textures.getTexture("C:4").getModifierTexture(), 0x0088AA));
            
            Reference.log.log(Level.INFO, "Loaded ThermalExpansion addon successfully.");
        }
        catch (Exception e)
        {
            Reference.log.log(Level.WARNING, "Couldn't load ThermalExpansion addon.");
        }
    }
}
