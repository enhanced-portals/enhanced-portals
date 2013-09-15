package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.lib.BlockIds;
import enhancedportals.portal.upgrades.Upgrade;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketUpgrade extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    byte type, upgrade;

    public PacketUpgrade()
    {

    }

    public PacketUpgrade(TileEntityPortalModifier modifier, byte t, byte u)
    {
        position = modifier.getBlockPosition();
        dimension = modifier.worldObj.provider.dimensionId;
        type = t;
        upgrade = u;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        type = stream.readByte();
        upgrade = stream.readByte();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        if (WorldHelper.getBlockId(dimension, position) == BlockIds.PortalModifier)
        {
            if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityPortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) WorldHelper.getTileEntity(dimension, position);

                if (type == 0)
                {
                    // install upgrade
                    if (modifier.upgradeHandler.addUpgrade(upgrade, modifier))
                    {
                        // remove from inventory

                        if (!WorldHelper.getWorld(dimension).isRemote)
                        {
                            // send update packet
                        }
                    }
                }
                else if (type == 1)
                {
                    // remove upgrade
                    if (modifier.upgradeHandler.removeUpgrade(upgrade, modifier))
                    {
                        if (!WorldHelper.getWorld(dimension).isRemote)
                        {
                            EntityItem entity = new EntityItem(WorldHelper.getWorld(dimension), position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, Upgrade.getUpgrade(upgrade).getItemStack());
                            WorldHelper.getWorld(dimension).spawnEntityInWorld(entity);

                            PacketHelper.sendPacketToAllAround(modifier, PacketEnhancedPortals.makePacket(new PacketPortalModifierUpgrade(modifier)));
                        }
                    }
                }
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, type, upgrade);
    }
}
