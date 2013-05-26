package enhancedportals.network.packet;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import alz.core.lib.PacketHelper;
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
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I", "b[]");

            if (objArray != null)
            {
                xCoord = (int) objArray[0];
                yCoord = (int) objArray[1];
                zCoord = (int) objArray[2];
                dimension = (int) objArray[3];
                upgrades = (byte[]) objArray[4];
                
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
                            upgradeList.remove((Object) b);
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
