package enhancedportals.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.PacketIds;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.network.packet.PacketNetworkUpdate;
import enhancedportals.network.packet.PacketRequestSync;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.tileentity.TileEntityEnhancedPortals;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class ServerPacketHandler implements IPacketHandler
{
    public WorldServer getWorldForDimension(int dim)
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dim);
    }

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        if (!packet.channel.equals(Reference.MOD_ID))
        {
            return;
        }

        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.data));
        byte packetID = -1;

        try
        {
            packetID = stream.readByte();

            if (packetID == PacketIds.TileEntityUpdate)
            {
                parseTileEntityUpdate(new PacketTEUpdate(stream));
            }
            else if (packetID == PacketIds.RequestSync)
            {
                parseRequestSync(new PacketRequestSync(stream), player);
            }
            else if (packetID == PacketIds.Gui)
            {
                parseGui(new PacketGui(stream), player);
            }
            else if (packetID == PacketIds.NetworkUpdate)
            {
                parseNetwork(new PacketNetworkUpdate(stream));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void parseGui(PacketGui packetGui, Player player)
    {
        if (packetGui.packetData.integerData[0] == 1 && packetGui.packetData.integerData[1] == 0)
        {
            packetGui.packetData.integerData[0] = 0;
            packetGui.packetData.integerData[1] = 1;

            ((EntityPlayer) player).openGui(EnhancedPortals.instance, packetGui.packetData.integerData[2], getWorldForDimension(packetGui.dimension), packetGui.xCoord, packetGui.yCoord, packetGui.zCoord);
            PacketDispatcher.sendPacketToPlayer(packetGui.getPacket(), player);
        }
    }

    private void parseNetwork(PacketNetworkUpdate update)
    {
        WorldServer world = getWorldForDimension(update.dimension);

        if (world.blockHasTileEntity(update.xCoord, update.yCoord, update.zCoord))
        {
            TileEntity tileEntity = world.getBlockTileEntity(update.xCoord, update.yCoord, update.zCoord);

            if (tileEntity instanceof TileEntityPortalModifier)
            {
                if (update.packetData == null)
                {
                    System.out.println("packet data is null");
                }

                ((TileEntityPortalModifier) tileEntity).network = update.packetData.stringData[0];

                PacketDispatcher.sendPacketToAllAround(update.xCoord + 0.5, update.yCoord + 0.5, update.zCoord + 0.5, 256, update.dimension, update.getPacket());
            }
        }
    }

    private void parseRequestSync(PacketRequestSync sync, Player player)
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

    private void parseTileEntityUpdate(PacketTEUpdate update)
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
