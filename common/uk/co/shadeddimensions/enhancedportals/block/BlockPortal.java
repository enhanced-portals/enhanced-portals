package uk.co.shadeddimensions.enhancedportals.block;

import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;
import uk.co.shadeddimensions.enhancedportals.util.PortalUtils;

public class BlockPortal extends net.minecraft.block.BlockPortal
{
    public BlockPortal()
    {
        super(Identifiers.Block.PORTAL_BLOCK);
        setBlockUnbreakable();
        setResistance(2000);
        setUnlocalizedName("portalFrame");
        setStepSound(soundGlassFootstep);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
        // Stop from breaking portal, this should never need to happen
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
    
    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        // teleport
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int par5)
    {
        int ID = blockAccess.getBlockId(x, y, z);
        
        if (ID == blockID || ID == Identifiers.Block.PORTAL_FRAME)
        {
            return false;
        }
        
        return true;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
    {
        int idPosX = par1iBlockAccess.getBlockId(par2 + 1, par3, par4),
            idNegX = par1iBlockAccess.getBlockId(par2 - 1, par3, par4),
            idPosY = par1iBlockAccess.getBlockId(par2, par3 + 1, par4),
            idNegY = par1iBlockAccess.getBlockId(par2, par3 - 1, par4),
            idPosZ = par1iBlockAccess.getBlockId(par2, par3, par4 + 1),
            idNegZ = par1iBlockAccess.getBlockId(par2, par3, par4 - 1);
        
        if ((idNegX == blockID || idNegX == Identifiers.Block.PORTAL_FRAME) && (idPosX == blockID || idPosX == Identifiers.Block.PORTAL_FRAME) && (idNegZ == blockID || idNegZ == Identifiers.Block.PORTAL_FRAME) && (idPosZ == blockID || idPosZ == Identifiers.Block.PORTAL_FRAME) && (idNegY == blockID || idNegY == Identifiers.Block.PORTAL_FRAME) && (idPosY == blockID || idPosY == Identifiers.Block.PORTAL_FRAME))
        {
            setBlockBounds(0, 0f, 0f, 1f, 1f, 1f);
        }
        else if ((idNegX == blockID || idNegX == Identifiers.Block.PORTAL_FRAME) && (idPosX == blockID || idPosX == Identifiers.Block.PORTAL_FRAME) && (idNegZ == blockID || idNegZ == Identifiers.Block.PORTAL_FRAME) && (idPosZ == blockID || idPosZ == Identifiers.Block.PORTAL_FRAME))
        {
            setBlockBounds(0, 0.375f, 0f, 1f, 0.625f, 1f);
        }
        else if ((idNegX == blockID || idNegX == Identifiers.Block.PORTAL_FRAME) && (idPosX == blockID || idPosX == Identifiers.Block.PORTAL_FRAME))
        {
            setBlockBounds(0f, 0f, 0.375f, 1f, 1f, 0.625f);
        }
        else if ((idNegZ == blockID || idNegZ == Identifiers.Block.PORTAL_FRAME) && (idPosZ == blockID || idPosZ == Identifiers.Block.PORTAL_FRAME))
        {
            setBlockBounds(0.375f, 0f, 0f, 0.625f, 1f, 1f);
        }
    }
    
    @Override
    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
    }
}
