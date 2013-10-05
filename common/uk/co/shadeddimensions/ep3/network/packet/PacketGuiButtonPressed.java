package uk.co.shadeddimensions.ep3.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import uk.co.shadeddimensions.ep3.container.ContainerEnhancedPortals;

public class PacketGuiButtonPressed extends MainPacket
{
    int button;

    public PacketGuiButtonPressed()
    {

    }

    public PacketGuiButtonPressed(int Button)
    {
        button = Button;
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        button = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        Container container = player.openContainer;

        if (container instanceof ContainerEnhancedPortals)
        {
            ContainerEnhancedPortals Container = (ContainerEnhancedPortals) container;
            Container.tile.buttonPressed(button, player);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        stream.writeInt(button);
    }
}
