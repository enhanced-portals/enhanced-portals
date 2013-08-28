package uk.co.shadeddimensions.enhancedportals.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import enhancedportals.lib.Reference;

public class CreativeTabEP2 extends CreativeTabs
{
    public CreativeTabEP2()
    {
        super(Reference.MOD_ID);
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(CommonProxy.blockFrame, 1, 0);
    }
}
