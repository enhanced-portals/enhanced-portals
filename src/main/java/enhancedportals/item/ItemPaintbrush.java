package enhancedportals.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import enhancedportals.EnhancedPortals;

public class ItemPaintbrush extends Item
{
    public static ItemPaintbrush instance;
    
    public static IIcon texture;

    public ItemPaintbrush()
    {
        super();
        instance = this;
        setCreativeTab(EnhancedPortals.creativeTab);
        setUnlocalizedName("nanobrush");
        setMaxStackSize(1);
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return texture;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        texture = register.registerIcon("enhancedportals:paintbrush");
    }
    
    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
    	return true;
    }
}
