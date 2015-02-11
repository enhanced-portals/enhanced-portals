package enhancedportals.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.tile.TileFrameController;

public class BlockFrameController extends BlockFrame {
    public BlockFrameController(String n) {
        super(n);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFrameController();
    }
}
