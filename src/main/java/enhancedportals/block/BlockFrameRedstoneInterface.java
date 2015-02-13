package enhancedportals.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.tile.TileFrameRedstoneInterface;

public class BlockFrameRedstoneInterface extends BlockFrame
{
    protected BlockFrameRedstoneInterface(String n)
    {
        super(n);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileFrameRedstoneInterface();
    }
}
