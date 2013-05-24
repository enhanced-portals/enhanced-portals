package enhancedportals;

import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Textures;
import enhancedportals.portal.PortalTexture;

// TODO DEPENDENCIES required-after:<modid>;
@Mod(modid = Reference.MOD_ID + "_TE", name = "EP2 ThermalExpansion", version = Reference.MOD_VERSION, dependencies = "required-after:" + Reference.MOD_ID)
public class EnhancedPortals_ThermalExpansion
{
    boolean                                        added = false;

    @Instance(Reference.MOD_ID + "_TE")
    public static EnhancedPortals_ThermalExpansion instance;

    @ServerStarting
    public void serverStarting(FMLServerStartingEvent event)
    {
        if (added)
        {
            return;
        }

        try
        {
            Item bucketRedstone = (Item) Class.forName("thermalexpansion.liquid.TELiquids").getField("bucketRedstone").get(null);
            Item bucketGlowstone = (Item) Class.forName("thermalexpansion.liquid.TELiquids").getField("bucketGlowstone").get(null);
            Item bucketEnder = (Item) Class.forName("thermalexpansion.liquid.TELiquids").getField("bucketEnder").get(null);

            Block blockRedstone = (Block) Class.forName("thermalexpansion.liquid.TELiquids").getField("blockRedstone").get(null);
            Block blockGlowstone = (Block) Class.forName("thermalexpansion.liquid.TELiquids").getField("blockGlowstone").get(null);
            Block blockEnder = (Block) Class.forName("thermalexpansion.liquid.TELiquids").getField("blockEnder").get(null);

            Textures.portalTextureMap.put("I:" + bucketRedstone.itemID + ":0", new PortalTexture("I:" + bucketRedstone.itemID + ":0", Block.blocksList[blockRedstone.blockID].getIcon(2, 0), Textures.getTexture("C:0").getModifierTexture(), 0xFF0000));
            Textures.portalTextureMap.put("I:" + bucketGlowstone.itemID + ":0", new PortalTexture("I:" + bucketGlowstone.itemID + ":0", Block.blocksList[blockGlowstone.blockID].getIcon(2, 0), Textures.getTexture("C:0").getModifierTexture(), 0xFFFF00));
            Textures.portalTextureMap.put("I:" + bucketEnder.itemID + ":0", new PortalTexture("I:" + bucketEnder.itemID + ":0", Block.blocksList[blockEnder.blockID].getIcon(2, 0), Textures.getTexture("C:0").getModifierTexture(), 0x00FF00));
        }
        catch (Exception e)
        {
            Reference.log.log(Level.WARNING, "Couldn't load ThermalExpansion addon.");
            e.printStackTrace();
        }

        added = true;
    }
}
