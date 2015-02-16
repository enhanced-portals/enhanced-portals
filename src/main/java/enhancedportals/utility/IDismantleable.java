package enhancedportals.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IDismantleable
{
    public void dismantleBlock(EntityPlayer player, World world, int x, int y, int z);
}
