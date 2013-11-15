package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;
import uk.co.shadeddimensions.ep3.util.WorldUtils;

public class TileScannerFrame extends TileEnhancedPortals
{
    WorldCoordinates scanner;
    
    public TileScanner getScanner()
    {
        if (scanner == null)
        {
            return null;
        }
        
        TileEntity tile = scanner.getBlockTileEntity();
                
        return tile instanceof TileScanner ? (TileScanner) tile : null;
    }
    
    @Override
    public boolean activate(EntityPlayer player)
    {
        if (worldObj.isRemote || player.inventory.getCurrentItem() == null || player.inventory.getCurrentItem().itemID != CommonProxy.itemWrench.itemID)
        {
            return false;
        }
        
        TileScanner scanner = getScanner();
        
        if (scanner == null || (!scanner.checkStructure(true, true) && !scanner.checkStructure(false, true)))
        {
            return false;
        }
        
        if (player.isSneaking())
        {
            scanner.performScan();
        }
        else
        {
            CommonProxy.openGui(player, GUIs.Scanner, scanner);
        }
        
        return true;
    }
    
    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        if (scanner != null)
        {
            stream.writeInt(scanner.posX);
            stream.writeInt(scanner.posY);
            stream.writeInt(scanner.posZ);
        }
        else
        {
            stream.writeInt(-1);
            stream.writeInt(-1);
            stream.writeInt(-1);
        }
    }
    
    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        WorldCoordinates c = new WorldCoordinates(stream.readInt(), stream.readInt(), stream.readInt(), worldObj.provider.dimensionId);
        
        if (c.posY != -1)
        {
            scanner = c;
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        
        if (scanner != null)
        {
             WorldUtils.saveWorldCoord(tag, scanner, "scanner");
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        
        if (tag.hasKey("scanner"))
        {
            scanner = WorldUtils.loadWorldCoord(tag, "scanner");
        }
    }
}
