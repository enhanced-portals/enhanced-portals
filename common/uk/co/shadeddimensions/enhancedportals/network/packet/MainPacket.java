package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import org.bouncycastle.util.Arrays;

import com.google.common.primitives.Bytes;

import enhancedportals.lib.Reference;

public abstract class MainPacket
{
    enum PacketType
    {
        REQUEST_DATA(PacketRequestData.class), PORTAL_FRAME_DATA(PacketPortalFrameData.class);

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

        return new Packet250CustomPayload(Reference.MOD_ID_NEW, Bytes.concat(new byte[] { packet.getID() }, bos.toByteArray()));
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
}
