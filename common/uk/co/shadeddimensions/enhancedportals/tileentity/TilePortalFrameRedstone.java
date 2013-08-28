package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;

public class TilePortalFrameRedstone extends TilePortalFrame
{
    byte previousRedstoneInputState = 0;
    
    @Override
    public Icon getTexture(int side, int renderpass)
    {
        if (renderpass == 1 && ClientProxy.isWearingGoggles)
        {
            return BlockFrame.redstoneOverlay;
        }
        
        return null;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        
        tagCompound.setByte("redstoneInputState", previousRedstoneInputState);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        
        previousRedstoneInputState = tagCompound.getByte("redstoneInputState");
    }
    
    @Override
    public boolean activate(EntityPlayer player)
    {
        return super.activate(player);
    }
    
    @Override
    public void neighborChanged(int id)
    {
        if (worldObj.isRemote)
        {
            return;
        }
        
        byte redstoneInputState = getHighestPowerState();
        
        if (redstoneInputState > 0 && previousRedstoneInputState == 0)
        {
            TilePortalFrameController c = getControllerValidated();
            
            if (c != null)
            {
                c.createPortal();
            }
        }
        else if (redstoneInputState == 0 && previousRedstoneInputState > 0)
        {
            TilePortalFrameController c = getControllerValidated();
            
            if (c != null)
            {
                c.removePortal();
            }
        }
        
        previousRedstoneInputState = redstoneInputState;
    }
    
    private byte getHighestPowerState()
    {
        byte current = 0;
        
        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            byte c = (byte) worldObj.getIndirectPowerLevelTo(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ, i);
            
            if (c > current)
            {
                current = c;
            }
        }
        
        return current;
    }
}
