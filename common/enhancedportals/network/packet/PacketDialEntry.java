package enhancedportals.network.packet;

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
    byte   Type, Thickness;

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
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I", "S", "S", "S", "b", "b");

            if (objArray != null)
            {
                xCoord = (int) objArray[0];
                yCoord = (int) objArray[1];
                zCoord = (int) objArray[2];
                dimension = (int) objArray[3];
                Name = (String) objArray[4];
                Texture = (String) objArray[5];
                Identifier = (String) objArray[6];
                Thickness = (byte) objArray[7];
                Type = (byte) objArray[8];

                return this;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (world.getBlockId(xCoord, yCoord, zCoord) == BlockIds.DialHomeDevice)
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
