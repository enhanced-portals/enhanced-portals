package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalController;
import uk.co.shadeddimensions.enhancedportals.util.Texture;

public class PacketPortalControllerData extends MainPacket
{
    ChunkCoordinates coord;
    Texture texture;
    
    public PacketPortalControllerData()
    {

    }
    
    public PacketPortalControllerData(TilePortalController tile)
    {
        coord = tile.getChunkCoordinates();
        texture = tile.texture;
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        coord = readChunkCoordinates(stream);
        texture = Texture.getTextureFromStream(stream);
        
        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(coord.posX, coord.posY, coord.posZ);

        if (tile != null && tile instanceof TilePortalController)
        {
            TilePortalController controller = (TilePortalController) tile;

            controller.texture = texture;
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        writeChunkCoordinates(coord, stream);
        texture.writeTextureToStream(stream);
    }
}
