package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.lib.BlockIds;
import enhancedportals.portal.upgrades.Upgrade;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketPortalModifierUpgrade extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    byte[] upgrades;

    public PacketPortalModifierUpgrade()
    {

    }

    public PacketPortalModifierUpgrade(TileEntityPortalModifier modifier)
    {
        position = modifier.getBlockPosition();
        dimension = modifier.worldObj.provider.dimensionId;
        upgrades = modifier.upgradeHandler.getInstalledUpgrades();
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        upgrades = new byte[stream.readInt()];

        for (int i = 0; i < upgrades.length; i++)
        {
            upgrades[i] = stream.readByte();
        }

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

                if (WorldHelper.getWorld(dimension).isRemote)
                {
                    modifier.upgradeHandler.addUpgradesFromByteArray(upgrades, modifier);
                    WorldHelper.markBlockForUpdate(dimension, position);
                }
                else
                {
                    Queue<Byte> upgradeList = new LinkedList<Byte>();

                    for (byte b : modifier.upgradeHandler.getInstalledUpgrades())
                    {
                        upgradeList.add(b);
                    }

                    for (byte b : upgrades)
                    {
                        if (modifier.upgradeHandler.hasUpgrade(b))
                        {
                            upgradeList.remove(b);
                        }
                        else
                        {
                            modifier.upgradeHandler.addUpgrade(b, modifier);
                        }
                    }

                    while (!upgradeList.isEmpty())
                    {
                        byte upgradeID = upgradeList.remove();

                        EntityItem entity = new EntityItem(WorldHelper.getWorld(dimension), position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, Upgrade.getUpgrade(upgradeID).getItemStack());
                        WorldHelper.getWorld(dimension).spawnEntityInWorld(entity);

                        for (int i = 0; i < modifier.upgradeHandler.getUpgrades().size(); i++)
                        {
                            if (modifier.upgradeHandler.getUpgrades().get(i).getUpgradeID() == upgradeID)
                            {
                                modifier.upgradeHandler.getUpgrades().remove(i);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, upgrades);
    }
}
