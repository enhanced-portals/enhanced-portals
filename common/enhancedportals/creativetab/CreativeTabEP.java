package enhancedportals.creativetab;

import uk.co.shadeddimensions.enhancedportals.EnhancedPortals_deprecated;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import enhancedportals.lib.Reference;

public class CreativeTabEP extends CreativeTabs
{
    public CreativeTabEP()
    {
        super(Reference.MOD_ID);
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(EnhancedPortals_deprecated.proxy.blockPortalModifier);
    }
}
