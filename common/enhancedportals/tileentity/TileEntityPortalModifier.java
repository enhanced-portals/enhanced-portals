package enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import alz.core.lib.WorldLocation;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.lib.BlockIds;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketRequestData;
import enhancedportals.portal.Portal;
import enhancedportals.portal.upgrades.UpgradeHandler;
import enhancedportals.portal.upgrades.modifier.UpgradeNetherFrame;
import enhancedportals.portal.upgrades.modifier.UpgradeResourceFrame;

public class TileEntityPortalModifier extends TileEntityEnhancedPortals
{
    public String         texture;
    public byte           thickness, redstoneSetting;
    public String         network;
    public ItemStack[]    inventory;

    public UpgradeHandler upgradeHandler;

    public TileEntityPortalModifier()
    {
        texture = "";
        thickness = 0;
        redstoneSetting = 0;
        network = "";

        upgradeHandler = new UpgradeHandler(5);
    }

    public boolean createPortal()
    {
        WorldLocation portalLocation = new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata()));
        return new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).createPortal(customBorderBlocks());
    }

    public int[] customBorderBlocks()
    {
        if (upgradeHandler.hasUpgrade(new UpgradeNetherFrame()) && upgradeHandler.hasUpgrade(new UpgradeResourceFrame()))
        {
            return new int[] { Block.blockDiamond.blockID, Block.blockGold.blockID, Block.blockIron.blockID, Block.blockNetherQuartz.blockID, Block.glowStone.blockID, Block.netherBrick.blockID };
        }
        else if (upgradeHandler.hasUpgrade(new UpgradeNetherFrame()))
        {
            return new int[] { Block.blockNetherQuartz.blockID, Block.glowStone.blockID, Block.netherBrick.blockID };
        }
        else if (upgradeHandler.hasUpgrade(new UpgradeResourceFrame()))
        {
            return new int[] { Block.blockDiamond.blockID, Block.blockGold.blockID, Block.blockIron.blockID };
        }

        return null;
    }

    public void handleRedstoneChanges(int redstoneLevel)
    {
        WorldLocation portalLocation = new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata()));

        if (redstoneSetting == 0)
        {
            if (redstoneLevel >= 1 && !isAnyActive())
            {
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).createPortal(customBorderBlocks());
            }
            else if (redstoneLevel == 0 && isActive())
            {
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).removePortal();
            }
        }
        else if (redstoneSetting == 1)
        {
            if (redstoneLevel == 0 && !isAnyActive())
            {
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).createPortal(customBorderBlocks());
            }
            else if (redstoneLevel >= 1 && isActive())
            {
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).removePortal();
            }
        }
        else if (redstoneSetting > 2)
        {
            byte rsLevel = (byte) (redstoneSetting - 2);
            
            if (redstoneLevel == rsLevel && !isAnyActive())
            {
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).createPortal(customBorderBlocks());
            }
            else if (redstoneLevel != rsLevel && isActive())
            {
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).removePortal();
            }
        }
    }

    public boolean isActive()
    {
        WorldLocation block = new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata()));
        TileEntityNetherPortal portal = null;

        if (block.getBlockId() != BlockIds.NetherPortal)
        {
            return false;
        }

        portal = (TileEntityNetherPortal) block.getTileEntity();

        if (worldObj.isRemote)
        {
            return portal.hasParent;
        }
        else
        {
            return portal != null && portal.parentModifier != null && portal.parentModifier.isEqual(new WorldLocation(xCoord, yCoord, zCoord, worldObj));
        }
    }

    public boolean isAnyActive()
    {
        WorldLocation block = new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata()));

        return block.getBlockId() == BlockIds.NetherPortal;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        texture = tagCompound.getString("Texture");
        thickness = tagCompound.getByte("Thickness");
        redstoneSetting = tagCompound.getByte("RedstoneSetting");
        network = tagCompound.getString("Frequency");
        redstoneSetting = tagCompound.getByte("RedstoneSetting");

        for (int i = 0; i < upgradeHandler.getMaximumUpgrades(); i++)
        {
            if (tagCompound.hasKey("Upgrade" + i))
            {
                upgradeHandler.addUpgrade(tagCompound.getByte("Upgrade" + i), this);
            }
        }
    }

    public void removePortal()
    {
        WorldLocation portalLocation = new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata()));
        new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).removePortal();
    }

    public boolean updateData(byte thick)
    {
        if (thick == thickness)
        {
            return false;
        }

        thickness = thick;
        return true;
    }


    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketRequestData(this)));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setString("Texture", texture);
        tagCompound.setByte("Thickness", thickness);
        tagCompound.setByte("RedstoneSetting", redstoneSetting);
        tagCompound.setString("Frequency", network);
        tagCompound.setByte("RedstoneSetting", redstoneSetting);

        int i = 0;
        for (byte b : upgradeHandler.getInstalledUpgrades())
        {
            tagCompound.setByte("Upgrade" + i, b);
            i++;
        }
    }
}
