package enhancedportals.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.tile.TileFrameTransferEnergy;
import enhancedportals.tile.TileFrameTransferFluid;
import enhancedportals.tile.TileFrameTransferItem;

public class BlockFrameTransfer extends BlockFrame
{
    protected BlockFrameTransfer(String n)
    {
        super(n);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return meta == 0 ? new TileFrameTransferEnergy() : meta == 1 ? new TileFrameTransferFluid() : new TileFrameTransferItem();
    }
}
