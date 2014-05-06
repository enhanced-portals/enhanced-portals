package enhancedportals.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.packet.PacketEP;
import enhancedportals.tileentity.TileEP;

@ChannelHandler.Sharable
public class PacketPipeline extends MessageToMessageCodec<FMLProxyPacket, PacketEP>
{
    private EnumMap<Side, FMLEmbeddedChannel> channels;
    private LinkedList<Class<? extends PacketEP>> packets = new LinkedList<Class<? extends PacketEP>>();
    private boolean isPostInitialised = false;

    // In line decoding and handling of the packet
    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception
    {
        ByteBuf payload = msg.payload();
        byte discriminator = payload.readByte();
        Class<? extends PacketEP> clazz = packets.get(discriminator);

        if (clazz == null)
        {
            throw new NullPointerException("No packet registered for discriminator: " + discriminator);
        }

        PacketEP pkt = clazz.newInstance();
        pkt.decodeInto(ctx, payload.slice());

        EntityPlayer player;

        switch (FMLCommonHandler.instance().getEffectiveSide())
        {
            case CLIENT:
                player = getClientPlayer();
                pkt.handleClientSide(player);
                break;

            case SERVER:
                INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                player = ((NetHandlerPlayServer) netHandler).playerEntity;
                pkt.handleServerSide(player);
                break;

            default:
        }

        out.add(pkt);
    }

    // In line encoding of the packet, including discriminator setting
    @Override
    protected void encode(ChannelHandlerContext ctx, PacketEP msg, List<Object> out) throws Exception
    {
        ByteBuf buffer = Unpooled.buffer();
        Class<? extends PacketEP> clazz = msg.getClass();

        if (!packets.contains(msg.getClass()))
        {
            throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());
        }

        byte discriminator = (byte) packets.indexOf(clazz);
        buffer.writeByte(discriminator);
        msg.encodeInto(ctx, buffer);
        FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        out.add(proxyPacket);
    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    // Method to call from FMLInitializationEvent
    public void initalise()
    {
        channels = NetworkRegistry.INSTANCE.newChannel("EP3", this);
    }

    // Method to call from FMLPostInitializationEvent
    // Ensures that packet discriminators are common between server and client by using logical sorting
    public void postInitialise()
    {
        if (isPostInitialised)
        {
            return;
        }

        isPostInitialised = true;
        Collections.sort(packets, new Comparator<Class<? extends PacketEP>>()
        {

            @Override
            public int compare(Class<? extends PacketEP> clazz1, Class<? extends PacketEP> clazz2)
            {
                int com = String.CASE_INSENSITIVE_ORDER.compare(clazz1.getCanonicalName(), clazz2.getCanonicalName());
                if (com == 0)
                {
                    com = clazz1.getCanonicalName().compareTo(clazz2.getCanonicalName());
                }

                return com;
            }
        });
    }

    /**
     * Register your packet with the pipeline. Discriminators are automatically set.
     * 
     * @param clazz
     *            the class to register
     * 
     * @return whether registration was successful. Failure may occur if 256 packets have been registered or if the registry already contains this packet
     */
    public boolean registerPacket(Class<? extends PacketEP> clazz)
    {
        if (packets.size() > 256)
        {
            EnhancedPortals.logger.warn("!!! You've registered too many packets...");
            return false;
        }

        if (packets.contains(clazz))
        {
            EnhancedPortals.logger.warn("!!! You've already registered this packet...");
            return false;
        }

        if (isPostInitialised)
        {
            EnhancedPortals.logger.warn("!!! You're registering your packet too late...");
            return false;
        }

        packets.add(clazz);
        return true;
    }

    /**
     * Send this message to the specified player.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     * 
     * @param message
     *            The message to send
     * @param player
     *            The player to send it to
     */
    public void sendTo(PacketEP message, EntityPlayerMP player)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     * 
     * @param message
     *            The message to send
     */
    public void sendToAll(PacketEP message)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     * 
     * @param message
     *            The message to send
     * @param point
     *            The {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint} around which to send
     */
    public void sendToAllAround(PacketEP message, NetworkRegistry.TargetPoint point)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToAllAround(PacketEP message, TileEP tile)
    {
        sendToAllAround(message, new TargetPoint(tile.getWorldObj().provider.dimensionId, tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, 128.0));
    }

    /**
     * Send this message to everyone within the supplied dimension.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     * 
     * @param message
     *            The message to send
     * @param dimensionId
     *            The dimension id to target
     */
    public void sendToDimension(PacketEP message, int dimensionId)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the server.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     * 
     * @param message
     *            The message to send
     */
    public void sendToServer(PacketEP message)
    {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeAndFlush(message);
    }
}