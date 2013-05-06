package enhancedportals.creativetab;

import enhancedportals.EnhancedPortals;
import enhancedportals.lib.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabEP extends CreativeTabs
{        
    public CreativeTabEP()
    {
        super(Reference.MOD_ID);
    }
    
    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(EnhancedPortals.proxy.blockNetherPortal);
    }
}
