package uk.co.shadeddimensions.ep3.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.CommonProxy;

public class CreativeTabEP3 extends CreativeTabs
{
    public static int portalColour = 5;
    
    public CreativeTabEP3()
    {
        super(Reference.ID);
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(CommonProxy.blockPortal, 1, portalColour);
    }
}
