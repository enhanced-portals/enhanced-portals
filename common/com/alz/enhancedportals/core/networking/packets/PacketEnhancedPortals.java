package com.alz.enhancedportals.core.networking.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.alz.enhancedportals.reference.Reference;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

public class PacketEnhancedPortals
{
    public int xCoord, yCoord, zCoord, Dimension;
    
    public PacketEnhancedPortals()
    {
        
    }
    
    public PacketEnhancedPortals(int x, int y, int z, int dimension)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        Dimension = dimension;
    }
    
    public PacketEnhancedPortals(TileEntity tileEntity)
    {
        xCoord = tileEntity.xCoord;
        yCoord = tileEntity.yCoord;
        zCoord = tileEntity.zCoord;
        Dimension = tileEntity.worldObj.provider.dimensionId;
    }
    
    public byte getPacketID()
    {
        return 0;
    }
    
    public boolean getChunkDataPacket()
    {
        return false;
    }
    
    public Packet250CustomPayload getPacket()
    {
        Packet250CustomPayload packet = new Packet250CustomPayload();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteStream);
        
        try
        {
            stream.writeByte(getPacketID());
            writePacketData(stream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        packet.channel = Reference.MOD_ID;
        packet.data = byteStream.toByteArray();
        packet.length = packet.data.length;
        packet.isChunkDataPacket = getChunkDataPacket();
        
        return packet;
    }
    
    public void readPacketData(DataInputStream stream) throws IOException
    {
        xCoord = stream.readInt();
        yCoord = stream.readInt();
        zCoord = stream.readInt();
        Dimension = stream.readInt();
    }
    
    public void writePacketData(DataOutputStream stream) throws IOException
    {
        stream.writeInt(xCoord);
        stream.writeInt(yCoord);
        stream.writeInt(zCoord);
        stream.writeInt(Dimension);
    }
}
