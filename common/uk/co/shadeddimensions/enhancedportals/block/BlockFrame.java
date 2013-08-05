package uk.co.shadeddimensions.enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;
import uk.co.shadeddimensions.enhancedportals.util.PortalUtils;

public class BlockFrame extends Block
{
    public BlockFrame()
    {
        super(Identifiers.Block.PORTAL_FRAME, Material.rock);
        setHardness(10);
        setResistance(2000);
        setUnlocalizedName("portal");
        setStepSound(soundStoneFootstep);
    }
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
    {
        if (world.isRemote)
        {
            return;
        }
        
        PortalUtils.removePortal((WorldServer) world, x, y, z);
    }
    
    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion par5Explosion)
    {
        if (world.isRemote)
        {
            return;
        }
        
        PortalUtils.removePortal((WorldServer) world, x, y, z);
    }
}
