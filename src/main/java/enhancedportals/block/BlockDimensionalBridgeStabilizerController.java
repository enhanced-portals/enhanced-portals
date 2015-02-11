package enhancedportals.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.tile.TileDimensionalBridgeStabilizerController;

public class BlockDimensionalBridgeStabilizerController extends BlockDimensionalBridgeStabilizer {
    public BlockDimensionalBridgeStabilizerController(String n) {
        super(n);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileDimensionalBridgeStabilizerController();
    }
}
