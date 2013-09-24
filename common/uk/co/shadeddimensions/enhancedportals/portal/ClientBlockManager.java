package uk.co.shadeddimensions.enhancedportals.portal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileNetworkInterface;

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
    public TileBiometricIdentifier getBiometric(World world)
    {
        return null;
    }
    
    @Override
    public TileDiallingDevice getDialDevice(World world)
    {
        return null;
    }
    
    @Override
    public TileNetworkInterface getNetworkInterface(World world)
    {
        return null;
    }
}
