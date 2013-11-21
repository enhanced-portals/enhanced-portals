package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.util.EntityData;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import cofh.util.EnergyHelper;

public class TileBiometricIdentifier extends TilePortalPart
{
    public ArrayList<EntityData> sendingEntityTypes, recievingEntityTypes;
    public boolean notFoundSend, notFoundRecieve, isActive, hasSeperateLists;
    ItemStack[] inventory;

    public TileBiometricIdentifier()
    {
        inventory = new ItemStack[2];
        sendingEntityTypes = new ArrayList<EntityData>();
        recievingEntityTypes = new ArrayList<EntityData>();
        hasSeperateLists = false;
        notFoundSend = notFoundRecieve = true;
        isActive = true;
    }

    public boolean canEntityBeSent(Entity entity)
    {
        if (!isActive)
        {
            return true;
        }

        boolean wasFound = false;

        for (EntityData data : sendingEntityTypes)
        {
            if (data.shouldCheckName() && data.EntityDisplayName.equals(entity.getEntityName()))
            {
                if (!data.isInverted)
                {
                    return true;
                }

                wasFound = true;
            }
            else if (data.shouldCheckClass() && data.EntityClass.isInstance(entity))
            {
                if (!data.isInverted)
                {
                    return true;
                }

                wasFound = true;
            }
            else if (data.shouldCheckNameAndClass() && data.EntityDisplayName.equals(entity.getEntityName()) && data.EntityClass.isInstance(entity))
            {
                if (!data.isInverted)
                {
                    return true;
                }

                wasFound = true;
            }
        }

        return !wasFound ? notFoundSend : false;
    }

    public boolean canEntityBeRecieved(Entity entity)
    {
        if (!isActive)
        {
            return true;
        }

        boolean wasFound = false;

        for (EntityData data : hasSeperateLists ? recievingEntityTypes : sendingEntityTypes)
        {
            if (data.shouldCheckName() && data.EntityDisplayName.equals(entity.getEntityName()))
            {
                if (!data.isInverted)
                {
                    return true;
                }

                wasFound = true;
            }
            else if (data.shouldCheckClass() && data.EntityClass.isInstance(entity))
            {
                if (!data.isInverted)
                {
                    return true;
                }

                wasFound = true;
            }
            else if (data.shouldCheckNameAndClass() && data.EntityDisplayName.equals(entity.getEntityName()) && data.EntityClass.isInstance(entity))
            {
                if (!data.isInverted)
                {
                    return true;
                }

                wasFound = true;
            }
        }

        return !wasFound ? hasSeperateLists ? notFoundRecieve : notFoundSend : false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setBoolean("isActive", isActive);
        tag.setBoolean("hasSeperateLists", hasSeperateLists);
        tag.setBoolean("notFoundSend", notFoundSend);
        tag.setBoolean("notFoundRecieve", notFoundRecieve);

        NBTTagList t = new NBTTagList();
        for (EntityData d : sendingEntityTypes)
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            d.saveToNBT(tagCompound);
            t.appendTag(tagCompound);
        }

        tag.setTag("sendingEntityTypes", t);

        if (hasSeperateLists)
        {
            NBTTagList t2 = new NBTTagList();
            for (EntityData d : recievingEntityTypes)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                d.saveToNBT(tagCompound);
                t.appendTag(tagCompound);
            }

