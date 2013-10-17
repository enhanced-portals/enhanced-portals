package uk.co.shadeddimensions.ep3.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemPaintbrush extends ItemEnhancedPortals
{
    Icon texture;
    
    public ItemPaintbrush(int id, String name)
    {
        super(id, true);
        setUnlocalizedName(name);
        maxStackSize = 1;
        setMaxDamage(0);
    }

    @Override
    public Icon getIcon(ItemStack stack, int pass)
    {
        return texture;
    }
    
    @Override
    public void registerIcons(IconRegister register)
    {
        texture = register.registerIcon("paintbrush");
    }
}
