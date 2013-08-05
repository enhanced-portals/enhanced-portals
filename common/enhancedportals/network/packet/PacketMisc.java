package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import uk.co.shadeddimensions.enhancedportals.EnhancedPortals_deprecated;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;

public class PacketMisc extends PacketEnhancedPortals
{
    byte type;
    int data;

    public PacketMisc()
    {

    }

    public PacketMisc(byte Type, int Data)
    {
        type = Type;
        data = Data;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        type = stream.readByte();
        data = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals_deprecated.proxy.getWorld(0);

        if (world.isRemote)
        {
            if (type == 1)
            {
                EnhancedPortals_deprecated.proxy.isIdentifierTaken = true;
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(type, this.data);
    }
}
