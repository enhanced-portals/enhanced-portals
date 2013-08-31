package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketRequestData extends MainPacket
{
    ChunkCoordinates location;

    public PacketRequestData()
    {

    }

    public PacketRequestData(int x, int y, int z)
    {
        location = new ChunkCoordinates(x, y, z);
    }

    public PacketRequestData(TileEntity tile)
    {
        location = new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord);
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        location = readChunkCoordinates(stream);
        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(location.posX, location.posY, location.posZ);

        if (tile instanceof TilePortalFrameController)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketPortalFrameControllerData((TilePortalFrameController) tile)), (Player) player);
        }
        
        if (tile instanceof TilePortalFrame) // Needs to be in addition to
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketPortalFrameData((TilePortalFrame) tile)), (Player) player);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        writeChunkCoordinates(location, stream);
    }
}
