package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import alz.core.lib.PacketHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import enhancedportals.EnhancedPortals;

public class PacketGui extends PacketEnhancedPortals
{
    int xCoord, yCoord, zCoord, dimension, guiID;    
    
    public PacketGui()
    {
        
    }
    
    public PacketGui(int x, int y, int z, int d, int gui)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = d;
        guiID = gui;
    }
    
    public PacketGui(int x, int y, int z, World world, int gui)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = world.provider.dimensionId;
        guiID = gui;
    }

    public PacketGui(TileEntity tileEntity, int gui)
    {
        xCoord = tileEntity.xCoord;
        yCoord = tileEntity.yCoord;
        zCoord = tileEntity.zCoord;
        dimension = tileEntity.worldObj.provider.dimensionId;
        guiID = gui;
    }
    
    @Override
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        Object[] packetData = PacketHelper.getObjects(data, "I", "I", "I", "I", "I");
        
        if (packetData != null)
        {
            xCoord = (int) packetData[0];
            yCoord = (int) packetData[1];
            zCoord = (int) packetData[2];
            dimension = (int) packetData[3];
            guiID = (int) packetData[4];
        }
        else
        {
            return null;
        }
        
        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        player.openGui(EnhancedPortals.instance, guiID, EnhancedPortals.proxy.getWorld(dimension), xCoord, yCoord, zCoord);
        
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(this), (Player) player); // Send it back to the player to open it clientside, TODO : validate
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension, guiID);
    }
}
