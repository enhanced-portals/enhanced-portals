package uk.co.shadeddimensions.enhancedportals.portal;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameBiometric;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameDialDevice;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;
import uk.co.shadeddimensions.enhancedportals.util.ChunkCoordinateUtils;

public class ControllerLink
{
    public static enum LinkStatus
    {
        SUCCESS, //
        FAIL_MultipleControllers, //
        FAIL_NoPortalBlocks, //
        FAIL_MultipleNetworkIdentifiers, //
        FAIL_MultipleDialDevices, //
        FAIL_NetworkInterfaceAndDialDevice, //
        FAIL_MultipleBiometric; //
    }
    
    World world;
    TilePortalFrameController controller;
    
    public ControllerLink(TilePortalFrameController control)
    {
        controller = control;
        world = controller.worldObj;
    }
    
    public LinkStatus doLink()
    {
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> processed = new LinkedList<ChunkCoordinates>();
        
        Queue<ChunkCoordinates> portalBlocks = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> frameBlocks = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> redstoneBlocks = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> networkBlocks = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> dialBlocks = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> biometricBlocks = new LinkedList<ChunkCoordinates>();
        
        toProcess.add(controller.getChunkCoordinates());
        
        while (!toProcess.isEmpty())
        {
            ChunkCoordinates c = toProcess.remove();
            
            if (!processed.contains(c))
            {
                TileEntity t = world.getBlockTileEntity(c.posX, c.posY, c.posZ);
                            
                if (t instanceof TilePortal)
                {
                    portalBlocks.add(c);
                }
                else if (t instanceof TilePortalFrameRedstone)
                {
                    redstoneBlocks.add(c);
                }
                else if (t instanceof TilePortalFrameNetworkInterface)
                {
                    networkBlocks.add(c);
                }
                else if (t instanceof TilePortalFrameDialDevice)
                {
                    dialBlocks.add(c);
                }
                else if (t instanceof TilePortalFrameBiometric)
                {
                    biometricBlocks.add(c);
                }
                else if (t instanceof TilePortalFrameController)
                {
                    if (!processed.isEmpty())
                    {
                        return LinkStatus.FAIL_MultipleControllers;
                    }
                }
                else if (t instanceof TilePortalFrame)
                {                
                    frameBlocks.add(c);
                }
            
                processed.add(c);
                addValidTouchingBlocks(c, toProcess);
            }
        }
        
        if (portalBlocks.isEmpty())
        {
            return LinkStatus.FAIL_NoPortalBlocks;
        }
        else if (networkBlocks.size() > 1)
        {
            return LinkStatus.FAIL_MultipleNetworkIdentifiers;
        }
        else if (dialBlocks.size() > 1)
        {
            return LinkStatus.FAIL_MultipleDialDevices;
        }
        else if (biometricBlocks.size() > 1)
        {
            return LinkStatus.FAIL_MultipleBiometric;
        }
        else if (networkBlocks.size() == 1 && dialBlocks.size() == 1)
        {
            return LinkStatus.FAIL_NetworkInterfaceAndDialDevice;
        }
        
        // everything seems to be OK - lets continue        
        controller.blockManager.clearAll();
        
        while (!portalBlocks.isEmpty()) // loop through portal blocks connecting them to controller
        {
            ChunkCoordinates c = portalBlocks.remove();
            TilePortal portal = (TilePortal) world.getBlockTileEntity(c.posX, c.posY, c.posZ);
            
            if (portal != null)
            {
                portal.controller = controller.getChunkCoordinates();
                controller.blockManager.addToPortals(c);
                CommonProxy.sendUpdatePacketToAllAround(portal);
            }
        }
        
        while (!frameBlocks.isEmpty()) // and the same for the basic frames
        {
            ChunkCoordinates c = frameBlocks.remove();            
            TilePortalFrame frame = (TilePortalFrame) world.getBlockTileEntity(c.posX, c.posY, c.posZ);
            
            if (frame != null)
            {
                frame.controller = controller.getChunkCoordinates();
                controller.blockManager.addToPortalFrames(c);
                CommonProxy.sendUpdatePacketToAllAround(frame);
            }
        }
        
        while (!redstoneBlocks.isEmpty()) // redstone too
        {
            ChunkCoordinates c = redstoneBlocks.remove();            
            TilePortalFrameRedstone frame = (TilePortalFrameRedstone) world.getBlockTileEntity(c.posX, c.posY, c.posZ);
            
            if (frame != null)
            {
                frame.controller = controller.getChunkCoordinates();
                controller.blockManager.addToRedstone(c);
                CommonProxy.sendUpdatePacketToAllAround(frame);
            }
        }
        
        if (!networkBlocks.isEmpty()) // there should only be one of these. (checked above)
        {
            ChunkCoordinates c = networkBlocks.remove();            
            TilePortalFrameNetworkInterface networkInterface = (TilePortalFrameNetworkInterface) world.getBlockTileEntity(c.posX, c.posY, c.posZ);
            
            if (networkInterface != null)
            {
                networkInterface.controller = controller.getChunkCoordinates();
                controller.blockManager.setNetworkInterface(c);
                CommonProxy.sendUpdatePacketToAllAround(networkInterface);
            }
        }
        else if (!dialBlocks.isEmpty()) // OR one of these (checked above)
        {
            ChunkCoordinates c = dialBlocks.remove();            
            TilePortalFrameDialDevice device = (TilePortalFrameDialDevice) world.getBlockTileEntity(c.posX, c.posY, c.posZ);
            
            if (device != null)
            {
                device.controller = controller.getChunkCoordinates();
                controller.blockManager.setDialDevice(c);
                CommonProxy.sendUpdatePacketToAllAround(device);
            }
        }
        
        if (!biometricBlocks.isEmpty()) // there should only be one of these. (checked above)
        {
            ChunkCoordinates c = biometricBlocks.remove();            
            TilePortalFrameBiometric biometric = (TilePortalFrameBiometric) world.getBlockTileEntity(c.posX, c.posY, c.posZ);
            
            if (biometric != null)
            {
                biometric.controller = controller.getChunkCoordinates();
                controller.blockManager.setBiometric(c);
                CommonProxy.sendUpdatePacketToAllAround(biometric);
            }
        }
        
        return LinkStatus.SUCCESS;
    }
    
    private void addValidTouchingBlocks(ChunkCoordinates coord, Queue<ChunkCoordinates> toProcess)
    {
        for (int i = 0; i < 6; i++)
        {
            ChunkCoordinates c = ChunkCoordinateUtils.offset(coord, ForgeDirection.getOrientation(i));
            int id = world.getBlockId(c.posX, c.posY, c.posZ);
            
            if (id == CommonProxy.blockPortal.blockID || id == CommonProxy.blockFrame.blockID)
            {
                toProcess.add(c);
            }
        }
    }
}
