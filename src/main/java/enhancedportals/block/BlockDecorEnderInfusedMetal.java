package enhancedportals.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import enhancedportals.utility.ConnectedTextures;

public class BlockDecorEnderInfusedMetal extends BlockDecoration
{
    public static BlockDecorEnderInfusedMetal instance;
    static ConnectedTextures connectedTextures;

    public BlockDecorEnderInfusedMetal(String n)
    {
        super(n);
        instance = this;
        connectedTextures = BlockStabilizer.connectedTextures.copy(this, -1);
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int f)
    {
        return connectedTextures.getIconForSide(blockAccess, x, y, z, f);
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        return connectedTextures.getBaseIcon();
    }

    @Override
    public void registerBlockIcons(IIconRegister iir)
    {

    }
}
