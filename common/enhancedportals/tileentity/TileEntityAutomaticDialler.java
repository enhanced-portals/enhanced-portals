package enhancedportals.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import enhancedcore.world.WorldPosition;
import enhancedportals.lib.BlockIds;

public class TileEntityAutomaticDialler extends TileEntityEnhancedPortals
{
    public boolean previousRedstone;
    public String activeNetwork;
    public WorldPosition connectedModifier;

    public TileEntityAutomaticDialler()
    {
        previousRedstone = false;
        activeNetwork = "";
    }

    public boolean getConnectedModifier()
    {
        if (connectedModifier != null)
        {
            return true;
        }

        WorldPosition location = new WorldPosition(xCoord, yCoord, zCoord, worldObj);

        if (location.getOffset(ForgeDirection.UP).getBlockId() == BlockIds.DialDeviceBasic)
        {
            connectedModifier = location.getOffset(ForgeDirection.UP);
        }
        else if (location.getOffset(ForgeDirection.DOWN).getBlockId() == BlockIds.DialDeviceBasic)
        {
            connectedModifier = location.getOffset(ForgeDirection.DOWN);
        }
        else if (location.getOffset(ForgeDirection.NORTH).getBlockId() == BlockIds.DialDeviceBasic)
        {
            connectedModifier = location.getOffset(ForgeDirection.NORTH);
        }
        else if (location.getOffset(ForgeDirection.SOUTH).getBlockId() == BlockIds.DialDeviceBasic)
        {
            connectedModifier = location.getOffset(ForgeDirection.SOUTH);
        }
        else if (location.getOffset(ForgeDirection.EAST).getBlockId() == BlockIds.DialDeviceBasic)
        {
            connectedModifier = location.getOffset(ForgeDirection.EAST);
        }
        else if (location.getOffset(ForgeDirection.WEST).getBlockId() == BlockIds.DialDeviceBasic)
        {
            connectedModifier = location.getOffset(ForgeDirection.WEST);
        }
        else
        {
            return false;
        }

        return true;
    }

    public void handleNeighborUpdate()
    {
        boolean redstoneSignal = worldObj.getStrongestIndirectPower(xCoord, yCoord, zCoord) > 0;

        if (redstoneSignal && !previousRedstone)
        {
            if (!getConnectedModifier())
            {
                return;
            }

            if (connectedModifier.getTileEntity() != null && connectedModifier.getTileEntity() instanceof TileEntityDialDeviceBasic)
            {
                TileEntityDialDeviceBasic dialDevice = (TileEntityDialDeviceBasic) connectedModifier.getTileEntity();
                dialDevice.processDiallingRequest(activeNetwork, null);
            }
        }

        previousRedstone = redstoneSignal;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        activeNetwork = tagCompound.getString("Network");
        previousRedstone = tagCompound.getBoolean("Redstone");
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            // TODO PacketDispatcher.sendPacketToServer(new
            // PacketRequestSync(this).getPacket());
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setString("Network", activeNetwork);
        tagCompound.setBoolean("Redstone", previousRedstone);
    }
}
