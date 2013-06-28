package enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.world.WorldPosition;
import enhancedportals.lib.BlockIds;
import enhancedportals.network.packet.PacketBasicDialDeviceUpdate;
import enhancedportals.network.packet.PacketEnhancedPortals;

public class TileEntityDialDeviceBasic extends TileEntityDialDeviceBase
{
    public TileEntityDialDeviceBasic()
    {
        active = false;
    }

    @Override
    protected int getBlockID()
    {
        return BlockIds.DialDeviceBasic;
    }

    public void processDiallingRequest(String network, EntityPlayer player)
    {
        processDiallingRequest(network, player, null, -1, -1);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        if (tagCompound.hasKey("ModifierX"))
        {
            active = true;
            modifierLocation = new WorldPosition(tagCompound.getInteger("ModifierX"), tagCompound.getInteger("ModifierY"), tagCompound.getInteger("ModifierZ"), tagCompound.getInteger("ModifierD"));
            ticksToGo = tagCompound.getInteger("TicksToGo");
            dialledNetwork = tagCompound.getString("DialledNetwork");
        }
    }

    @Override
    protected void sendUpdatePacket()
    {
        PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketBasicDialDeviceUpdate(this)));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        if (active)
        {
            tagCompound.setInteger("ModifierX", modifierLocation.getX());
            tagCompound.setInteger("ModifierY", modifierLocation.getY());
            tagCompound.setInteger("ModifierZ", modifierLocation.getZ());
            tagCompound.setInteger("TicksToGo", ticksToGo);
            tagCompound.setString("DialledNetwork", dialledNetwork);
        }
    }
}
