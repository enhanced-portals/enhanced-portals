package uk.co.shadeddimensions.enhancedportals.item;

import net.minecraft.item.Item;
import uk.co.shadeddimensions.enhancedportals.lib.Reference;

public class ItemEP extends Item
{
    public ItemEP(int par1, boolean tab)
    {
        super(par1);

        if (tab)
        {
            setCreativeTab(Reference.creativeTab);
        }
    }
}
