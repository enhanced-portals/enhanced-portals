package enhancedportals.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.world.BlockPosition;
import enhancedportals.lib.BlockIds;
import enhancedportals.network.packet.PacketDialDeviceUpdate;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.portal.network.DialDeviceNetworkObject;

public class TileEntityDialDevice extends TileEntityDialDeviceBase
{
    public ArrayList<DialDeviceNetworkObject> destinationList;
    public int selectedDestination, tickTimer;

    public TileEntityDialDevice()
    {
        destinationList = new ArrayList<DialDeviceNetworkObject>();
        selectedDestination = -1;
        active = false;
        tickTimer = 720;
    }

    @Override
    protected int getBlockID()
    {
        return BlockIds.DialDevice;
    }

    public boolean hasDestination(DialDeviceNetworkObject obj2)
    {
        for (DialDeviceNetworkObject obj : destinationList)
        {
            if (obj.displayName.equals(obj2.displayName) && obj.texture.equals(obj2.texture) && obj.network.equals(obj2.network) && obj.thickness == obj2.thickness)
            {
                return true;
            }
        }

        return false;
    }

    public void processDiallingRequest(int id, EntityPlayer player)
    {
        DialDeviceNetworkObject obj = destinationList.get(id);
        processDiallingRequest(obj.network, player, obj.texture, obj.thickness, tickTimer);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        timer = tagCompound.getInteger("TickTimer");

        if (tagCompound.hasKey("mX"))
        {
            active = true;
            modifierLocation = new BlockPosition(tagCompound.getInteger("mX"), tagCompound.getInteger("mY"), tagCompound.getInteger("mZ"));
            timer = tagCompound.getInteger("Timer");
            ticksToGo = tagCompound.getInteger("TicksToGo");
            dialledNetwork = tagCompound.getString("DialledNetwork");
        }

        NBTTagList list = tagCompound.getTagList("Entries");

        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);

            String name = tag.getString("Name"), network = tag.getString("Network");
            byte thickness = tag.getByte("Thickness");
            String texture = tag.getString("Texture");

            destinationList.add(new DialDeviceNetworkObject(name, network, texture, thickness));
        }
    }

    public boolean removeDestination(DialDeviceNetworkObject obj2)
    {
        for (int i = 0; i < destinationList.size(); i++)
        {
            DialDeviceNetworkObject obj = destinationList.get(i);

            if (obj.displayName.equals(obj2.displayName) && obj.texture.equals(obj2.texture) && obj.network.equals(obj2.network) && obj.thickness == obj2.thickness)
            {
                destinationList.remove(i);
                return true;
            }
        }

        return false;
    }

    @Override
    protected void sendUpdatePacket()
    {
        PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketDialDeviceUpdate(this)));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("TickTimer", tickTimer);

        if (active)
        {
            tagCompound.setInteger("mX", modifierLocation.getX());
            tagCompound.setInteger("mY", modifierLocation.getY());
            tagCompound.setInteger("mZ", modifierLocation.getZ());
            tagCompound.setInteger("Timer", timer);
            tagCompound.setInteger("TicksToGo", ticksToGo);
            tagCompound.setString("DialledNetwork", dialledNetwork);
        }

        NBTTagList list = new NBTTagList();

        for (int i = 0; i < destinationList.size(); i++)
        {
            NBTTagCompound tag = new NBTTagCompound();
            DialDeviceNetworkObject obj = destinationList.get(i);

            if (obj == null)
            {
                continue;
            }

            tag.setString("Name", obj.displayName);
            tag.setString("Network", obj.network);
            tag.setByte("Thickness", obj.thickness);
            tag.setString("Texture", obj.texture);

            list.appendTag(tag);
        }

        tagCompound.setTag("Entries", list);
    }
}
