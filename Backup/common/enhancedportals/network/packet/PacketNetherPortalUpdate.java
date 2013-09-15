package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.lib.BlockIds;
import enhancedportals.portal.Portal;
import enhancedportals.tileentity.TileEntityNetherPortal;

public class PacketNetherPortalUpdate extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    String texture;
    byte thickness;
    boolean particles, sound, hasParent;

    public PacketNetherPortalUpdate()
    {

    }

    public PacketNetherPortalUpdate(TileEntityNetherPortal portal)
    {
        position = portal.getBlockPosition();
        dimension = portal.worldObj.provider.dimensionId;
        particles = portal.producesParticles;
        sound = portal.producesSound;
        texture = portal.texture;
        thickness = portal.thickness;
        hasParent = portal.hasParent;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        thickness = stream.readByte();
        particles = stream.readBoolean();
        sound = stream.readBoolean();
        texture = stream.readUTF();
        hasParent = stream.readBoolean();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        if (WorldHelper.getBlockId(dimension, position) == BlockIds.NetherPortal)
        {
            if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityNetherPortal)
            {
                Portal portal = new Portal(position.getX(), position.getY(), position.getZ(), WorldHelper.getWorld(dimension));

                portal.updateData(sound, particles, thickness);
                portal.updateTexture(texture);

                ((TileEntityNetherPortal) WorldHelper.getTileEntity(dimension, position)).hasParent = hasParent;
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, thickness, particles, sound, texture, hasParent);
    }
}
