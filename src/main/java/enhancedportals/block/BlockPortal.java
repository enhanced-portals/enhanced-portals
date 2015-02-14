package enhancedportals.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import enhancedportals.tile.TileFrameController;
import enhancedportals.tile.TileFramePortalManipulator;
import enhancedportals.tile.TilePortal;

public class BlockPortal extends BlockContainer
{
    IIcon texture;

    public BlockPortal(String n)
    {
        super(Material.portal);
        setBlockUnbreakable();
        setResistance(2000);
        setBlockName(n);
        setLightOpacity(0);
        setStepSound(soundTypeGlass);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TilePortal();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int m)
    {
        return super.getIcon(blockAccess, x, y, z, m);
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        return texture;
    }

    @Override
    public Item getItem(World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public int getLightValue()
    {
        return 14;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        return 0;
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        texture = register.registerIcon("enhancedportals:portal");
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        TileEntity tile = blockAccess.getTileEntity(x, y, z);

        if (tile instanceof TilePortal)
        {
            /*
             * TilePortal portal = (TilePortal) tile; TileFrameController
             * controller = portal.getPortalController();
             * TileFramePortalManipulator manip = controller == null ? null :
             * controller.getModuleManipulator();
             * 
             * if (controller != null && manip != null &&
             * manip.isPortalInvisible()) { setBlockBounds(0f, 0f, 0f, 0f, 0f,
             * 0f); return; }
             */

            int meta = blockAccess.getBlockMetadata(x, y, z);
            
            if (meta == 0) // Not set for a reason
            {
                setBlockBounds(0f, 0f, 0f, 1f, 1, 1f);
            }
            else if (meta == 1) // X
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
            else if (meta >= 4) // (X / Z / XZ) invis
            {
                setBlockBounds(0, 0, 0, 0, 0, 0);
            }
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        if (blockAccess.getBlock(x, y, z) == this /* || blockAccess.getBlock(x, y, z) == BlockFrame.instance */ || blockAccess.getBlockMetadata(x, y, z) >= 4) { return false; }
        return super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }
}
