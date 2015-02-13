package enhancedportals.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.tile.TileFrameNetworkInterface;

public class BlockFrameNetworkInterface extends BlockFrame
{
    protected BlockFrameNetworkInterface(String n)
    {
        super(n);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileFrameNetworkInterface();
    }
}
