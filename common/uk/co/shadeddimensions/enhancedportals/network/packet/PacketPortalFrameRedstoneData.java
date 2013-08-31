package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;

public class PacketPortalFrameRedstoneData extends MainPacket
{
    ChunkCoordinates pos;
    boolean output;
    byte state;

    public PacketPortalFrameRedstoneData()
    {

    }

    public PacketPortalFrameRedstoneData(TilePortalFrameRedstone tile)
    {
        pos = tile.getChunkCoordinates();
        output = tile.output;
        state = tile.getState();
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        pos = readChunkCoordinates(stream);
        output = stream.readBoolean();
        state = stream.readByte();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(pos.posX, pos.posY, pos.posZ);

        if (tile instanceof TilePortalFrameRedstone)
        {
            TilePortalFrameRedstone redstone = (TilePortalFrameRedstone) tile;

            redstone.output = output;
            redstone.setState(state);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        writeChunkCoordinates(pos, stream);
        stream.writeBoolean(output);
        stream.writeByte(state);
    }
}
