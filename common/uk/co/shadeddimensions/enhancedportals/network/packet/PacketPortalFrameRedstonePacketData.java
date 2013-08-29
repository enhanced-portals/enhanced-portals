package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import uk.co.shadeddimensions.enhancedportals.gui.ContainerPortalFrameRedstone;

public class PacketPortalFrameRedstonePacketData extends MainPacket
{
    int button;
    
    public PacketPortalFrameRedstonePacketData()
    {
        
    }
    
    public PacketPortalFrameRedstonePacketData(int Button)
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
                
        if (container instanceof ContainerPortalFrameRedstone)
        {
            ContainerPortalFrameRedstone rsContainer = (ContainerPortalFrameRedstone) container;            
            rsContainer.redstone.buttonPressed(button, player);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        stream.writeInt(button);
    }
}
