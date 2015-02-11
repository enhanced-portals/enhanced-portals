package enhancedportals.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.tile.TileFrameDialDevice;

public class BlockFrameDialDevice extends BlockFrame {
    protected BlockFrameDialDevice(String n) {
        super(n);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFrameDialDevice();
    }
}
