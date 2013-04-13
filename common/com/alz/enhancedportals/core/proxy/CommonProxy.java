package com.alz.enhancedportals.core.proxy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommonProxy
{
    public void register()
    {

    }
    
    public WorldServer getWorldForDimension(int dim)
    {
        return MinecraftServer.getServer().worldServerForDimension(dim);
    }
}
