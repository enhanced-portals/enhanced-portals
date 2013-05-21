package enhancedportals.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;
import enhancedportals.network.packet.PacketRequestSync;
import enhancedportals.portal.Portal;

public class TileEntityNetherPortal extends TileEntityEnhancedPortals
{
    public String texture;
    public boolean producesSound, producesParticles;
    public byte thickness;
    public WorldLocation parentModifier;
    public boolean hasParent;

    public TileEntityNetherPortal()
    {
        texture = "";
        producesSound = true;
        producesParticles = true;
        thickness = 0;
        hasParent = false;
    }

    @Override
    public PacketData getPacketData()
    {
        PacketData data = new PacketData();
        data.integerData = new int[] { producesSound ? 1 : 0, producesParticles ? 1 : 0, thickness, parentModifier != null ? 1 : 0 };
        data.stringData = new String[] { texture };

        return data;
    }

    @Override
    public void parsePacketData(PacketData data)
    {
        if (data == null || data.integerData == null || data.integerData.length != 4 || data.stringData.length != 1)
        {
            System.out.println("Unexpected packet recieved." + data);
            return;
        }

        Portal portal = new Portal(xCoord, yCoord, zCoord, worldObj);
        portal.updateTexture(data.stringData[0]);
        portal.updateData(data.integerData[0] == 1, data.integerData[1] == 1, (byte) data.integerData[2]);

        if (worldObj.isRemote)
        {
            hasParent = data.integerData[3] == 1;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        texture = tagCompound.getString("Texture");
        producesSound = tagCompound.getBoolean("Sound");
        producesParticles = tagCompound.getBoolean("Particles");
        thickness = tagCompound.getByte("Thickness");

        if (tagCompound.getBoolean("HasModifier"))
        {
            int x = tagCompound.getInteger("ParentX"), y = tagCompound.getInteger("ParentY"), z = tagCompound.getInteger("ParentZ"), d = tagCompound.getInteger("ParentD");
            parentModifier = new WorldLocation(x, y, z, d);
        }
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            if (getBlockMetadata() == 3 || getBlockMetadata() == 5 || getBlockMetadata() == 7)
            {
                PacketDispatcher.sendPacketToServer(new PacketRequestSync(this).getPacket());
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setString("Texture", texture);
        tagCompound.setBoolean("Sound", producesSound);
        tagCompound.setBoolean("Particles", producesParticles);
        tagCompound.setByte("Thickness", thickness);
        tagCompound.setBoolean("HasModifier", parentModifier != null);

        if (parentModifier != null)
        {
            tagCompound.setInteger("ParentX", parentModifier.xCoord);
            tagCompound.setInteger("ParentY", parentModifier.yCoord);
            tagCompound.setInteger("ParentZ", parentModifier.zCoord);
            tagCompound.setInteger("ParentD", parentModifier.dimension);
        }
    }
}
