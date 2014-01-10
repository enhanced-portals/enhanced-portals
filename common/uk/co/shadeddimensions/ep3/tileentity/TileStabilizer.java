package uk.co.shadeddimensions.ep3.tileentity;

import java.util.ArrayList;

import cofh.api.energy.IEnergyHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class TileStabilizer extends TileEnhancedPortals implements IEnergyHandler
{
    ChunkCoordinates mainBlock;
    int rows;

    public TileStabilizer()
    {
        mainBlock = null;
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
    public boolean activate(EntityPlayer player)
    {
        if (worldObj.isRemote || player.inventory.getCurrentItem() == null || player.inventory.getCurrentItem().getItem().itemID != CommonProxy.itemWrench.itemID)
        {
            return false;
        }

        if (mainBlock != null)
        {
            TileStabilizerMain main = getMainBlock();

            if (main != null)
            {
                CommonProxy.openGui(player, GUIs.DimensionalBridgeStabilizer, main);
                return true;
            }
        }

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

        ArrayList<ChunkCoordinates> blocks = checkShape(topLeft, true); // Try the X axis

        if (blocks.isEmpty())
        {
            blocks = checkShape(topLeft, false); // Try the Z axis before failing
        }

        if (!blocks.isEmpty()) // success
        {
            for (ChunkCoordinates c : blocks)
            {
                TileEntity tile = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);

                if (tile instanceof TileStabilizerMain)
                {
                    worldObj.setBlock(c.posX, c.posY, c.posZ, CommonProxy.blockStabilizer.blockID, 0, 3);
                    tile = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
                }

                if (tile instanceof TileStabilizer)
                {
                    TileStabilizer t = (TileStabilizer) tile;
                    t.mainBlock = topLeft;
                    CommonProxy.sendUpdatePacketToAllAround(t);
                }
            }

            worldObj.setBlock(topLeft.posX, topLeft.posY, topLeft.posZ, CommonProxy.blockStabilizer.blockID, 1, 3);

            TileStabilizerMain main = (TileStabilizerMain) topLeft.getBlockTileEntity();
            main.setData(blocks, rows);
        }

        return true;
    }

    ArrayList<ChunkCoordinates> checkShape(WorldCoordinates topLeft, boolean isX)
    {
        ArrayList<ChunkCoordinates> blocks = new ArrayList<ChunkCoordinates>();

        int tempHeight = 0;
        ChunkCoordinates heightChecker = new ChunkCoordinates(topLeft);

        while (worldObj.getBlockId(heightChecker.posX, heightChecker.posY, heightChecker.posZ) == CommonProxy.blockStabilizer.blockID)
        {
            heightChecker.posY--;
            tempHeight++;
        }

        if (tempHeight < 2)
        {
            return new ArrayList<ChunkCoordinates>();
        }

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                for (int k = 0; k < tempHeight; k++)
                {
                    if (worldObj.getBlockId(topLeft.posX + (isX ? i : j), topLeft.posY - k, topLeft.posZ + (!isX ? i : j)) != CommonProxy.blockStabilizer.blockID)
                    {
                        return new ArrayList<ChunkCoordinates>();
                    }

                    blocks.add(new ChunkCoordinates(topLeft.posX + (isX ? i : j), topLeft.posY - k, topLeft.posZ + (!isX ? i : j)));
                }
            }
        }

        rows = tempHeight;
        return blocks;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        GeneralUtils.saveChunkCoord(tag, mainBlock, "mainBlock");
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        mainBlock = GeneralUtils.loadChunkCoord(tag, "mainBlock");
    }

    @Override
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
    public void validate()
    {
        // Don't call super - we don't need to send any packets here
        tileEntityInvalid = false;
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
    public boolean canInterface(ForgeDirection from)
    {
        return getMainBlock() != null;
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
}
