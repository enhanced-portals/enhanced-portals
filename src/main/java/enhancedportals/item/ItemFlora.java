package enhancedportals.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFlora extends ItemPowered
{
    Block[] whitelist = new Block[] { Blocks.grass, Blocks.tallgrass }; // TODO
                                                                        // Customise
                                                                        // via
                                                                        // config

    public ItemFlora(String n)
    {
        super(n);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return false;
    }
}
