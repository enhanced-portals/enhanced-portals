package enhancedportals.computercraft.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.block.BlockDialDeviceBasic;
import enhancedportals.computercraft.tileentity.TileEntityDialDeviceBasic_cc;

public class BlockDialDeviceBasic_cc extends BlockDialDeviceBasic
{
    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntityDialDeviceBasic_cc();
    }
}
