package com.alz.enhancedportals.core.proxy;

import net.minecraftforge.client.MinecraftForgeClient;

import com.alz.enhancedportals.core.client.renderer.ItemDHDRenderer;
import com.alz.enhancedportals.core.client.renderer.TileEntityDHDRenderer;
import com.alz.enhancedportals.core.tileentity.TileEntityDialHomeDevice;
import com.alz.enhancedportals.reference.BlockIds;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void register()
    {
        MinecraftForgeClient.registerItemRenderer(BlockIds.DIAL_HOME_DEVICE, new ItemDHDRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDialHomeDevice.class, new TileEntityDHDRenderer());
    }
}
