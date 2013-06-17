package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.portal.network.DialDeviceNetworkObject;
import enhancedportals.tileentity.TileEntityDialDevice;

public class PacketDialEntry extends PacketEnhancedPortals
{
    int xCoord, yCoord, zCoord, dimension;
    String Name, Texture, Identifier;
    byte Type, Thickness;

    public PacketDialEntry()
    {

    }

    public PacketDialEntry(TileEntityDialDevice device, byte type, DialDeviceNetworkObject obj)
    {
        xCoord = device.xCoord;
        yCoord = device.yCoord;
        zCoord = device.zCoord;
        dimension = device.worldObj.provider.dimensionId;
        Name = obj.displayName;
        Texture = obj.texture;
        Identifier = obj.network;
        Thickness = obj.thickness;
        Type = type;
    }

    public PacketDialEntry(TileEntityDialDevice device, byte type, String name, String texture, String identifier, byte thickness)
    {
        xCoord = device.xCoord;
        yCoord = device.yCoord;
        zCoord = device.zCoord;
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
        xCoord = stream.readInt();
        yCoord = stream.readInt();
        zCoord = stream.readInt();
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
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (world.getBlockId(xCoord, yCoord, zCoord) == BlockIds.DialDevice)
        {
            if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityDialDevice)
            {
                if (Type == 0)
                {
                    TileEntityDialDevice dial = (TileEntityDialDevice) world.getBlockTileEntity(xCoord, yCoord, zCoord);

                    if (!dial.hasDestination(new DialDeviceNetworkObject(Name, Identifier, Texture, Thickness)))
                    {
                        dial.destinationList.add(new DialDeviceNetworkObject(Name, Identifier, Texture, Thickness));
                    }
                }
                else if (Type == 1)
                {
                    TileEntityDialDevice dial = (TileEntityDialDevice) world.getBlockTileEntity(xCoord, yCoord, zCoord);

                    dial.removeDestination(new DialDeviceNetworkObject(Name, Identifier, Texture, Thickness));
                }
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension, Name, Texture, Identifier, Thickness, Type);
    }
}
