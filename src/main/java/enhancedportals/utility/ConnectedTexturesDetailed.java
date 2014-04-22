package enhancedportals.utility;

import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class ConnectedTexturesDetailed extends ConnectedTextures
{
    private static final short[] connectionToIndex = { 0, 36, 12, 24, 1, 37, 13, 25, 3, 39, 15, 27, 2, 38, 14, 26 }, transformIndex26 = { 26, 45, 44, 22, 33, 23, 35, 9, 32, 34, 10, 21, 11, 8, 20, 46 };

    protected ConnectedTexturesDetailed()
    {
        super();
    }

    public ConnectedTexturesDetailed(String textureLocation, int block, int meta)
    {
        super(textureLocation, block, meta);
        textures = new Icon[47];
    }

    public ConnectedTexturesDetailed(String textureLocation, int block, int meta, int meta2)
    {
        super(textureLocation, block, meta, meta2);
        textures = new Icon[47];
    }

    @Override
    public ConnectedTexturesDetailed copy(int id, int meta)
    {
        ConnectedTexturesDetailed ct = new ConnectedTexturesDetailed();
        ct.textures = textures;
        ct.blockID = id;
        ct.blockMeta = meta;

        return ct;
    }

    @Override
    public ConnectedTexturesDetailed copy(int id, int meta, int meta2)
    {
        ConnectedTexturesDetailed ct = new ConnectedTexturesDetailed();
        ct.textures = textures;
        ct.blockID = id;
        ct.blockMeta = meta;
        ct.subMeta = meta2;

        return ct;
    }

    @Override
    public Icon getIconForSide(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        boolean[] connectingBlock = new boolean[4], diagonal = new boolean[4];
        int index = 0;

        if (side == 0 || side == 1)
        {
            connectingBlock[0] = canConnectTo(blockAccess, x - 1, y, z);
            connectingBlock[1] = canConnectTo(blockAccess, x + 1, y, z);
            connectingBlock[2] = canConnectTo(blockAccess, x, y, z + 1);
            connectingBlock[3] = canConnectTo(blockAccess, x, y, z - 1);
        }
        else if (side == 2)
        {
            connectingBlock[0] = canConnectTo(blockAccess, x + 1, y, z);
            connectingBlock[1] = canConnectTo(blockAccess, x - 1, y, z);
            connectingBlock[2] = canConnectTo(blockAccess, x, y - 1, z);
            connectingBlock[3] = canConnectTo(blockAccess, x, y + 1, z);
        }
        else if (side == 3)
        {
            connectingBlock[0] = canConnectTo(blockAccess, x - 1, y, z);
            connectingBlock[1] = canConnectTo(blockAccess, x + 1, y, z);
            connectingBlock[2] = canConnectTo(blockAccess, x, y - 1, z);
            connectingBlock[3] = canConnectTo(blockAccess, x, y + 1, z);
        }
        else if (side == 4)
        {
            connectingBlock[0] = canConnectTo(blockAccess, x, y, z - 1);
            connectingBlock[1] = canConnectTo(blockAccess, x, y, z + 1);
            connectingBlock[2] = canConnectTo(blockAccess, x, y - 1, z);
            connectingBlock[3] = canConnectTo(blockAccess, x, y + 1, z);
        }
        else
        {
            connectingBlock[0] = canConnectTo(blockAccess, x, y, z + 1);
            connectingBlock[1] = canConnectTo(blockAccess, x, y, z - 1);
            connectingBlock[2] = canConnectTo(blockAccess, x, y - 1, z);
            connectingBlock[3] = canConnectTo(blockAccess, x, y + 1, z);
        }

        index = connectionToIndex[(connectingBlock[0] ? 8 : 0) | (connectingBlock[1] ? 4 : 0) | (connectingBlock[2] ? 2 : 0) | (connectingBlock[3] ? 1 : 0)];

        if (side == 0 || side == 1)
        {
            diagonal[0] = !canConnectTo(blockAccess, x + 1, y, z + 1);
            diagonal[1] = !canConnectTo(blockAccess, x - 1, y, z + 1);
            diagonal[2] = !canConnectTo(blockAccess, x + 1, y, z - 1);
            diagonal[3] = !canConnectTo(blockAccess, x - 1, y, z - 1);
        }
        else if (side == 2)
        {
            diagonal[0] = !canConnectTo(blockAccess, x - 1, y - 1, z);
            diagonal[1] = !canConnectTo(blockAccess, x + 1, y - 1, z);
            diagonal[2] = !canConnectTo(blockAccess, x - 1, y + 1, z);
            diagonal[3] = !canConnectTo(blockAccess, x + 1, y + 1, z);
        }
        else if (side == 3)
        {
            diagonal[0] = !canConnectTo(blockAccess, x + 1, y - 1, z);
            diagonal[1] = !canConnectTo(blockAccess, x - 1, y - 1, z);
            diagonal[2] = !canConnectTo(blockAccess, x + 1, y + 1, z);
            diagonal[3] = !canConnectTo(blockAccess, x - 1, y + 1, z);
        }
        else if (side == 4)
        {
            diagonal[0] = !canConnectTo(blockAccess, x, y - 1, z + 1);
            diagonal[1] = !canConnectTo(blockAccess, x, y - 1, z - 1);
            diagonal[2] = !canConnectTo(blockAccess, x, y + 1, z + 1);
            diagonal[3] = !canConnectTo(blockAccess, x, y + 1, z - 1);
        }
        else
        {
            diagonal[0] = !canConnectTo(blockAccess, x, y - 1, z - 1);
            diagonal[1] = !canConnectTo(blockAccess, x, y - 1, z + 1);
            diagonal[2] = !canConnectTo(blockAccess, x, y + 1, z - 1);
            diagonal[3] = !canConnectTo(blockAccess, x, y + 1, z + 1);
        }

        if (index == 13 && diagonal[0])
        {
            index = 4;
        }
        else if (index == 15 && diagonal[1])
        {
            index = 5;
        }
        else if (index == 37 && diagonal[2])
        {
            index = 16;
        }
        else if (index == 39 && diagonal[3])
        {
            index = 17;
        }
        else if (index == 14 && diagonal[0] && diagonal[1])
        {
            index = 7;
        }
        else if (index == 25 && diagonal[0] && diagonal[2])
        {
            index = 6;
        }
        else if (index == 27 && diagonal[3] && diagonal[1])
        {
            index = 19;
        }
        else if (index == 38 && diagonal[3] && diagonal[2])
        {
            index = 18;
        }
        else if (index == 14 && !diagonal[0] && diagonal[1])
        {
            index = 31;
        }
        else if (index == 25 && diagonal[0] && !diagonal[2])
        {
            index = 30;
        }
        else if (index == 27 && !diagonal[3] && diagonal[1])
        {
            index = 41;
        }
        else if (index == 38 && diagonal[3] && !diagonal[2])
        {
            index = 40;
        }
        else if (index == 14 && diagonal[0] && !diagonal[1])
        {
            index = 29;
        }
        else if (index == 25 && !diagonal[0] && diagonal[2])
        {
            index = 28;
        }
        else if (index == 27 && diagonal[3] && !diagonal[1])
        {
            index = 43;
        }
        else if (index == 38 && !diagonal[3] && diagonal[2])
        {
            index = 42;
        }
        else if (index == 26)
        {
            index = transformIndex26[(diagonal[0] ? 8 : 0) | (diagonal[1] ? 4 : 0) | (diagonal[2] ? 2 : 0) | (diagonal[3] ? 1 : 0)];
        }

        return textures[index];
    }
}
