package uk.co.shadeddimensions.ep3.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.lib.Reference;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ItemGoggles extends ItemArmor
{
    Icon icon;
    
    public ItemGoggles(int id, String name)
    {
        super(id, EnumArmorMaterial.CLOTH, RenderingRegistry.addNewArmourRendererPrefix("epGoggles"), 0);
        setCreativeTab(Reference.creativeTab);
        setUnlocalizedName(name);
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
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return "enhancedportals:textures/models/armor/goggles.png";
    }
}
