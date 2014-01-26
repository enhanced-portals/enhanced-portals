package uk.co.shadeddimensions.ep3.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.lib.GUIs;

public class ItemGuide extends ItemEnhancedPortals
{
    public static Icon texture;

    public ItemGuide(int id, String name)
    {
        super(id, true);
        setUnlocalizedName(name);
        setMaxStackSize(1);
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return texture;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        texture = register.registerIcon("enhancedportals:paintbrush");
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        player.openGui(EnhancedPortals.instance, GUIs.Guide.ordinal(), world, 0, 0, 0);
        return super.onItemRightClick(stack, world, player);
    }
}
