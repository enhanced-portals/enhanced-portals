package enhancedportals.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.PacketIds;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.network.packet.PacketNetworkUpdate;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.tileentity.TileEntityEnhancedPortals;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class ClientPacketHandler implements IPacketHandler
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

            if (packetID == PacketIds.TileEntityUpdate)
            {
                parseTileEntityUpdate(new PacketTEUpdate(stream));
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

    private void parseNetwork(PacketNetworkUpdate update)
    {
        World world = FMLClientHandler.instance().getClient().theWorld;

        if (world.blockHasTileEntity(update.xCoord, update.yCoord, update.zCoord))
        {
            TileEntity tileEntity = world.getBlockTileEntity(update.xCoord, update.yCoord, update.zCoord);

            if (tileEntity instanceof TileEntityPortalModifier)
            {
                ((TileEntityPortalModifier)tileEntity).network = update.packetData.stringData[0];
            }
        }
    }

    private void parseGui(PacketGui packetGui, Player player)
    {
        if (packetGui.packetData.integerData[0] == 0 && packetGui.packetData.integerData[1] == 1)
        {
            ((EntityPlayer)player).openGui(EnhancedPortals.instance, GuiIds.PortalModifierNetwork, FMLClientHandler.instance().getClient().theWorld, packetGui.xCoord, packetGui.yCoord, packetGui.zCoord);
        }
    }

    private void parseTileEntityUpdate(PacketTEUpdate update)
    {
        World world = FMLClientHandler.instance().getClient().theWorld;

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
