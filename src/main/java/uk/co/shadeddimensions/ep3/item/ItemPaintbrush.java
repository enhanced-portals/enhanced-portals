package uk.co.shadeddimensions.ep3.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.lib.Reference;

public class ItemPaintbrush extends Item
{
    public static int ID;
    public static ItemPaintbrush instance;
    
    public static Icon texture;

    public ItemPaintbrush()
    {
        super(ID);
        ID += 256;
        instance = this;
        setCreativeTab(Reference.creativeTab);
        setUnlocalizedName("nanobrush");
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
    public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
    {
        return true;
    }
}
