package enhancedportals.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import enhancedportals.utility.WorldUtils;

public class PacketRerender extends PacketEP
{
    int posX, posY, posZ;

    public PacketRerender()
    {

    }

    public PacketRerender(int x, int y, int z)
    {
        posX = x;
        posY = y;
        posZ = z;
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        posX = buffer.readInt();
        posY = buffer.readInt();
        posZ = buffer.readInt();
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(posX);
        buffer.writeInt(posY);
        buffer.writeInt(posZ);
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        WorldUtils.markForUpdate(player.worldObj, posX, posY, posZ);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {

    }
}
