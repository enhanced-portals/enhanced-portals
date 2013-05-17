package enhancedportals.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;
import enhancedportals.network.packet.PacketRequestSync;
import enhancedportals.portal.Portal;
import enhancedportals.portal.PortalTexture;

public class TileEntityNetherPortal extends TileEntityEnhancedPortals
{
    public PortalTexture texture;
    public boolean producesSound, producesParticles;
    public byte thickness;
    public WorldLocation parentModifier;
    public boolean hasParent;

    public TileEntityNetherPortal()
    {
        texture = new PortalTexture((byte) 0);
        producesSound = true;
        producesParticles = true;
        thickness = 0;
        hasParent = false;
    }

    @Override
    public PacketData getPacketData()
    {
        PacketData data = new PacketData();
        data.integerData = new int[] { texture.colour, texture.blockID, texture.metaData, producesSound ? 1 : 0, producesParticles ? 1 : 0, thickness, parentModifier != null ? 1 : 0 };
        data.stringData = new String[] { texture.liquidID };

        return data;
    }

    @Override
    public void parsePacketData(PacketData data)
    {
        if (data == null || data.integerData == null || data.integerData.length != 7 || data.stringData.length != 1)
        {
            System.out.println("Unexpected packet recieved." + data);
            return;
        }

        PortalTexture newTexture;
        boolean sound, particles;
        byte portalThickness;

        if (data.integerData[0] != -1)
        {
            newTexture = new PortalTexture((byte) data.integerData[0]);
        }
        else if (data.integerData[1] != -1)
        {
            newTexture = new PortalTexture(data.integerData[1], data.integerData[2]);
        }
        else
        {
            newTexture = new PortalTexture(data.stringData[0]);
        }

        sound = data.integerData[3] == 1;
        particles = data.integerData[4] == 1;
        portalThickness = (byte) data.integerData[5];

        Portal portal = new Portal(xCoord, yCoord, zCoord, worldObj);
        portal.updateTexture(newTexture);
        portal.updateData(sound, particles, portalThickness);

        if (worldObj.isRemote)
        {
            hasParent = data.integerData[6] == 1;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        byte colour = tagCompound.getByte("Colour");
        int blockID = tagCompound.getInteger("BlockID"), metadata = tagCompound.getInteger("Metadata");
        String liquid = tagCompound.getString("LiquidID");

        if (colour != -1)
        {
            texture = new PortalTexture(colour);
        }
        else if (blockID != -1)
        {
            texture = new PortalTexture(blockID, metadata);
        }
        else
        {
            texture = new PortalTexture(liquid);
        }

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

        tagCompound.setByte("Colour", texture.colour);
        tagCompound.setInteger("BlockID", texture.blockID);
        tagCompound.setString("LiquidID", texture.liquidID);
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
            tagCompound.setInteger("ParentD", parentModifier.dimension);
        }
    }
}
