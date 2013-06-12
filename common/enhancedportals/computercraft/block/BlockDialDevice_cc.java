package enhancedportals.computercraft.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.block.BlockDialDevice;
import enhancedportals.computercraft.tileentity.TileEntityDialDevice_cc;

public class BlockDialDevice_cc extends BlockDialDevice
{
    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntityDialDevice_cc();
    }
}
