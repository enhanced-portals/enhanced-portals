package uk.co.shadeddimensions.enhancedportals.portal;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TileEP;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileRedstoneInterface;
import uk.co.shadeddimensions.enhancedportals.util.ChunkCoordinateUtils;

public class BlockManager
{
    ArrayList<ChunkCoordinates> portal;
    ArrayList<ChunkCoordinates> portalFrame;
    ArrayList<ChunkCoordinates> redstone;
    ChunkCoordinates biometric;
    ChunkCoordinates dialDevice;
    ChunkCoordinates networkInterface;
    
    boolean isProcessing;
    
    public BlockManager()
    {
        portal = new ArrayList<ChunkCoordinates>();
        portalFrame = new ArrayList<ChunkCoordinates>();
        redstone = new ArrayList<ChunkCoordinates>();
        biometric = dialDevice = networkInterface = null;
        isProcessing = false;
    }
    
    public void saveData(NBTTagCompound tag)
    {
        ChunkCoordinateUtils.saveChunkCoordList(tag, portal, "portal");
        ChunkCoordinateUtils.saveChunkCoordList(tag, portalFrame, "portalFrame");
        ChunkCoordinateUtils.saveChunkCoordList(tag, redstone, "redstone");
        ChunkCoordinateUtils.saveChunkCoord(tag, biometric, "biometric");
        ChunkCoordinateUtils.saveChunkCoord(tag, dialDevice, "dialDevice");
        ChunkCoordinateUtils.saveChunkCoord(tag, networkInterface, "networkInterface");
    }
    
    public void loadData(NBTTagCompound tag)
    {
        portal = ChunkCoordinateUtils.loadChunkCoordList(tag, "portal");
        portalFrame = ChunkCoordinateUtils.loadChunkCoordList(tag, "portalFrame");
        redstone = ChunkCoordinateUtils.loadChunkCoordList(tag, "redstone");
        biometric = ChunkCoordinateUtils.loadChunkCoord(tag, "biometric");
        dialDevice = ChunkCoordinateUtils.loadChunkCoord(tag, "dialDevice");
        networkInterface = ChunkCoordinateUtils.loadChunkCoord(tag, "networkInterface");
    }
    
    public void clearPortals()
    {
        portal.clear();
    }
    
    public void clearPortalFrames()
    {
        portalFrame.clear();
    }
    
    public void clearRedstone()
    {
        redstone.clear();
    }
    
    public void clearBiometric()
    {
        biometric = null;
    }
    
    public void clearDialDevice()
    {
        dialDevice = null;
    }
    
    public void clearNetworkInterface()
    {
        networkInterface = null;
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
        isProcessing = false;
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
        isProcessing = false;
    }
    
    public void destroyAndClearAll(World world)
    {        
        destroyAll(world);
        clearAll();
    }
    
    public void addToPortals(ChunkCoordinates c)
    {
        portal.add(c);
    }
    
    public void addToPortalFrames(ChunkCoordinates c)
    {
        portalFrame.add(c);
    }
    
    public void addToRedstone(ChunkCoordinates c)
    {
        redstone.add(c);
    }
    
    public void setBiometric(ChunkCoordinates c)
    {
        biometric = c;
    }
    
    public void setDialDevice(ChunkCoordinates c)
    {
        dialDevice = c;
    }
    
    public void setNetworkInterface(ChunkCoordinates c)
    {
        networkInterface = c;
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

    public ArrayList<ChunkCoordinates> getPortalsCoord()
    {
        return portal;
    }

    public ArrayList<ChunkCoordinates> getRedstoneCoord()
    {
        return redstone;
    }
    
    public ArrayList<ChunkCoordinates> getPortalFrameCoord()
    {
        return portalFrame;
    }
    
    public ChunkCoordinates getBiometricCoord()
    {
        return biometric;
    }
    
    public ChunkCoordinates getDialDeviceCoord()
    {
        return dialDevice;
    }
    
    public ChunkCoordinates getNetworkInterfaceCoord()
    {
        return networkInterface;
    }
}
