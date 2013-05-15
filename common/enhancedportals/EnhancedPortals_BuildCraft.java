package enhancedportals;

import java.util.logging.Level;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Settings;
import enhancedportals.lib.Textures;
import enhancedportals.portal.PortalTexture;

@Mod(modid=Reference.MOD_ID + "_BC", name="EP2 BuildCraft", version=Reference.MOD_VERSION, dependencies="required-after:BuildCraft|Energy;required-after:" + Reference.MOD_ID)
public class EnhancedPortals_BuildCraft implements IIconRegister
{    
    Icon fuelTexture;
    
    @Instance(Reference.MOD_ID + "_BC")
    public static EnhancedPortals_BuildCraft instance;
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        Textures.IconsToRegister.add(this);
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        Settings.CustomIconMap.put("Fuel", fuelTexture);
        
        try
        {
            Item bucketOil = (Item) Class.forName("buildcraft.BuildCraftEnergy").getField("bucketOil").get(null);
            Item bucketFuel = (Item) Class.forName("buildcraft.BuildCraftEnergy").getField("bucketFuel").get(null);
            
            Settings.ValidItemsList.add(bucketFuel.itemID);
            Settings.ValidItemsList.add(bucketOil.itemID);
            Settings.ItemPortalTextureMap.put(bucketFuel.itemID + ":0", new PortalTexture("Fuel"));
            Settings.ItemPortalTextureMap.put(bucketOil.itemID + ":0", new PortalTexture("Oil"));
        }
        catch (Exception e)
        {
            Reference.log.log(Level.WARNING, "Couldn't load BuildCraft items.");
        }
    }
    
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        fuelTexture = iconRegister.registerIcon("buildcraft:../items/fuel");
    }
}