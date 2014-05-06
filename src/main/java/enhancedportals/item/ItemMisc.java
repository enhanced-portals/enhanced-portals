package enhancedportals.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import enhancedportals.EnhancedPortals;

public class ItemMisc extends Item
{
    public static ItemMisc instance;
    
    final static String[] NAMES = new String[] { "blankPortalModule", "blankUpgrade" };
    IIcon[] texture;

    public ItemMisc()
    {
        super();
        instance = this;
        setCreativeTab(EnhancedPortals.creativeTab);
        setUnlocalizedName("miscItems");
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return texture[par1];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < NAMES.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return getUnlocalizedName() + "." + NAMES[par1ItemStack.getItemDamage()];
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        texture = new IIcon[NAMES.length];

        for (int i = 0; i < NAMES.length; i++)
        {
            texture[i] = register.registerIcon("enhancedportals:" + NAMES[i]);
        }
    }
}
