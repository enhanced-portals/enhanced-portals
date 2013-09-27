package uk.co.shadeddimensions.enhancedportals.api;

import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileModuleManipulator;

public interface IPortalUpgrade
{
    public String getUniqueID(ItemStack upgrade);
    
    public boolean canInstallUpgrade(TileModuleManipulator moduleManipulator, IPortalUpgrade[] installedUpgrades, ItemStack upgrade);
    public boolean canRemoveUpgrade(TileModuleManipulator moduleManipulator, IPortalUpgrade[] installedUpgrades, ItemStack upgrade);
    
    public void onUpgradeInstalled(TileModuleManipulator moduleManipulator, ItemStack upgrade);
    public void onUpgradeRemoved(TileModuleManipulator moduleManipulator, ItemStack upgrade);
    public void onPortalCreated(TileModuleManipulator moduleManipulator, ItemStack upgrade);
    public void onPortalRemoved(TileModuleManipulator moduleManipulator, ItemStack upgrade);
}
