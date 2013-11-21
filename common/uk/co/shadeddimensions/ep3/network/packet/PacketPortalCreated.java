package uk.co.shadeddimensions.ep3.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.client.particle.PortalCreationFX;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.Player;

public class PacketPortalCreated extends PacketEnhancedPortals
{
    int x, y, z;

    public PacketPortalCreated()
    {

    }

    public PacketPortalCreated(TileEnhancedPortals tile)
    {
        x = tile.xCoord;
        y = tile.yCoord;
        z = tile.zCoord;
    }

    @Override
    public void readPacketData(DataInputStream stream) throws IOException
    {
        x = stream.readInt();
        y = stream.readInt();
        z = stream.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream stream) throws IOException
    {
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(z);
    }

    @Override
    public void clientPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        World w = ((EntityPlayer) player).worldObj;

        for (int i = 0; i < 70; i++)
        {
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new PortalCreationFX(w, x, y + 1, z));
        }
    }
}
