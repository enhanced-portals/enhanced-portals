package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.portal.Portal;
import enhancedportals.tileentity.TileEntityNetherPortal;

public class PacketNetherPortalUpdate extends PacketEnhancedPortals
{
    int xCoord, yCoord, zCoord, dimension;
    String texture;
    byte thickness;
    boolean particles, sound, hasParent;

    public PacketNetherPortalUpdate()
    {

    }

    public PacketNetherPortalUpdate(TileEntityNetherPortal portal)
    {
        xCoord = portal.xCoord;
        yCoord = portal.yCoord;
        zCoord = portal.zCoord;
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
        xCoord = stream.readInt();
        yCoord = stream.readInt();
        zCoord = stream.readInt();
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
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (world.getBlockId(xCoord, yCoord, zCoord) == BlockIds.NetherPortal)
        {
            if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityNetherPortal)
            {
                Portal portal = new Portal(xCoord, yCoord, zCoord, world);

                portal.updateData(sound, particles, thickness);
                portal.updateTexture(texture);

                ((TileEntityNetherPortal) world.getBlockTileEntity(xCoord, yCoord, zCoord)).hasParent = hasParent;
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension, thickness, particles, sound, texture, hasParent);
    }
}
