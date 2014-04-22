package uk.co.shadeddimensions.ep3.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import uk.co.shadeddimensions.ep3.tileentity.TileEP;
import uk.co.shadeddimensions.library.container.ContainerBase;
import cpw.mods.fml.common.network.Player;
import enhancedportals.inventory.BaseContainer;

public class PacketGuiData extends PacketEnhancedPortals
{
    NBTTagCompound tag;

    public PacketGuiData()
    {

    }

    public PacketGuiData(NBTTagCompound t)
    {
        tag = t;
    }

    @Override
    public void clientPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        Container container = ((EntityPlayer) player).openContainer;
        
        if (container != null && container instanceof ContainerBase)
        {
            ((TileEP) ((ContainerBase) container).object).packetGui(tag, (EntityPlayer) player);
        }
        else if (container != null && container instanceof BaseContainer)
        {
            ((BaseContainer) container).handleGuiPacket(tag, (EntityPlayer) player);
        }
    }

    @Override
    public void readPacketData(DataInputStream stream) throws IOException
    {
        short length = stream.readShort();
        byte[] compressed = new byte[length];
        stream.readFully(compressed);
        tag = CompressedStreamTools.decompress(compressed);
    }

    @Override
    public void serverPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        Container container = ((EntityPlayer) player).openContainer;

        if (container != null && container instanceof ContainerBase)
        {
            ((TileEP) ((ContainerBase) container).object).packetGui(tag, (EntityPlayer) player);
        }
        else if (container != null && container instanceof BaseContainer)
        {
            ((BaseContainer) container).handleGuiPacket(tag, (EntityPlayer) player);
        }
    }

    @Override
    public void writePacketData(DataOutputStream stream) throws IOException
    {
        byte[] compressed = CompressedStreamTools.compress(tag);
        stream.writeShort(compressed.length);
        stream.write(compressed);
    }
}
