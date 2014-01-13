package uk.co.shadeddimensions.ep3.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.item.base.ItemPortalTool;

public class ItemWrench extends ItemPortalTool
{
    Icon texture;

    public ItemWrench(int id, String name)
    {
        super(id, true, name);
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return texture;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        texture = register.registerIcon("enhancedportals:wrench");
    }
}
