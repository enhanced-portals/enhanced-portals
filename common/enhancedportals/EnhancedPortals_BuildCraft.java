package enhancedportals;

import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
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

@Mod(modid = Reference.MOD_ID + "_BC", name = "EP2 BuildCraft", version = Reference.MOD_VERSION, dependencies = "required-after:BuildCraft|Energy;required-after:" + Reference.MOD_ID)
public class EnhancedPortals_BuildCraft
{
    Icon                                     fuelTexture;

    @Instance(Reference.MOD_ID + "_BC")
    public static EnhancedPortals_BuildCraft instance;

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
        
        try
        {
            Item bucketOil = (Item) Class.forName("buildcraft.BuildCraftEnergy").getField("bucketOil").get(null);
            Item bucketFuel = (Item) Class.forName("buildcraft.BuildCraftEnergy").getField("bucketFuel").get(null);
            Block blockOil = (Block) Class.forName("buildcraft.BuildCraftEnergy").getField("oilMoving").get(null);

            Textures.portalTextureMap.put("I:" + bucketFuel.itemID + ":0", new PortalTexture("I:" + bucketFuel.itemID + ":0", fuelTexture, Textures.getTexture("C:11").getModifierTexture(), 0xFFFF00));
            Textures.portalTextureMap.put("I:" + bucketOil.itemID + ":0", new PortalTexture("I:" + bucketOil.itemID + ":0", Block.blocksList[blockOil.blockID].getIcon(2, 0), Textures.getTexture("C:0").getModifierTexture(), 0));
        }
        catch (Exception e)
        {
            Reference.log.log(Level.WARNING, "Couldn't load BuildCraft addon.");
        }
    }
}
