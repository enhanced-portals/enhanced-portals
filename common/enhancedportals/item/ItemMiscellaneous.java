package enhancedportals.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedcore.util.MathHelper;
import enhancedportals.lib.ItemIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;

public class ItemMiscellaneous extends Item
{
    Icon[]   textures;
    String[] names = { "upgradeCard" };

    public ItemMiscellaneous()
    {
        super(ItemIds.MiscellaneousItems);
        hasSubtypes = true;
        setMaxDamage(0);
        setCreativeTab(Reference.CREATIVE_TAB);
        setUnlocalizedName(Localization.MiscellaneousItems_Name);
        maxStackSize = 64;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        if (par1 > textures.length)
        {
            return textures[0];
        }

        return textures[par1];
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < names.length; var4++)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = MathHelper.clampInt(par1ItemStack.getItemDamage(), 0, 15);
        return super.getUnlocalizedName() + "." + names[i];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        textures = new Icon[names.length];

        for (int i = 0; i < names.length; i++)
        {
            textures[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + names[i]);
        }
    }
}
