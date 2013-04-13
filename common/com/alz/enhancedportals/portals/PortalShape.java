package com.alz.enhancedportals.portals;

import com.alz.enhancedportals.helpers.WorldLocation;

public enum PortalShape
{
    X, Z, HORIZONTAL, UNKNOWN;

    public static PortalShape getPortalShape(WorldLocation location)
    {
        if (PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord - 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord + 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord, location.yCoord + 1, location.zCoord), true) && PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord, location.yCoord - 1, location.zCoord), true))
        {
            return PortalShape.X;
        }
        else if (PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord, location.yCoord, location.zCoord - 1), true) && PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord, location.yCoord, location.zCoord + 1), true) && PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord, location.yCoord + 1, location.zCoord), true) && PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord, location.yCoord - 1, location.zCoord), true))
        {
            return PortalShape.Z;
        }
        else if (PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord - 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord + 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord, location.yCoord, location.zCoord + 1), true) && PortalHandler.isBlockFrame(location.getWorld().getBlockId(location.xCoord, location.yCoord, location.zCoord - 1), true))
        {
            return PortalShape.HORIZONTAL;
        }

        return PortalShape.UNKNOWN;
    }
}
