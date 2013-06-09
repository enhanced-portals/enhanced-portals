package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import enhancedcore.packet.PacketHelper;
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
        Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I", "I");

        if (objArray != null)
        {
            xCoord = Integer.parseInt(objArray[0].toString());
            yCoord = Integer.parseInt(objArray[1].toString());
            zCoord = Integer.parseInt(objArray[2].toString());
            dimension = Integer.parseInt(objArray[3].toString());
            guiID = Integer.parseInt(objArray[4].toString());
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
