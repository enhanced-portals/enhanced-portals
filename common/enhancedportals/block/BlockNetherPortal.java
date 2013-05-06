package enhancedportals.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Portal;
import enhancedportals.lib.PortalTexture;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Settings;
import enhancedportals.lib.WorldLocationBA;
import enhancedportals.tileentity.TileEntityNetherPortal;

public class BlockNetherPortal extends BlockEnhancedPortals
{
    public BlockNetherPortal()
    {
        super(BlockIds.NetherPortal, Material.portal);
        setHardness(-1.0F);
        setStepSound(soundGlassFootstep);
        setLightValue(0.75F);
        setUnlocalizedName(Localization.NetherPortal_Name);
        setCreativeTab(Reference.CREATIVE_TAB);
        setTickRandomly(true);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityNetherPortal();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) blockAccess.getBlockTileEntity(x, y, z);
        return netherPortal.getTexture().getIcon(side);
    }
    
    @Override
    public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        return new Portal(x, y, z, worldObj).handleBlockActivation(player);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        PortalTexture.registerTextures(iconRegister);
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        super.updateTick(world, y, x, z, random);
                
        if (random.nextInt(100) < Settings.PigmenLevel - 1)
        {
            if (random.nextInt(2000) < world.difficultySetting)
            {
                int y2 = y;
                
                while (!world.doesBlockHaveSolidTopSurface(x, y2, z) && y2 > 0)
                {
                    y2--;
                }
                
                if (y2 > 0 && world.isBlockNormalCube(x, y2 + 1, z))
                {
                    Entity zombiePigman = ItemMonsterPlacer.spawnCreature(world, 57, (double)x + 0.5, (double)y2 + 1.1, (double)z + 0.5);
                    
                    if (zombiePigman != null)
                    {
                        zombiePigman.timeUntilPortal = zombiePigman.getPortalCooldown();
                    }
                }
            }
        }
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
    
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        
        if (meta == 2 || meta == 3) // XY
        {
            setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
        }
        else if (meta == 4 || meta == 5) // ZY
        {
            setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
        }
        else if (meta == 6 || meta == 7) // XZ
        {
            setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
        }
    }
    
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public boolean tryToCreatePortal(World world, int x, int y, int z)
    {        
        return new Portal(x, y, z, world).createPortal();
    }
    
    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
        new Portal(x, y, z, world).handleNeighborChange(id);
    }
    
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        
        if (meta == 0 || meta == 1)
        {
            if (!blockAccess.isBlockOpaqueCube(x, y, z))
            {
                return new WorldLocationBA(x, y, z, blockAccess).getMaterial() != blockMaterial;
            }
        }
        if (meta == 2 || meta == 3) // XY
        {
            return side == 2 || side == 3;
        }
        else if (meta == 4 || meta == 5) // ZY
        {
            return side == 4 || side == 5;
        }
        else if (meta == 6 || meta == 7) // ZX
        {
            return side == 0 || side == 1;
        }
        
        return false;
    }
    
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
    
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        // TODO Teleport
    }
    
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if (random.nextInt(100) < Settings.SoundLevel - 1 && random.nextInt(100) == 0)
        {
            world.playSound((double)x + 0.5, (double)y + 0.5, (double)z + 0.5, "portal.portal", 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }
        
        if (random.nextInt(100) < Settings.ParticleLevel - 1)
        {
            for (int i = 0; i < 4; i++)
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
    
                world.spawnParticle("portal", d0, d1, d2, d3, d4, d5); // TODO COLOURED PARTICLES
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return 0;
    }
}
