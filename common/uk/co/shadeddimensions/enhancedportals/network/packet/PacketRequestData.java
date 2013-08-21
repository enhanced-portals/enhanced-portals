package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketRequestData extends MainPacket
{
    public int x, y, z;

    public PacketRequestData()
    {

    }

    public PacketRequestData(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PacketRequestData(TileEntity tile)
    {
        x = tile.xCoord;
        y = tile.yCoord;
        z = tile.zCoord;
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        x = stream.readInt();
        y = stream.readInt();
        z = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TilePortalFrame)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketPortalFrameData((TilePortalFrame) tile)), (Player) player);
        }
        else if (tile instanceof TilePortalController)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketPortalControllerData((TilePortalController) tile)), (Player) player);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(z);
    }
}
