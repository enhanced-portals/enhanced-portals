package uk.co.shadeddimensions.library.ct;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

/***
 * A basic Connected Texture implementation. Does not contain inner corners.
 * 
 * @author Alz454
 */
public class ConnectedTextures
{
    protected Icon[] textures;
    protected int blockID, blockMeta, subMeta = -1;
    protected String textureLoc;

    protected ConnectedTextures()
    {

    }

    public ConnectedTextures(String textureLocation, int block, int meta)
    {
        textureLoc = textureLocation;
        blockID = block;
        blockMeta = meta;
        textures = new Icon[16];
    }

    public ConnectedTextures(String textureLocation, int block, int meta, int meta2)
    {
        textureLoc = textureLocation;
        blockID = block;
        blockMeta = meta;
        subMeta = meta2;
        textures = new Icon[16];
    }

    protected boolean canConnectTo(IBlockAccess blockAccess, int x, int y, int z)
    {
        if (blockID == blockAccess.getBlockId(x, y, z))
        {
            int meta = blockAccess.getBlockMetadata(x, y, z);

            if (blockMeta == -1)
            {
                return true;
            }
            else if (subMeta == -1)
            {
                return blockMeta == meta;
            }
            else
            {
                return blockMeta == meta || meta == subMeta;
            }
        }

        return false;
    }

    public ConnectedTextures copy(int id, int meta)
    {
        ConnectedTextures ct = new ConnectedTextures();
        ct.textures = textures;
        ct.blockID = id;
        ct.blockMeta = meta;

        return ct;
    }

    public ConnectedTextures copy(int id, int meta, int meta2)
    {
        ConnectedTextures ct = new ConnectedTextures();
        ct.textures = textures;
        ct.blockID = id;
        ct.blockMeta = meta;
        ct.subMeta = meta2;

        return ct;
    }

    public Icon getBaseIcon()
    {
        return textures[0];
    }

    public Icon getIconForSide(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        boolean[] connectingBlock = new boolean[6];
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

        if ((connectingBlock[0] & !connectingBlock[1] & !connectingBlock[2] & !connectingBlock[3]) != false)
        {
            index = 14;
        }
        else if ((!connectingBlock[0] & connectingBlock[1] & !connectingBlock[2] & !connectingBlock[3]) != false)
        {
            index = 12;
        }
        else if ((!connectingBlock[0] & !connectingBlock[1] & connectingBlock[2] & !connectingBlock[3]) != false)
        {
            index = 13;
        }
        else if ((!connectingBlock[0] & !connectingBlock[1] & !connectingBlock[2] & connectingBlock[3]) != false)
        {
            index = 15;
        }
        else if ((connectingBlock[0] & connectingBlock[1] & !connectingBlock[2] & !connectingBlock[3]) != false)
        {
            index = 8;
        }
        else if ((!connectingBlock[0] & !connectingBlock[1] & connectingBlock[2] & connectingBlock[3]) != false)
        {
            index = 11;
        }
        else if ((connectingBlock[0] & !connectingBlock[1] & connectingBlock[2] & !connectingBlock[3]) != false)
        {
            index = 2;
        }
        else if ((connectingBlock[0] & !connectingBlock[1] & !connectingBlock[2] & connectingBlock[3]) != false)
        {
            index = 4;
        }
        else if ((!connectingBlock[0] & connectingBlock[1] & connectingBlock[2] & !connectingBlock[3]) != false)
        {
            index = 3;
        }
        else if ((!connectingBlock[0] & connectingBlock[1] & !connectingBlock[2] & connectingBlock[3]) != false)
        {
            index = 5;
        }
        else if ((!connectingBlock[0] & connectingBlock[1] & connectingBlock[2] & connectingBlock[3]) != false)
        {
            index = 9;
        }
        else if ((connectingBlock[0] & !connectingBlock[1] & connectingBlock[2] & connectingBlock[3]) != false)
        {
            index = 10;
        }
        else if ((connectingBlock[0] & connectingBlock[1] & !connectingBlock[2] & connectingBlock[3]) != false)
        {
            index = 7;
        }
        else if ((connectingBlock[0] & connectingBlock[1] & connectingBlock[2] & !connectingBlock[3]) != false)
        {
            index = 6;
        }
        else if ((connectingBlock[0] & connectingBlock[1] & connectingBlock[2] & connectingBlock[3]) != false)
        {
            index = 1;
        }

        return textures[index];
    }

    public void registerIcons(IconRegister register)
    {
        for (int i = 0; i < textures.length; i++)
        {
            textures[i] = register.registerIcon(String.format(textureLoc, i));
        }
    }
}
