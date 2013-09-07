package uk.co.shadeddimensions.enhancedportals.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;

public class ItemWrench extends ItemEP2
{
    public ItemWrench(int id, String name) // TODO: Needs a better name, "wrench" doesn't really describe what it does.
    {
        super(id, true);
        setUnlocalizedName(name);
        maxStackSize = 1;
        setMaxDamage(0);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (world.isRemote)
        {
            return false;
        }

        if (world.getBlockId(x, y, z) == CommonProxy.blockFrame.blockID)
        {
            return true;
        }

        return false;
    }
}
