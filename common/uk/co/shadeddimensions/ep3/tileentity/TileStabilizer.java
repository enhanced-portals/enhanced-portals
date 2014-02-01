package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;
import uk.co.shadeddimensions.library.util.ItemHelper;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileStabilizer extends TileEnhancedPortals implements IEnergyHandler
{
    ChunkCoordinates mainBlock;
    int rows;
    boolean is3x3 = false;

    @SideOnly(Side.CLIENT)
    public boolean isFormed;

    public TileStabilizer()
    {
        mainBlock = null;
    }

    public boolean activate(EntityPlayer player)
    {
        if (worldObj.isRemote)
        {
            return true;
        }

        TileStabilizerMain main = getMainBlock();

        if (main != null)
        {
            return main.activate(player);
        }
        else
        {
            if (ItemHelper.isWrench(player.inventory.getCurrentItem()))
            {
                WorldCoordinates topLeft = getWorldCoordinates();

                while (topLeft.offset(ForgeDirection.WEST).getBlockId() == CommonProxy.blockStabilizer.blockID) // Get the westernmost block
                {
                    topLeft = topLeft.offset(ForgeDirection.WEST);
                }

                while (topLeft.offset(ForgeDirection.NORTH).getBlockId() == CommonProxy.blockStabilizer.blockID) // Get the northenmost block
                {
                    topLeft = topLeft.offset(ForgeDirection.NORTH);
                }

                while (topLeft.offset(ForgeDirection.UP).getBlockId() == CommonProxy.blockStabilizer.blockID) // Get the highest block
                {
                    topLeft = topLeft.offset(ForgeDirection.UP);
                }

                System.out.println("Checking 3x3");
                ArrayList<ChunkCoordinates> blocks = checkShapeThreeWide(topLeft); // 3x3
                
                if (blocks.isEmpty())
                {
                    System.out.println("Checking 3x2 X");
                    blocks = checkShapeTwoWide(topLeft, true); // Try the 3x2 X axis
    
                    if (blocks.isEmpty())
                    {
                        System.out.println("Checking 3x2 Z");
                        blocks = checkShapeTwoWide(topLeft, false); // Try the 3x2 Z axis before failing
                    }
                }

                if (!blocks.isEmpty()) // success
                {
                    for (ChunkCoordinates c : blocks) // make sure we're not interrupting something
                    {
                        TileEntity tile = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
                        
                        if (tile instanceof TileStabilizer)
                        {
                            if (((TileStabilizer) tile).getMainBlock() != null)
                            {
                                return false;
                            }
                        }
                        else if (tile instanceof TileStabilizerMain)
                        {
                            return false;
                        }
                    }
                    
                    for (ChunkCoordinates c : blocks)
                    {
                        worldObj.setBlock(c.posX, c.posY, c.posZ, CommonProxy.blockStabilizer.blockID, 0, 2);
                        
                        TileEntity tile = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);

                        if (tile instanceof TileStabilizer)
                        {
                            TileStabilizer t = (TileStabilizer) tile;
                            t.mainBlock = topLeft;
                            CommonProxy.sendUpdatePacketToAllAround(t);
                        }
                    }

                    worldObj.setBlock(topLeft.posX, topLeft.posY, topLeft.posZ, CommonProxy.blockStabilizer.blockID, 1, 3);
                    
                    TileEntity tile = topLeft.getBlockTileEntity();

                    if (tile instanceof TileStabilizerMain)
                    {
                        ((TileStabilizerMain) tile).setData(blocks, rows, is3x3);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        TileStabilizerMain main = getMainBlock();

        if (main == null)
        {
            return;
        }

        main.deconstruct();
    }

    @Override
    public boolean canInterface(ForgeDirection from)
    {
        return getMainBlock() != null;
    }

    ArrayList<ChunkCoordinates> checkShapeThreeWide(WorldCoordinates topLeft)
    {
        ArrayList<ChunkCoordinates> blocks = new ArrayList<ChunkCoordinates>();
        ChunkCoordinates heightChecker = new ChunkCoordinates(topLeft);
        rows = 0;

        while (worldObj.getBlockId(heightChecker.posX, heightChecker.posY, heightChecker.posZ) == CommonProxy.blockStabilizer.blockID)
        {
            heightChecker.posY--;
            rows++;
        }
        
        if (rows < 2)
        {
            rows = 0;
            return new ArrayList<ChunkCoordinates>();
        }

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < rows; k++)
                {
                    if (worldObj.getBlockId(topLeft.posX + i, topLeft.posY - k, topLeft.posZ + j) != CommonProxy.blockStabilizer.blockID)
                    {
                        return new ArrayList<ChunkCoordinates>();
                    }
    
                    blocks.add(new ChunkCoordinates(topLeft.posX + i, topLeft.posY - k, topLeft.posZ + j));
                }
            }
        }
        
        is3x3 = true;
        return blocks;
    }
    
    ArrayList<ChunkCoordinates> checkShapeTwoWide(WorldCoordinates topLeft, boolean isX)
    {
        ArrayList<ChunkCoordinates> blocks = new ArrayList<ChunkCoordinates>();
        ChunkCoordinates heightChecker = new ChunkCoordinates(topLeft);
        rows = 0;

        while (worldObj.getBlockId(heightChecker.posX, heightChecker.posY, heightChecker.posZ) == CommonProxy.blockStabilizer.blockID)
        {
            heightChecker.posY--;
            rows++;
        }

        if (rows < 2)
        {
            rows = 0;
            return new ArrayList<ChunkCoordinates>();
        }
        
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                for (int k = 0; k < rows; k++)
                {
                    if (worldObj.getBlockId(topLeft.posX + (isX ? i : j), topLeft.posY - k, topLeft.posZ + (!isX ? i : j)) != CommonProxy.blockStabilizer.blockID)
                    {
                        return new ArrayList<ChunkCoordinates>();
                    }

                    blocks.add(new ChunkCoordinates(topLeft.posX + (isX ? i : j), topLeft.posY - k, topLeft.posZ + (!isX ? i : j)));
                }
            }
        }

        is3x3 = false;
        return blocks;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        TileStabilizerMain main = getMainBlock();

        if (main == null)
        {
            return 0;
        }

        return main.extractEnergy(from, maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        TileStabilizerMain main = getMainBlock();

        if (main == null)
        {
            return 0;
        }

        return main.getEnergyStored(from);
    }

    /***
     * Gets the block that does all the processing for this multiblock. If that block is self, will return self.
     */
    public TileStabilizerMain getMainBlock()
    {
        if (mainBlock != null)
        {
            TileEntity tile = worldObj.getBlockTileEntity(mainBlock.posX, mainBlock.posY, mainBlock.posZ);

            if (tile != null && tile instanceof TileStabilizerMain)
            {
                return (TileStabilizerMain) tile;
            }
        }

        return null;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        TileStabilizerMain main = getMainBlock();

        if (main == null)
        {
            return 0;
        }

        return main.getMaxEnergyStored(from);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        mainBlock = GeneralUtils.loadChunkCoord(tag, "mainBlock");
    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        isFormed = stream.readBoolean();
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        stream.writeBoolean(mainBlock != null);
    }

    /* IEnergyHandler */
    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        TileStabilizerMain main = getMainBlock();

        if (main == null)
        {
            return 0;
        }

        return main.receiveEnergy(from, maxReceive, simulate);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        GeneralUtils.saveChunkCoord(tag, mainBlock, "mainBlock");
    }
}
