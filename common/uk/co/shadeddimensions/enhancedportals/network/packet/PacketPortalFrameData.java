package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;

public class PacketPortalFrameData extends MainPacket
{
    ChunkCoordinates coord;

    public PacketPortalFrameData()
    {
        // TODO NOT SURE IF THIS IS REQUIRED ANYMORE
    }

    public PacketPortalFrameData(TilePortalFrame frame)
    {
        coord = frame.getChunkCoordinates();
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        coord = readChunkCoordinates(stream);

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(coord.posX, coord.posY, coord.posZ);

        if (tile != null && tile instanceof TilePortalFrame)
        {
            // TilePortalFrame frame = (TilePortalFrame) tile;

            world.markBlockForRenderUpdate(coord.posX, coord.posY, coord.posZ);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        writeChunkCoordinates(coord, stream);
    }
}
