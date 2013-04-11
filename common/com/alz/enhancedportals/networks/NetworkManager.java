package com.alz.enhancedportals.networks;

import net.minecraft.server.MinecraftServer;

public class NetworkManager
{
    private DialHomeDeviceNetwork DHDNetwork;
    private PortalModifierNetwork PMNetwork;
    
    private MinecraftServer serverInstance;
    
    public void init(MinecraftServer server)
    {
        serverInstance = server;
        
        DHDNetwork = new DialHomeDeviceNetwork();
        PMNetwork = new PortalModifierNetwork();
    }
}
