package uk.co.shadeddimensions.ep3.addon;

import cofh.api.core.IInitializer;
import cpw.mods.fml.common.Loader;

public class AddonThermalExpansion implements IInitializer
{
    @Override
    public boolean preInit()
    {
        return false;
    }

    @Override
    public boolean initialize() // IMC needs to be done here.
    {
        if (Loader.isModLoaded("ThermalExpansion"))
        {
            
        }
        
        return false;
    }

    @Override
    public boolean postInit()
    {
        return false;
    }
}
