package uk.co.shadeddimensions.ep3.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;

public class PacketRedstoneInterfaceData extends MainPacket
{
    ChunkCoordinates pos, controller;
    boolean output;
    byte state;

    public PacketRedstoneInterfaceData()
    {

    }

    public PacketRedstoneInterfaceData(TileRedstoneInterface tile)
    {
        pos = tile.getChunkCoordinates();
        controller = tile.controller;
        output = tile.output;
        state = tile.getState();
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        pos = readChunkCoordinates(stream);
        controller = readChunkCoordinates(stream);
        output = stream.readBoolean();
        state = stream.readByte();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(pos.posX, pos.posY, pos.posZ);

        if (tile instanceof TileRedstoneInterface)
        {
            TileRedstoneInterface redstone = (TileRedstoneInterface) tile;

            redstone.output = output;
            redstone.controller = controller;
            redstone.setState(state);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        writeChunkCoordinates(pos, stream);
        writeChunkCoordinates(controller, stream);
        stream.writeBoolean(output);
        stream.writeByte(state);
    }
}
