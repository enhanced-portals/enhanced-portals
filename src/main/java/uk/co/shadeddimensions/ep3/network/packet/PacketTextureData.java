package uk.co.shadeddimensions.ep3.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.util.PortalTextureManager;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketTextureData extends PacketEnhancedPortals
{
    int id, x, y, z;
    PortalTextureManager ptm;

    public PacketTextureData()
    {
        id = -1;
    }

    public PacketTextureData(int i, int x, int y, int z)
    {
        id = i;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PacketTextureData(PortalTextureManager t)
    {
        id = -1;
        ptm = t;
    }

    @Override
    public void clientPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        if (id != -1)
        {
            return;
        }

        ClientProxy.dialEntryTexture = ptm;
        FMLClientHandler.instance().getClient().currentScreen.initGui(); // Force the UI elements to update to reflect the data that should be shown
    }

    @Override
    public void readPacketData(DataInputStream stream) throws IOException
    {
        if (stream.readBoolean())
        {
            id = stream.readInt();
            x = stream.readInt();
            y = stream.readInt();
            z = stream.readInt();
        }
        else
        {
            short length = stream.readShort();
            byte[] compressed = new byte[length];
            stream.readFully(compressed);
            NBTTagCompound data = CompressedStreamTools.decompress(compressed);

            ptm = new PortalTextureManager();

            if (data.hasKey("Texture"))
            {
                ptm.readFromNBT(data, "Texture");
            }

            id = -1;
        }
    }

    @Override
    public void serverPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        TileDiallingDevice dial = (TileDiallingDevice) ((EntityPlayer) player).worldObj.getBlockTileEntity(x, y, z);

        if (dial != null)
        {
            PortalTextureManager PTM = dial.glyphList.get(id).texture;
            PacketDispatcher.sendPacketToPlayer(new PacketTextureData(PTM).getPacket(), player);
        }
    }

    @Override
    public void writePacketData(DataOutputStream stream) throws IOException
    {
        if (id > -1)
        {
            stream.writeBoolean(true);
            stream.writeInt(id);
            stream.writeInt(x);
            stream.writeInt(y);
            stream.writeInt(z);
        }
        else
        {
            stream.writeBoolean(false);
            NBTTagCompound data = new NBTTagCompound();

            if (ptm != null)
            {
                ptm.writeToNBT(data, "Texture");
            }

            byte[] compressed = CompressedStreamTools.compress(data);
            stream.writeShort(compressed.length);
            stream.write(compressed);
        }
    }
}
