package uk.co.shadeddimensions.ep3.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.tileentity.TileScanner;
import uk.co.shadeddimensions.ep3.tileentity.TileScannerFrame;
import uk.co.shadeddimensions.ep3.util.ConnectedTextures;

public class BlockScanner extends BlockEnhancedPortals
{
    static ConnectedTextures connectedTextures;
    static Icon mainTexture;

    public BlockScanner(int id, String name)
    {
        super(id, Material.rock, true);
        setHardness(5);
        setResistance(2000);
        setUnlocalizedName(name);
        setStepSound(soundStoneFootstep);

        connectedTextures = new ConnectedTextures("enhancedportals:scanner/border_%s", id, 1);
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        return meta == 0 ? mainTexture : connectedTextures.getNormalIcon();
    }
    
    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        
        return meta == 0 ? mainTexture : connectedTextures.getIconForFace(blockAccess, x, y, z, side);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        if (metadata == 0)
        {
            return new TileScanner();
        }
        else if (metadata == 1)
        {
            return new TileScannerFrame();
        }

        return null;
    }
    
    @Override
    public void registerIcons(IconRegister register)
    {
        connectedTextures.registerIcons(register);
        mainTexture = register.registerIcon("enhancedportals:scanner/main");
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(blockID, 1, world.getBlockMetadata(x, y, z));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(int par1, CreativeTabs creativeTab, List list)
    {
        for (int i = 0; i < 2; i++)
        {
            list.add(new ItemStack(blockID, 1, i));
        }
    }
    
    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }
}
