package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.api.IPortalModule;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.client.particle.PortalFX;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TileModuleManipulator extends TilePortalFrameSpecial
{
    ItemStack[] inventory;

    public TileModuleManipulator()
    {
        inventory = new ItemStack[10];
    }
    
    @Override
    public boolean activate(EntityPlayer player)
    {
        ItemStack item = player.inventory.getCurrentItem();

        if (item != null)
        {
            if (ItemHelper.isWrench(item) && !player.isSneaking())
            {
                CommonProxy.openGui(player, GUIs.ModuleManipulator, this);
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

    public boolean canSendEntity(Entity entity)
    {
        for (ItemStack i : getModules())
        {
            if (((IPortalModule) i.getItem()).onEntityTeleportStart(entity, this, i))
            {
                return false;
            }
        }

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
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);

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

    @Override
    public Icon getBlockTexture(int side, int pass)
    {
        if (pass == 0)
        {
            return super.getBlockTexture(side, pass);
        }

        return CommonProxy.forceShowFrameOverlays || wearingGoggles ? BlockFrame.overlayIcons[6] : BlockFrame.overlayIcons[0];
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
                    CommonProxy.sendUpdatePacketToAllAround(this);
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
        return stack != null && (i < 9 && stack.getItem() instanceof IPortalModule && !hasModule(((IPortalModule) stack.getItem()).getID(stack)));
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
    public void usePacket(java.io.DataInputStream stream) throws IOException
    {
        super.usePacket(stream);

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
}
