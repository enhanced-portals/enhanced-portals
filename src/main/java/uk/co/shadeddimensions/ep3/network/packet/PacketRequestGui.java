package uk.co.shadeddimensions.ep3.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import uk.co.shadeddimensions.ep3.tileentity.TileEP;
import cpw.mods.fml.common.network.Player;
import enhancedportals.EnhancedPortals;

public class PacketRequestGui extends PacketEnhancedPortals
{
    int x, y, z, gui;
    TileEP t;
    
    public PacketRequestGui()
    {
        
    }
    
    public PacketRequestGui(TileEP tile, int guiid)
    {
        t = tile;
        gui = guiid;
    }

    @Override
    public void readPacketData(DataInputStream stream) throws IOException
    {
        x = stream.readInt();
        y = stream.readInt();
        z = stream.readInt();
        gui = stream.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream stream) throws IOException
    {
        stream.writeInt(t.xCoord);
        stream.writeInt(t.yCoord);
        stream.writeInt(t.zCoord);
        stream.writeInt(gui);
    }
    
    @Override
    public void serverPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        TileEntity t = ((EntityPlayer) player).worldObj.getBlockTileEntity(x, y, z);

        if (t != null && t instanceof TileEP)
        {
            ((EntityPlayer) player).openGui(EnhancedPortals.instance, gui, t.worldObj, x, y, z);
        }
    }
}
