package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.tileentity.TileEntityAutomaticDialler;
import enhancedportals.tileentity.TileEntityDialDevice;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;
import enhancedportals.tileentity.TileEntityEnhancedPortals;
import enhancedportals.tileentity.TileEntityNetherPortal;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketRequestData extends PacketEnhancedPortals
{
    public int dimension;
    BlockPosition position;

    public PacketRequestData()
    {

    }

    public PacketRequestData(TileEntityEnhancedPortals tileEntity)
    {
        position = tileEntity.getBlockPosition();
        dimension = tileEntity.worldObj.provider.dimensionId;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityPortalModifier)
        {
            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate((TileEntityPortalModifier) WorldHelper.getTileEntity(dimension, position))), (Player) player);
            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpgrade((TileEntityPortalModifier) WorldHelper.getTileEntity(dimension, position))), (Player) player);
        }
        else if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityNetherPortal)
        {
            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketNetherPortalUpdate((TileEntityNetherPortal) WorldHelper.getTileEntity(dimension, position))), (Player) player);
        }
        else if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityDialDevice)
        {
            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketDialDeviceUpdate((TileEntityDialDevice) WorldHelper.getTileEntity(dimension, position))), (Player) player);
        }
        else if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityDialDeviceBasic)
        {
            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketBasicDialDeviceUpdate((TileEntityDialDeviceBasic) WorldHelper.getTileEntity(dimension, position))), (Player) player);
        }
        else if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityAutomaticDialler)
        {
            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketAutomaticDiallerUpdate((TileEntityAutomaticDialler) WorldHelper.getTileEntity(dimension, position))), (Player) player);
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension);
    }
}
