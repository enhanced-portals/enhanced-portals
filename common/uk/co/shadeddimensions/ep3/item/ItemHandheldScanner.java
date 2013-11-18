package uk.co.shadeddimensions.ep3.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.container.ContainerScanner;
import uk.co.shadeddimensions.ep3.container.InventoryScanner;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.util.EntityData;
import cofh.api.energy.ItemEnergyContainer;

public class ItemHandheldScanner extends ItemEnergyContainer
{    
    public ItemHandheldScanner(int par1, String name)
    {
        super(par1, 2000, 250, 250);
        setUnlocalizedName(name);
        setCreativeTab(Reference.creativeTab);
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    public static InventoryScanner getInventory(ItemStack stack)
    {
        return new InventoryScanner(stack);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par2World.isRemote)
        {
            if (par3EntityPlayer.isSneaking())
            {
                scanEntity(par3EntityPlayer, par3EntityPlayer, par1ItemStack);
                return par1ItemStack;
            }

            par3EntityPlayer.openGui(EnhancedPortals.instance, GUIs.Scanner.ordinal(), par2World, 0, 0, 0);
        }

        return par1ItemStack;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        scanEntity(entity, player, stack);
        return true;
    }

    private void scanEntity(Entity entity, EntityPlayer player, ItemStack stack)
    {
        NBTTagCompound tag = null;

        if (stack.hasTagCompound())
        {
            tag = stack.getTagCompound();
        }
        else
        {
            tag = new NBTTagCompound();
        }

        NBTTagList list = null;

        if (tag.hasKey("scanned"))
        {
            list = tag.getTagList("scanned");
        }
        else
        {
            list = new NBTTagList();
        }

        int id = 0;

        if (player.isSneaking())
        {
            id = EntityData.getParentEntityID(entity);

            if (id == 0)
            {
                id = EntityList.getEntityID(entity);
            }
        }
        else
        {
            id = EntityList.getEntityID(entity);
        }

        if (id == 0)
        {
            return;
        }

        NBTTagCompound t = new NBTTagCompound();
        t.setString("Name", entity.getEntityName());
        t.setInteger("ID", id);
        list.appendTag(t);

        tag.setTag("scanned", list);
        stack.setTagCompound(tag);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (par3Entity instanceof EntityPlayer && !par2World.isRemote && ((EntityPlayer) par3Entity).inventory.getCurrentItem() != null && ((EntityPlayer) par3Entity).inventory.getCurrentItem().equals(itemStack))
        {
            EntityPlayer player = (EntityPlayer) par3Entity;

            if (player.openContainer != null && player.openContainer instanceof ContainerScanner)
            {
                ContainerScanner scanner = (ContainerScanner) player.openContainer;

                if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("scanned"))
                {
                    if (scanner.scannerInventory.getStackInSlot(0) != null && drainPower(itemStack))
                    {
                        if (scanner.scannerInventory.getStackInSlot(1) == null)
                        {
                            NBTTagList list = itemStack.getTagCompound().getTagList("scanned");
                            ItemStack s = new ItemStack(CommonProxy.itemEntityCard, 1);
                            NBTTagCompound t = new NBTTagCompound();
                            t.setTag("entities", list);
                            s.setTagCompound(t);

                            NBTTagCompound c = itemStack.getTagCompound();
                            c.removeTag("scanned");
                            itemStack.setTagCompound(c);

                            scanner.scannerInventory.decrStackSize(0, 1);
                            scanner.scannerInventory.setInventorySlotContents(1, s);
                        }
                        else
                        {
                            NBTTagList list = itemStack.getTagCompound().getTagList("scanned");                        
                            ItemStack s = scanner.scannerInventory.getStackInSlot(1);
                            NBTTagCompound t = s.getTagCompound();
                            NBTTagList l = t.getTagList("entities");

                            for (int i = 0; i < list.tagCount(); i++)
                            {
                                l.appendTag((NBTTagCompound) list.tagAt(i));
                            }

                            t.setTag("entities", l);
                            s.setTagCompound(t);

                            NBTTagCompound c = itemStack.getTagCompound();
                            c.removeTag("scanned");
                            itemStack.setTagCompound(c);

                            scanner.scannerInventory.setInventorySlotContents(1, s);
                        }

                        scanner.hasChanged = true;
                    }
                    else if (scanner.scannerInventory.getStackInSlot(0) == null && scanner.scannerInventory.getStackInSlot(1) != null && drainPower(itemStack))
                    {
                        NBTTagList list = itemStack.getTagCompound().getTagList("scanned");                        
                        ItemStack s = scanner.scannerInventory.getStackInSlot(1);
                        NBTTagCompound t = s.getTagCompound();
                        NBTTagList l = t.getTagList("entities");

                        for (int i = 0; i < list.tagCount(); i++)
                        {
                            l.appendTag((NBTTagCompound) list.tagAt(i));
                        }

                        t.setTag("entities", l);
                        s.setTagCompound(t);

                        NBTTagCompound c = itemStack.getTagCompound();
                        c.removeTag("scanned");
                        itemStack.setTagCompound(c);

                        scanner.scannerInventory.setInventorySlotContents(1, s);
                        scanner.hasChanged = true;
                    }
                }

                if (scanner.hasChanged)
                {
                    scanner.saveToNBT(itemStack);
                }
            }
        }
    }
    
    private boolean drainPower(ItemStack itemStack)
    {
        if (CommonProxy.redstoneFluxPowerMultiplier > 0)
        {
            if (((ItemEnergyContainer) itemStack.getItem()).extractEnergy(itemStack, 50 * CommonProxy.redstoneFluxPowerMultiplier, true) != 50 * CommonProxy.redstoneFluxPowerMultiplier)
            {
                return false;
            }
            else
            {
                ((ItemEnergyContainer) itemStack.getItem()).extractEnergy(itemStack, 50 * CommonProxy.redstoneFluxPowerMultiplier, false);
                return true;
            }
        }
        
        return true;
    }
}