            tag.setTag("recievingEntityTypes", t2);
        }

        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];

            if (stack != null)
            {
                NBTTagCompound t2 = new NBTTagCompound();
                t2.setByte("Slot", (byte) i);
                stack.writeToNBT(t2);
                itemList.appendTag(t2);
            }
        }

        tag.setTag("Inventory", itemList);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        isActive = tag.getBoolean("isActive");
        hasSeperateLists = tag.getBoolean("hasSeperateLists");
        notFoundSend = tag.getBoolean("notFoundSend");
        notFoundRecieve = tag.getBoolean("notFoundRecieve");

        NBTTagList l = tag.getTagList("sendingEntityTypes");
        for (int i = 0; i < l.tagCount(); i++)
        {
            EntityData d = new EntityData().readFromNBT((NBTTagCompound) l.tagAt(i));

            if (d != null && d.EntityClass != null)
            {
                sendingEntityTypes.add(d);
            }
        }

        if (tag.hasKey("recievingEntityTypes"))
        {
            NBTTagList l2 = tag.getTagList("recievingEntityTypes");

            for (int i = 0; i < l2.tagCount(); i++)
            {
                EntityData d = new EntityData().readFromNBT((NBTTagCompound) l2.tagAt(i));

                if (d != null && d.EntityClass != null)
                {
                    recievingEntityTypes.add(d);
                }
            }
        }

        if (tag.hasKey("Inventory"))
        {
            NBTTagList tagList = tag.getTagList("Inventory");

            for (int i = 0; i < tagList.tagCount(); i++)
            {
                NBTTagCompound t2 = (NBTTagCompound) tagList.tagAt(i);
                byte slot = t2.getByte("Slot");

                if (slot >= 0 && slot < inventory.length)
                {
                    inventory[slot] = ItemStack.loadItemStackFromNBT(t2);
                }
            }
        }
    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);

        isActive = stream.readBoolean();
        hasSeperateLists = stream.readBoolean();
        notFoundSend = stream.readBoolean();
        notFoundRecieve = stream.readBoolean();
        sendingEntityTypes.clear();
        recievingEntityTypes.clear();

        byte size = stream.readByte();

        for (int i = 0; i < size; i++)
        {
            sendingEntityTypes.add(new EntityData(stream.readUTF(), EntityData.getClassFromID(stream.readInt()), stream.readBoolean(), stream.readByte()));
        }

        if (hasSeperateLists)
        {
            size = stream.readByte();

            for (int i = 0; i < size; i++)
            {
                recievingEntityTypes.add(new EntityData(stream.readUTF(), EntityData.getClassFromID(stream.readInt()), stream.readBoolean(), stream.readByte()));
            }
        }
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);

        stream.writeBoolean(isActive);
        stream.writeBoolean(hasSeperateLists);
        stream.writeBoolean(notFoundSend);
        stream.writeBoolean(notFoundRecieve);
        stream.writeByte(sendingEntityTypes.size());

        for (EntityData d : sendingEntityTypes)
        {
            stream.writeUTF(d.EntityDisplayName);
            stream.writeInt(EntityData.getEntityID(d.EntityClass));
            stream.writeBoolean(d.isInverted);
            stream.writeByte(d.checkType);
        }

        if (hasSeperateLists)
        {
            stream.writeByte(recievingEntityTypes.size());

            for (EntityData d : recievingEntityTypes)
            {
                stream.writeUTF(d.EntityDisplayName);
                stream.writeInt(EntityData.getEntityID(d.EntityClass));
                stream.writeBoolean(d.isInverted);
                stream.writeByte(d.checkType);
            }
        }
    }

    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        super.guiActionPerformed(payload, player);

        if (payload.data.hasKey("list"))
        {
            boolean list = payload.data.getBoolean("list");

            if (payload.data.hasKey("invert"))
            {
                int id = payload.data.getInteger("invert");

                (list ? sendingEntityTypes : recievingEntityTypes).get(id).isInverted = !(list ? sendingEntityTypes : recievingEntityTypes).get(id).isInverted;
            }
            else if (payload.data.hasKey("type"))
            {
                int id = payload.data.getInteger("type");

                (list ? sendingEntityTypes : recievingEntityTypes).get(id).checkType++;

                if ((list ? sendingEntityTypes : recievingEntityTypes).get(id).checkType > 2)
                {
                    (list ? sendingEntityTypes : recievingEntityTypes).get(id).checkType = 0;
                }
            }
            else if (payload.data.hasKey("remove"))
            {
                int id = payload.data.getInteger("remove");

                (list ? sendingEntityTypes : recievingEntityTypes).remove(id);
            }
            else if (payload.data.hasKey("default"))
            {
                if (list)
                {
                    notFoundSend = !notFoundSend;
                }
                else
                {
                    notFoundRecieve = !notFoundRecieve;
                }
            }

            CommonProxy.sendUpdatePacketToAllAround(this);
        }
        else if (payload.data.hasKey("toggleSeperateLists"))
        {
            hasSeperateLists = !hasSeperateLists;
            CommonProxy.sendUpdatePacketToAllAround(this);
        }
    }

    @Override
    public void onNeighborBlockChange(int blockID)
    {
        isActive = getHighestPowerState() == 0;
    }

    public ArrayList<EntityData> copySendingEntityTypes()
    {
        ArrayList<EntityData> list = new ArrayList<EntityData>();

        for (EntityData data : sendingEntityTypes)
        {
            list.add(new EntityData(data.EntityDisplayName, data.EntityClass, data.isInverted, data.checkType));
        }

        return list;
    }

    public ArrayList<EntityData> copyRecievingEntityTypes()
    {
        ArrayList<EntityData> list = new ArrayList<EntityData>();

        for (EntityData data : recievingEntityTypes)
        {
            list.add(new EntityData(data.EntityDisplayName, data.EntityClass, data.isInverted, data.checkType));
        }

        return list;
    }

    /* IInventory */
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
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack stack = getStackInSlot(i);

        if (stack != null)
        {
            if (stack.stackSize <= j)
            {
                setInventorySlotContents(i, null);
            }
            else
            {
                stack = stack.splitStack(j);

                if (stack.stackSize == 0)
                {
                    setInventorySlotContents(i, null);
                }
            }
        }

        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return inventory[i];
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
    }

    @Override
    public String getInvName()
    {
        return "tile.ep3.frame.scanner.name";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return EnergyHelper.isEnergyContainerItem(itemstack);
    }

    public void applyBiometricFilters(int slotIndex, ItemStack s)
    {
        NBTTagCompound t = s.getTagCompound();
        NBTTagList l = t.getTagList("entities");

        for (int i = 0; i < l.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) l.tagAt(i);
            EntityData entity = new EntityData(tag.getString("Name"), EntityData.getClassFromID(tag.getInteger("ID")), false, (byte) 0);

            if (slotIndex == 0)
            {
                sendingEntityTypes.add(entity);
            }
            else if (slotIndex == 1)
            {
                recievingEntityTypes.add(entity);
            }
        }

        CommonProxy.sendUpdatePacketToAllAround(this);
    }
}
