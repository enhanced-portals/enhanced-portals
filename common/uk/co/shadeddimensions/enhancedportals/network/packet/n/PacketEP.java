package uk.co.shadeddimensions.enhancedportals.network.packet.n;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import uk.co.shadeddimensions.enhancedportals.lib.Reference;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.Player;

public abstract class PacketEP
{
    enum PacketType
    {
        TILE(PacketEP.class);
        
        Class<?extends PacketEP> clas;
        
        PacketType(Class<?extends PacketEP> c)
        {
            clas = c;
        }
    }
    
    boolean isChunkDataPacket;
    PacketType packetType;
    
    public PacketEP()
    {
        isChunkDataPacket = false;
        
        for (PacketType t : PacketType.values())
        {
            if (t.clas == getClass())
            {
                packetType = t;
                break;
            }
        }

        if (packetType == null)
        {
            throw new RuntimeException("Can not create an unregistered packet!");
        }
    }
    
    public byte[] populatePacket()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        try
        {
            dos.writeByte(packetType.ordinal());
            writeData(dos);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return baos.toByteArray();
    }

    public Packet250CustomPayload get()
    {
        return getPacket(this);
    }
    
    public abstract void readData(DataInputStream stream) throws IOException;
    public abstract void writeData(DataOutputStream stream) throws IOException;
    
    public abstract void execute(INetworkManager networkManager, Player player);
    
    // statics
    public static PacketEP buildPacket(byte[] data)
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        PacketEP packet = null;
        
        try
        {
            packet = PacketType.values()[dis.readByte()].clas.newInstance();
            packet.readData(dis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
                
        return packet;
    }
    
    public static Packet250CustomPayload getPacket(PacketEP packetEp)
    {
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = Reference.ID;
        packet.data = packetEp.populatePacket();
        packet.length = packet.data.length;
        packet.isChunkDataPacket = packetEp.isChunkDataPacket;
        
        return packet;
    }
}
