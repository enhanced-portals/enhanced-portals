package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import enhancedcore.packet.PacketHelper;
import enhancedportals.EnhancedPortals;
import enhancedportals.tileentity.TileEntityAutomaticDialler;
import enhancedportals.tileentity.TileEntityDialDevice;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;
import enhancedportals.tileentity.TileEntityNetherPortal;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketRequestData extends PacketEnhancedPortals
{
    public int xCoord, yCoord, zCoord, dimension;

    public PacketRequestData()
    {

    }

    public PacketRequestData(TileEntity tileEntity)
    {
        xCoord = tileEntity.xCoord;
        yCoord = tileEntity.yCoord;
        zCoord = tileEntity.zCoord;
        dimension = tileEntity.worldObj.provider.dimensionId;
    }

    @Override
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I");

            if (objArray != null && objArray.length == 4)
            {
                xCoord = (int) objArray[0];
                yCoord = (int) objArray[1];
                zCoord = (int) objArray[2];
                dimension = (int) objArray[3];

                return this;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityPortalModifier)
        {
            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate((TileEntityPortalModifier) world.getBlockTileEntity(xCoord, yCoord, zCoord))), (Player) player);
            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpgrade((TileEntityPortalModifier) world.getBlockTileEntity(xCoord, yCoord, zCoord))), (Player) player);
        }
        else if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityNetherPortal)
        {
            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketNetherPortalUpdate((TileEntityNetherPortal) world.getBlockTileEntity(xCoord, yCoord, zCoord))), (Player) player);
        }
        else if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityDialDevice)
        {
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketDialDeviceUpdate((TileEntityDialDevice) world.getBlockTileEntity(xCoord, yCoord, zCoord))));
        }
        else if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityDialDeviceBasic)
        {
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketBasicDialDeviceUpdate((TileEntityDialDeviceBasic) world.getBlockTileEntity(xCoord, yCoord, zCoord))));
        }
        else if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityAutomaticDialler)
        {
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketAutomaticDiallerUpdate((TileEntityAutomaticDialler) world.getBlockTileEntity(xCoord, yCoord, zCoord))));
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension);
    }
}
