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
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.util.EntityData;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TileBiometricIdentifier extends TilePortalFrame
{
    public long lastUpdateTime;
    public ArrayList<EntityData> entityList;
    public boolean defaultPermissions, isActive;
    ItemStack[] inventory;

    public TileBiometricIdentifier()
    {
        inventory = new ItemStack[2];
        entityList = new ArrayList<EntityData>();
        defaultPermissions = isActive = true;
    }

    @Override
    public boolean activate(EntityPlayer player)
    {
        ItemStack item = player.inventory.getCurrentItem();

        if (item != null)
        {
            if (ItemHelper.isWrench(item) && !player.isSneaking())
            {
                CommonProxy.openGui(player, GUIs.BiometricIdentifier, this);
                return true;
            }
            else if (item != null && item.itemID == CommonProxy.itemPaintbrush.itemID)
            {
                TilePortalController controller = getPortalController();

                if (controller != null && controller.isFullyInitialized())
                {
                    CommonProxy.openGui(player, GUIs.TexturesFrame, controller);
                    return true;
                }
            }
        }

        return false;
    }

    public void applyBiometricFilters(int slotIndex, ItemStack s)
    {
        NBTTagCompound t = s.getTagCompound();
        NBTTagList l = t.getTagList("entities");

        for (int i = 0; i < l.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) l.tagAt(i);
            EntityData entity = new EntityData(tag.getString("Name"), EntityData.getClassFromID(tag.getInteger("ID")), false, (byte) 0);
            entityList.add(entity);
            lastUpdateTime = System.currentTimeMillis();
        }

        CommonProxy.sendUpdatePacketToAllAround(this);
    }

    public boolean canEntityTravel(Entity entity)
    {
        if (!isActive)
        {
            return true;
        }
        
        boolean skippedAll = true, decision = defaultPermissions;
        
        for (EntityData data : entityList)
        {
            int var = data.isEntityAcceptable(entity);
            
            if (var != -1)
            {
                skippedAll = false;
                decision = var == 1;
            }
        }
        
        return skippedAll ? defaultPermissions : decision;
    }

    public ArrayList<EntityData> copySendingEntityTypes()
    {
        ArrayList<EntityData> list = new ArrayList<EntityData>();

        for (EntityData data : entityList)
        {
            list.add(new EntityData(data.EntityDisplayName, data.EntityClass, data.disallow, data.checkType));
        }

        return list;
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
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);

        stream.writeBoolean(isActive);
        stream.writeBoolean(defaultPermissions);
        stream.writeByte(entityList.size());

        for (EntityData d : entityList)
        {
            stream.writeUTF(d.EntityDisplayName);
            stream.writeInt(EntityData.getEntityID(d.EntityClass));
            stream.writeBoolean(d.disallow);
            stream.writeByte(d.checkType);
        }
    }

    @Override
    public Icon getBlockTexture(int side, int pass)
    {
        if (pass == 0)
        {
            return super.getBlockTexture(side, pass);
        }

        return CommonProxy.forceShowFrameOverlays || GeneralUtils.isWearingGoggles() ? BlockFrame.overlayIcons[5] : BlockFrame.overlayIcons[0];
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public String getInvName()
    {
        return "tile.ep3.frame.scanner.name";
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
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return inventory[i];
    }

    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        super.guiActionPerformed(payload, player);

        if (payload.data.hasKey("invert"))
        {
            int id = payload.data.getInteger("invert");

            entityList.get(id).disallow = !entityList.get(id).disallow;
            lastUpdateTime = System.currentTimeMillis();
        }
        else if (payload.data.hasKey("type"))
        {
            int id = payload.data.getInteger("type");

            entityList.get(id).checkType++;

            if (entityList.get(id).checkType > 2)
            {
                entityList.get(id).checkType = 0;
                lastUpdateTime = System.currentTimeMillis();
            }
        }
        else if (payload.data.hasKey("remove"))
        {
            int id = payload.data.getInteger("remove");

            entityList.remove(id);
            lastUpdateTime = System.currentTimeMillis();
        }
        else if (payload.data.hasKey("default"))
        {
            defaultPermissions = !defaultPermissions;
        }

        CommonProxy.sendUpdatePacketToAllAround(this);
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return GeneralUtils.isEnergyContainerItem(itemstack);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void onNeighborBlockChange(int blockID)
    {
        isActive = getHighestPowerState() == 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        isActive = tag.getBoolean("isActive");
        defaultPermissions = tag.getBoolean("notFoundSend");

        NBTTagList l = tag.getTagList("sendingEntityTypes");
        for (int i = 0; i < l.tagCount(); i++)
        {
            EntityData d = new EntityData().readFromNBT((NBTTagCompound) l.tagAt(i));

            if (d != null && d.EntityClass != null)
            {
                entityList.add(d);
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
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);

        isActive = stream.readBoolean();
        defaultPermissions = stream.readBoolean();
        entityList.clear();
        lastUpdateTime = System.currentTimeMillis();

        byte size = stream.readByte();

        for (int i = 0; i < size; i++)
        {
            entityList.add(new EntityData(stream.readUTF(), EntityData.getClassFromID(stream.readInt()), stream.readBoolean(), stream.readByte()));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setBoolean("isActive", isActive);
        tag.setBoolean("notFoundSend", defaultPermissions);

        NBTTagList t = new NBTTagList();
        for (EntityData d : entityList)
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            d.saveToNBT(tagCompound);
            t.appendTag(tagCompound);
        }

        tag.setTag("sendingEntityTypes", t);

        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];

            if (stack != null)
            {
                NBTTagCompound t3 = new NBTTagCompound();
                t3.setByte("Slot", (byte) i);
                stack.writeToNBT(t3);
                itemList.appendTag(t3);
            }
        }

        tag.setTag("Inventory", itemList);
    }
}
