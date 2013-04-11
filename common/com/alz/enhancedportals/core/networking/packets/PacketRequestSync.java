package com.alz.enhancedportals.core.networking.packets;

import net.minecraft.tileentity.TileEntity;

public class PacketRequestSync extends PacketEnhancedPortals
{
    public PacketRequestSync(int x, int y, int z, int dimension)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        Dimension = dimension;
    }
    
    public PacketRequestSync(TileEntity tileEntity)
    {
        xCoord = tileEntity.xCoord;
        yCoord = tileEntity.yCoord;
        zCoord = tileEntity.zCoord;
        Dimension = tileEntity.worldObj.provider.dimensionId;
    }
}
