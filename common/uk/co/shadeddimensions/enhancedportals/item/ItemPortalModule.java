package uk.co.shadeddimensions.enhancedportals.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.enhancedportals.api.IPortalModule;
import uk.co.shadeddimensions.enhancedportals.client.particle.PortalFX;
import uk.co.shadeddimensions.enhancedportals.lib.Reference;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileModuleManipulator;

public class ItemPortalModule extends ItemEP implements IPortalModule
{
    public static enum PortalModules
    {
        REMOVE_PARTICLES,
        RAINBOW_PARTICLES,
        REMOVE_SOUNDS,
        KEEP_MOMENTUM,
        INVISIBLE_PORTAL;
    }
    
    public ItemPortalModule(int par1, String name)
    {
        super(par1, true);
        setUnlocalizedName(name);
        setMaxDamage(0);
        setMaxStackSize(32);
        setHasSubtypes(true);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(int itemID, CreativeTabs creativeTab, List list)
    {
        for (int i = 0; i < PortalModules.values().length; i++)
        {
            list.add(new ItemStack(itemID, 1, i));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        list.add(StatCollector.translateToLocal(getUnlocalizedNameInefficiently(stack) + ".desc"));
    }

    @Override
    public String getUniqueID(ItemStack upgrade)
    {
        return Reference.SHORT_ID + "." + upgrade.getItemDamage();
    }

    @Override
    public boolean canInstallUpgrade(TileModuleManipulator moduleManipulator, IPortalModule[] installedUpgrades, ItemStack upgrade)
    {
        return true;
    }

    @Override
    public boolean canRemoveUpgrade(TileModuleManipulator moduleManipulator, IPortalModule[] installedUpgrades, ItemStack upgrade)
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

    @Override
    public void onParticleCreated(TileModuleManipulator moduleManipulator, ItemStack upgrade, PortalFX particle)
    {
        if (upgrade.getItemDamage() == PortalModules.RAINBOW_PARTICLES.ordinal())
        {
            particle.setParticleRed((float) Math.random());
            particle.setParticleGreen((float) Math.random());
            particle.setParticleBlue((float) Math.random());
        }
    }

    @Override
    public boolean disableParticles(TileModuleManipulator moduleManipulator, ItemStack upgrade)
    {
        return upgrade.getItemDamage() == PortalModules.REMOVE_PARTICLES.ordinal();
    }

    @Override
    public boolean disablePortalRendering(TileModuleManipulator modulemanipulator, ItemStack upgrade)
    {
        return upgrade.getItemDamage() == PortalModules.INVISIBLE_PORTAL.ordinal();
    }
}
