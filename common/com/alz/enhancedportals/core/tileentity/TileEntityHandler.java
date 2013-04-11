package com.alz.enhancedportals.core.tileentity;

import com.alz.enhancedportals.reference.Strings;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntityHandler
{
    public static void init()
    {
        GameRegistry.registerTileEntity(TileEntityDialHomeDevice.class, Strings.Block.DIAL_HOME_DEVICE_NAME);
        GameRegistry.registerTileEntity(TileEntityNetherPortal.class, "netherPortal");
        GameRegistry.registerTileEntity(TileEntityPortalModifier.class, Strings.Block.PORTAL_MODIFIER_NAME);
    }
}
