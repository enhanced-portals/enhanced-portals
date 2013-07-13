package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityPortalModifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;

public class PacketRedstoneControl extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    byte redstone;
    
    public PacketRedstoneControl()
    {
        
    }
    
    public PacketRedstoneControl(TileEntityPortalModifier modifier)    
    {
        position = modifier.getBlockPosition();
        dimension = modifier.worldObj.provider.dimensionId;
        redstone = modifier.redstoneSetting;
    }
    
    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        redstone = stream.readByte();

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
                modifier.redstoneSetting = redstone;
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, redstone);
    }
}
