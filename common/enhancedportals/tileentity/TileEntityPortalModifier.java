package enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.ItemIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Textures;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;
import enhancedportals.network.packet.PacketRequestSync;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.portal.Portal;

public class TileEntityPortalModifier extends TileEntityEnhancedPortals implements IInventory
{
    public String texture;
    public byte thickness, redstoneSetting;
    public String network;
    private boolean particles, sounds, oldRedstoneState;
    public ItemStack[] inventory;
    public boolean[] upgrades;

    public TileEntityPortalModifier()
    {
        texture = "";
        thickness = 0;
        redstoneSetting = 0;
        network = "";
        particles = true;
        sounds = true;
        oldRedstoneState = false;
        inventory = new ItemStack[1];

        upgrades = new boolean[8];

        for (int i = 0; i < upgrades.length; i++)
        {
            upgrades[i] = false;
        }
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
        if (hasUpgrade(5) && hasUpgrade(6))
        {
            return new int[] { Block.blockDiamond.blockID, Block.blockGold.blockID, Block.blockIron.blockID, Block.blockNetherQuartz.blockID, Block.glowStone.blockID, Block.netherBrick.blockID };
        }
        else if (hasUpgrade(5))
        {
            return new int[] { Block.blockNetherQuartz.blockID, Block.glowStone.blockID, Block.netherBrick.blockID };
        }
        else if (hasUpgrade(6))
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

    public int getInstalledUpgradeCount()
    {
        int count = 0;

        for (boolean upgrade : upgrades)
        {
            if (upgrade)
            {
                count++;
            }
        }

        return count;
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

    public int getMaxInstalledUpgrades()
    {
        return 5;
    }

    @Override
    public PacketData getPacketData()
    {
        PacketData data = new PacketData();
        data.integerData = new int[] { sounds ? 1 : 0, particles ? 1 : 0, thickness, redstoneSetting };
        data.stringData = new String[] { texture, network };

        byte[] byteData = new byte[upgrades.length];
        for (int i = 0; i < upgrades.length; i++)
        {
            byteData[i] = (byte) (upgrades[i] ? 1 : 0);
        }
        data.byteData = byteData;

        return data;
    }

    public boolean getParticles()
    {
        if (!hasUpgrade(0))
        {
            particles = true;
        }

        return particles;
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    public boolean getSounds()
    {
        if (!hasUpgrade(1))
        {
            sounds = true;
        }

        return sounds;
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

    public boolean hasUpgrade(int id)
    {
        if (getInstalledUpgradeCount() >= getMaxInstalledUpgrades())
        {
            return true;
        }

        return upgrades.length > id && upgrades[id];
    }

    public boolean hasUpgradeNoCheck(int id)
    {
        return upgrades.length > id && upgrades[id];
    }

    public void installUpgrade(int id)
    {
        if (id < upgrades.length && !hasUpgrade(id))
        {
            upgrades[id] = true;
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
            return portal != null && portal.parentModifier != null && portal.parentModifier.equals(new WorldLocation(xCoord, yCoord, zCoord, worldObj));
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
            else if (stack.itemID == ItemIds.PortalModifierUpgrade + 256 && !hasUpgrade(stack.getItemDamage()))
            {
                if (hasUpgrade(2) && stack.getItemDamage() == 3)
                {
                    return;
                }
                else if (hasUpgrade(3) && stack.getItemDamage() == 2)
                {
                    return;
                }

                installUpgrade(stack.getItemDamage());
                setInventorySlotContents(0, null);

                if (!worldObj.isRemote)
                {
                    PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 256, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
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
    public void parsePacketData(PacketData data)
    {
        if (data == null || data.integerData == null || data.integerData.length != 4 || data.byteData.length != upgrades.length || data.stringData.length != 2)
        {
            System.out.println("Unexpected packet recieved. " + data.integerData.length + " " + data.byteData.length + " " + data.stringData.length);
            return;
        }

        updateTexture(data.stringData[0]);
        updateData(data.integerData[0] == 1, data.integerData[1] == 1, (byte) data.integerData[2]);
        redstoneSetting = (byte) data.integerData[3];
        network = data.stringData[1];

        for (int i = 0; i < upgrades.length; i++)
        {
            upgrades[i] = data.byteData[i] == 1;
        }

        if (worldObj.isRemote)
        {
            worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        texture = tagCompound.getString("Texture");
        thickness = tagCompound.getByte("Thickness");
        redstoneSetting = tagCompound.getByte("RedstoneSetting");
        network = tagCompound.getString("Frequency");
        particles = tagCompound.getBoolean("Particles");
        sounds = tagCompound.getBoolean("Sounds");
        oldRedstoneState = tagCompound.getBoolean("OldRedstoneState");
        redstoneSetting = tagCompound.getByte("RedstoneSetting");

        for (int i = 0; i < upgrades.length; i++)
        {
            upgrades[i] = tagCompound.getBoolean("Upgrade" + i);
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

    public void setParticles(boolean state)
    {
        if (hasUpgrade(0))
        {
            particles = state;
        }
    }

    public void setSounds(boolean state)
    {
        if (hasUpgrade(1))
        {
            sounds = state;
        }
    }

    public boolean updateData(boolean sound, boolean part, byte thick)
    {
        if (sound == sounds && part == particles && thick == thickness)
        {
            return false;
        }

        sounds = sound;
        particles = part;
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
            PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 256, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
        }

        return true;
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToServer(new PacketRequestSync(this).getPacket());
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
        tagCompound.setBoolean("Particles", particles);
        tagCompound.setBoolean("Sounds", sounds);
        tagCompound.setBoolean("OldRedstoneState", oldRedstoneState);
        tagCompound.setByte("RedstoneSetting", redstoneSetting);

        for (int i = 0; i < upgrades.length; i++)
        {
            tagCompound.setBoolean("Upgrade" + i, upgrades[i]);
        }
    }
}