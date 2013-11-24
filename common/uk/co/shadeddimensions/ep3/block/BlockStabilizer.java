package uk.co.shadeddimensions.ep3.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
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
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
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
    public Icon getIcon(int par1, int par2)
    {
        return connectedTextures.getNormalIcon();
    }

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return connectedTextures.getIconForFace(blockAccess, x, y, z, side);
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        connectedTextures.registerIcons(iconRegister);
    }
}
