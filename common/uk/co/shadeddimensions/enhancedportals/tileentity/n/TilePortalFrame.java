package uk.co.shadeddimensions.enhancedportals.tileentity.n;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import uk.co.shadeddimensions.enhancedportals.network.ISynchronizedTile;
import uk.co.shadeddimensions.enhancedportals.tileentity.TileEP;

public class TilePortalFrame extends TileEP implements IInventory, ISynchronizedTile
{
    ChunkCoordinates portalController;
    
    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        
        if (portalController != null)
        {
            tagCompound.setInteger("ControllerX", portalController.posX);
            tagCompound.setInteger("ControllerY", portalController.posZ);
            tagCompound.setInteger("ControllerZ", portalController.posY);
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        
        if (tagCompound.hasKey("ControllerX"))
        {
            portalController = new ChunkCoordinates(tagCompound.getInteger("ControllerX"), tagCompound.getInteger("ControllerY"), tagCompound.getInteger("ControllerZ"));
        }
    }
    
    /* Networking */
    @Override
    public void setPacketData()
    {
        
    }

    @Override
    public void executePacket()
    {
        
    }
    
    /* Generic IInventory Base */    
    @Override
    public int getSizeInventory()
    {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    { }

    @Override
    public String getInvName()
    {
        return null;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return false;
    }

    @Override
    public void openChest()
    { }

    @Override
    public void closeChest()
    { }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }
}
