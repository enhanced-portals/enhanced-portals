package uk.co.shadeddimensions.ep3.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import uk.co.shadeddimensions.ep3.container.ContainerEnhancedPortals;

public class PacketGuiInteger extends MainPacket
{
    int id, data;

    public PacketGuiInteger()
    {

    }

    public PacketGuiInteger(int i, int d)
    {
        id = i;
        data = d;
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        id = stream.readInt();
        data = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        Container container = player.openContainer;

        if (container instanceof ContainerEnhancedPortals)
        {
            ContainerEnhancedPortals cont = (ContainerEnhancedPortals) container;
            cont.tile.actionPerformed(id, data, player);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        stream.writeInt(id);
        stream.writeInt(data);
    }
}
