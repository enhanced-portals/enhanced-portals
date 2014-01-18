package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;

public class TilePortalPart extends TileEnhancedPortals implements IInventory
{
    public ChunkCoordinates portalController;

    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        if (!worldObj.isRemote)
        {
            if (oldBlockID == worldObj.getBlockId(xCoord, yCoord, zCoord))
            {
                return;
            }

            TilePortalController controller = getPortalController();

            if (controller != null)
            {
                controller.partBroken(false);
            }
        }
    }

    @Override
    public void closeChest()
    {

    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        return null;
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);
        GeneralUtils.writeChunkCoord(stream, portalController);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 0;
    }

    @Override
    public String getInvName()
    {
        return null;
    }

    public TilePortalController getPortalController()
    {
        if (portalController != null)
        {
            TileEntity tile = worldObj.getBlockTileEntity(portalController.posX, portalController.posY, portalController.posZ);

            if (tile instanceof TilePortalController)
            {
                return (TilePortalController) tile;
            }

            portalController = null;
        }

        return null;
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
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return null;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void onBlockPlacedBy(EntityLivingBase entity, ItemStack stack)
    {
        if (!worldObj.isRemote)
        {
            for (int i = 0; i < 6; i++)
            {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                TileEntity tile = worldObj.getBlockTileEntity(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);

                if (tile != null && tile instanceof TilePortalPart)
                {
                    ((TilePortalPart) tile).breakBlock(0, 0);
                }
            }
        }
    }

    @Override
    public void openChest()
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        portalController = GeneralUtils.loadChunkCoord(tag, "Controller");
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {

    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);
        portalController = GeneralUtils.readChunkCoord(stream);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        GeneralUtils.saveChunkCoord(tag, portalController, "Controller");
    }
}
