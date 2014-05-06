package enhancedportals.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import enhancedportals.EnhancedPortals;

public class ItemGlasses extends ItemArmor
{
    public static ItemGlasses instance;

    IIcon icon;

    public ItemGlasses(String n)
    {
        super(ArmorMaterial.CLOTH, EnhancedPortals.proxy.gogglesRenderIndex, 0);
        instance = this;
        setCreativeTab(EnhancedPortals.creativeTab);
        setUnlocalizedName(n);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return "enhancedportals:textures/models/armor/goggles.png";
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return icon;
    }

    @Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2)
    {
        return false;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        icon = iconRegister.registerIcon("enhancedportals:goggles");
    }
}
