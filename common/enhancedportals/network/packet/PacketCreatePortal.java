package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.tileentity.TileEntityEnhancedPortals;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketCreatePortal extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    boolean type;

    public PacketCreatePortal()
    {

    }

    public PacketCreatePortal(TileEntityEnhancedPortals tileEntity, boolean Type)
    {
        position = tileEntity.getBlockPosition();
        dimension = tileEntity.worldObj.provider.dimensionId;
        type = Type;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        type = stream.readBoolean();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityPortalModifier)
        {
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) WorldHelper.getTileEntity(dimension, position);

            if (type)
            {
                modifier.createPortal();
            }
            else
            {
                modifier.removePortal();
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, type);
    }
}
