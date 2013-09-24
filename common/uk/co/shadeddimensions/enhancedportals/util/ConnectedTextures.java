package uk.co.shadeddimensions.enhancedportals.util;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class ConnectedTextures
{
    Icon[] textures;
    String loc;
    int blockID, meta;

    /***
     * @param location
     *            Must contain %s where the texture number (0-16) is.
     * @param blockId
     *            Block ID
     * @param metadata
     *            Use -1 for it to look for any metadata, use 0-16 to be specific.
     */
    public ConnectedTextures(String location, int blockId, int metadata)
    {
        textures = new Icon[16];
        loc = location;
        blockID = blockId;
        meta = metadata;
    }

    private boolean canConnectTo(int id, int m)
    {
        return id == blockID && meta == m;
    }

    private int[] getBlockIDs(IBlockAccess blockAccess, int x, int y, int z)
    {
        int[] blockIds = new int[6];

        for (int i = 0; i < blockIds.length; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            blockIds[i] = blockAccess.getBlockId(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
        }

        return blockIds;
    }

    private int[] getBlockMetas(IBlockAccess blockAccess, int x, int y, int z)
    {
        int[] blockMetas = new int[6];

        for (int i = 0; i < blockMetas.length; i++)
        {
            if (meta == -1)
            {
                blockMetas[i] = -1;
            }
            else
            {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                blockMetas[i] = blockAccess.getBlockMetadata(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
            }
        }

        return blockMetas;
    }

    private Icon getDownIcon(int[] blockIds, int[] blockMetas)
    {
        boolean up = false, down = false, left = false, right = false;

        if (canConnectTo(blockIds[3], blockMetas[3]))
        {
            down = true;
        }

        if (canConnectTo(blockIds[2], blockMetas[2]))
        {
            up = true;
        }

        if (canConnectTo(blockIds[4], blockMetas[4]))
        {
            left = true;
        }

        if (canConnectTo(blockIds[5], blockMetas[5]))
        {
            right = true;
        }

        if (up && down && left && right)
        {
            return textures[1]; // Centre
        }
        else if (up && down && left)
        {
            return textures[10]; // Right Line
        }
        else if (up && down && right)
        {
            return textures[9]; // Left Line
        }
        else if (up && left && right)
        {
            return textures[7]; // Bottom Line
        }
        else if (down && left && right)
        {
            return textures[6]; // Top Line
        }
        else if (down && up)
        {
            return textures[11]; // Right & Left Line
        }
        else if (left && right)
        {
            return textures[8]; // Up & Down Line
        }
        else if (down && left)
        {
            return textures[2]; // Top Right Corner
        }
        else if (down && right)
        {
            return textures[3]; // Top Left Corner
        }
        else if (up && left)
        {
            return textures[4]; // Bottom Right Corner
        }
        else if (up && right)
        {
            return textures[5]; // Bottom Left Corner
        }
        else if (down)
        {
            return textures[13]; // Top cap
        }
        else if (up)
        {
            return textures[15]; // Bottom cap
        }
        else if (left)
        {
            return textures[14]; // Right cap
        }
        else if (right)
        {
            return textures[12]; // Left cap
        }

        return textures[0];
    }

    private Icon getEastWestIcon(int[] blockIds, int[] blockMetas, int side)
    {
        boolean up = false, down = false, left = false, right = false;

        if (canConnectTo(blockIds[0], blockMetas[0]))
        {
            down = true;
        }

        if (canConnectTo(blockIds[1], blockMetas[1]))
        {
            up = true;
        }

        if (side == 4 && canConnectTo(blockIds[2], blockMetas[2]) || side == 5 && canConnectTo(blockIds[3], blockMetas[3]))
        {
            left = true;
        }

        if (side == 4 && canConnectTo(blockIds[3], blockMetas[3]) || side == 5 && canConnectTo(blockIds[2], blockMetas[2]))
        {
            right = true;
        }

        if (up && down && left && right)
        {
            return textures[1];
        }
        else if (up && down && left)
        {
            return textures[10];
        }
        else if (up && down && right)
        {
            return textures[9];
        }
        else if (up && left && right)
        {
            return textures[7];
        }
        else if (down && left && right)
        {
            return textures[6];
        }
        else if (down && up)
        {
            return textures[11];
        }
        else if (left && right)
        {
            return textures[8];
        }
        else if (down && left)
        {
            return textures[2];
        }
        else if (down && right)
        {
            return textures[3];
        }
        else if (up && left)
        {
            return textures[4];
        }
        else if (up && right)
        {
            return textures[5];
        }
        else if (down)
        {
            return textures[13];
        }
        else if (up)
        {
            return textures[15];
        }
        else if (left)
        {
            return textures[14];
        }
        else if (right)
        {
            return textures[12];
        }

        return textures[0];
    }

    public Icon getIconForFace(IBlockAccess blockAccess, int x, int y, int z, int face)
    {
        int[] blockIds = getBlockIDs(blockAccess, x, y, z);
        int[] blockMetas = getBlockMetas(blockAccess, x, y, z);

        switch (face)
        {
            default:
                return textures[0];

            case 0:
                return getDownIcon(blockIds, blockMetas);

            case 1:
                return getUpIcon(blockIds, blockMetas);

            case 2:
            case 3:
                return getNorthSouthIcon(blockIds, blockMetas, face);

            case 4:
            case 5:
                return getEastWestIcon(blockIds, blockMetas, face);

        }
    }

    public Icon getNormalIcon()
    {
        return textures[0];
    }

    private Icon getNorthSouthIcon(int[] blockIds, int[] blockMetas, int side)
    {
        boolean up = false, down = false, left = false, right = false;

        if (canConnectTo(blockIds[0], blockMetas[0]))
        {
            down = true;
        }

        if (canConnectTo(blockIds[1], blockMetas[1]))
        {
            up = true;
        }

        if (side == 2 && canConnectTo(blockIds[4], blockMetas[4]) || side == 3 && canConnectTo(blockIds[5], blockMetas[5]))
        {
            left = true;
        }

        if (side == 2 && canConnectTo(blockIds[5], blockMetas[5]) || side == 3 && canConnectTo(blockIds[4], blockMetas[4]))
        {
            right = true;
        }

        if (up && down && left && right)
        {
            return textures[1];
        }
        else if (up && down && left)
        {
            return textures[9];
        }
        else if (up && down && right)
        {
            return textures[10];
        }
        else if (up && left && right)
        {
            return textures[7];
        }
        else if (down && left && right)
        {
            return textures[6];
        }
        else if (down && up)
        {
            return textures[11];
        }
        else if (left && right)
        {
            return textures[8];
        }
        else if (down && left)
        {
            return textures[3];
        }
        else if (down && right)
        {
            return textures[2];
        }
        else if (up && left)
        {
            return textures[5];
        }
        else if (up && right)
        {
            return textures[4];
        }
        else if (down)
        {
            return textures[13];
        }
        else if (up)
        {
            return textures[15];
        }
        else if (left)
        {
            return textures[12];
        }
        else if (right)
        {
            return textures[14];
        }

        return textures[0];
    }

    private Icon getUpIcon(int[] blockIds, int[] blockMetas)
    {
        boolean up = false, down = false, left = false, right = false;

        if (canConnectTo(blockIds[4], blockMetas[4]))
        {
            down = true;
        }

        if (canConnectTo(blockIds[5], blockMetas[5]))
        {
            up = true;
        }

        if (canConnectTo(blockIds[2], blockMetas[2]))
        {
            left = true;
        }

        if (canConnectTo(blockIds[3], blockMetas[3]))
        {
            right = true;
        }

        if (up && down && left && right)
        {
            return textures[1];
        }
        else if (up && down && left)
        {
            return textures[7];
        }
        else if (up && down && right)
        {
            return textures[6];
        }
        else if (up && left && right)
        {
            return textures[9];
        }
        else if (down && left && right)
        {
            return textures[10];
        }
        else if (down && up)
        {
            return textures[8];
        }
        else if (left && right)
        {
            return textures[11];
        }
        else if (down && left)
        {
            return textures[4];
        }
        else if (down && right)
        {
            return textures[2];
        }
        else if (up && left)
        {
            return textures[5];
        }
        else if (up && right)
        {
            return textures[3];
        }
        else if (down)
        {
            return textures[14];
        }
        else if (up)
        {
            return textures[12];
        }
        else if (left)
        {
            return textures[15];
        }
        else if (right)
        {
            return textures[13];
        }

        return textures[0];
    }

    public void registerIcons(IconRegister register)
    {
        for (int i = 0; i < textures.length; i++)
        {
            textures[i] = register.registerIcon(String.format(loc, i));
        }
    }
}
