package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class TileScanner extends TileEnhancedPortals implements IInventory
{
    ItemStack[] inventory;
    public boolean isActive, oldActive;

    public TileScanner()
    {
        inventory = new ItemStack[2];
        isActive = oldActive = false;
    }

    @Override
    public boolean activate(EntityPlayer player)
    {
        if (worldObj.isRemote)
        {
            return false;
        }

        ItemStack item = player.inventory.getCurrentItem();

        if (item != null && item.itemID == CommonProxy.itemWrench.itemID)
        {
            if (!checkStructure(true, true))
            {
                if (!checkStructure(false, true))
                {
                    return false;
                }
            }

            if (player.isSneaking())
            {
                performScan();
            }
            else
            {
                CommonProxy.openGui(player, GUIs.Scanner, this);
            }
            
            return true;
        }

        return false;
    }

    public void performScan()
    {
        if (getStackInSlot(0) == null)
        {
            return;
        }
        
        decrStackSize(0, 1);
        ItemStack stack = getStackInSlot(1) == null ? new ItemStack(CommonProxy.itemEntityCard, 1) : getStackInSlot(1);
        NBTTagCompound tag;
        
        if (!stack.hasTagCompound())
        {
            tag = new NBTTagCompound();
        }
        else
        {
            tag = (NBTTagCompound) stack.getTagCompound().copy();
        }
        
        NBTTagList list;
        
        if (tag.hasKey("entities"))
        {
            list = tag.getTagList("entities");
        }
        else
        {
            list = new NBTTagList();
        }
        
        for (Object o : worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xCoord - 2, yCoord + 1, zCoord - 2, xCoord + 2, yCoord + 1.9, zCoord + 2)))
        {
            Entity e = (Entity) o;
            
            NBTTagCompound en = new NBTTagCompound();
            en.setString("Name", e.getEntityName());
            en.setInteger("ID", EntityList.getEntityID(e));
            list.appendTag(en);
        }
                
        tag.setTag("entities", list);
        stack.setTagCompound(tag);
        inventory[1] = stack;
    }
    
    boolean checkStructure(boolean small, boolean updateFrameBlocks)
    {
        ArrayList<ChunkCoordinates> frameBlocks = new ArrayList<ChunkCoordinates>();
        int size = small ? 5 : 7;

        WorldCoordinates northWestCorner = getWorldCoordinates();
        northWestCorner.posX -= small ? 2 : 3;
        northWestCorner.posZ -= small ? 2 : 3;
        northWestCorner.posY++;

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (i == 0 || i == size - 1)
                {
                    // do all
                    if (worldObj.getBlockId(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j) == CommonProxy.blockScanner.blockID && worldObj.getBlockMetadata(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j) == 1)
                    {
                        frameBlocks.add(new ChunkCoordinates(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j));
                    }
                    else
                    {
                        return false;
                    }
                }
                else 
                {
                    // do first one and last one
                    if (j == 0 || j == size -1)
                    {
                        if (worldObj.getBlockId(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j) == CommonProxy.blockScanner.blockID && worldObj.getBlockMetadata(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j) == 1)
                        {
                            frameBlocks.add(new ChunkCoordinates(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j));
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        if (!worldObj.isAirBlock(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j))
                        {
                            return false;
                        }
                    }
                }

            }
        }

        northWestCorner.posX++;
        northWestCorner.posZ++;
        northWestCorner.posY--;

        for (int i = 0; i < size - 2; i++)
        {
            for (int j = 0; j < size - 2; j++)
            {
                if (i == (small ? 1 : 2) && j == (small ? 1 : 2))
                {
                    // skip the controller
                }
                else
                {
                    if (worldObj.getBlockId(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j) != Block.blockIron.blockID)
                    {
                        return false;
                    }
                }
            }
        }

        if (updateFrameBlocks)
        {
            for (ChunkCoordinates c : frameBlocks)
            {
                TileScannerFrame frame = (TileScannerFrame) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
                WorldCoordinates oldScanner = frame.scanner;
                frame.scanner = getWorldCoordinates();

                if (!frame.scanner.equals(oldScanner))
                {
                    CommonProxy.sendUpdatePacketToAllAround(frame);
                }
            }
        }

        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];

            if (stack != null)
            {
                NBTTagCompound t = new NBTTagCompound();
                t.setByte("Slot", (byte) i);
                stack.writeToNBT(t);
                itemList.appendTag(t);
            }
        }

        tag.setTag("Inventory", itemList);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        NBTTagList tagList = tag.getTagList("Inventory");

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound t = (NBTTagCompound) tagList.tagAt(i);
            byte slot = t.getByte("Slot");

            if (slot >= 0 && slot < inventory.length)
            {
                inventory[slot] = ItemStack.loadItemStackFromNBT(t);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(int blockID)
    {
        isActive = getHighestPowerState() > 1;

        if (isActive != oldActive)
        {
            CommonProxy.sendUpdatePacketToAllAround(this);
            oldActive = isActive;
        }
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        stream.writeBoolean(isActive);
    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        isActive = stream.readBoolean();
    }

    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        Random rand = new Random();

        for (int i = 0; i < getSizeInventory(); i++)
        {
            ItemStack item = getStackInSlot(i);

            if (item != null && item.stackSize > 0)
            {
                float randomX = rand.nextFloat() * 0.8F + 0.1F;
                float randomY = rand.nextFloat() * 0.8F + 0.1F;
                float randomZ = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(worldObj, xCoord + randomX, yCoord + randomY, zCoord + randomZ, new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

                if (item.hasTagCompound())
                {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                worldObj.spawnEntityInWorld(entityItem);
                item.stackSize = 0;
            }
        }
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
        return "tile.ep3.scanner.controller.name";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void openChest()
    {

    }

    @Override
    public void closeChest()
    {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return itemstack == null || (i == 0 && itemstack.itemID == CommonProxy.itemEntityCard.itemID);
    }
}
