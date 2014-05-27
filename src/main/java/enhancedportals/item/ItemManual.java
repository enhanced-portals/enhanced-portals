package enhancedportals.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.ClientProxy;
import enhancedportals.network.GuiHandler;

public class ItemManual extends Item
{
    public static ItemManual instance;
    IIcon texture;

    public ItemManual(String n)
    {
        super();
        instance = this;
        setCreativeTab(EnhancedPortals.creativeTab);
        setUnlocalizedName(n);
        setMaxStackSize(1);
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return texture;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        player.openGui(EnhancedPortals.instance, GuiHandler.MANUAL, world, 0, 0, 0);
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (world.isRemote && player.isSneaking() && ClientProxy.setManualPageFromBlock(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)))
        {
            player.openGui(EnhancedPortals.instance, GuiHandler.MANUAL, world, 0, 0, 0);
            return true;
        }

        return false;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        texture = iconRegister.registerIcon("enhancedportals:manual");
    }
}
