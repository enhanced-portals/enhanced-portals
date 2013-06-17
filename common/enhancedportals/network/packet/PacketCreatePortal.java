package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedportals.EnhancedPortals;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketCreatePortal extends PacketEnhancedPortals
{
    int xCoord, yCoord, zCoord, dimension;
    boolean type;

    public PacketCreatePortal()
    {

    }

    public PacketCreatePortal(TileEntity tileEntity, boolean Type)
    {
        xCoord = tileEntity.xCoord;
        yCoord = tileEntity.yCoord;
        zCoord = tileEntity.zCoord;
        dimension = tileEntity.worldObj.provider.dimensionId;
        type = Type;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        xCoord = stream.readInt();
        yCoord = stream.readInt();
        zCoord = stream.readInt();
        dimension = stream.readInt();
        type = stream.readBoolean();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityPortalModifier)
        {
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(xCoord, yCoord, zCoord);

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
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension, type);
    }
}
