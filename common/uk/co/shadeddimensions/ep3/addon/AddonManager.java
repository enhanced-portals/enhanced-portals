package uk.co.shadeddimensions.ep3.addon;

import java.util.ArrayList;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class AddonManager
{
    ArrayList<Addon> addons;
    public static AddonManager instance = new AddonManager();

    public AddonManager()
    {
        addons = new ArrayList<Addon>();
        addons.add(new AddonThermalExpansion());
    }

    public void preInit(FMLPreInitializationEvent event)
    {
        for (Addon a : addons)
        {
            a.preInit(event);
        }
    }

    public void init(FMLInitializationEvent event)
    {
        for (Addon a : addons)
        {
            a.init(event);
        }
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        for (Addon a : addons)
        {
            a.postInit(event);
        }
    }
}
