package uk.co.shadeddimensions.enhancedportals.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;

public class BlockEP extends BlockContainer
{
    public Icon[] textures = new Icon[16];
    protected boolean connectedTexture;

    public BlockEP(int par1, Material par2Material, boolean tab)
    {
        super(par1, par2Material);

        if (tab)
        {
            setCreativeTab(EnhancedPortals.creativeTab);
        }

        connectedTexture = false;
    }

    protected void setConnectedTexture()
    {
        connectedTexture = true;
    }

    protected boolean canConnectTo(int id)
    {
        return id == blockID;
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        return textures[0];
    }

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        if (connectedTexture)
        {
            boolean up = false, down = false, left = false, right = false;
            int[] blockIds = new int[6];

            for (int i = 0; i < blockIds.length; i++)
            {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                blockIds[i] = blockAccess.getBlockId(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
            }

            if (side == 0)
            {
                if (canConnectTo(blockIds[3]))
                {
                    down = true;
                }

                if (canConnectTo(blockIds[2]))
                {
                    up = true;
                }

                if (canConnectTo(blockIds[4]))
                {
                    left = true;
                }

                if (canConnectTo(blockIds[5]))
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
            }
            else if (side == 1)
            {
                if (canConnectTo(blockIds[4]))
                {
                    down = true;
                }

                if (canConnectTo(blockIds[5]))
                {
                    up = true;
                }

                if (canConnectTo(blockIds[2]))
                {
                    left = true;
                }

                if (canConnectTo(blockIds[3]))
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
            }
            else if (side == 2 || side == 3)
            {
                if (canConnectTo(blockIds[0]))
                {
                    down = true;
                }

                if (canConnectTo(blockIds[1]))
                {
                    up = true;
                }

                if (side == 2 && canConnectTo(blockIds[4]) || side == 3 && canConnectTo(blockIds[5]))
                {
                    left = true;
                }

                if (side == 2 && canConnectTo(blockIds[5]) || side == 3 && canConnectTo(blockIds[4]))
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
            }
            else if (side == 4 || side == 5)
            {
                if (canConnectTo(blockIds[0]))
                {
                    down = true;
                }

                if (canConnectTo(blockIds[1]))
                {
                    up = true;
                }

                if (side == 4 && canConnectTo(blockIds[2]) || side == 5 && canConnectTo(blockIds[3]))
                {
                    left = true;
                }

                if (side == 4 && canConnectTo(blockIds[3]) || side == 5 && canConnectTo(blockIds[2]))
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
            }
        }

        return textures[0];
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

}
