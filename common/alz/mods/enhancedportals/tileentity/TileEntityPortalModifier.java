package alz.mods.enhancedportals.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import alz.mods.enhancedportals.client.ClientProxy;
import alz.mods.enhancedportals.helpers.EntityHelper;
import alz.mods.enhancedportals.helpers.PortalHelper;
import alz.mods.enhancedportals.helpers.WorldHelper;
import alz.mods.enhancedportals.networking.ITileEntityNetworking;
import alz.mods.enhancedportals.networking.PacketTileUpdate;
import alz.mods.enhancedportals.portals.PortalData;
import alz.mods.enhancedportals.portals.PortalTexture;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.teleportation.TeleportData;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileEntityPortalModifier extends TileEntity implements IInventory, ITileEntityNetworking, IPeripheral
{
    //private int Colour, Frequency;
    public boolean HadPower;
    private ItemStack[] inv;
    private List<IComputerAccess> computerAccess;
    public PortalData PortalData;

    public TileEntityPortalModifier()
    {
        inv = new ItemStack[3];
        computerAccess = new ArrayList<IComputerAccess>();
        PortalData = new PortalData();
    }

    @Override
    public void attach(IComputerAccess computer)
    {
        computerAccess.add(computer);
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception
    {
        if (method == 0)
        {
            return new Object[] { PortalData.Frequency };
        }
        else if (method == 1)
        {
            if (arguments == null || arguments.length != 1)
            {
                return new Object[] { "Usage: setFrequency(<frequency>)" };
            }

            try
            {
                PortalData.Frequency = (int) Double.parseDouble(arguments[0].toString());

                if (Reference.ServerHandler != null)
                {
                    Reference.ServerHandler.ModifierNetwork.addToNetwork("" + PortalData.Frequency, new TeleportData(xCoord, yCoord, zCoord, worldObj.provider.dimensionId));
                }

                return new Object[] { true };
            }
            catch (NumberFormatException e)
            {
                return new Object[] { false };
            }
        }
        else if (method == 2)
        {
            return new Object[] { PortalData.Texture.GetSwapped() };
        }
        else if (method == 3)
        {
            if (arguments == null || arguments.length != 1)
            {
                return new Object[] { "Usage:", "setColour(<0 - 15>)", "setColour(<colour name>)", "Valid colour names:", "black, red, green, brown, blue, purple, cyan, silver, gray, pink, lime, yellow, lightBlue, magenta, orange, white" };
            }

            try
            {
                int col = (int) Double.parseDouble(arguments[0].toString());

                if (col < 0)
                {
                    col = 0;
                }

                if (col > 15)
                {
                    col = 15;
                }

                setColour(col, true);

                if (Reference.ServerHandler != null)
                {
                    Reference.ServerHandler.sendUpdatePacketToNearbyClients(this);
                }

                int[] offset = WorldHelper.offsetDirectionBased(worldObj, xCoord, yCoord, zCoord);
                WorldHelper.floodUpdateMetadata(worldObj, offset[0], offset[1], offset[2], 90, col);

                return new Object[] { true };
            }
            catch (NumberFormatException e)
            {
                int count = 0;

                for (String str : ItemDye.dyeColorNames)
                {
                    if (arguments[0].toString().toLowerCase().equals(str.toLowerCase()))
                    {
                        setColour(count, true);

                        if (Reference.ServerHandler != null)
                        {
                            Reference.ServerHandler.sendUpdatePacketToNearbyClients(this);
                        }

                        int[] offset = WorldHelper.offsetDirectionBased(worldObj, xCoord, yCoord, zCoord);
                        WorldHelper.floodUpdateMetadata(worldObj, offset[0], offset[1], offset[2], 90, PortalData.Texture.Get());

                        return new Object[] { true };
                    }

                    count++;
                }

                return new Object[] { false };
            }
        }
        else if (method == 4 || method == 5 || method == 6)
        {
            return computerCreatePortal();
        }
        else if (method == 7 || method == 8 || method == 9)
        {
            return computerRemovePortal();
        }

        return null;
    }

    @Override
    public boolean canAttachToSide(int side)
    {
        return hasUpgrade(4);
    }

    @Override
    public void closeChest()
    {
    }

    private Object[] computerCreatePortal()
    {
        ForgeDirection direction = WorldHelper.getBlockDirection(worldObj, xCoord, yCoord, zCoord);
        int[] blockToTest = WorldHelper.offsetDirectionBased(worldObj, xCoord, yCoord, zCoord, direction);
        int blockID = worldObj.getBlockId(blockToTest[0], blockToTest[1], blockToTest[2]);
        boolean createdPortal = false;

        if (hasUpgrade(0))
        {
            createdPortal = PortalHelper.createPortalAround(worldObj, xCoord, yCoord, zCoord, PortalData.Texture.Get());
        }
        else if (WorldHelper.isBlockPortalRemovable(blockID))
        {
            createdPortal = PortalHelper.createPortal(worldObj, blockToTest[0], blockToTest[1], blockToTest[2], PortalData.Texture.Get());
        }

        setState(createdPortal);

        return new Object[] { createdPortal };
    }

    private Object[] computerRemovePortal()
    {
        ForgeDirection direction = WorldHelper.getBlockDirection(worldObj, xCoord, yCoord, zCoord);
        int[] blockToTest = WorldHelper.offsetDirectionBased(worldObj, xCoord, yCoord, zCoord, direction);
        int blockID = worldObj.getBlockId(blockToTest[0], blockToTest[1], blockToTest[2]), meta = worldObj.getBlockMetadata(blockToTest[0], blockToTest[1], blockToTest[2]);

        if (hasUpgrade(0))
        {
            PortalHelper.removePortalAround(worldObj, xCoord, yCoord, zCoord);
        }
        else if (blockID == Reference.BlockIDs.NetherPortal && meta == PortalData.Texture.Get())
        {
            PortalHelper.removePortal(worldObj, blockToTest[0], blockToTest[1], blockToTest[2]);
        }

        return new Object[] { false };
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack stack = getStackInSlot(slot);

        if (stack != null)
        {
            if (stack.stackSize <= amount)
            {
                setInventorySlotContents(slot, null);
            }
            else
            {
                stack = stack.splitStack(amount);

                if (stack.stackSize == 0)
                {
                    setInventorySlotContents(slot, null);
                }
            }
        }

        return stack;
    }

    @Override
    public void detach(IComputerAccess computer)
    {
        computerAccess.remove(computer);
    }

    public void dropItemStack(ItemStack stack)
    {
        EntityItem item = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, stack);
        worldObj.spawnEntityInWorld(item);
        stack.stackSize = 0;
    }

    public int getColour()
    {
        return PortalData.Texture.Get();
    }

    public int getFrequency()
    {
        return PortalData.Frequency;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public String getInvName()
    {
        return "alz.ep.modifier";
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "getFrequency", "setFrequency", "getColour", "setColour", "turnOn", "createPortal", "create", "turnOff", "removePortal", "remove" };
    }

    @Override
    public int getSizeInventory()
    {
        return inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inv[slot];
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack stack = getStackInSlot(slot);

        if (stack != null)
        {
            setInventorySlotContents(slot, null);
        }

        return stack;
    }

    @Override
    public String getType()
    {
        return "Portal Modifier";
    }

    @Override
    public PacketTileUpdate getUpdatePacket()
    {
        return new PacketTileUpdate(xCoord, yCoord, zCoord, worldObj.provider.dimensionId, new int[] { PortalData.Frequency, PortalData.Texture.Get() });
    }

    public boolean hasFreeSpace()
    {
        if (inv[0] != null && inv[1] != null && inv[2] != null)
        {
            return false;
        }

        return true;
    }

    public boolean hasUpgrade(int id)
    {
        for (ItemStack stack : inv)
        {
            if (stack != null && stack.getItemDamage() == id)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack)
    {
        return EntityHelper.canAcceptItemStack(this, itemstack);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void parseUpdatePacket(PacketTileUpdate packet)
    {
        if (packet.data == null)
        {
            return;
        }

        PortalData.Frequency = packet.data[0];
        PortalData.Texture = PortalTexture.getPortalTexture(packet.data[1]);

        if (worldObj.isRemote)
        {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        PortalData.Texture = PortalTexture.getPortalTexture(tag.getInteger("colour"));
        PortalData.Frequency = tag.getInteger("freq");
        HadPower = tag.getBoolean("power");

        NBTTagList tagList = tag.getTagList("inventory");

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound theTag = (NBTTagCompound) tagList.tagAt(i);
            byte slot = theTag.getByte("slot");

            if (slot >= 0 && slot < inv.length)
            {
                inv[slot] = ItemStack.loadItemStackFromNBT(theTag);
            }
        }
    }

    public void setColour(int colour)
    {
        setColour(colour, false);
    }

    /* COMPUTERCRAFT */

    public void setColour(int colour, boolean swapBlackPurple)
    {
        PortalData.Texture = PortalTexture.getPortalTexture(colour);

        for (IComputerAccess computer : computerAccess)
        {
            computer.queueEvent("enhancedPortals_colourChanged", new Object[] { colour });
        }
    }

    public void setFrequency(int frequency)
    {
        PortalData.Frequency = frequency;

        for (IComputerAccess computer : computerAccess)
        {
            computer.queueEvent("enhancedPortals_frequencyChanged", new Object[] { frequency });
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inv[slot] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    public void setPortalData(PortalData data)
    {
        PortalData = data;
    }

    public void setState(boolean state)
    {
        for (IComputerAccess computer : computerAccess)
        {
            computer.queueEvent("enhancedPortals_portalChanged", new Object[] { state });
        }
    }

    public void setTexture(PortalTexture texture)
    {
        PortalData.Texture = texture;
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            ClientProxy.RequestTileData(this);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setInteger("colour", PortalData.Texture.Get());
        tag.setInteger("freq", PortalData.Frequency);
        tag.setBoolean("power", HadPower);

        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < inv.length; i++)
        {
            ItemStack stack = inv[i];

            if (stack == null)
            {
                continue;
            }

            NBTTagCompound theTag = new NBTTagCompound();
            theTag.setByte("slot", (byte) i);
            stack.writeToNBT(theTag);
            tagList.appendTag(theTag);
        }

        tag.setTag("inventory", tagList);
    }
}
