package uk.co.shadeddimensions.ep3.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import uk.co.shadeddimensions.ep3.tileentity.TileEP;
import cpw.mods.fml.common.network.Player;

public class PacketTileGui extends PacketTileUpdate
{
    public PacketTileGui()
    {

    }

    public PacketTileGui(TileEP tile)
    {
        t = tile;
        isChunkDataPacket = true;
    }
    
    @Override
    public void clientPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        TileEntity t = ((EntityPlayer) player).worldObj.getBlockTileEntity(x, y, z);

        if (t != null && t instanceof TileEP)
        {
            try
            {
                ((TileEP) t).packetGuiUse(s);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void readPacketData(DataInputStream stream) throws IOException
    {
        x = stream.readInt();
        y = stream.readInt();
        z = stream.readInt();
        s = stream;
    }

    @Override
    public void writePacketData(DataOutputStream stream) throws IOException
    {
        stream.writeInt(t.xCoord);
        stream.writeInt(t.yCoord);
        stream.writeInt(t.zCoord);

        t.packetGuiFill(stream);
    }
}
