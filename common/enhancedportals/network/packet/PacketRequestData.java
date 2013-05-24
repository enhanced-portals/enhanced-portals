package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.bouncycastle.util.Arrays;

import alz.core.lib.Misc;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
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
        xCoord = Misc.byteArrayToInteger(Arrays.copyOfRange(data, 0, 4));
        yCoord = Misc.byteArrayToInteger(Arrays.copyOfRange(data, 4, 8));
        zCoord = Misc.byteArrayToInteger(Arrays.copyOfRange(data, 8, 12));
        dimension = data[12];

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (world.getBlockId(xCoord, yCoord, zCoord) == BlockIds.PortalModifier)
        {
            if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityPortalModifier)
            {
                PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate((TileEntityPortalModifier) world.getBlockTileEntity(xCoord, yCoord, zCoord))), (Player) player);
            }
            else if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityNetherPortal)
            {
                PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketNetherPortalUpdate((TileEntityNetherPortal) world.getBlockTileEntity(xCoord, yCoord, zCoord))), (Player) player);
            }
            else if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityDialDevice)
            {

            }
            else if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityDialDeviceBasic)
            {

            }
            else if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityAutomaticDialler)
            {

            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        byte[] byteData = Misc.integerToByteArray(xCoord);
        Misc.concatByteArray(byteData, Misc.integerToByteArray(yCoord));
        Misc.concatByteArray(byteData, Misc.integerToByteArray(zCoord));
        Misc.concatByteArray(byteData, new byte[] { (byte) dimension });

        return byteData;
    }
}
