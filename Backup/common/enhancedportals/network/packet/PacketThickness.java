package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketThickness extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    byte thickness;

    public PacketThickness()
    {

    }

    public PacketThickness(TileEntityPortalModifier modifier)
    {
        position = modifier.getBlockPosition();
        dimension = modifier.worldObj.provider.dimensionId;
        thickness = modifier.thickness;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        thickness = stream.readByte();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        if (WorldHelper.getBlockId(dimension, position) == BlockIds.PortalModifier)
        {
            if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityPortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) WorldHelper.getTileEntity(dimension, position);
                modifier.thickness = thickness;
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, thickness);
    }
}
