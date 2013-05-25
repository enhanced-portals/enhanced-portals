package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import alz.core.lib.Misc;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.portal.Portal;
import enhancedportals.tileentity.TileEntityNetherPortal;

public class PacketNetherPortalUpdate extends PacketEnhancedPortals
{
    int    xCoord, yCoord, zCoord, dimension;
    String texture;
    byte   thickness;
    boolean particles, sound;

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
    }

    @Override
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = Misc.getObjects(data, "I", "I", "I", "I", "b", "B", "B", "S");

            if (objArray != null && objArray.length == 8)
            {
                xCoord = (int) objArray[0];
                yCoord = (int) objArray[1];
                zCoord = (int) objArray[2];
                dimension = (int) objArray[3];
                thickness = (byte) objArray[4];
                particles = (boolean) objArray[5];
                sound = (boolean) objArray[6];
                texture = (String) objArray[7];

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
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return Misc.getByteArray(xCoord, yCoord, zCoord, dimension, thickness, particles, sound, texture);
    }
}
