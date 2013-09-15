package uk.co.shadeddimensions.enhancedportals.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.enhancedportals.lib.Reference;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;

public class CreativeTabEP2 extends CreativeTabs
{
    public CreativeTabEP2()
    {
        super(Reference.ID);
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(CommonProxy.blockFrame, 1, 0);
    }
}
