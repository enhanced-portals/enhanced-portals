package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.portal.ClientBlockManager;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TilePortalController;

public class PacketPortalControllerData extends MainPacket
{
    ChunkCoordinates coord;
    int attachedFrames, attachedFrameRedstone, attachedPortals;
    int FrameColour, PortalColour, ParticleColour, ParticleType;
    boolean biometric, dialDevice, networkInterface;
    String uID;
    
    int item1ID, item1Meta, item2ID, item2Meta;

    public PacketPortalControllerData()
    {

    }

    public PacketPortalControllerData(TilePortalController tile)
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
        
        if (tile.getStackInSlot(0) != null)
        {
            item1ID = tile.getStackInSlot(0).itemID;
            item1Meta = tile.getStackInSlot(0).getItemDamage();
        }
        else
        {
            item1ID = item1Meta = 0;
        }
        
        if (tile.getStackInSlot(1) != null)
        {
            item2ID = tile.getStackInSlot(1).itemID;
            item2Meta = tile.getStackInSlot(1).getItemDamage();
        }
        else
        {
            item2ID = item2Meta = 0;
        }
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
        item1ID = stream.readInt();
        item1Meta = stream.readInt();
        item2ID = stream.readInt();
        item2Meta = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(coord.posX, coord.posY, coord.posZ);

        if (tile != null && tile instanceof TilePortalController)
        {
            TilePortalController controller = (TilePortalController) tile;

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
            
            if (item1ID != 0)
            {
                controller.setInventorySlotContents(0, new ItemStack(item1ID, 1, item1Meta));
            }
            
            if (item2ID != 0)
            {
                controller.setInventorySlotContents(1, new ItemStack(item2ID, 1, item2Meta));
            }
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
        stream.writeInt(item1ID);
        stream.writeInt(item1Meta);
        stream.writeInt(item2ID);
        stream.writeInt(item2Meta);
    }
}
