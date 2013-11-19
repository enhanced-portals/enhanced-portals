package uk.co.shadeddimensions.ep3.addon;

import uk.co.shadeddimensions.ep3.network.CommonProxy;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class AddonThermalExpansion extends Addon
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
        if (!Loader.isModLoaded("ThermalExpansion"))
        {
            CommonProxy.logger.warning("Thermal Expansion was not found. Energy requirements have been disabled");
            CommonProxy.redstoneFluxPowerMultiplier = 0;
        }
    }
}
