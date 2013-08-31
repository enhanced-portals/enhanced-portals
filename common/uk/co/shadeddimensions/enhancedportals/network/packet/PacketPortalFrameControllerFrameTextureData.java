package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameControllerFrameTexture;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class PacketPortalFrameControllerFrameTextureData extends MainPacket
{
    int colour;
    String texture;

    public PacketPortalFrameControllerFrameTextureData()
    {

    }
    
    public PacketPortalFrameControllerFrameTextureData(TilePortalFrameController controller)
    {
        colour = controller.frameTexture.TextureColour;
        texture = controller.frameTexture.Texture;
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        colour = stream.readInt();
        texture = stream.readUTF();
        
        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        Container container = player.openContainer;
        
        if (container != null && container instanceof ContainerPortalFrameControllerFrameTexture)
        {
            TilePortalFrameController controller = ((ContainerPortalFrameControllerFrameTexture) container).controller;
            
            controller.frameTexture.TextureColour = colour;
            controller.frameTexture.Texture = texture;
            CommonProxy.sendUpdatePacketToAllAround(controller);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        stream.writeInt(colour);
        stream.writeUTF(texture);
    }
}
