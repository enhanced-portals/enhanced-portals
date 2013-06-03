package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityAutomaticDialler;

public class PacketAutomaticDiallerUpdate extends PacketEnhancedPortals
{
    int xCoord, yCoord, zCoord, dimension;
    String theNetwork;
    
    public PacketAutomaticDiallerUpdate()
    {
        
    }
    
    public PacketAutomaticDiallerUpdate(TileEntityAutomaticDialler dialler)
    {
        xCoord = dialler.xCoord;
        yCoord = dialler.yCoord;
        zCoord = dialler.zCoord;
        dimension = dialler.worldObj.provider.dimensionId;
        theNetwork = dialler.activeNetwork;
    }

    @Override
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I", "S");

            if (objArray != null)
            {
                xCoord = (int) objArray[0];
                yCoord = (int) objArray[1];
                zCoord = (int) objArray[2];
                dimension = (int) objArray[3];
                theNetwork = (String) objArray[4];

                return this;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (world.getBlockId(xCoord, yCoord, zCoord) == BlockIds.AutomaticDialler)
        {
            if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityAutomaticDialler)
            {
                TileEntityAutomaticDialler dialler = (TileEntityAutomaticDialler) world.getBlockTileEntity(xCoord, yCoord, zCoord);                
                dialler.activeNetwork = theNetwork;
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension, theNetwork);
    }
}
