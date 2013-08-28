package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketRequestData;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TilePortalFrame extends TileEP
{
    public ChunkCoordinates controller;
    
    public TilePortalFrame()
    {
        controller = new ChunkCoordinates(0, -1, 0);
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
    
    @Override
    public Icon getTexture(int side, int renderpass)
    {
        return null;
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketRequestData(this)));
        }
    }
    
    public boolean activate(EntityPlayer player)
    {
        return false;
    }
    
    public void neighborChanged(int id)
    {
        
    }
}
