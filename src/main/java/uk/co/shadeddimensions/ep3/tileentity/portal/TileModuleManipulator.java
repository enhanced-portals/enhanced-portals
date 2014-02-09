package uk.co.shadeddimensions.ep3.tileentity.portal;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.api.IPortalModule;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.client.particle.PortalFX;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.PacketHandlerServer;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TileModuleManipulator extends TileFrame implements IInventory
{
    ItemStack[] inventory = new ItemStack[9];
    
    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
    	if (player.isSneaking())
		{
			return false;
		}
    	
        TileController controller = getPortalController();

        if (stack != null && controller != null && controller.isFinalized())
        {
            if (ItemHelper.isWrench(stack) && !player.isSneaking())
            {
                GuiHandler.openGui(player, this, GuiHandler.MODULE_MANIPULATOR);
                return true;
            }
            else if (stack.itemID == ItemPaintbrush.ID)
            {
                GuiHandler.openGui(player, controller, GuiHandler.TEXTURE_FRAME);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack s = getStackInSlot(i);
        s.stackSize -= j;

        return s;
    }

    @Override
    public void packetFill(DataOutputStream stream) throws IOException
    {
        super.packetFill(stream);

        for (int i = 0; i < getSizeInventory(); i++)
        {
            if (getStackInSlot(i) != null)
            {
                stream.writeInt(getStackInSlot(i).itemID);
                stream.writeInt(getStackInSlot(i).getItemDamage());
            }
            else
            {
                stream.writeInt(0);
                stream.writeInt(0);
            }
        }
    }

    public IPortalModule[] getInstalledUpgrades()
    {
        IPortalModule[] modules = new IPortalModule[getSizeInventory()];

        for (int i = 0; i < getSizeInventory(); i++)
        {
            ItemStack s = getStackInSlot(i);

            if (s != null)
            {
                modules[i] = (IPortalModule) s.getItem();
            }
        }

        return modules;
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

    public ArrayList<ItemStack> getModules()
    {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();

        for (ItemStack i : inventory)
        {
            if (i != null && i.getItem() instanceof IPortalModule)
            {
                list.add(i);
            }
        }

        return list;
    }

    public ItemStack getModifierItem()
    {
        return inventory[9];
    }

    public boolean hasModule(String ID)
    {
        for (ItemStack i : getModules())
        {
            if (((IPortalModule) i.getItem()).getID(i).equals(ID))
            {
                return true;
            }
        }

        return false;
    }

    public boolean installUpgrade(ItemStack stack)
    {
        if (stack == null || !(stack.getItem() instanceof IPortalModule))
        {
            return false;
        }

        IPortalModule pModule = (IPortalModule) stack.getItem();

        if (!hasModule(pModule.getID(stack)) && pModule.canInstallUpgrade(this, getInstalledUpgrades(), stack))
        {
            for (int i = 0; i < getSizeInventory(); i++)
            {
                if (getStackInSlot(i) == null)
                {
                    ItemStack s = stack.copy();
                    s.stackSize = 1;

                    setInventorySlotContents(i, s);
                    PacketHandlerServer.sendUpdatePacketToAllAround(this);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack)
    {
        return stack != null && (stack.getItem() instanceof IPortalModule && !hasModule(((IPortalModule) stack.getItem()).getID(stack)));
    }

    public boolean isPortalInvisible()
    {
        for (ItemStack i : getModules())
        {
            if (((IPortalModule) i.getItem()).disablePortalRendering(this, i))
            {
                return true;
            }
        }

        return false;
    }

    public void onEntityTeleported(Entity entity)
    {
        for (ItemStack i : getModules())
        {
            ((IPortalModule) i.getItem()).onEntityTeleportEnd(entity, this, i);
        }
    }

    public void particleCreated(PortalFX portalFX)
    {
        for (ItemStack i : getModules())
        {
            ((IPortalModule) i.getItem()).onParticleCreated(this, i, portalFX);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        NBTTagList list = tagCompound.getTagList("Inventory");
        for (int i = 0; i < list.tagCount(); i++)
        {
            inventory[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) list.tagAt(i));
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
    }

    public boolean shouldKeepMomentumOnTeleport()
    {
        for (ItemStack i : getModules())
        {
            if (((IPortalModule) i.getItem()).keepMomentumOnTeleport(this, i))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void packetUse(java.io.DataInputStream stream) throws IOException
    {
        super.packetUse(stream);

        for (int i = 0; i < getSizeInventory(); i++)
        {
            int id = stream.readInt(), meta = stream.readInt();

            if (id != 0)
            {
                setInventorySlotContents(i, new ItemStack(id, 1, meta));
            }
        }

        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
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

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
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
}
