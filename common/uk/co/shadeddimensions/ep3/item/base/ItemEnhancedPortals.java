package uk.co.shadeddimensions.ep3.item.base;

import net.minecraft.item.Item;
import uk.co.shadeddimensions.ep3.lib.Reference;

public class ItemEnhancedPortals extends Item
{
    public ItemEnhancedPortals(int par1, boolean tab)
    {
        super(par1);

        if (tab)
        {
            setCreativeTab(Reference.creativeTab);
        }
    }
}
