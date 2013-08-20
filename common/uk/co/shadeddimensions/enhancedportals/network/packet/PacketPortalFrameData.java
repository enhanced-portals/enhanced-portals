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
import uk.co.shadeddimensions.enhancedportals.util.Texture;

public class PacketPortalFrameData extends MainPacket
{
    int colour;
    ChunkCoordinates coord, controller;
    String texture;
    boolean[] activeSide;

    public PacketPortalFrameData()
    {

    }

    public PacketPortalFrameData(TilePortalFrame frame)
    {
        coord = new ChunkCoordinates(frame.xCoord, frame.yCoord, frame.zCoord);
        controller = frame.controller;
        colour = frame.texture.TextureColour;
        activeSide = frame.activeSide;
        texture = frame.texture.Texture;
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        activeSide = new boolean[6];
        coord = readChunkCoordinates(stream);
        controller = readChunkCoordinates(stream);
        colour = stream.readInt();
        
        for (int i = 0; i < 6; i++)
        {
            activeSide[i] = stream.readBoolean();
        }
        
        texture = stream.readUTF();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(coord.posX, coord.posY, coord.posZ);

        if (tile != null && tile instanceof TilePortalFrame)
        {
            TilePortalFrame frame = (TilePortalFrame) tile;

            frame.controller = controller;
            frame.texture = new Texture(texture, colour, 0xFFFFFF, 0);
            frame.activeSide = activeSide;
            world.markBlockForRenderUpdate(coord.posX, coord.posY, coord.posZ);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        writeChunkCoordinates(coord, stream);
        writeChunkCoordinates(controller, stream);
        stream.writeInt(colour);
        
        for (int i = 0; i < 6; i++)
        {
            stream.writeBoolean(activeSide[i]);
        }
        
        stream.writeUTF(texture);
    }
}
