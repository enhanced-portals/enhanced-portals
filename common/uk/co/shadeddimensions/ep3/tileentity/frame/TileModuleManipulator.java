package uk.co.shadeddimensions.ep3.tileentity.frame;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import uk.co.shadeddimensions.ep3.api.IPortalModule;
import uk.co.shadeddimensions.ep3.client.particle.PortalFX;
import uk.co.shadeddimensions.ep3.portal.StackHelper;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;

public class TileModuleManipulator extends TilePortalFrame
{
    ItemStack[] inventory;

    public TileModuleManipulator()
    {
        inventory = new ItemStack[9];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack s = getStackInSlot(i);
        s.stackSize -= j;

        return s;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public String getInvName()
    {
        return "tile.ep3.portalFrame.upgrade.name";
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

    public boolean hasModule(String ID)
    {
        for (ItemStack i : inventory)
        {
            if (i != null && ((IPortalModule) i.getItem()).getID(i).equals(ID))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack)
    {
        return StackHelper.isUpgrade(stack) && !hasModule(((IPortalModule) stack.getItem()).getID(stack));
    }

    public void particleCreated(PortalFX portalFX)
    {
        for (ItemStack i : inventory)
        {
            if (i != null)
            {
                ((IPortalModule) i.getItem()).onParticleCreated(this, i, portalFX);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        NBTTagList list = tagCompound.getTagList("Inventory");

        for (int i = 0; i < list.tagList.size(); i++)
        {
            inventory[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) list.tagList.get(i));
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
    }

    public boolean shouldRenderPortal()
    {
        for (ItemStack i : inventory)
        {
            if (i != null)
            {
                if (((IPortalModule) i.getItem()).disablePortalRendering(this, i))
                {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        NBTTagList list = new NBTTagList();

        for (ItemStack s : inventory)
        {
            if (s != null)
            {
                NBTTagCompound compound = new NBTTagCompound();
                s.writeToNBT(compound);
                list.appendTag(compound);
            }
        }

        tagCompound.setTag("Inventory", list);
    }
}
