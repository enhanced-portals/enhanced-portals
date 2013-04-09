package alz.mods.enhancedportals.networking;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;
import alz.mods.enhancedportals.portals.PortalData;
import alz.mods.enhancedportals.reference.Reference;

public class PacketAddPortalData extends PacketUpdate
{
    public PortalData portalData;

    @Override
    public void addPacketData(DataOutputStream stream) throws IOException
    {
        super.addPacketData(stream);

        stream.writeUTF(portalData.DisplayName); // Should only need to send the name they chose for it, since the texture & location is calculated from items in the TileEntities inventory.
    }

    @Override
    public Packet250CustomPayload getClientPacket()
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        Packet250CustomPayload packet = new Packet250CustomPayload();

        try
        {
            dataStream.writeByte(getPacketID());
            addPacketData(dataStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        packet.channel = Reference.MOD_ID;
        packet.data = byteStream.toByteArray();
        packet.length = packet.data.length;
        packet.isChunkDataPacket = true;

        return packet;
    }

    @Override
    public void getPacketData(DataInputStream stream) throws IOException
    {
        super.getPacketData(stream);

        portalData = new PortalData();
        portalData.DisplayName = stream.readUTF();
    }

    @Override
    public int getPacketID()
    {
        return Reference.Networking.DialDevice_NewPortalData;
    }

    @Override
    public Packet250CustomPayload getServerPacket()
    {
        return null;
    }
}
