package uk.co.shadeddimensions.ep3.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.library.container.ContainerBase;
import cpw.mods.fml.common.network.Player;

public class PacketGuiData extends PacketEnhancedPortals
{
    GuiPayload payload;

    public PacketGuiData()
    {

    }

    public PacketGuiData(GuiPayload pload)
    {
        payload = pload;
    }

    @Override
    public void readPacketData(DataInputStream stream) throws IOException
    {
        payload = new GuiPayload(stream);
    }

    @Override
    public void writePacketData(DataOutputStream stream) throws IOException
    {
        payload.write(stream);
    }

    @Override
    public void serverPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        Container container = ((EntityPlayer) player).openContainer;

        if (container != null && container instanceof ContainerBase)
        {
            ((TileEnhancedPortals) ((ContainerBase) container).object).guiActionPerformed(payload, (EntityPlayer) player);
        }
    }
    
    @Override
    public void clientPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        Container container = ((EntityPlayer) player).openContainer;

        if (container != null && container instanceof ContainerBase)
        {
            ((TileEnhancedPortals) ((ContainerBase) container).object).guiActionPerformed(payload, (EntityPlayer) player);
        }
    }
}
