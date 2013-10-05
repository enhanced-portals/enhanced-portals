package uk.co.shadeddimensions.ep3.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockInterface
{
    public double minX, minY, minZ;
    public double maxX, maxY, maxZ;

    public Block baseBlock = Block.waterStill;
    public Icon texture = null;

    public float getBlockBrightness(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return baseBlock.getBlockBrightness(iblockaccess, i, j, k);
    }

    public Icon getBlockTextureFromSide(int i)
    {
        if (texture == null)
        {
            return baseBlock.getBlockTextureFromSide(i);
        }
        else
        {
            return texture;
        }
    }
}
