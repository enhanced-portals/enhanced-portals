package enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.world.WorldLocation;
import enhancedportals.lib.BlockIds;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketRequestData;
import enhancedportals.portal.Portal;
import enhancedportals.portal.upgrades.UpgradeHandler;
import enhancedportals.portal.upgrades.modifier.UpgradeDialDevice;
import enhancedportals.portal.upgrades.modifier.UpgradeNetherFrame;
import enhancedportals.portal.upgrades.modifier.UpgradeResourceFrame;

public class TileEntityPortalModifier extends TileEntityEnhancedPortals
{
    public String         texture;
    public byte           thickness, redstoneSetting, redstoneState;
    public String         modifierNetwork, dialDeviceNetwork, tempDialDeviceNetwork;
    public UpgradeHandler upgradeHandler;
    
    public TileEntityPortalModifier()
    {
        texture = "";
        thickness = 0;
        redstoneSetting = 0;
        redstoneState = 0;
        modifierNetwork = "";
        dialDeviceNetwork = "";
        tempDialDeviceNetwork = "";

        upgradeHandler = new UpgradeHandler(5);
    }

    public boolean createPortal()
    {
        if (upgradeHandler.hasUpgrade(new UpgradeDialDevice()))
        {
            return false;
        }

        return new Portal(this).createPortal(customBorderBlocks());
    }

    public boolean createPortal(ItemStack stack)
    {
        if (upgradeHandler.hasUpgrade(new UpgradeDialDevice()))
        {
            return false;
        }

        return new Portal(this).createPortal(customBorderBlocks(), stack);
    }

    public boolean createPortalFromDialDevice()
    {
        return new Portal(this).createPortal(customBorderBlocks());
    }

    public boolean createPortalFromDialDevice(String texture, byte thickness)
    {
        return new Portal(this).createPortal(texture, thickness, customBorderBlocks());
    }

    public int[] customBorderBlocks()
    {
        if (upgradeHandler.hasUpgrade(new UpgradeNetherFrame()) && upgradeHandler.hasUpgrade(new UpgradeResourceFrame()))
        {
            return new int[] { Block.blockDiamond.blockID, Block.blockGold.blockID, Block.blockIron.blockID, Block.blockEmerald.blockID, Block.blockNetherQuartz.blockID, Block.glowStone.blockID, Block.netherBrick.blockID };
        }
        else if (upgradeHandler.hasUpgrade(new UpgradeNetherFrame()))
        {
            return new int[] { Block.blockNetherQuartz.blockID, Block.glowStone.blockID, Block.netherBrick.blockID };
        }
        else if (upgradeHandler.hasUpgrade(new UpgradeResourceFrame()))
        {
            return new int[] { Block.blockDiamond.blockID, Block.blockGold.blockID, Block.blockIron.blockID, Block.blockEmerald.blockID };
        }

        return null;
    }

    public void handleRedstoneChanges(int redstoneLevel)
    {
        if (isRemotelyControlled())
        {
            return;
        }

        if (redstoneSetting == 0)
        {
            if (redstoneLevel >= 1 && redstoneState == 0 && !isAnyActive())
            {
                new Portal(this).createPortal(customBorderBlocks());
            }
            else if (redstoneLevel == 0 && redstoneState > 0 && isActive())
            {
                new Portal(this).removePortal();
            }
        }
        else if (redstoneSetting == 1)
        {
            if (redstoneLevel == 0 && redstoneState > 0 && !isAnyActive())
            {
                new Portal(this).createPortal(customBorderBlocks());
            }
            else if (redstoneLevel >= 1 && redstoneState == 0 && isActive())
            {
                new Portal(this).removePortal();
            }
        }
        else if (redstoneSetting > 2)
        {
            byte rsLevel = (byte) (redstoneSetting - 2);

            if (redstoneLevel == rsLevel && redstoneState != rsLevel && !isAnyActive())
            {
                new Portal(this).createPortal(customBorderBlocks());
            }
            else if (redstoneLevel != rsLevel && redstoneState == rsLevel && isActive())
            {
                new Portal(this).removePortal();
            }
        }

        redstoneState = (byte) redstoneLevel;
    }

    public boolean isActive()
    {
        WorldLocation block = new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata()));
        TileEntityNetherPortal portal = null;

        if (!(block.getTileEntity() instanceof TileEntityNetherPortal))
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
            return portal != null && portal.getParentModifier() != null && portal.getParentModifier().isEqual(new WorldLocation(xCoord, yCoord, zCoord, worldObj));
        }
    }

    public boolean isAnyActive()
    {
        WorldLocation block = new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata()));

        return block.getBlockId() == BlockIds.NetherPortal;
    }

    public boolean isRemotelyControlled()
    {
        return upgradeHandler.hasUpgrade(new UpgradeDialDevice());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        texture = tagCompound.getString("Texture");
        thickness = tagCompound.getByte("Thickness");
        redstoneSetting = tagCompound.getByte("RedstoneSetting");
        redstoneState = tagCompound.getByte("RedstoneState");
        modifierNetwork = tagCompound.getString("mNetwork");
        dialDeviceNetwork = tagCompound.getString("dNetwork");
        tempDialDeviceNetwork = tagCompound.getString("dNetworkTemp");
        redstoneSetting = tagCompound.getByte("RedstoneSetting");

        for (int i = 0; i < upgradeHandler.getMaximumUpgrades(); i++)
        {
            if (tagCompound.hasKey("Upgrade" + i))
            {
                upgradeHandler.addUpgradeNoActivate(tagCompound.getByte("Upgrade" + i), this);
            }
        }
    }

    public void removePortal()
    {
        new Portal(this).removePortal();
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
        tagCompound.setByte("RedstoneState", redstoneState);
        tagCompound.setString("mNetwork", modifierNetwork);
        tagCompound.setString("dNetwork", dialDeviceNetwork);
        tagCompound.setString("dNetworkTemp", tempDialDeviceNetwork);
        tagCompound.setByte("RedstoneSetting", redstoneSetting);

        int i = 0;
        for (byte b : upgradeHandler.getInstalledUpgrades())
        {
            tagCompound.setByte("Upgrade" + i, b);
            i++;
        }
    }
}
