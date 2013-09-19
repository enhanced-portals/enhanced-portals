package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameNetworkInterface;

public class PacketNetworkInterfaceData extends MainPacket
{
    ChunkCoordinates coord;
    String networkID;
    int connectedPortals;
    
    public PacketNetworkInterfaceData()
    {
        
    }
    
    public PacketNetworkInterfaceData(TilePortalFrameNetworkInterface tile)
    {
        coord = tile.getChunkCoordinates();
        networkID = tile.NetworkIdentifier;
        connectedPortals = CommonProxy.networkManager.getNetworkedPortals(tile.NetworkIdentifier).size() - 1;
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        coord = readChunkCoordinates(stream);
        networkID = stream.readUTF();
        connectedPortals = stream.readInt();
        
        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(coord.posX, coord.posY, coord.posZ);

        if (tile != null && tile instanceof TilePortalFrameNetworkInterface)
        {
            TilePortalFrameNetworkInterface ni = (TilePortalFrameNetworkInterface) tile;

            ni.NetworkIdentifier = networkID;
            ni.connectedPortals = connectedPortals;
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        writeChunkCoordinates(coord, stream);
        stream.writeUTF(networkID);
        stream.writeInt(connectedPortals);
    }
}
