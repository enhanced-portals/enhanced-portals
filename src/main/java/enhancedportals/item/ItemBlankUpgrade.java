package enhancedportals.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import enhancedportals.EnhancedPortals;

public class ItemBlankUpgrade extends Item
{
    public static ItemBlankUpgrade instance;
    IIcon texture;

    public ItemBlankUpgrade(String n)
    {
        super();
        instance = this;
        setCreativeTab(EnhancedPortals.creativeTab);
        setUnlocalizedName(n);
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return texture;
    }

    @Override
    public void registerIcons(IIconRegister ir)
    {
        texture = ir.registerIcon("blankUpgrade");
    }
}
