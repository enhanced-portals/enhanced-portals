package enhancedportals.tileentity;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.block.BlockStabilizer;
import enhancedportals.network.CommonProxy;
import enhancedportals.utility.GeneralUtils;
import enhancedportals.utility.WorldCoordinates;

public class TileStabilizer extends TileEP implements IEnergyHandler, IPowerReceptor
{
    ChunkCoordinates mainBlock;
    int rows;
    boolean is3x3 = false;
    private final PowerHandler mjHandler;

    @SideOnly(Side.CLIENT)
    public boolean isFormed;

    public TileStabilizer()
    {
        mainBlock = null;
        float energyUsage = CommonProxy.REDSTONE_FLUX_COST / CommonProxy.RF_PER_MJ;
        mjHandler = new PowerHandler(this, Type.MACHINE);
        mjHandler.configure(2.0f, energyUsage, energyUsage * 0.2f, 1500);
        mjHandler.configurePowerPerdition(0, 0);
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
            if (GeneralUtils.isWrench(player.inventory.getCurrentItem()))
            {
                WorldCoordinates topLeft = getWorldCoordinates();

                while (topLeft.offset(ForgeDirection.WEST).getBlock() == BlockStabilizer.instance) // Get the westernmost block
                {
                    topLeft = topLeft.offset(ForgeDirection.WEST);
                }

                while (topLeft.offset(ForgeDirection.NORTH).getBlock() == BlockStabilizer.instance) // Get the northenmost block
                {
                    topLeft = topLeft.offset(ForgeDirection.NORTH);
                }

                while (topLeft.offset(ForgeDirection.UP).getBlock() == BlockStabilizer.instance) // Get the highest block
                {
                    topLeft = topLeft.offset(ForgeDirection.UP);
                }

                ArrayList<ChunkCoordinates> blocks = checkShapeThreeWide(topLeft); // 3x3

                if (blocks.isEmpty())
                {
                    blocks = checkShapeTwoWide(topLeft, true); // Try the 3x2 X axis

                    if (blocks.isEmpty())
                    {
                        blocks = checkShapeTwoWide(topLeft, false); // Try the 3x2 Z axis before failing
                    }
                }

                if (!blocks.isEmpty()) // success
                {
                    for (ChunkCoordinates c : blocks) // make sure we're not interrupting something
                    {
                        TileEntity tile = worldObj.getTileEntity(c.posX, c.posY, c.posZ);

                        if (tile instanceof TileStabilizer)
                        {
                            if (((TileStabilizer) tile).getMainBlock() != null)
                            {
                                TileStabilizerMain m = ((TileStabilizer) tile).getMainBlock();
                                m.deconstruct();
                            }
                        }
                        else if (tile instanceof TileStabilizerMain)
                        {
                            ((TileStabilizerMain) tile).deconstruct();
                        }
                    }

                    for (ChunkCoordinates c : blocks)
                    {
                        worldObj.setBlock(c.posX, c.posY, c.posZ, BlockStabilizer.instance, 0, 2);

                        TileEntity tile = worldObj.getTileEntity(c.posX, c.posY, c.posZ);

                        if (tile instanceof TileStabilizer)
                        {
                            TileStabilizer t = (TileStabilizer) tile;
                            t.mainBlock = topLeft;
                            worldObj.markBlockForUpdate(t.xCoord, t.yCoord, t.zCoord);
                        }
                    }

                    worldObj.setBlock(topLeft.posX, topLeft.posY, topLeft.posZ, BlockStabilizer.instance, 1, 3);

                    TileEntity tile = topLeft.getTileEntity();

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

    public void breakBlock(Block b, int oldMetadata)
    {
        TileStabilizerMain main = getMainBlock();

        if (main == null)
        {
            return;
        }

        main.deconstruct();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return true;
    }

    ArrayList<ChunkCoordinates> checkShapeThreeWide(WorldCoordinates topLeft)
    {
        ArrayList<ChunkCoordinates> blocks = new ArrayList<ChunkCoordinates>();
        ChunkCoordinates heightChecker = new ChunkCoordinates(topLeft);
        rows = 0;

        while (worldObj.getBlock(heightChecker.posX, heightChecker.posY, heightChecker.posZ) == BlockStabilizer.instance)
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
                    if (worldObj.getBlock(topLeft.posX + i, topLeft.posY - k, topLeft.posZ + j) != BlockStabilizer.instance)
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

        while (worldObj.getBlock(heightChecker.posX, heightChecker.posY, heightChecker.posZ) == BlockStabilizer.instance)
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
                    if (worldObj.getBlock(topLeft.posX + (isX ? i : j), topLeft.posY - k, topLeft.posZ + (!isX ? i : j)) != BlockStabilizer.instance)
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
    public void doWork(PowerHandler workProvider)
    {
        int acceptedEnergy = receiveEnergy(null, (int) (mjHandler.useEnergy(1.0F, CommonProxy.REDSTONE_FLUX_COST / CommonProxy.RF_PER_MJ, false) * CommonProxy.RF_PER_MJ), false);
        mjHandler.useEnergy(1.0F, acceptedEnergy / CommonProxy.RF_PER_MJ, true);
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
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("formed", mainBlock != null);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
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
            TileEntity tile = worldObj.getTileEntity(mainBlock.posX, mainBlock.posY, mainBlock.posZ);

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
    public PowerReceiver getPowerReceiver(ForgeDirection side)
    {
        return mjHandler.getPowerReceiver();
    }

    @Override
    public World getWorld()
    {
        return worldObj;
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.func_148857_g();
        isFormed = tag.getBoolean("formed");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        mainBlock = GeneralUtils.loadChunkCoord(tag, "mainBlock");
    }

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
