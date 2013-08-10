package uk.co.shadeddimensions.enhancedportals.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;

public class ItemWrench extends Item
{
    public ItemWrench() // Needs a better name, "wrench" doesn't really describe what it does.
    {
        super(Identifiers.Item.WRENCH);
        setUnlocalizedName("ep2.itemWrench");
        maxStackSize = 1;
        setMaxDamage(0);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (world.getBlockId(x, y, z) == Identifiers.Block.PORTAL_FRAME && world.getBlockMetadata(x, y, z) == 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, 1, 2);
            return true;
        }

        return false;
    }
}
