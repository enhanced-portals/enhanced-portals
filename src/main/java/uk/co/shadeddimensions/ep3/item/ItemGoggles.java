package uk.co.shadeddimensions.ep3.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.lib.Reference;

public class ItemGoggles extends ItemArmor
{
    public static int ID;
    public static ItemGoggles instance;
    
    Icon icon;

    public ItemGoggles()
    {
        super(ID, EnumArmorMaterial.CLOTH, EnhancedPortals.proxy.gogglesRenderIndex, 0);
        ID += 256;
        instance = this;
        setCreativeTab(Reference.creativeTab);
        setUnlocalizedName("glasses");
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return "enhancedportals:textures/models/armor/goggles.png";
    }

    @Override
    public Icon getIcon(ItemStack stack, int pass)
    {
        return icon;
    }

    @Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2)
    {
        return false;
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        icon = iconRegister.registerIcon("enhancedportals:goggles");
    }
}
