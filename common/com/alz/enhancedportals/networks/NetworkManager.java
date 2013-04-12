package com.alz.enhancedportals.networks;

import net.minecraft.server.MinecraftServer;

public class NetworkManager
{
    private DialHomeDeviceNetwork DHDNetwork;
    private PortalModifierNetwork PMNetwork;
    public MinecraftServer serverInstance;

    public NetworkManager(MinecraftServer server)
    {
        serverInstance = server;

        DHDNetwork = new DialHomeDeviceNetwork(this);
        PMNetwork = new PortalModifierNetwork(this);
    }

    public void saveData()
    {
        DHDNetwork.saveData();
        PMNetwork.saveData();
    }
}
