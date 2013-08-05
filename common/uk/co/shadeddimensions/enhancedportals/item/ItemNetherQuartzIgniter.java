package uk.co.shadeddimensions.enhancedportals.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;
import uk.co.shadeddimensions.enhancedportals.util.PortalUtils;

public class ItemNetherQuartzIgniter extends Item
{
    public ItemNetherQuartzIgniter()
    {
        super(Identifiers.Item.NETHER_QUARTZ_IGNITER);
        setUnlocalizedName("netherQuartzIgniter");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            if (world.getBlockId(x, y, z) == Identifiers.Block.PORTAL_FRAME)
            {
                if (player.isSneaking())
                {
                    PortalUtils.removePortal((WorldServer) world, x, y, z);
                }
                else
                {
                    PortalUtils.createPortal((WorldServer) world, x, y, z);
                }

                return true;
            }
        }

        return false;
    }
}
