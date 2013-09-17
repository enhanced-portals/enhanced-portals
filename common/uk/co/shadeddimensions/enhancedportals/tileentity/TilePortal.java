package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.lib.GuiIds;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketRequestData;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TilePortal extends TileEP implements IInventory
{
    public ChunkCoordinates controller;

    public TilePortal()
    {
        controller = new ChunkCoordinates(0, -1, 0);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("ControllerX", controller.posX);
        tagCompound.setInteger("ControllerY", controller.posY);
        tagCompound.setInteger("ControllerZ", controller.posZ);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        if (tagCompound.hasKey("ControllerX")) // Otherwise, leave it as 0, -1, 0
        {
            controller = new ChunkCoordinates(tagCompound.getInteger("ControllerX"), tagCompound.getInteger("ControllerY"), tagCompound.getInteger("ControllerZ"));
        }
    }

    public boolean validateController()
    {
        if (controller.posY == -1)
        {
            return false;
        }
        else
        {
            TileEntity tile = worldObj.getBlockTileEntity(controller.posX, controller.posY, controller.posZ);
            return tile != null && tile instanceof TilePortalFrameController;
        }
    }

    public TilePortalFrameController getControllerValidated()
    {
        return validateController() ? (TilePortalFrameController) worldObj.getBlockTileEntity(controller.posX, controller.posY, controller.posZ) : null;
    }

    public boolean activate(EntityPlayer player)
    {
        if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == CommonProxy.itemWrench.itemID)
        {
            TilePortalFrameController control = getControllerValidated();

            if (control != null)
            {
                player.openGui(EnhancedPortals.instance, GuiIds.PORTAL_CONTROLLER, worldObj, control.xCoord, control.yCoord, control.zCoord);
                return true;
            }
        }

        return false;
    }

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

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketRequestData(this)));
        }
    }
}
