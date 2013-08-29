package uk.co.shadeddimensions.enhancedportals.tileentity;

import uk.co.shadeddimensions.enhancedportals.util.PortalTexture;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

public class TilePortal extends TileEP
{
    public ChunkCoordinates controller;
    
    public TilePortal()
    {
        controller = new ChunkCoordinates(0, -1, 0);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        
        tagCompound.setInteger("ControllerX", controller.posX);
        tagCompound.setInteger("ControllerY", controller.posY);
        tagCompound.setInteger("ControllerZ", controller.posZ);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        
        if (tagCompound.hasKey("ControllerX")) // Otherwise, leave it as 0, -1, 0
        {
            controller = new ChunkCoordinates(tagCompound.getInteger("ControllerX"), tagCompound.getInteger("ControllerY"), tagCompound.getInteger("ControllerZ"));
        }
    }
    
    public boolean validateController()
    {
        if (controller.posY == -1)
        {
            return false;
        }
        else
        {
            TileEntity tile = worldObj.getBlockTileEntity(controller.posX, controller.posY, controller.posZ);            
            return tile != null && tile instanceof TilePortalFrameController;
        }
    }
    
    public TilePortalFrameController getControllerValidated()
    {
        return validateController() ? (TilePortalFrameController) worldObj.getBlockTileEntity(controller.posX, controller.posY, controller.posZ) : null;
    }
    
    public PortalTexture getPortalTexture()
    {
        PortalTexture tex = new PortalTexture();
        TilePortalFrameController controller = getControllerValidated();
        
        if (controller != null)
        {
            tex = controller.portalTexture;
        }
        
        return tex;
    }
}
