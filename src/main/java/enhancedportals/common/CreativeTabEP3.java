package enhancedportals.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.block.BlockPortal;
import enhancedportals.EnhancedPortals;

public class CreativeTabEP3 extends CreativeTabs
{
    public CreativeTabEP3()
    {
        super(EnhancedPortals.ID);
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(BlockPortal.instance, 1, 0);
    }
}
