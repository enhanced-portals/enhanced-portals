package enhancedportals.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.lib.Portal;
import enhancedportals.lib.PortalTexture;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;
import enhancedportals.network.packet.PacketRequestSync;
import enhancedportals.network.packet.PacketTEUpdate;

public class TileEntityNetherPortal extends TileEntityEnhancedPortals
{
    public PortalTexture texture;
    WorldLocation parentModifier;
    
    public TileEntityNetherPortal()
    {
        texture = new PortalTexture(0);
    }
    
    public PortalTexture getTexture()
    {
        return texture;
    }
    
    public WorldLocation getParentModifier()
    {
        return parentModifier;
    }

    public void setTexture(PortalTexture texture)
    {
        if (this.texture.isEqualTo(texture))
        {
            System.out.println("Texture is old texture: " + texture.blockID + ", " + (worldObj.isRemote ? "CLIENT" : "SERVER"));
            return;
        }
        
        this.texture = texture;
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
                
        if (!worldObj.isRemote)
        {
            if (getBlockMetadata() == 3 || getBlockMetadata() == 5 || getBlockMetadata() == 7)
            {
                PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
            }
        }
    }
    
    public void setParentModifier(WorldLocation loc)
    {
        this.parentModifier = loc;
    }
    
    @Override
    public void parsePacketData(PacketData data)
    {
        if (data == null || data.integerData == null || data.integerData.length != 3)
        {
            System.out.println("Unexpected packet recieved." + data);
            return;
        }
                
        PortalTexture newTexture;
        
        if (data.integerData[0] != -1)
        {
            newTexture = new PortalTexture(data.integerData[0]);
        }
        else
        {
            newTexture = new PortalTexture(data.integerData[1], data.integerData[2]);
        }
        
        new Portal(xCoord, yCoord, zCoord, worldObj).updateTexture(newTexture);
        
        //PortalHandler.floodFillTexture(new WorldLocation(xCoord, yCoord, zCoord, worldObj), newTexture, texture, getBlockMetadata());
    }
    
    @Override
    public PacketData getPacketData()
    {
        PacketData data = new PacketData();
        data.integerData = new int[] { texture.colour == null ? -1 : texture.colour.ordinal(), texture.blockID, texture.metaData };
        
        return data;
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
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        
        int colour = tagCompound.getInteger("Colour"),
            blockID = tagCompound.getInteger("BlockID"),
            metadata = tagCompound.getInteger("Metadata");
        
        if (colour != -1)
        {
            texture = new PortalTexture(colour);
        }
        else
        {
            texture = new PortalTexture(blockID, metadata);
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        
        tagCompound.setInteger("Colour", texture.colour == null ? -1 : texture.colour.ordinal());
        tagCompound.setInteger("BlockID", texture.blockID);
        tagCompound.setInteger("Metadata", texture.metaData);
    }
}
