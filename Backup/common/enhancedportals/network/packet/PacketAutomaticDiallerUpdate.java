package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityAutomaticDialler;

public class PacketAutomaticDiallerUpdate extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    String theNetwork;

    public PacketAutomaticDiallerUpdate()
    {

    }

    public PacketAutomaticDiallerUpdate(TileEntityAutomaticDialler dialler)
    {
        position = dialler.getBlockPosition();
        dimension = dialler.worldObj.provider.dimensionId;
        theNetwork = dialler.activeNetwork;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        theNetwork = stream.readUTF();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        if (WorldHelper.getBlockId(dimension, position) == BlockIds.AutomaticDialler)
        {
            if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityAutomaticDialler)
            {
                TileEntityAutomaticDialler dialler = (TileEntityAutomaticDialler) WorldHelper.getTileEntity(dimension, position);
                dialler.activeNetwork = theNetwork;
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, theNetwork);
    }
}
