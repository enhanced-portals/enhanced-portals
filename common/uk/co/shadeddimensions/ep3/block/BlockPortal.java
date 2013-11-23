package uk.co.shadeddimensions.ep3.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import uk.co.shadeddimensions.ep3.client.particle.PortalFX;
import uk.co.shadeddimensions.ep3.item.ItemPortalModule;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPortal extends BlockEnhancedPortals
{
    Icon texture;

    public BlockPortal(int id, String name)
    {
        super(id, Material.portal, false);
        setBlockUnbreakable();
        setResistance(2000);
        setUnlocalizedName(name);
        setStepSound(soundGlassFootstep);
    }

    @Override
    public boolean canBeReplacedByLeaves(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TilePortal();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        return texture;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TilePortal portal = (TilePortal) blockAccess.getBlockTileEntity(x, y, z);
        TilePortalController controller = portal.getPortalController();

        if (controller != null)
        {
            ItemStack stack = controller.getStackInSlot(1);

            if (controller.activeTextureData.hasCustomPortalTexture())
            {
                if (ClientProxy.customPortalTextures.size() > controller.activeTextureData.getCustomPortalTexture() && ClientProxy.customPortalTextures.get(controller.activeTextureData.getCustomPortalTexture()) != null)
                {
                    return ClientProxy.customPortalTextures.get(controller.activeTextureData.getCustomPortalTexture());
                }
            }
            else if (stack != null)
            {
                if (FluidContainerRegistry.isFilledContainer(stack))
                {
                    return FluidContainerRegistry.getFluidForFilledItem(stack).getFluid().getIcon();
                }
                else
                {
                    return Block.blocksList[stack.itemID].getIcon(side, stack.getItemDamage());
                }
            }
        }

        return texture;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        return 14;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return 0;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        TilePortalController controller = ((TilePortal) world.getBlockTileEntity(x, y, z)).getPortalController();
        TileModuleManipulator module = controller == null ? null : controller.blockManager.getModuleManipulator(world);

        if (random.nextInt(100) == 0 && (module == null || !module.hasModule(ItemPortalModule.PortalModules.REMOVE_SOUNDS.getUniqueID())))
        {
            world.playSound(x + 0.5D, y + 0.5D, z + 0.5D, "portal.portal", 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        if (module != null && module.hasModule(ItemPortalModule.PortalModules.REMOVE_PARTICLES.getUniqueID()))
        {
            return;
        }

        for (int l = 0; l < 4; ++l)
        {
            double d0 = x + random.nextFloat();
            double d1 = y + random.nextFloat();
            double d2 = z + random.nextFloat();
            double d3 = 0.0D;
            double d4 = 0.0D;
            double d5 = 0.0D;
            int i1 = random.nextInt(2) * 2 - 1;
            d3 = (random.nextFloat() - 0.5D) * 0.5D;
            d4 = (random.nextFloat() - 0.5D) * 0.5D;
            d5 = (random.nextFloat() - 0.5D) * 0.5D;

            if (metadata == 1)
            {
                d2 = z + 0.5D + 0.25D * i1;
                d5 = random.nextFloat() * 2.0F * i1;
            }
            else if (metadata == 2)
            {
                d0 = x + 0.5D + 0.25D * i1;
                d3 = random.nextFloat() * 2.0F * i1;
            }
            else if (metadata == 3)
            {
                d1 = y + 0.5D + 0.25D * i1;
                d4 = random.nextFloat() * 2.0F * i1;
            }

            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new PortalFX(world, controller, module, d0, d1, d2, d3, d4, d5));
        }
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        TilePortal portal = (TilePortal) blockAccess.getBlockTileEntity(x, y, z);
        TilePortalController controller = portal.getPortalController();
        TileModuleManipulator manip = controller == null ? null : controller.blockManager.getModuleManipulator(controller.worldObj);

        if (controller != null && manip != null)
        {
            if (manip != null && !manip.shouldRenderPortal())
            {
                setBlockBounds(0f, 0f, 0f, 0f, 0f, 0f);
                return;
            }
        }

        int meta = blockAccess.getBlockMetadata(x, y, z);

        if (meta == 1) // X
        {
            setBlockBounds(0f, 0f, 0.375f, 1f, 1f, 0.625f);
        }
        else if (meta == 2) // Z
        {
            setBlockBounds(0.375f, 0f, 0f, 0.625f, 1f, 1f);
        }
        else if (meta == 3) // XZ
        {
            setBlockBounds(0, 0.375f, 0f, 1f, 0.625f, 1f);
        }
        else
        {
            setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f);
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        if (blockAccess.getBlockMaterial(x, y, z) == Material.portal)
        {
            return false;
        }

        return super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        texture = iconRegister.registerIcon("enhancedportals:portal");
    }
}
