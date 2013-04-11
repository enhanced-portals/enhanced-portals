package com.alz.enhancedportals.core.networking.packets;

import net.minecraft.tileentity.TileEntity;

public class PacketRequestSync extends PacketEnhancedPortals
{
    public PacketRequestSync()
    {
        super();
    }
    
    public PacketRequestSync(int x, int y, int z, int dimension)
    {
        super(x, y, z, dimension);
    }
    
    public PacketRequestSync(TileEntity tileEntity)
    {
        super(tileEntity);
    }
}
