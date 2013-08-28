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
import uk.co.shadeddimensions.enhancedportals.util.PortalTexture;
import uk.co.shadeddimensions.enhancedportals.util.Texture;

public class PacketPortalFrameControllerData extends MainPacket
{
    ChunkCoordinates coord;
    Texture frameTexture;
    PortalTexture portalTexture;
    int attachedFrames, attachedFrameRedstone, attachedPortals;

    public PacketPortalFrameControllerData()
    {

    }

    public PacketPortalFrameControllerData(TilePortalFrameController tile)
    {
        coord = tile.getChunkCoordinates();
        portalTexture = tile.portalTexture;
        frameTexture = tile.frameTexture;
        attachedFrames = tile.getAttachedFrames();
        attachedFrameRedstone = tile.getAttachedFrameRedstone();
        attachedPortals = tile.getAttachedPortals();
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        coord = readChunkCoordinates(stream);
        portalTexture = PortalTexture.getTextureFromStream(stream);
        frameTexture = Texture.getTextureFromStream(stream);
        attachedFrames = stream.readInt();
        attachedFrameRedstone = stream.readInt();
        attachedPortals = stream.readInt();

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

            controller.portalTexture = portalTexture;
            controller.frameTexture = frameTexture;
            controller.attachedFrames = attachedFrames;
            controller.attachedFrameRedstone = attachedFrameRedstone;
            controller.attachedPortals = attachedPortals;
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        writeChunkCoordinates(coord, stream);
        portalTexture.writeTextureToStream(stream);
        frameTexture.writeTextureToStream(stream);
        stream.writeInt(attachedFrames);
        stream.writeInt(attachedFrameRedstone);
        stream.writeInt(attachedPortals);
    }
}
