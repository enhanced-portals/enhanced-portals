package enhancedportals.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;

public class TileEntityAutomaticDialler extends TileEntityEnhancedPortals
{
    /**
     * Redstone setting - 0 = default, -1 = activate on no signal, 1+ = activate on that level of redstone signal
     */
    public byte redstoneSetting, previousRedstoneSetting;
    public String activeNetwork;
    public WorldLocation connectedModifier;
    
    public TileEntityAutomaticDialler()
    {
        redstoneSetting = 0;
        activeNetwork = "";
    }
    
    public boolean getSimple()
    {
        return getBlockMetadata() == 0;
    }

    @Override
    public PacketData getPacketData()
    {
        return new PacketData(new int[0], new byte[] { redstoneSetting }, new String[] { activeNetwork });
    }

    @Override
    public void parsePacketData(PacketData data)
    {
        redstoneSetting = data.byteData[0];
        activeNetwork = data.stringData[0];
    }
    
    public void getConnectedModifier()
    {
        WorldLocation location = new WorldLocation(xCoord, yCoord, zCoord, worldObj);
        int lookingForBlock = blockMetadata == 0 ? BlockIds.DialHomeDeviceBasic : BlockIds.DialHomeDevice;

        if (location.getOffset(ForgeDirection.UP).getBlockId() == lookingForBlock)
        {
            connectedModifier = location.getOffset(ForgeDirection.UP);
        }
        else if (location.getOffset(ForgeDirection.DOWN).getBlockId() == lookingForBlock)
        {
            connectedModifier = location.getOffset(ForgeDirection.DOWN);
        }
        else if (location.getOffset(ForgeDirection.NORTH).getBlockId() == lookingForBlock)
        {
            connectedModifier = location.getOffset(ForgeDirection.NORTH);
        }
        else if (location.getOffset(ForgeDirection.SOUTH).getBlockId() == lookingForBlock)
        {
            connectedModifier = location.getOffset(ForgeDirection.SOUTH);
        }
        else if (location.getOffset(ForgeDirection.EAST).getBlockId() == lookingForBlock)
        {
            connectedModifier = location.getOffset(ForgeDirection.EAST);
        }
        else if (location.getOffset(ForgeDirection.WEST).getBlockId() == lookingForBlock)
        {
            connectedModifier = location.getOffset(ForgeDirection.WEST);
        }
    }
    
    @Override
    public void validate()
    {
        super.validate();
    }    
    
    public void handleNeighborUpdate()
    {
        int redstoneSignal = worldObj.getStrongestIndirectPower(xCoord, yCoord, zCoord);
        
        if (previousRedstoneSetting > 0)
        {
            return;
        }
        
        if ((16 - redstoneSetting) - redstoneSignal > 0)
        {            
            if (getSimple())
            {
                getConnectedModifier();
                
                if (connectedModifier == null)
                {
                    return;
                }
                
                if (connectedModifier.getTileEntity() != null && connectedModifier.getTileEntity() instanceof TileEntityDialDeviceBasic)
                {
                    TileEntityDialDeviceBasic dialDevice = (TileEntityDialDeviceBasic) connectedModifier.getTileEntity();
                    
                    dialDevice.processDiallingRequest(activeNetwork, null);
                }
            }
        }
        
        previousRedstoneSetting = (byte) redstoneSignal;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        
        tagCompound.setString("Network", activeNetwork);
        tagCompound.setByte("Redstone", redstoneSetting);
        tagCompound.setByte("PreviousRedstone", previousRedstoneSetting);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        
        activeNetwork = tagCompound.getString("Network");
        redstoneSetting = tagCompound.getByte("Redstone");
        previousRedstoneSetting = tagCompound.getByte("PreviousRedstone");
    }
}
