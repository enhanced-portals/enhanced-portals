package uk.co.shadeddimensions.ep3.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;

public class BlockStabilizer extends BlockEnhancedPortals
{
    public BlockStabilizer(int id, String name)
    {
        super(id, Material.rock, true);
        setHardness(5);
        setResistance(2000);
        setUnlocalizedName(name);
        setStepSound(soundStoneFootstep);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileStabilizer();
    }
}
