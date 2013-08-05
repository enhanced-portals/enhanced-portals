package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.util.Texture;

public class PacketPortalFrameData extends MainPacket
{
    int x, y, z, colour;
    String texture;
    
    public PacketPortalFrameData()
    {
        
    }
    
    public PacketPortalFrameData(TilePortalFrame frame)
    {
        x = frame.xCoord;
        y = frame.yCoord;
        z = frame.zCoord;
        colour = frame.texture.TextureColour;
        texture = frame.texture.Texture;
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        x = stream.readInt();
        y = stream.readInt();
        z = stream.readInt();
        colour = stream.readInt();
        texture = stream.readUTF();
        
        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        
        if (tile != null && tile instanceof TilePortalFrame)
        {
            TilePortalFrame frame = (TilePortalFrame) tile;
            
            frame.texture = new Texture(texture, colour, 0xFFFFFF);
            world.markBlockForRenderUpdate(x, y, z);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(z);
        stream.writeInt(colour);
        stream.writeUTF(texture);
    }
}
