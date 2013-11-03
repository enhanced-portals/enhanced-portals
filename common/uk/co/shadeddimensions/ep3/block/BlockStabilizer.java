package uk.co.shadeddimensions.ep3.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
import uk.co.shadeddimensions.ep3.util.ConnectedTextures;

public class BlockStabilizer extends BlockEnhancedPortals
{
    static ConnectedTextures connectedTextures;
    
    public BlockStabilizer(int id, String name)
    {
        super(id, Material.rock, true);
        setHardness(5);
        setResistance(2000);
        setUnlocalizedName(name);
        setStepSound(soundStoneFootstep);
        connectedTextures = new ConnectedTextures("enhancedportals:bridge/bridge_%s", id, -1);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileStabilizer();
    }
    
    @Override
    public Icon getIcon(int par1, int par2)
    {
        return connectedTextures.getNormalIcon();
    }
    
    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileStabilizer tile = (TileStabilizer) blockAccess.getBlockTileEntity(x, y, z);        
        return tile != null && tile.hasConfigured ? connectedTextures.getIconForFace(blockAccess, x, y, z, side) : connectedTextures.getNormalIcon();
    }
    
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        connectedTextures.registerIcons(iconRegister);
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return blockAccess.getBlockId(x, y, z) == blockID ? false : super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
        
    public int getRenderBlockPass()
    {
        return 1;
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
}
