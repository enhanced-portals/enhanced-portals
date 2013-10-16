package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class TileEnhancedPortals extends TileEntity
{
    public String owner;
    public boolean isOwnerLocked;
    boolean hasPower;
    
    public TileEnhancedPortals()
    {
        owner = "";
        isOwnerLocked = hasPower = false;
    }
    
    public ChunkCoordinates getChunkCoordinates()
    {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }
    
    public WorldCoordinates getWorldCoordinates()
    {
        return new WorldCoordinates(xCoord, yCoord, zCoord, worldObj.provider.dimensionId);
    }
    
    public void guiButtonPressed(int buttonID, EntityPlayer player)
    {
        
    }
    
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        if (payload.data.hasKey("isOwnerLocked") && player.getEntityName().equals(owner))
        {
            isOwnerLocked = payload.data.getBoolean("isOwnerLocked");
            CommonProxy.sendUpdatePacketToAllAround(this);
        }
    }
    
    public String getOwner()
    {
        return owner;
    }
    
    public void setOwner(Entity entity)
    {
        owner = entity != null ? entity.getEntityName() : null;
    }
    
    public void onBlockPlacedBy(EntityLivingBase entity, ItemStack stack)
    {
        setOwner(entity);
    }
    
    public void onNeighborBlockChange(int blockID)
    {
        
    }
    
    public void updateTick(Random random)
    {
        
    }
    
    public void onEntityCollidedWithBlock(Entity entity)
    {
        
    }
    
    public boolean activate(EntityPlayer player)
    {
        return false;
    }
    
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        
    }
    
    public int isProvidingStrongPower(int side)
    {
        return 0;
    }
    
    public int isProvidingWeakPower(int side)
    {
        return 0;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        
        tag.setString("owner", owner);
        tag.setBoolean("isOwnerLocked", isOwnerLocked);
        tag.setBoolean("hasPower", hasPower);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        
        owner = tag.getString("owner");
        isOwnerLocked = tag.getBoolean("isOwnerLocked");
        hasPower = tag.getBoolean("hasPower");
    }
    
    public void usePacket(DataInputStream stream) throws IOException
    {
        owner = stream.readUTF();
        isOwnerLocked = stream.readBoolean();
        hasPower = stream.readBoolean();
    }
    
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        stream.writeUTF(owner);
        stream.writeBoolean(isOwnerLocked);
        stream.writeBoolean(hasPower);
    }
}
