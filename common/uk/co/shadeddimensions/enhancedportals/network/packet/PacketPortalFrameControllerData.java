package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class PacketPortalFrameControllerData extends MainPacket
{
    ChunkCoordinates coord;
    int attachedFrames, attachedFrameRedstone, attachedPortals;
    int FrameColour, PortalColour, ParticleColour, ParticleType;
    String uID;

    public PacketPortalFrameControllerData()
    {

    }

    public PacketPortalFrameControllerData(TilePortalFrameController tile)
    {
        coord = tile.getChunkCoordinates();
        attachedFrames = tile.getAttachedFrames();
        attachedFrameRedstone = tile.getAttachedFrameRedstone();
        attachedPortals = tile.getAttachedPortals();
        uID = tile.UniqueIdentifier;
        FrameColour = tile.FrameColour;
        PortalColour = tile.PortalColour;
        ParticleColour = tile.ParticleColour;
        ParticleType = tile.ParticleType;
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        coord = readChunkCoordinates(stream);
        attachedFrames = stream.readInt();
        attachedFrameRedstone = stream.readInt();
        attachedPortals = stream.readInt();
        uID = stream.readUTF();
        FrameColour = stream.readInt();
        PortalColour = stream.readInt();
        ParticleColour = stream.readInt();
        ParticleType = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(coord.posX, coord.posY, coord.posZ);

        if (tile != null && tile instanceof TilePortalFrameController)
        {
            TilePortalFrameController controller = (TilePortalFrameController) tile;

            controller.attachedFrames = attachedFrames;
            controller.attachedFrameRedstone = attachedFrameRedstone;
            controller.attachedPortals = attachedPortals;
            controller.UniqueIdentifier = uID;
            controller.FrameColour = FrameColour;
            controller.PortalColour = PortalColour;
            controller.ParticleColour = ParticleColour;
            controller.ParticleType = ParticleType;
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        writeChunkCoordinates(coord, stream);
        stream.writeInt(attachedFrames);
        stream.writeInt(attachedFrameRedstone);
        stream.writeInt(attachedPortals);
        stream.writeUTF(uID);
        stream.writeInt(FrameColour);
        stream.writeInt(PortalColour);
        stream.writeInt(ParticleColour);
        stream.writeInt(ParticleType);
    }
}
