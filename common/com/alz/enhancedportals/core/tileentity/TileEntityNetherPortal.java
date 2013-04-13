package com.alz.enhancedportals.core.tileentity;

import com.alz.enhancedportals.helpers.WorldLocation;
import com.alz.enhancedportals.portals.PortalTexture;

public class TileEntityNetherPortal extends TileEntityEnhancedPortals
{
    public WorldLocation ParentModifier;
    public PortalTexture PortalTexture;
    
    public TileEntityNetherPortal()
    {
        requiredMetadataForUpdate = 1;
    }
}
