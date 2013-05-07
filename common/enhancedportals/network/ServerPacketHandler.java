package enhancedportals.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import enhancedportals.lib.PacketIds;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketRequestSync;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.tileentity.TileEntityEnhancedPortals;

public class ServerPacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        if (packet.channel != Reference.MOD_ID)
        {
            return;
        }

        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.data));
        byte packetID = -1;

        try
        {
            packetID = stream.readByte();

            System.out.println("Recieved server packet of ID " + packetID);

            if (packetID == PacketIds.TileEntityUpdate)
            {
                parseTileEntityUpdate(new PacketTEUpdate(stream));
            }
            else if (packetID == PacketIds.RequestSync)
            {
                parseRequestSync(new PacketRequestSync(stream), player);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public WorldServer getWorldForDimension(int dim)
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dim);
    }
    
    public void parseRequestSync(PacketRequestSync sync, Player player)
    {
        WorldServer world = getWorldForDimension(sync.dimension);

        if (world.blockHasTileEntity(sync.xCoord, sync.yCoord, sync.zCoord))
        {
            TileEntity tileEntity = world.getBlockTileEntity(sync.xCoord, sync.yCoord, sync.zCoord);

            if (tileEntity instanceof TileEntityEnhancedPortals)
            {
                PacketDispatcher.sendPacketToPlayer(new PacketTEUpdate((TileEntityEnhancedPortals) tileEntity).getPacket(), player);
            }
        }
    }

    public void parseTileEntityUpdate(PacketTEUpdate update)
    {
        World world = getWorldForDimension(update.dimension);

        if (update.tileEntityExists(world))
        {
            ((TileEntityEnhancedPortals) update.getTileEntity(world)).parsePacketData(update.packetData);
        }
        else
        {
            System.out.println(String.format("Could not find enhanced portals tile entity at %s, %s, %s", update.xCoord, update.yCoord, update.zCoord));
        }
    }
}
