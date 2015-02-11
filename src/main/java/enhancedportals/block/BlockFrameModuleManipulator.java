package enhancedportals.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.tile.TileFrameModuleManipulator;

public class BlockFrameModuleManipulator extends BlockFrame {
    protected BlockFrameModuleManipulator(String n) {
        super(n);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFrameModuleManipulator();
    }
}
