package enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Portal;
import enhancedportals.lib.PortalTexture;
import enhancedportals.lib.Settings;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;
import enhancedportals.network.packet.PacketRequestSync;
import enhancedportals.network.packet.PacketTEUpdate;

public class TileEntityPortalModifier extends TileEntityEnhancedPortals implements IInventory
{
    public PortalTexture texture;
    public byte thickness, redstoneSetting;
    public String network;
    public boolean particles, sounds, oldRedstoneState;
    public ItemStack[] inventory;

    public TileEntityPortalModifier()
    {
        texture = new PortalTexture(0);
        thickness = 0;
        redstoneSetting = 0;
        network = "0";
        particles = true;
        sounds = true;
        oldRedstoneState = false;
        inventory = new ItemStack[1];
    }

    @Override
    public void closeChest()
    {

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
    public PacketData getPacketData()
    {
        PacketData data = new PacketData();
        data.integerData = new int[] { texture.colour == null ? -1 : texture.colour.ordinal(), texture.blockID, texture.metaData, sounds ? 1 : 0, particles ? 1 : 0, thickness, redstoneSetting };
        data.stringData = new String[] { network };

        return data;
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
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).createPortal();
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
                new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).createPortal();
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
            PortalTexture text = null;

            if (stack.itemID == Item.dyePowder.itemID)
            {
                text = new PortalTexture(stack.getItemDamage());
            }
            else if (Settings.isValidItem(stack.itemID))
            {
                text = Settings.getPortalTextureFromItem(stack, texture);
            }
            else if (stack.getItemName().startsWith("tile.") && !Settings.isBlockExcluded(stack.itemID))
            {
                text = new PortalTexture(stack.itemID, stack.getItemDamage());
            }

            if (text != null)
            {
                updateTexture(text);
                setInventorySlotContents(0, null);
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
        if (data == null || data.integerData == null || data.integerData.length != 7 || data.stringData.length != 1)
        {
            System.out.println("Unexpected packet recieved. " + data);
            return;
        }

        PortalTexture newTexture;
        boolean sound, particles;
        byte portalThickness;

        if (data.integerData[0] != -1)
        {
            newTexture = new PortalTexture(data.integerData[0]);
        }
        else
        {
            newTexture = new PortalTexture(data.integerData[1], data.integerData[2]);
        }

        sound = data.integerData[3] == 1;
        particles = data.integerData[4] == 1;
        portalThickness = (byte) data.integerData[5];
        updateTexture(newTexture);
        updateData(sound, particles, portalThickness);
        redstoneSetting = (byte) data.integerData[6];
        network = data.stringData[0];
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        int colour = tagCompound.getInteger("Colour"), blockID = tagCompound.getInteger("BlockID"), metadata = tagCompound.getInteger("Metadata");

        if (colour != -1)
        {
            texture = new PortalTexture(colour);
        }
        else
        {
            texture = new PortalTexture(blockID, metadata);
        }

        thickness = tagCompound.getByte("Thickness");
        redstoneSetting = tagCompound.getByte("RedstoneSetting");
        network = tagCompound.getString("Frequency");
        particles = tagCompound.getBoolean("Particles");
        sounds = tagCompound.getBoolean("Sounds");
        oldRedstoneState = tagCompound.getBoolean("OldRedstoneState");
        redstoneSetting = tagCompound.getByte("RedstoneSetting");
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
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

    public boolean updateTexture(PortalTexture text)
    {
        if (text.isEqualTo(texture))
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

        tagCompound.setInteger("Colour", texture.colour == null ? -1 : texture.colour.ordinal());
        tagCompound.setInteger("BlockID", texture.blockID);
        tagCompound.setInteger("Metadata", texture.metaData);
        tagCompound.setByte("Thickness", thickness);
        tagCompound.setByte("RedstoneSetting", redstoneSetting);
        tagCompound.setString("Frequency", network);
        tagCompound.setBoolean("Particles", particles);
        tagCompound.setBoolean("Sounds", sounds);
        tagCompound.setBoolean("OldRedstoneState", oldRedstoneState);
        tagCompound.setByte("RedstoneSetting", redstoneSetting);
    }
}
