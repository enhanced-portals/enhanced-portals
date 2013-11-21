package uk.co.shadeddimensions.ep3.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;

public class BlockManager
{
    ArrayList<ChunkCoordinates> basicFrames;
    ArrayList<ChunkCoordinates> portals;
    ArrayList<ChunkCoordinates> redstoneInterfaces;

    ChunkCoordinates networkInterface;
    ChunkCoordinates dialDevice;
    ChunkCoordinates biometricIdentifier;
    ChunkCoordinates moduleManipulator;

    WorldCoordinates dimensionalBridgeStabilizer;

    public BlockManager()
    {
        basicFrames = new ArrayList<ChunkCoordinates>();
        portals = new ArrayList<ChunkCoordinates>();
        redstoneInterfaces = new ArrayList<ChunkCoordinates>();
    }

    public int getFrameCount()
    {
        return basicFrames.size();
    }

    public int getPortalCount()
    {
        return portals.size();
    }

    public int getRedstoneInterfaceCount()
    {
        return redstoneInterfaces.size();
    }

    public boolean getHasNetworkInterface()
    {
        return networkInterface != null;
    }

    public boolean getHasDialDevice()
    {
        return dialDevice != null;
    }

    public boolean getHasBiometricIdentifier()
    {
        return biometricIdentifier != null;
    }

    public boolean getHasModuleManipulator()
    {
        return moduleManipulator != null;
    }

    public void writeToNBT(NBTTagCompound tag)
    {
        GeneralUtils.saveChunkCoordList(tag, basicFrames, "Frames");
        GeneralUtils.saveChunkCoordList(tag, portals, "Portals");
        GeneralUtils.saveChunkCoordList(tag, redstoneInterfaces, "RedstoneInterfaces");
        GeneralUtils.saveChunkCoord(tag, networkInterface, "NetworkInterface");
        GeneralUtils.saveChunkCoord(tag, dialDevice, "DialDevice");
        GeneralUtils.saveChunkCoord(tag, biometricIdentifier, "BiometricIdentifier");
        GeneralUtils.saveChunkCoord(tag, moduleManipulator, "ModuleManipulator");
        GeneralUtils.saveWorldCoord(tag, dimensionalBridgeStabilizer, "DimensionalBridgeStabilizer");
    }

    public BlockManager readFromNBT(NBTTagCompound tag)
    {
        basicFrames = GeneralUtils.loadChunkCoordList(tag, "Frames");
        portals = GeneralUtils.loadChunkCoordList(tag, "Portals");
        redstoneInterfaces = GeneralUtils.loadChunkCoordList(tag, "RedstoneInterfaces");
        networkInterface = GeneralUtils.loadChunkCoord(tag, "NetworkInterface");
        dialDevice = GeneralUtils.loadChunkCoord(tag, "DialDevice");
        biometricIdentifier = GeneralUtils.loadChunkCoord(tag, "BiometricIdentifier");
        moduleManipulator = GeneralUtils.loadChunkCoord(tag, "ModuleManipulator");
        dimensionalBridgeStabilizer = GeneralUtils.loadWorldCoord(tag, "DimensionalBridgeStabilizer");
        return this;
    }

    public void writeToPacket(DataOutputStream stream) throws IOException
    {
        stream.writeInt(basicFrames.size());
        stream.writeInt(portals.size());
        stream.writeInt(redstoneInterfaces.size());
        stream.writeBoolean(networkInterface != null);
        stream.writeBoolean(dialDevice != null);
        stream.writeBoolean(biometricIdentifier != null);
        GeneralUtils.writeChunkCoord(stream, moduleManipulator);
    }

    public BlockManager readFromPacket(DataInputStream stream) throws IOException
    {
        // Nothing to do serverside
        return this;
    }

    public BlockManager addBasicFrame(ChunkCoordinates c)
    {
        basicFrames.add(c);
        return this;
    }

    public BlockManager addPortalBlock(ChunkCoordinates c)
    {
        portals.add(c);
        return this;
    }

    public BlockManager addRedstoneInterface(ChunkCoordinates c)
    {
        redstoneInterfaces.add(c);
        return this;
    }

