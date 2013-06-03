package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketPortalModifierUpdate extends PacketEnhancedPortals
{
    int xCoord, yCoord, zCoord, dimension;
    byte thickness, redstoneSetting;
    String texture, network;

    public PacketPortalModifierUpdate()
    {

    }

    public PacketPortalModifierUpdate(TileEntityPortalModifier modifier)
    {
        xCoord = modifier.xCoord;
        yCoord = modifier.yCoord;
        zCoord = modifier.zCoord;
        thickness = modifier.thickness;
        redstoneSetting = modifier.redstoneSetting;
        dimension = modifier.worldObj.provider.dimensionId;
        texture = modifier.texture;
        network = modifier.network;
    }

    @Override
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I", "b", "b", "S", "S");

            if (objArray != null && objArray.length == 8)
            {
                xCoord = (int) objArray[0];
                yCoord = (int) objArray[1];
                zCoord = (int) objArray[2];
                dimension = (int) objArray[3];
                thickness = (byte) objArray[4];
                redstoneSetting = (byte) objArray[5];
                texture = (String) objArray[6];
                network = (String) objArray[7];

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

        if (world.getBlockId(xCoord, yCoord, zCoord) == BlockIds.PortalModifier)
        {
            if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityPortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(xCoord, yCoord, zCoord);

                modifier.network = this.network;
                modifier.texture = texture;
                modifier.redstoneSetting = redstoneSetting;
                modifier.thickness = thickness;

                world.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension, thickness, redstoneSetting, texture, network);
    }
}
