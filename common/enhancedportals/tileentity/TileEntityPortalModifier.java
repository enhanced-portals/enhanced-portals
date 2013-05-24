package enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import alz.core.lib.WorldLocation;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.ItemIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Textures;
import enhancedportals.portal.Portal;
import enhancedportals.portal.upgrades.Upgrade;
import enhancedportals.portal.upgrades.UpgradeHandler;
import enhancedportals.portal.upgrades.modifier.UpgradeAdvancedDimensional;
import enhancedportals.portal.upgrades.modifier.UpgradeDimensional;
import enhancedportals.portal.upgrades.modifier.UpgradeNetherFrame;
import enhancedportals.portal.upgrades.modifier.UpgradeResourceFrame;

public class TileEntityPortalModifier extends TileEntityEnhancedPortals implements IInventory
{
    public String         texture;
    public byte           thickness, redstoneSetting;
    public String         network;
    private boolean       oldRedstoneState;
    public ItemStack[]    inventory;

    public UpgradeHandler upgradeHandler;

    public TileEntityPortalModifier()
    {
        texture = "";
        thickness = 0;
        redstoneSetting = 0;
        network = "";
        oldRedstoneState = false;
        inventory = new ItemStack[1];

        upgradeHandler = new UpgradeHandler(5);
    }

    @Override
    public void closeChest()
    {

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

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack stack = inventory[i];
        stack.stackSize -= j;

        return stack;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public String getInvName()
    {
        return Localization.PortalModifier_Name;
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return inventory[i];
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return inventory[i];
    }

    public void handleRedstoneChanges(boolean currentRedstoneState)
    {
        WorldLocation portalLocation = new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata()));

        if (redstoneSetting == 0)
        {
            if (!oldRedstoneState && currentRedstoneState && !isAnyActive())
            {
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).createPortal(customBorderBlocks());
            }
            else if (oldRedstoneState && !currentRedstoneState && isActive())
            {
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).removePortal();
            }
        }
        else if (redstoneSetting == 1)
        {
            if (oldRedstoneState && !currentRedstoneState && !isAnyActive())
            {
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).createPortal(customBorderBlocks());
            }
            else if (!oldRedstoneState && currentRedstoneState && isActive())
            {
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).removePortal();
            }
        }

        oldRedstoneState = currentRedstoneState;
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
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack)
    {
        return true;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void onInventoryChanged()
    {
        super.onInventoryChanged();

        if (getStackInSlot(0) != null)
        {
            ItemStack stack = getStackInSlot(0);
            String text = null;

            if (Textures.getTextureFromItemStack(stack) != null)
            {
                text = Textures.getTextureFromItemStack(stack).getID();

                if (text.equals(texture))
                {
                    text = Textures.getTextureFromItemStack(stack, texture).getID();
                }

                updateTexture(text);
                setInventorySlotContents(0, null);
            }
            else if (stack.itemID == ItemIds.PortalModifierUpgrade + 256 && !upgradeHandler.hasUpgrade(Upgrade.getUpgrade(stack.getItemDamage())))
            {
                if (upgradeHandler.hasUpgrade(new UpgradeDimensional()) && stack.getItemDamage() == 3)
                {
                    return;
                }
                else if (upgradeHandler.hasUpgrade(new UpgradeAdvancedDimensional()) && stack.getItemDamage() == 2)
                {
                    return;
                }

                upgradeHandler.addUpgrade(Upgrade.getUpgrade(stack.getItemDamage()), this);
                setInventorySlotContents(0, null);

                if (!worldObj.isRemote)
                {
                 // TODO  PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 256, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
                }

                return;
            }
        }
    }

    @Override
    public void openChest()
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        texture = tagCompound.getString("Texture");
        thickness = tagCompound.getByte("Thickness");
        redstoneSetting = tagCompound.getByte("RedstoneSetting");
        network = tagCompound.getString("Frequency");
        oldRedstoneState = tagCompound.getBoolean("OldRedstoneState");
        redstoneSetting = tagCompound.getByte("RedstoneSetting");

        for (int i = 0; i < upgradeHandler.getMaximumUpgrades(); i++)
        {
            if (tagCompound.hasKey("Upgrade" + i))
            {
                System.out.println("Found Upgrade" + i + ": " + tagCompound.getByte("Upgrade" + i));
                upgradeHandler.addUpgrade(tagCompound.getByte("Upgrade" + i), this);
            }
        }
    }

    public void removePortal()
    {
        WorldLocation portalLocation = new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata()));
        new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).removePortal();
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
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

    public boolean updateTexture(String text)
    {
        if (text.equals(texture))
        {
            return false;
        }

        texture = text;
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);

        if (!worldObj.isRemote)
        {
         // TODO PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 256, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
        }

        return true;
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
         // TODO PacketDispatcher.sendPacketToServer(new PacketRequestSync(this).getPacket());
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
        tagCompound.setBoolean("OldRedstoneState", oldRedstoneState);
        tagCompound.setByte("RedstoneSetting", redstoneSetting);

        int i = 0;
        for (byte b : upgradeHandler.getInstalledUpgrades())
        {
            System.out.println("Saved Upgrade" + i + ": " + b);
            tagCompound.setByte("Upgrade" + i, b);
            i++;
        }
    }
}
