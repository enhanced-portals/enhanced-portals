package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class TilePortalPart extends TileEnhancedPortals implements IInventory
{
    public WorldCoordinates portalController;
    
    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        
        if (portalController != null)
        {
            NBTTagCompound t = new NBTTagCompound();
            t.setInteger("X", portalController.posX);
            t.setInteger("Y", portalController.posY);
            t.setInteger("Z", portalController.posZ);
            t.setInteger("D", portalController.dimension);
            
            tag.setTag("portalController", t);
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        
        if (tag.hasKey("portalController"))
        {
            NBTTagCompound t = (NBTTagCompound) tag.getTag("portalController");
            
            portalController = new WorldCoordinates(t.getInteger("X"), t.getInteger("Y"), t.getInteger("Z"), t.getInteger("D"));
        }
    }
    
    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);
        
        if (portalController != null)
        {
            stream.writeInt(portalController.posX);
            stream.writeInt(portalController.posY);
            stream.writeInt(portalController.posZ);
        }
        else
        {
            stream.writeInt(0);
            stream.writeInt(-1);
            stream.writeInt(0);
        }
    }
    
    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);
        
        WorldCoordinates c = new WorldCoordinates(stream.readInt(), stream.readInt(), stream.readInt(), worldObj.provider.dimensionId);
        
        if (c.posY > -1)
        {
            portalController = c;
        }
        else
        {
            portalController = null;
        }
    }
        
    public TilePortalController getPortalController()
    {
        return portalController != null ? (TilePortalController) portalController.getBlockTileEntity() : null;
    }

    /* IInventory */
    
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
    {
        
    }

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
        return false;
    }
}
