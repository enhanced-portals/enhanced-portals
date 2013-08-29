package uk.co.shadeddimensions.enhancedportals.tileentity;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
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
    
    public boolean isTouchingPortal()
    {
        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            
            if (worldObj.getBlockId(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ) == CommonProxy.blockPortal.blockID)
            {
                return true;
            }
        }
        
        return false;
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
    
    public void selfBroken()
    {
        TilePortalFrameController control = getControllerValidated();

        if (control != null)
        {
            if (isTouchingPortal())
            {
                control.destroyAllPortal();
            }

            control.removeFrame(this);
        }
    }
    
    public int isProvidingStrongPower(int side)
    {
        return 0;
    }
    
    public int isProvidingWeakPower(int side)
    {
        return 0;
    }
    
    public void scheduledTick(Random random)
    {
        
    }

    public void entityTouch(Entity entity)
    {
        
    }
}
