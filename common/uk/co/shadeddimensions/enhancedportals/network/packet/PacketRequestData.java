package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileRedstoneInterface;
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

        if (tile instanceof TilePortalController)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketPortalControllerData((TilePortalController) tile)), (Player) player);
        }
        else if (tile instanceof TileModuleManipulator)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketModuleManipulator((TileModuleManipulator) tile)), (Player) player);
        }
        else if (tile instanceof TileNetworkInterface)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketNetworkInterfaceData((TileNetworkInterface) tile)), (Player) player);
        }
        else if (tile instanceof TileRedstoneInterface)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketRedstoneInterfaceData((TileRedstoneInterface) tile)), (Player) player);
        }
        else if (tile instanceof TilePortal)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketPortalData((TilePortal) tile)), (Player) player);
        }
        else if (tile instanceof TilePortalFrame)
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
