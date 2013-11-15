package uk.co.shadeddimensions.ep3.tileentity;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class TileScanner extends TileEnhancedPortals
{
    boolean isActive = false;

    @Override
    public boolean activate(EntityPlayer player)
    {
        if (worldObj.isRemote)
        {
            return false;
        }
        
        ItemStack item = player.inventory.getCurrentItem();

        if (item != null && item.itemID == CommonProxy.itemWrench.itemID)
        {
            if (!isActive)
            {
                if (!checkStructure(true, true))
                {
                    if (!checkStructure(false, true))
                    {
                        return false;
                    }
                }

                CommonProxy.openGui(player, GUIs.Scanner, this);
                return true;
            }
        }

        return false;
    }

    boolean checkStructure(boolean small, boolean updateFrameBlocks)
    {
        ArrayList<ChunkCoordinates> frameBlocks = new ArrayList<ChunkCoordinates>();
        int size = small ? 5 : 7;

        WorldCoordinates northWestCorner = getWorldCoordinates();
        northWestCorner.posX -= small ? 2 : 3;
        northWestCorner.posZ -= small ? 2 : 3;
        northWestCorner.posY++;

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (i == 0 || i == size - 1)
                {
                    // do all
                    if (worldObj.getBlockId(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j) == CommonProxy.blockScanner.blockID && worldObj.getBlockMetadata(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j) == 1)
                    {
                        frameBlocks.add(new ChunkCoordinates(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j));
                    }
                    else
                    {
                        return false;
                    }
                }
                else 
                {
                    // do first one and last one
                    if (j == 0 || j == size -1)
                    {
                        if (worldObj.getBlockId(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j) == CommonProxy.blockScanner.blockID && worldObj.getBlockMetadata(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j) == 1)
                        {
                            frameBlocks.add(new ChunkCoordinates(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j));
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        if (!worldObj.isAirBlock(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j))
                        {
                            return false;
                        }
                    }
                }

            }
        }

        northWestCorner.posX++;
        northWestCorner.posZ++;
        northWestCorner.posY--;

        for (int i = 0; i < size - 2; i++)
        {
            for (int j = 0; j < size - 2; j++)
            {
                if (i == (small ? 1 : 2) && j == (small ? 1 : 2))
                {
                    // skip the controller
                }
                else
                {
                    if (worldObj.getBlockId(northWestCorner.posX + i, northWestCorner.posY, northWestCorner.posZ + j) != Block.blockIron.blockID)
                    {
                        return false;
                    }
                }
            }
        }

        if (updateFrameBlocks)
        {
            for (ChunkCoordinates c : frameBlocks)
            {
                TileScannerFrame frame = (TileScannerFrame) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
                frame.scanner = getWorldCoordinates();
            }
        }
        
        System.out.println("SUCCESS");
        return true;
    }
}
