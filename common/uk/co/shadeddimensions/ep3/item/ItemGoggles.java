package uk.co.shadeddimensions.ep3.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.lib.Reference;

public class ItemGoggles extends ItemArmor
{
    public ItemGoggles(int id, String name)
    {
        super(id, EnumArmorMaterial.CLOTH, 0, 0);
        setCreativeTab(Reference.creativeTab);
        setUnlocalizedName(name);
    }
        
    @Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2)
    {
        return false;
    }
    
    @Override
    public Icon getIcon(ItemStack stack, int pass)
    {
        return Item.appleGold.getIconFromDamage(0); // TODO
    }
    
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        // TODO
    }
}
