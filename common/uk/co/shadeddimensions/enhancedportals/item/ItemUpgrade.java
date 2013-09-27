package uk.co.shadeddimensions.enhancedportals.item;

import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.enhancedportals.api.IPortalUpgrade;
import uk.co.shadeddimensions.enhancedportals.lib.Reference;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileModuleManipulator;

public class ItemUpgrade extends ItemEP implements IPortalUpgrade
{
    public ItemUpgrade(int par1, String name)
    {
        super(par1, true);
        setUnlocalizedName(name);
    }

    @Override
    public String getUniqueID(ItemStack upgrade)
    {
        return Reference.SHORT_ID + "." + upgrade.getItemDamage();
    }

    @Override
    public boolean canInstallUpgrade(TileModuleManipulator moduleManipulator, IPortalUpgrade[] installedUpgrades, ItemStack upgrade)
    {
        return true;
    }

    @Override
    public boolean canRemoveUpgrade(TileModuleManipulator moduleManipulator, IPortalUpgrade[] installedUpgrades, ItemStack upgrade)
    {
        return true;
    }

    @Override
    public void onUpgradeInstalled(TileModuleManipulator moduleManipulator, ItemStack upgrade)
    {
        
    }

    @Override
    public void onUpgradeRemoved(TileModuleManipulator moduleManipulator, ItemStack upgrade)
    {
        
    }

    @Override
    public void onPortalCreated(TileModuleManipulator moduleManipulator, ItemStack upgrade)
    {
        
    }

    @Override
    public void onPortalRemoved(TileModuleManipulator moduleManipulator, ItemStack upgrade)
    {
        
    }
}
