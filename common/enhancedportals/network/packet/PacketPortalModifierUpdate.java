package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedcore.world.WorldPosition;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketPortalModifierUpdate extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    byte thickness, redstoneSetting;
    String texture, modifierNetwork, dialDeviceNetwork;

    public PacketPortalModifierUpdate()
    {

    }

    public PacketPortalModifierUpdate(TileEntityPortalModifier modifier)
    {
        position = modifier.getBlockPosition();
        thickness = modifier.thickness;
        redstoneSetting = modifier.redstoneSetting;
        dimension = modifier.worldObj.provider.dimensionId;
        texture = modifier.texture;
        modifierNetwork = modifier.modifierNetwork;
        dialDeviceNetwork = modifier.dialDeviceNetwork;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        thickness = stream.readByte();
        redstoneSetting = stream.readByte();
        texture = stream.readUTF();
        modifierNetwork = stream.readUTF();
        dialDeviceNetwork = stream.readUTF();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        WorldPosition loc = new WorldPosition(position.getX(), position.getY(), position.getZ(), dimension);

        if (WorldHelper.getBlockId(dimension, position) == BlockIds.PortalModifier)
        {
            if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityPortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) WorldHelper.getTileEntity(dimension, position);

                modifier.texture = texture;
                modifier.redstoneSetting = redstoneSetting;
                modifier.thickness = thickness;

                if (!WorldHelper.getWorld(dimension).isRemote)
                {
                    if (modifier.isRemotelyControlled())
                    {
                        EnhancedPortals.proxy.DialDeviceNetwork.removeFromAllNetworks(loc);
                        EnhancedPortals.proxy.DialDeviceNetwork.addToNetwork(dialDeviceNetwork, loc);

                        if (EnhancedPortals.proxy.DialDeviceNetwork.isInNetwork(dialDeviceNetwork, loc))
                        {
                            modifier.dialDeviceNetwork = dialDeviceNetwork;
                        }
                        else
                        {
                            modifier.dialDeviceNetwork = "";
                            PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketMisc((byte) 1, 0)), (Player) player);
                        }
                    }
                    else
                    {
                        EnhancedPortals.proxy.ModifierNetwork.removeFromAllNetworks(loc);
                        EnhancedPortals.proxy.ModifierNetwork.addToNetwork(modifierNetwork, loc);

                        modifier.modifierNetwork = modifierNetwork;
                    }
                }
                else
                {
                    modifier.modifierNetwork = modifierNetwork;
                    modifier.dialDeviceNetwork = dialDeviceNetwork;
                }

                WorldHelper.markBlockForUpdate(dimension, position);
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, thickness, redstoneSetting, texture, modifierNetwork, dialDeviceNetwork);
    }
}
