package uk.co.shadeddimensions.ep3.addon;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class AddonThaumcraft extends Addon
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Override
    public void init(FMLInitializationEvent event)
    {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        if (Loader.isModLoaded("Thaumcraft"))
        {
            
            
            //CommonProxy.logger.warning("Loaded TC4 addon");
        }
    }
}
