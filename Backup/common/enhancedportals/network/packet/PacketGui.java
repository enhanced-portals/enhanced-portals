package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import uk.co.shadeddimensions.enhancedportals.EnhancedPortals_deprecated;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedportals.tileentity.TileEntityEnhancedPortals;

public class PacketGui extends PacketEnhancedPortals
{
    int dimension, guiID;
    BlockPosition position;

    public PacketGui()
    {

    }

    public PacketGui(int x, int y, int z, int d, int gui)
    {
        position = new BlockPosition(x, y, z);
        dimension = d;
        guiID = gui;
    }

    public PacketGui(int x, int y, int z, World world, int gui)
    {
        position = new BlockPosition(x, y, z);
        dimension = world.provider.dimensionId;
        guiID = gui;
    }

    public PacketGui(TileEntityEnhancedPortals tileEntity, int gui)
    {
        position = tileEntity.getBlockPosition();
        dimension = tileEntity.worldObj.provider.dimensionId;
        guiID = gui;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        guiID = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        player.openGui(EnhancedPortals_deprecated.instance, guiID, EnhancedPortals_deprecated.proxy.getWorld(dimension), position.getX(), position.getY(), position.getZ());

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(this), (Player) player);
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, guiID);
    }
}
