package uk.co.shadeddimensions.ep3.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.PortalUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNetherPortal extends net.minecraft.block.BlockPortal
{
    public BlockNetherPortal(int id)
    {
        super(id);
    }
    
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!CommonProxy.disablePigmen && par1World.provider.isSurfaceWorld() && par5Random.nextInt(2000) < par1World.difficultySetting)
        {
            int l;

            for (l = par3; !par1World.doesBlockHaveSolidTopSurface(par2, l, par4) && l > 0; --l)
            {
                ;
            }

            if (l > 0 && !par1World.isBlockNormalCube(par2, l + 1, par4))
            {
                Entity entity = ItemMonsterPlacer.spawnCreature(par1World, 57, (double)par2 + 0.5D, (double)l + 1.1D, (double)par4 + 0.5D);

                if (entity != null)
                {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        int id = par1IBlockAccess.getBlockId(par2, par3, par4);
        
        if (id == this.blockID || id == Block.obsidian.blockID)
        {
            return false;
        }
        
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if (!CommonProxy.netherDisableSounds && random.nextInt(100) == 0)
        {
            world.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "portal.portal", 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        if (!CommonProxy.netherDisableParticles)
        {
            for (int l = 0; l < 4; ++l)
            {
                double d0 = (double)((float)x + random.nextFloat());
                double d1 = (double)((float)y + random.nextFloat());
                double d2 = (double)((float)z + random.nextFloat());
                double d3 = 0.0D;
                double d4 = 0.0D;
                double d5 = 0.0D;
                int i1 = random.nextInt(2) * 2 - 1;
                d3 = ((double)random.nextFloat() - 0.5D) * 0.5D;
                d4 = ((double)random.nextFloat() - 0.5D) * 0.5D;
                d5 = ((double)random.nextFloat() - 0.5D) * 0.5D;
    
                if (world.getBlockId(x - 1, y, z) != this.blockID && world.getBlockId(x + 1, y, z) != this.blockID)
                {
                    d0 = (double)x + 0.5D + 0.25D * (double)i1;
                    d3 = (double)(random.nextFloat() * 2.0F * (float)i1);
                }
                else
                {
                    d2 = (double)z + 0.5D + 0.25D * (double)i1;
                    d5 = (double)(random.nextFloat() * 2.0F * (float)i1);
                }
    
                world.spawnParticle("portal", d0, d1, d2, d3, d4, d5);
            }
        }
    }
    
    @Override
    public boolean tryToCreatePortal(World world, int x, int y, int z)
    {
        return PortalUtils.createNetherPortalFrom(world, x, y, z);
    }
}
