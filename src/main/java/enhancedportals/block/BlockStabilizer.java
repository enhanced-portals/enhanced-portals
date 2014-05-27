package enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import enhancedportals.EnhancedPortals;
import enhancedportals.tileentity.TileStabilizer;
import enhancedportals.tileentity.TileStabilizerMain;
import enhancedportals.utility.ConnectedTexturesDetailed;

public class BlockStabilizer extends BlockContainer
{
    public static BlockStabilizer instance;
    public static ConnectedTexturesDetailed connectedTextures;

    public BlockStabilizer(String n)
    {
        super(Material.rock);
        instance = this;
        setHardness(5);
        setResistance(2000);
        setBlockName(n);
        setStepSound(soundTypeStone);
        setCreativeTab(EnhancedPortals.creativeTab);
        connectedTextures = new ConnectedTexturesDetailed("enhancedportals:bridge/%s", this, -1);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block b, int newID)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileStabilizer)
        {
            ((TileStabilizer) tile).breakBlock(b, newID);
        }
        else if (tile instanceof TileStabilizerMain)
        {
            ((TileStabilizerMain) tile).breakBlock(b, newID);
        }

        super.breakBlock(world, x, y, z, b, newID);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        if (metadata == 0)
        {
            return new TileStabilizer();
        }
        else if (metadata == 1)
        {
            return new TileStabilizerMain();
        }

        return null;
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntity tile = blockAccess.getTileEntity(x, y, z);

        if (tile instanceof TileStabilizer)
        {
            return ((TileStabilizer) tile).isFormed ? connectedTextures.getIconForSide(blockAccess, x, y, z, side) : connectedTextures.getBaseIcon();
        }

        return connectedTextures.getIconForSide(blockAccess, x, y, z, side);
    }

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        return connectedTextures.getBaseIcon();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileStabilizer)
        {
            return ((TileStabilizer) tile).activate(player);
        }
        else if (tile instanceof TileStabilizerMain)
        {
            return ((TileStabilizerMain) tile).activate(player);
        }

        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        connectedTextures.registerIcons(iconRegister);
    }
}
