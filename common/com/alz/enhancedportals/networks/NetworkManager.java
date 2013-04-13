package com.alz.enhancedportals.networks;

import com.alz.enhancedportals.core.tileentity.TileEntityEnhancedPortals;

import net.minecraft.server.MinecraftServer;

public class NetworkManager
{
    public DialHomeDeviceNetwork DHDNetwork;
    public PortalModifierNetwork PMNetwork;
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

    public void sendUpdatePacketToNearbyClients(TileEntityEnhancedPortals tileEntity)
    {
        
    }
}
