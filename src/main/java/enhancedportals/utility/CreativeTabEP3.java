package enhancedportals.utility;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import enhancedportals.EnhancedPortals;
import enhancedportals.block.BlockPortal;

public class CreativeTabEP3 extends CreativeTabs
{
    public CreativeTabEP3()
    {
        super(EnhancedPortals.MOD_ID);
    }

    @Override
    public Item getTabIconItem()
    {
        return new ItemStack(BlockPortal.instance).getItem();
    }
}
