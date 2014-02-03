package uk.co.shadeddimensions.library.ct;

import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

/**
 * A more detailed version of {@link ConnectedTextures}. Contains inner corners.
 * 
 * @author Alz454
 */
public class ConnectedTexturesDetailed extends ConnectedTextures
{
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
        boolean[] connectingBlock = new boolean[6], diagonal = new boolean[6];
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

        if (connectingBlock[0] & !connectingBlock[1] & !connectingBlock[2] & !connectingBlock[3])
        {
            index = 3;
        }
        else if (!connectingBlock[0] & connectingBlock[1] & !connectingBlock[2] & !connectingBlock[3])
        {
            index = 1;
        }
        else if (!connectingBlock[0] & !connectingBlock[1] & connectingBlock[2] & !connectingBlock[3])
        {
            index = 12;
        }
        else if (!connectingBlock[0] & !connectingBlock[1] & !connectingBlock[2] & connectingBlock[3])
        {
            index = 36;
        }
        else if (connectingBlock[0] & connectingBlock[1] & !connectingBlock[2] & !connectingBlock[3])
        {
            index = 2;
        }
        else if (!connectingBlock[0] & !connectingBlock[1] & connectingBlock[2] & connectingBlock[3])
        {
            index = 24;
        }
        else if (connectingBlock[0] & !connectingBlock[1] & connectingBlock[2] & !connectingBlock[3])
        {
            index = 15;
        }
        else if (connectingBlock[0] & !connectingBlock[1] & !connectingBlock[2] & connectingBlock[3])
        {
            index = 39;
        }
        else if (!connectingBlock[0] & connectingBlock[1] & connectingBlock[2] & !connectingBlock[3])
        {
            index = 13;
        }
        else if (!connectingBlock[0] & connectingBlock[1] & !connectingBlock[2] & connectingBlock[3])
        {
            index = 37;
        }
        else if (!connectingBlock[0] & connectingBlock[1] & connectingBlock[2] & connectingBlock[3])
        {
            index = 25;
        }
        else if (connectingBlock[0] & !connectingBlock[1] & connectingBlock[2] & connectingBlock[3])
        {
            index = 27;
        }
        else if (connectingBlock[0] & connectingBlock[1] & !connectingBlock[2] & connectingBlock[3])
        {
            index = 38;
        }
        else if (connectingBlock[0] & connectingBlock[1] & connectingBlock[2] & !connectingBlock[3])
        {
            index = 14;
        }
        else if (connectingBlock[0] & connectingBlock[1] & connectingBlock[2] & connectingBlock[3])
        {
            index = 26;
        }

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
        else if (index == 26 && diagonal[0] && diagonal[1] && diagonal[2] && diagonal[3])
        {
            index = 46;
        }
        else if (index == 26 && !diagonal[0] && diagonal[1] && diagonal[2] && diagonal[3])
        {
            index = 9;
        }
        else if (index == 26 && diagonal[0] && !diagonal[1] && diagonal[2] && diagonal[3])
        {
            index = 21;
        }
        else if (index == 26 && diagonal[0] && diagonal[1] && !diagonal[2] && diagonal[3])
        {
            index = 8;
        }
        else if (index == 26 && diagonal[0] && diagonal[1] && diagonal[2] && !diagonal[3])
        {
            index = 20;
        }
        else if (index == 26 && diagonal[0] && diagonal[1] && !diagonal[2] && !diagonal[3])
        {
            index = 11;
        }
        else if (index == 26 && !diagonal[0] && !diagonal[1] && diagonal[2] && diagonal[3])
        {
            index = 22;
        }
        else if (index == 26 && !diagonal[0] && diagonal[1] && !diagonal[2] && diagonal[3])
        {
            index = 23;
        }
        else if (index == 26 && diagonal[0] && !diagonal[1] && diagonal[2] && !diagonal[3])
        {
            index = 10;
        }
        else if (index == 26 && diagonal[0] && !diagonal[1] && !diagonal[2] && diagonal[3])
        {
            index = 34;
        }
        else if (index == 26 && !diagonal[0] && diagonal[1] && diagonal[2] && !diagonal[3])
        {
            index = 35;
        }
        else if (index == 26 && diagonal[0] && !diagonal[1] && !diagonal[2] && !diagonal[3])
        {
            index = 32;
        }
        else if (index == 26 && !diagonal[0] && diagonal[1] && !diagonal[2] && !diagonal[3])
        {
            index = 33;
        }
        else if (index == 26 && !diagonal[0] && !diagonal[1] && diagonal[2] && !diagonal[3])
        {
            index = 44;
        }
        else if (index == 26 && !diagonal[0] && !diagonal[1] && !diagonal[2] && diagonal[3])
        {
            index = 45;
        }

        return textures[index];
    }
}
