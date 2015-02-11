package enhancedportals.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedportals.tile.TilePortal;

public class BlockPortal extends BlockContainer {
    protected BlockPortal() {
        super(Material.glass);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TilePortal();
    }
}
