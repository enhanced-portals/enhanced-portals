package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ChunkCoordinates;

import org.bouncycastle.util.Arrays;

import uk.co.shadeddimensions.enhancedportals.lib.Reference;

import com.google.common.primitives.Bytes;

public abstract class MainPacket
{
    enum PacketType
    {
        REQUEST_DATA(PacketRequestData.class), //
        PORTAL_DATA(PacketPortalData.class), //
        PORTAL_FRAME_DATA(PacketPortalFrameData.class), //
        PORTAL_CONTROLLER_DATA(PacketPortalFrameControllerData.class), //
        PORTAL_REDSTONE_DATA(PacketPortalFrameRedstoneData.class), //
        PORTAL_NETWORK_INTERFACE_DATA(PacketNetworkInterfaceData.class), //
        GUI_BUTTON_PRESSED(PacketGuiButtonPressed.class), //
        GUI_REQUEST(PacketGuiRequest.class), //
        GUI_STRING(PacketGuiString.class), //
        GUI_INTEGER(PacketGuiInteger.class);

        private Class<? extends MainPacket> packetType;

        private PacketType(Class<? extends MainPacket> cls)
        {
            packetType = cls;
        }

        MainPacket make()
        {
            try
            {
                return packetType.newInstance();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static Packet250CustomPayload makePacket(MainPacket packet)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream stream = new DataOutputStream(bos);

        try
        {
            packet.generatePacket(stream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new Packet250CustomPayload(Reference.ID, Bytes.concat(new byte[] { packet.getID() }, bos.toByteArray()));
    }

    public static MainPacket readPacket(INetworkManager network, byte[] payload)
    {
        try
        {
            int type = payload[0];
            PacketType eType = PacketType.values()[type];
            byte[] data = Arrays.copyOfRange(payload, 1, payload.length);

            return eType.make().consumePacket(new DataInputStream(new ByteArrayInputStream(data)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private PacketType type;

    public MainPacket()
    {
        for (PacketType t : PacketType.values())
        {
            if (t.packetType == getClass())
            {
                type = t;
                break;
            }
        }

        if (type == null)
        {
            throw new RuntimeException("Can not create an unregistered packet!");
        }
    }

    public abstract MainPacket consumePacket(DataInputStream stream) throws IOException;

    public abstract void execute(INetworkManager network, EntityPlayer player);

    public abstract void generatePacket(DataOutputStream stream) throws IOException;

    public byte getID()
    {
        return (byte) type.ordinal();
    }

    protected ChunkCoordinates readChunkCoordinates(DataInputStream stream) throws IOException
    {
        return new ChunkCoordinates(stream.readInt(), stream.readInt(), stream.readInt());
    }

    protected void writeChunkCoordinates(ChunkCoordinates c, DataOutputStream stream) throws IOException
    {
        if (c == null)
        {
            c = new ChunkCoordinates(0, -1, 0);
        }

        stream.writeInt(c.posX);
        stream.writeInt(c.posY);
        stream.writeInt(c.posZ);
    }
}
