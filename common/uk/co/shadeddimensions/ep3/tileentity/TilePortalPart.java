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
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.item.base.ItemPortalTool;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class TilePortalPart extends TileEnhancedPortals implements IInventory
{
    public WorldCoordinates portalController;

    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        TilePortalController controller = getPortalController();

        if (controller != null)
        {
            controller.partBroken();
        }
    }

    @Override
    public void onBlockPlacedBy(EntityLivingBase entity, ItemStack stack)
    {
        for (int i = 0; i < 6; i++)
        {
            WorldCoordinates c = getWorldCoordinates().offset(ForgeDirection.getOrientation(i));            
            TileEntity tile = c.getBlockTileEntity();

            if (tile != null && tile instanceof TilePortalPart)
            {
                ((TilePortalPart) tile).breakBlock(0, 0);
            }
        }
    }

    @Override
    public boolean activate(EntityPlayer player)
    {
        ItemStack s = player.inventory.getCurrentItem();

        if ((s != null && s.getItem() instanceof ItemPortalTool) || this instanceof TileDiallingDevice)
        {
            TilePortalController controller = getPortalController();

            if (controller == null || !controller.hasConfigured)
            {
                return false;
            }
            else if (this instanceof TileDiallingDevice)
            {
                if (controller.getUniqueIdentifier() == null)
                {
                    if (!worldObj.isRemote)
                    {
                        player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.noUidSet"));
                    }

                    return false;
                }
                
                CommonProxy.openGui(player, GUIs.DiallingDevice, this);
                return true;
            }

            if (s.getItem().itemID == CommonProxy.itemPaintbrush.itemID)
            {
                if (this instanceof TilePortal)
                {
                    CommonProxy.openGui(player, player.isSneaking() ? GUIs.TexturesParticle : GUIs.TexturesPortal, controller);
                }
                else
                {
                    CommonProxy.openGui(player, GUIs.TexturesFrame, controller);
                }
                
                return true;
            }
            else if (s.getItem().itemID == CommonProxy.itemWrench.itemID)
            {
                if (this instanceof TilePortalController || this instanceof TileFrame || this instanceof TilePortal)
                {
                    CommonProxy.openGui(player, GUIs.PortalController, controller);
                }
                else if (this instanceof TileRedstoneInterface)
                {
                    CommonProxy.openGui(player, GUIs.RedstoneInterface, this);
                }
                else if (this instanceof TileNetworkInterface)
                {
                    if (controller.getUniqueIdentifier() == null)
                    {
                        if (!worldObj.isRemote)
                        {
                            player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.noUidSet"));
                        }

                        return false;
                    }
                    else
                    {
                        CommonProxy.openGui(player, GUIs.NetworkInterface, controller);
                    }
                }
                else if (this instanceof TileBiometricIdentifier)
                {
                    CommonProxy.openGui(player, GUIs.BiometricIdentifier, this);
                }
                else if (this instanceof TileModuleManipulator)
                {
                    CommonProxy.openGui(player, GUIs.ModuleManipulator, this);
                }
                
                return true;
            }

            return true;
        }

        return false;
    }

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
