package enhancedportals.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.world.WorldPosition;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketRequestData;

public class TileEntityNetherPortal extends TileEntityEnhancedPortals
{
    public String texture;
    public boolean producesSound, producesParticles;
    public byte thickness;
    private WorldPosition parentModifier;
    public boolean hasParent;

    public TileEntityNetherPortal()
    {
        texture = "";
        producesSound = true;
        producesParticles = true;
        thickness = 0;
        hasParent = false;
    }

    public WorldPosition getParentModifier()
    {
        hasParent = parentModifier != null;
        return parentModifier;
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
            setParentModifier(new WorldPosition(x, y, z, d));
        }
    }

    public void setParentModifier(WorldPosition loc)
    {
        parentModifier = loc;
        hasParent = parentModifier != null;
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            if (getBlockMetadata() == 3 || getBlockMetadata() == 5 || getBlockMetadata() == 7)
            {
                PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketRequestData(this)));
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
            tagCompound.setInteger("ParentX", parentModifier.getX());
            tagCompound.setInteger("ParentY", parentModifier.getY());
            tagCompound.setInteger("ParentZ", parentModifier.getZ());
            tagCompound.setInteger("ParentD", parentModifier.getDimension());
        }
    }
}
