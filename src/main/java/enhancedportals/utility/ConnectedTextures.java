package enhancedportals.utility;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class ConnectedTextures
{
    protected Icon[] textures;
    protected int blockID, blockMeta, subMeta = -1;
    protected String textureLoc;
    private static final short[] connectionToIndex = { 0, 15, 13, 11, 12, 5, 3, 9, 14, 4, 2, 10, 8, 7, 6, 1 };

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
            if (blockMeta == -1)
            {
                return true;
            }

            int meta = blockAccess.getBlockMetadata(x, y, z);

            if (blockMeta == meta)
            {
                return true;
            }
            else
            // check if subMeta is valid, if not return false
            {
                return subMeta == -1 ? false : meta == subMeta;
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
        boolean[] connectingBlock = new boolean[4];
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

        return textures[connectionToIndex[(connectingBlock[0] ? 8 : 0) | (connectingBlock[1] ? 4 : 0) | (connectingBlock[2] ? 2 : 0) | (connectingBlock[3] ? 1 : 0)]];
    }

    public void registerIcons(IconRegister register)
    {
        for (int i = 0; i < textures.length; i++)
        {
            textures[i] = register.registerIcon(String.format(textureLoc, i));
        }
    }
}
