package enhancedportals.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.lib.Portal;
import enhancedportals.lib.PortalTexture;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;
import enhancedportals.network.packet.PacketRequestSync;

public class TileEntityNetherPortal extends TileEntityEnhancedPortals
{
    public PortalTexture texture;
    public boolean producesSound, producesParticles;
    public byte thickness;
    public WorldLocation parentModifier;

    public TileEntityNetherPortal()
    {
        texture = new PortalTexture(0);
        producesSound = true;
        producesParticles = true;
        thickness = 0;
    }

    @Override
    public PacketData getPacketData()
    {
        PacketData data = new PacketData();
        data.integerData = new int[] { texture.colour == null ? -1 : texture.colour.ordinal(), texture.blockID, texture.metaData, producesSound ? 1 : 0, producesParticles ? 1 : 0, thickness };

        return data;
    }

    @Override
    public void parsePacketData(PacketData data)
    {
        if (data == null || data.integerData == null || data.integerData.length != 6)
        {
            System.out.println("Unexpected packet recieved." + data);
            return;
        }

        PortalTexture newTexture;
        boolean sound, particles;
        byte portalThickness;

        if (data.integerData[0] != -1)
        {
            newTexture = new PortalTexture(data.integerData[0]);
        }
        else
        {
            newTexture = new PortalTexture(data.integerData[1], data.integerData[2]);
        }

        sound = data.integerData[3] == 1;
        particles = data.integerData[4] == 1;
        portalThickness = (byte) data.integerData[5];

        Portal portal = new Portal(xCoord, yCoord, zCoord, worldObj);
        portal.updateTexture(newTexture);
        portal.updateData(sound, particles, portalThickness);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        int colour = tagCompound.getInteger("Colour"), blockID = tagCompound.getInteger("BlockID"), metadata = tagCompound.getInteger("Metadata");

        if (colour != -1)
        {
            texture = new PortalTexture(colour);
        }
        else
        {
            texture = new PortalTexture(blockID, metadata);
        }

        producesSound = tagCompound.getBoolean("Sound");
        producesParticles = tagCompound.getBoolean("Particles");
        thickness = tagCompound.getByte("Thickness");
        
        if (tagCompound.getBoolean("HasModifier"))
        {
            int x = tagCompound.getInteger("ParentX"), y = tagCompound.getInteger("ParentY"), z = tagCompound.getInteger("ParentZ");
            parentModifier = new WorldLocation(x, y, z);
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

        tagCompound.setInteger("Colour", texture.colour == null ? -1 : texture.colour.ordinal());
        tagCompound.setInteger("BlockID", texture.blockID);
        tagCompound.setInteger("Metadata", texture.metaData);
        tagCompound.setBoolean("Sound", producesSound);
        tagCompound.setBoolean("Particles", producesParticles);
        tagCompound.setByte("Thickness", thickness);
        tagCompound.setBoolean("HasModifier", parentModifier != null);
        
        if (parentModifier != null)
        {
            tagCompound.setInteger("ParentX", parentModifier.xCoord);
            tagCompound.setInteger("ParentY", parentModifier.yCoord);
            tagCompound.setInteger("ParentZ", parentModifier.zCoord);
        }
    }
}
