package uk.co.shadeddimensions.ep3.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.item.base.ItemEnhancedPortals;

public class ItemMisc extends ItemEnhancedPortals
{
    final static String[] NAMES = new String[] { "blankPortalModule", "blankUpgrade" };
    Icon[] texture;

    public ItemMisc(int par1, String name)
    {
        super(par1, true);
        setUnlocalizedName(name);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return getUnlocalizedName() + "." + NAMES[par1ItemStack.getItemDamage()];
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return texture[par1];
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        texture = new Icon[NAMES.length];

        for (int i = 0; i < NAMES.length; i++)
        {
            texture[i] = register.registerIcon("enhancedportals:" + NAMES[i]);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < NAMES.length; i++)
        {
            par3List.add(new ItemStack(itemID, 1, i));
        }
    }
}
