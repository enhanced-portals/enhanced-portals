package enhancedportals.network.packet;

import java.io.DataInputStream;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.lib.PacketIds;
import enhancedportals.tileentity.TileEntityEnhancedPortals;

public class PacketTEUpdate extends PacketUpdate
{
    public PacketTEUpdate()
    {
        super();
    }
    
    public PacketTEUpdate(TileEntityEnhancedPortals tileEntity)
    {
        super();
        
        xCoord = tileEntity.xCoord;
        yCoord = tileEntity.yCoord;
        zCoord = tileEntity.zCoord;
        dimension = tileEntity.worldObj.provider.dimensionId;
        packetData = tileEntity.getPacketData();
        isChunkPacket = true;
    }
    
    public PacketTEUpdate(DataInputStream stream)
    {
        super(stream);
    }

    public boolean tileEntityExists(World world)
    {
        return world.blockExists(xCoord, yCoord, zCoord);
    }
    
    public TileEntity getTileEntity(World world)
    {
        return world.getBlockTileEntity(xCoord, yCoord, zCoord);
    }
    
    @Override
    public int getPacketID()
    {
        return PacketIds.TileEntityUpdate;
    }
}
