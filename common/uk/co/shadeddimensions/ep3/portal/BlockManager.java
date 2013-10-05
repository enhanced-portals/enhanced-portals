package uk.co.shadeddimensions.ep3.portal;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TileEP;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import uk.co.shadeddimensions.ep3.util.ChunkCoordinateUtils;

public class BlockManager
{
    ArrayList<ChunkCoordinates> portal;
    ArrayList<ChunkCoordinates> portalFrame;
    ArrayList<ChunkCoordinates> redstone;

    ChunkCoordinates biometric;
    ChunkCoordinates dialDevice;
    ChunkCoordinates networkInterface;
    ChunkCoordinates moduleManipulator;

    boolean isProcessing;

    public BlockManager()
    {
        portal = new ArrayList<ChunkCoordinates>();
        portalFrame = new ArrayList<ChunkCoordinates>();
        redstone = new ArrayList<ChunkCoordinates>();
        biometric = dialDevice = networkInterface = moduleManipulator = null;
        isProcessing = false;
    }

    public void addToPortalFrames(ChunkCoordinates c)
    {
        portalFrame.add(c);
    }

    public void addToPortals(ChunkCoordinates c)
    {
        portal.add(c);
    }

    public void addToRedstone(ChunkCoordinates c)
    {
        redstone.add(c);
    }

    /***
     * Clears all lists.
     */
    public void clearAll()
    {
        if (isProcessing)
        {
            return;
        }

        isProcessing = true;
        clearPortals();
        clearPortalFrames();
        clearRedstone();
        clearBiometric();
        clearDialDevice();
        clearNetworkInterface();
        clearModuleManipulator();
        isProcessing = false;
    }

    public void clearBiometric()
    {
        biometric = null;
    }

    public void clearDialDevice()
    {
        dialDevice = null;
    }

    public void clearModuleManipulator()
    {
        moduleManipulator = null;
    }

    public void clearNetworkInterface()
    {
        networkInterface = null;
    }

    public void clearPortalFrames()
    {
        portalFrame.clear();
    }

    public void clearPortals()
    {
        portal.clear();
    }

    public void clearRedstone()
    {
        redstone.clear();
    }

    /***
     * Removes all portal blocks and resets frames.
     */
    public void destroyAll(World world)
    {
        if (isProcessing)
        {
            return;
        }

        isProcessing = true;
        destroyPortals(world);
        destroyPortalFrames(world);
        destroyRedstone(world);
        destroyBiometric(world);
        destroyDiallingDevice(world);
        destroyNetworkInterface(world);
        destroyModuleManipulator(world);
        isProcessing = false;
    }

    public void destroyAndClearAll(World world)
    {
        destroyAll(world);
        clearAll();
    }

    public void destroyBiometric(World world)
    {
        if (biometric != null)
        {
            TileEntity tile = world.getBlockTileEntity(biometric.posX, biometric.posY, biometric.posZ);

            if (tile != null)
            {
                ((TileBiometricIdentifier) tile).controller = null;
                CommonProxy.sendUpdatePacketToAllAround((TileEP) tile);
            }
        }
    }

    public void destroyDiallingDevice(World world)
    {
        if (dialDevice != null)
        {
            TileEntity tile = world.getBlockTileEntity(dialDevice.posX, dialDevice.posY, dialDevice.posZ);

            if (tile != null)
            {
                ((TileDiallingDevice) tile).controller = null;
                CommonProxy.sendUpdatePacketToAllAround((TileEP) tile);
            }
        }
    }

    public void destroyModuleManipulator(World world)
    {
        if (moduleManipulator != null)
        {
            TileEntity tile = world.getBlockTileEntity(moduleManipulator.posX, moduleManipulator.posY, moduleManipulator.posZ);

            if (tile != null)
            {
                ((TileModuleManipulator) tile).controller = null;
                CommonProxy.sendUpdatePacketToAllAround((TileEP) tile);
            }
        }
    }

    public void destroyNetworkInterface(World world)
    {
        if (networkInterface != null)
        {
            TileEntity tile = world.getBlockTileEntity(networkInterface.posX, networkInterface.posY, networkInterface.posZ);

            if (tile != null)
            {
                ((TileNetworkInterface) tile).controller = null;
                CommonProxy.sendUpdatePacketToAllAround((TileEP) tile);
            }
        }
    }

    public void destroyPortalFrames(World world)
    {
        for (ChunkCoordinates c : portalFrame)
        {
            TileEntity tile = world.getBlockTileEntity(c.posX, c.posY, c.posZ);

            if (tile != null)
            {
                ((TilePortalFrame) tile).controller = null;
                CommonProxy.sendUpdatePacketToAllAround((TileEP) tile);
            }
        }
    }

