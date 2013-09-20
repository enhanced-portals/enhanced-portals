package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.portal.ClientBlockManager;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class PacketPortalFrameControllerData extends MainPacket
{
    ChunkCoordinates coord;
    int attachedFrames, attachedFrameRedstone, attachedPortals;
    int FrameColour, PortalColour, ParticleColour, ParticleType;
    boolean biometric, dialDevice, networkInterface;
    String uID;

    public PacketPortalFrameControllerData()
    {

    }

    public PacketPortalFrameControllerData(TilePortalFrameController tile)
    {
        coord = tile.getChunkCoordinates();
        attachedFrames = tile.blockManager.getPortalFrameCoord().size();
        attachedFrameRedstone = tile.blockManager.getRedstoneCoord().size();
        attachedPortals = tile.blockManager.getPortalsCoord().size();
        uID = tile.UniqueIdentifier;
        FrameColour = tile.FrameColour;
        PortalColour = tile.PortalColour;
        ParticleColour = tile.ParticleColour;
        ParticleType = tile.ParticleType;
        biometric = tile.blockManager.getBiometricCoord() != null;
        dialDevice = tile.blockManager.getDialDeviceCoord() != null;
        networkInterface = tile.blockManager.getNetworkInterfaceCoord() != null;
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
        biometric = stream.readBoolean();
        dialDevice = stream.readBoolean();
        networkInterface = stream.readBoolean();

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

            controller.UniqueIdentifier = uID;
            controller.FrameColour = FrameColour;
            controller.PortalColour = PortalColour;
            controller.ParticleColour = ParticleColour;
            controller.ParticleType = ParticleType;
            
            controller.blockManager = new ClientBlockManager();
            ClientBlockManager blockManager = (ClientBlockManager) controller.blockManager;
            blockManager.portal = attachedPortals;
            blockManager.portalFrame = attachedFrames;
            blockManager.redstone = attachedFrameRedstone;
            blockManager.biometric = biometric;
            blockManager.dialDevice = dialDevice;
            blockManager.networkInterface = networkInterface;
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
        stream.writeBoolean(biometric);
        stream.writeBoolean(dialDevice);
        stream.writeBoolean(networkInterface);
    }
}
