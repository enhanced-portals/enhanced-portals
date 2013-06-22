package enhancedportals.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockInterface
{
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

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
