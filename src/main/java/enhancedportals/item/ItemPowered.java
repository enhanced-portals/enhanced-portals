package enhancedportals.item;

import enhancedportals.network.ProxyCommon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemPowered extends Item
{
    private IIcon texture;

    public ItemPowered(String n)
    {
        super();
        setCreativeTab(ProxyCommon.creativeTab);
        setUnlocalizedName(n);
        setMaxStackSize(1);
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return texture;
    }

    public boolean canPerformTask(int amt)
    {
        return true; // TODO
    }

    public void drainEnergy(int amt)
    {
        // TODO
    }
}
