package enhancedportals.network.packet;

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
    int    xCoord, yCoord, zCoord, dimension;
    String texture;
    byte   thickness;
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
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I", "b", "B", "B", "S", "B");

            if (objArray != null)
            {
                xCoord = (int) objArray[0];
                yCoord = (int) objArray[1];
                zCoord = (int) objArray[2];
                dimension = (int) objArray[3];
                thickness = (byte) objArray[4];
                particles = (boolean) objArray[5];
                sound = (boolean) objArray[6];
                texture = (String) objArray[7];
                hasParent = (boolean) objArray[8];

                return this;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
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
