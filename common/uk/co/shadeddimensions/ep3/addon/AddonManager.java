package uk.co.shadeddimensions.ep3.addon;

import java.util.ArrayList;

import cofh.api.core.IInitializer;

public class AddonManager
{
    ArrayList<IInitializer> addons;
    public static AddonManager instance = new AddonManager();

    public AddonManager()
    {
        addons = new ArrayList<IInitializer>();
    }

    public void init()
    {
        for (IInitializer a : addons)
        {
            a.initialize();
        }
    }

    public void postInit()
    {
        for (IInitializer a : addons)
        {
            a.postInit();
        }
    }

    public void preInit()
    {
        for (IInitializer a : addons)
        {
            a.preInit();
        }
    }
}
