package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.portal.upgrades.Upgrade;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketPortalModifierUpgrade extends PacketEnhancedPortals
{
    int xCoord, yCoord, zCoord, dimension;
    byte[] upgrades;

    public PacketPortalModifierUpgrade()
    {

    }

    public PacketPortalModifierUpgrade(TileEntityPortalModifier modifier)
    {
        xCoord = modifier.xCoord;
        yCoord = modifier.yCoord;
        zCoord = modifier.zCoord;
        dimension = modifier.worldObj.provider.dimensionId;
        upgrades = modifier.upgradeHandler.getInstalledUpgrades();
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        xCoord = stream.readInt();
        yCoord = stream.readInt();
        zCoord = stream.readInt();
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
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (world.getBlockId(xCoord, yCoord, zCoord) == BlockIds.PortalModifier)
        {
            if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityPortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(xCoord, yCoord, zCoord);

                if (world.isRemote)
                {
                    modifier.upgradeHandler.addUpgradesFromByteArray(upgrades, modifier);
                    world.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
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

                        EntityItem entity = new EntityItem(world, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, Upgrade.getUpgrade(upgradeID).getItemStack());
                        world.spawnEntityInWorld(entity);

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
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension, upgrades);
    }
}