    public void destroyPortals(World world)
    {
        for (ChunkCoordinates c : portal)
        {
            if (world.getBlockId(c.posX, c.posY, c.posZ) == CommonProxy.blockPortal.blockID)
            {
                world.setBlockToAir(c.posX, c.posY, c.posZ);
            }
        }
    }

    public void destroyRedstone(World world)
    {
        for (ChunkCoordinates c : redstone)
        {
            TileEntity tile = world.getBlockTileEntity(c.posX, c.posY, c.posZ);

            if (tile != null)
            {
                ((TileRedstoneInterface) tile).controller = null;
                CommonProxy.sendUpdatePacketToAllAround((TileEP) tile);
            }
        }
    }

    public TileBiometricIdentifier getBiometric(World world)
    {
        if (biometric != null)
        {
            TileEntity tile = world.getBlockTileEntity(biometric.posX, biometric.posY, biometric.posZ);

            if (tile != null && tile instanceof TileBiometricIdentifier)
            {
                return (TileBiometricIdentifier) tile;
            }
        }

        return null;
    }

    public ChunkCoordinates getBiometricCoord()
    {
        return biometric;
    }

    public TileDiallingDevice getDialDevice(World world)
    {
        if (dialDevice != null)
        {
            TileEntity tile = world.getBlockTileEntity(dialDevice.posX, dialDevice.posY, dialDevice.posZ);

            if (tile != null && tile instanceof TileDiallingDevice)
            {
                return (TileDiallingDevice) tile;
            }
        }

        return null;
    }

    public ChunkCoordinates getDialDeviceCoord()
    {
        return dialDevice;
    }

    public TileModuleManipulator getModuleManipulator(World world)
    {
        if (moduleManipulator != null)
        {
            TileEntity tile = world.getBlockTileEntity(moduleManipulator.posX, moduleManipulator.posY, moduleManipulator.posZ);

            if (tile != null && tile instanceof TileModuleManipulator)
            {
                return (TileModuleManipulator) tile;
            }
        }

        return null;
    }

    public ChunkCoordinates getModuleManipulatorCoord()
    {
        return moduleManipulator;
    }

    public TileNetworkInterface getNetworkInterface(World world)
    {
        if (networkInterface != null)
        {
            TileEntity tile = world.getBlockTileEntity(networkInterface.posX, networkInterface.posY, networkInterface.posZ);

            if (tile != null && tile instanceof TileNetworkInterface)
            {
                return (TileNetworkInterface) tile;
            }
        }

        return null;
    }

    public ChunkCoordinates getNetworkInterfaceCoord()
    {
        return networkInterface;
    }

    public ArrayList<ChunkCoordinates> getPortalFrameCoord()
    {
        return portalFrame;
    }

    public ArrayList<ChunkCoordinates> getPortalsCoord()
    {
        return portal;
    }

    public ArrayList<ChunkCoordinates> getRedstoneCoord()
    {
        return redstone;
    }

    public void loadData(NBTTagCompound tag)
    {
        portal = ChunkCoordinateUtils.loadChunkCoordList(tag, "portal");
        portalFrame = ChunkCoordinateUtils.loadChunkCoordList(tag, "portalFrame");
        redstone = ChunkCoordinateUtils.loadChunkCoordList(tag, "redstone");
        biometric = ChunkCoordinateUtils.loadChunkCoord(tag, "biometric");
        dialDevice = ChunkCoordinateUtils.loadChunkCoord(tag, "dialDevice");
        networkInterface = ChunkCoordinateUtils.loadChunkCoord(tag, "networkInterface");
        moduleManipulator = ChunkCoordinateUtils.loadChunkCoord(tag, "moduleManipulator");
    }

    public void saveData(NBTTagCompound tag)
    {
        ChunkCoordinateUtils.saveChunkCoordList(tag, portal, "portal");
        ChunkCoordinateUtils.saveChunkCoordList(tag, portalFrame, "portalFrame");
        ChunkCoordinateUtils.saveChunkCoordList(tag, redstone, "redstone");
        ChunkCoordinateUtils.saveChunkCoord(tag, biometric, "biometric");
        ChunkCoordinateUtils.saveChunkCoord(tag, dialDevice, "dialDevice");
        ChunkCoordinateUtils.saveChunkCoord(tag, networkInterface, "networkInterface");
        ChunkCoordinateUtils.saveChunkCoord(tag, moduleManipulator, "moduleManipulator");
    }

    public void setBiometric(ChunkCoordinates c)
    {
        biometric = c;
    }

    public void setDialDevice(ChunkCoordinates c)
    {
        dialDevice = c;
    }

    public void setModuleManipulator(ChunkCoordinates c)
    {
        moduleManipulator = c;
    }

    public void setNetworkInterface(ChunkCoordinates c)
    {
        networkInterface = c;
    }
}
