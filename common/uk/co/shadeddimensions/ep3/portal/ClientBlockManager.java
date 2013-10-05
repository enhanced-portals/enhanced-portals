package uk.co.shadeddimensions.ep3.portal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;

public class ClientBlockManager extends BlockManager
{
    public int portal;
    public int portalFrame;
    public int redstone;
    public boolean biometric;
    public boolean dialDevice;
    public boolean networkInterface;

    @Override
    public TileBiometricIdentifier getBiometric(World world)
    {
        return null;
    }

    @Override
    public TileDiallingDevice getDialDevice(World world)
    {
        return null;
    }

    public TileModuleManipulator getModuleManipulatorBa(IBlockAccess blockAccess)
    {
        if (moduleManipulator != null)
        {
            TileEntity tile = blockAccess.getBlockTileEntity(moduleManipulator.posX, moduleManipulator.posY, moduleManipulator.posZ);

            if (tile != null && tile instanceof TileModuleManipulator)
            {
                return (TileModuleManipulator) tile;
            }
        }

        return null;
    }

    @Override
    public TileNetworkInterface getNetworkInterface(World world)
    {
        return null;
    }

    @Override
    public void loadData(NBTTagCompound tag)
    {

    }

    @Override
    public void saveData(NBTTagCompound tag)
    {

    }
}
