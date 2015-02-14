package enhancedportals.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.tile.TileFramePortalManipulator;

public class BlockFramePortalManipulator extends BlockFrame
{
    public BlockFramePortalManipulator(String n)
    {
        super(n);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileFramePortalManipulator();
    }
}
