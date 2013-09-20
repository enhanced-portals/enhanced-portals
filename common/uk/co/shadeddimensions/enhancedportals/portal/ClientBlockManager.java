package uk.co.shadeddimensions.enhancedportals.portal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameBiometric;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameDialDevice;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameNetworkInterface;

public class ClientBlockManager extends BlockManager
{
    public int portal;
    public int portalFrame;
    public int redstone;
    public boolean biometric;
    public boolean dialDevice;
    public boolean networkInterface;
    
    @Override
    public void saveData(NBTTagCompound tag)
    {
        
    }
    
    @Override
    public void loadData(NBTTagCompound tag)
    {
        
    }
    
    @Override
    public TilePortalFrameBiometric getBiometric(World world)
    {
        return null;
    }
    
    @Override
    public TilePortalFrameDialDevice getDialDevice(World world)
    {
        return null;
    }
    
    @Override
    public TilePortalFrameNetworkInterface getNetworkInterface(World world)
    {
        return null;
    }
}
