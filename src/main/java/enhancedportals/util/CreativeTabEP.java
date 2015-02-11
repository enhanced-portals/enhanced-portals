package enhancedportals.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.ProxyCommon;

public class CreativeTabEP extends CreativeTabs {
    public CreativeTabEP() {
        super(EnhancedPortals.MOD_ID);
    }

    @Override
    public Item getTabIconItem() {
        return new ItemStack(ProxyCommon.portalController).getItem();
    }
}
