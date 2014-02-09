package uk.co.shadeddimensions.ep3.network.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import uk.co.shadeddimensions.ep3.lib.Reference;
import cpw.mods.fml.common.network.Player;

public abstract class PacketEnhancedPortals
{
    enum Packets
    {
        PacketRequestData(PacketRequestData.class), PacketTileUpdate(PacketTileUpdate.class), PacketTileGui(PacketTileGui.class), PacketGuiData(PacketGuiData.class), PacketTextureData(PacketTextureData.class), PacketRerender(PacketRerender.class);

        public static Packets getID(Class<? extends PacketEnhancedPortals> c)
        {
            return reverseLookup.get(c);
        }

        public static Packets getPackets(int id)
        {
            return values()[id];
        }

        Class<? extends PacketEnhancedPortals> clazz;

        private Packets(Class<? extends PacketEnhancedPortals> c)
        {
            clazz = c;
            reverseLookup.put(c, this);
        }

        public PacketEnhancedPortals getPacket()
        {
            try
            {
                return clazz.newInstance();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Failed to create a new packet instance.");
            }
        }
    }

    private static Map<Class<? extends PacketEnhancedPortals>, Packets> reverseLookup = new HashMap<Class<? extends PacketEnhancedPortals>, Packets>();

    public static PacketEnhancedPortals getPacket(byte id)
    {
        return Packets.getPackets(id).getPacket();
    }

    protected boolean isChunkDataPacket;

    public void clientPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        throw new RuntimeException("Shouldn't recieve this packet on the client!");
    }

    public int getID()
    {
        return Packets.getID(this.getClass()).ordinal();
    }

    public Packet250CustomPayload getPacket()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try
        {
            dos.writeByte(getID());
            writePacketData(dos);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload p = new Packet250CustomPayload();
        p.channel = Reference.SHORT_ID;
        p.data = baos.toByteArray();
        p.length = p.data.length;
        p.isChunkDataPacket = isChunkDataPacket;

        return p;
    }

    public abstract void readPacketData(DataInputStream stream) throws IOException;

    public void serverPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        throw new RuntimeException("Shouldn't recieve this packet on the server!");
    }

    public abstract void writePacketData(DataOutputStream stream) throws IOException;
}
