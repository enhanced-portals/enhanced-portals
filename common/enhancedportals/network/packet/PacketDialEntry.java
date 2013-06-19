package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.lib.BlockIds;
import enhancedportals.portal.network.DialDeviceNetworkObject;
import enhancedportals.tileentity.TileEntityDialDevice;

public class PacketDialEntry extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    String Name, Texture, Identifier;
    byte Type, Thickness;

    public PacketDialEntry()
    {

    }

    public PacketDialEntry(TileEntityDialDevice device, byte type, DialDeviceNetworkObject obj)
    {
        position = device.getBlockPosition();
        dimension = device.worldObj.provider.dimensionId;
        Name = obj.displayName;
        Texture = obj.texture;
        Identifier = obj.network;
        Thickness = obj.thickness;
        Type = type;
    }

    public PacketDialEntry(TileEntityDialDevice device, byte type, String name, String texture, String identifier, byte thickness)
    {
        position = device.getBlockPosition();
        dimension = device.worldObj.provider.dimensionId;
        Name = name;
        Texture = texture;
        Identifier = identifier;
        Thickness = thickness;
        Type = type;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        Name = stream.readUTF();
        Texture = stream.readUTF();
        Identifier = stream.readUTF();
        Thickness = stream.readByte();
        Type = stream.readByte();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        if (WorldHelper.getBlockId(dimension, position) == BlockIds.DialDevice)
        {
            if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityDialDevice)
            {
                if (Type == 0)
                {
                    TileEntityDialDevice dial = (TileEntityDialDevice) WorldHelper.getTileEntity(dimension, position);

                    if (!dial.hasDestination(new DialDeviceNetworkObject(Name, Identifier, Texture, Thickness)))
                    {
                        dial.destinationList.add(new DialDeviceNetworkObject(Name, Identifier, Texture, Thickness));
                    }
                }
                else if (Type == 1)
                {
                    TileEntityDialDevice dial = (TileEntityDialDevice) WorldHelper.getTileEntity(dimension, position);

                    dial.removeDestination(new DialDeviceNetworkObject(Name, Identifier, Texture, Thickness));
                }
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, Name, Texture, Identifier, Thickness, Type);
    }
}