    public BlockManager setNetworkInterface(ChunkCoordinates c)
    {
        networkInterface = c;
        return this;
    }

    public BlockManager setDialDevice(ChunkCoordinates c)
    {
        dialDevice = c;
        return this;
    }

    public BlockManager setBiometricIdentifier(ChunkCoordinates c)
    {
        biometricIdentifier = c;
        return this;
    }

    public BlockManager setModuleManipulator(ChunkCoordinates c)
    {
        moduleManipulator = c;
        return this;
    }

    public BlockManager setDimensionalBridgeStabilizer(WorldCoordinates w)
    {
        dimensionalBridgeStabilizer = w;
        return this;
    }

    public ArrayList<ChunkCoordinates> getPortalFrames()
    {
        return basicFrames;
    }

    public ArrayList<ChunkCoordinates> getPortals()
    {
        return portals;
    }

    public ArrayList<ChunkCoordinates> getRedstoneInterfaces()
    {
        return redstoneInterfaces;
    }

    public ChunkCoordinates getNetworkInterface()
    {
        return networkInterface;
    }

    public TileNetworkInterface getNetworkInterface(World w)
    {
        if (networkInterface != null)
        {
            TileEntity tile = w.getBlockTileEntity(networkInterface.posX, networkInterface.posY, networkInterface.posZ);

            if (tile instanceof TileNetworkInterface)
            {
                return (TileNetworkInterface) tile;
            }
        }

        return null;
    }

    public ChunkCoordinates getDialDevice()
    {
        return dialDevice;
    }

    public TileDiallingDevice getDialDevice(World w)
    {
        if (dialDevice != null)
        {
            TileEntity tile = w.getBlockTileEntity(dialDevice.posX, dialDevice.posY, dialDevice.posZ);

            if (tile instanceof TileDiallingDevice)
            {
                return (TileDiallingDevice) tile;
            }
        }

        return null;
    }

    public ChunkCoordinates getBiometricIdentifier()
    {
        return biometricIdentifier;
    }

    public TileBiometricIdentifier getBiometricIdentifier(World w)
    {
        if (biometricIdentifier != null)
        {
            TileEntity tile = w.getBlockTileEntity(biometricIdentifier.posX, biometricIdentifier.posY, biometricIdentifier.posZ);

            if (tile instanceof TileBiometricIdentifier)
            {
                return (TileBiometricIdentifier) tile;
            }
        }

        return null;
    }

    public ChunkCoordinates getModuleManipulator()
    {
        return moduleManipulator;
    }

    public TileModuleManipulator getModuleManipulator(World w)
    {
        if (moduleManipulator != null)
        {
            TileEntity tile = w.getBlockTileEntity(moduleManipulator.posX, moduleManipulator.posY, moduleManipulator.posZ);

            if (tile instanceof TileModuleManipulator)
            {
                return (TileModuleManipulator) tile;
            }
        }

        return null;
    }

    public WorldCoordinates getDimensionalBridgeStabilizer()
    {
        return dimensionalBridgeStabilizer;
    }

    public TileStabilizerMain getDimensionalBridgeStabilizerTile()
    {
        if (dimensionalBridgeStabilizer != null)
        {
            World w = dimensionalBridgeStabilizer.getWorld();
            TileEntity tile = w.getBlockTileEntity(dimensionalBridgeStabilizer.posX, dimensionalBridgeStabilizer.posY, dimensionalBridgeStabilizer.posZ);

            if (tile instanceof TileStabilizerMain)
            {
                return (TileStabilizerMain) tile;
            }
            else if (tile instanceof TileStabilizer)
            {
                TileStabilizer t = (TileStabilizer) tile;
                TileStabilizerMain m = t.getMainBlock();

                if (m != null)
                {
                    setDimensionalBridgeStabilizer(m.getWorldCoordinates());
                    return m;
                }
            }

            dimensionalBridgeStabilizer = null;
        }

        return null;
    }

    public void removeFrame(ChunkCoordinates chunkCoordinates)
    {
        basicFrames.remove(chunkCoordinates);
    }
}
