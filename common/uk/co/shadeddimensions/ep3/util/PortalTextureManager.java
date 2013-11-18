package uk.co.shadeddimensions.ep3.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PortalTextureManager
{
    int frameColour, customFrameTexture;
    int portalColour, customPortalTexture;
    int particleColour, particleType;
    ItemStack[] inventory;
    
    public PortalTextureManager()
    {
        frameColour = portalColour = 0xffffff;
        particleColour = 0x0077D8;
        particleType = 0;
        customFrameTexture = customPortalTexture = -1;
        inventory = new ItemStack[2];
    }
    
    public PortalTextureManager(PortalTextureManager p)
    {
        frameColour = p.frameColour;
        portalColour = p.portalColour;
        particleColour = p.particleColour;
        particleType = p.particleType;
        customFrameTexture = p.customFrameTexture;
        customPortalTexture = p.customPortalTexture;
        inventory = p.inventory;
    }
    
    public void readFromNBT(NBTTagCompound tag, String tagName)
    {
        NBTTagCompound t = tag.getCompoundTag(tagName);
        frameColour = t.getInteger("frameColour");
        portalColour = t.getInteger("portalColour");
        particleColour = t.getInteger("particleColour");
        particleType = t.getInteger("particleType");
        customFrameTexture = t.getInteger("customFrameTexture");
        customPortalTexture = t.getInteger("customPortalTexture");        
        NBTTagList l = t.getTagList("Inventory");
        
        for (int i = 0; i < inventory.length; i++)
        {
            NBTTagCompound T = (NBTTagCompound) l.tagAt(i);            
            inventory[i] = ItemStack.loadItemStackFromNBT(T);
        }
    }
    
    public void writeToNBT(NBTTagCompound tag, String tagName)
    {
        NBTTagCompound t = new NBTTagCompound();
        t.setInteger("frameColour", frameColour);
        t.setInteger("portalColour", portalColour);
        t.setInteger("particleColour", particleColour);
        t.setInteger("particleType", particleType);
        t.setInteger("customFrameTexture", customFrameTexture);
        t.setInteger("customPortalTexture", customPortalTexture);        
        NBTTagList l = new NBTTagList();
        
        for (int i = 0; i < inventory.length; i++)
        {
            NBTTagCompound T = new NBTTagCompound();
            
            if (inventory[i] != null)
            {
                inventory[i].writeToNBT(T);
            }
            
            l.appendTag(T); // Make sure we still write it when it's null, so inventory slots don't get shifted when reloading
        }
        
        t.setTag("Inventory", l);        
        tag.setTag(tagName, t);
    }
    
    public void usePacket(DataInputStream stream) throws IOException
    {
        frameColour = stream.readInt();
        portalColour = stream.readInt();
        particleColour = stream.readInt();
        particleType = stream.readInt();
        customFrameTexture = stream.readInt();
        customPortalTexture = stream.readInt();
        
        for (int i = 0; i < inventory.length; i++)
        {
            int ID = stream.readInt(), meta = stream.readInt();
            
            if (ID == 0)
            {
                inventory[i] = null;
            }
            else
            {
                inventory[i] = new ItemStack(ID, 1, meta);
            }
        }
    }
    
    public void writeToPacket(DataOutputStream stream) throws IOException
    {
        stream.writeInt(frameColour);
        stream.writeInt(portalColour);
        stream.writeInt(particleColour);
        stream.writeInt(particleType);
        stream.writeInt(customFrameTexture);
        stream.writeInt(customPortalTexture);
        
        for (int i = 0; i < inventory.length; i++)
        {
            if (inventory[i] != null)
            {
                stream.writeInt(inventory[i].itemID);
                stream.writeInt(inventory[i].getItemDamage());
            }
            else
            {
                stream.writeInt(0);
                stream.writeInt(0);
            }
        }
    }
    
    public int getFrameColour()
    {
        return frameColour;
    }
    
    public void setFrameColour(int i)
    {
        frameColour = i;
    }
    
    public int getCustomFrameTexture()
    {
        return customFrameTexture;
    }
    
    public void setCustomFrameTexture(int i)
    {
        customFrameTexture = i;
    }
    
    public boolean hasCustomFrameTexture()
    {
        return customFrameTexture > -1;
    }
    
    public int getPortalColour()
    {
        return portalColour;
    }
    
    public void setPortalColour(int i)
    {
        portalColour = i;
    }
    
    public int getCustomPortalTexture()
    {
        return customPortalTexture;
    }
    
    public void setCustomPortalTexture(int i)
    {
        customPortalTexture = i;
    }
    
    public boolean hasCustomPortalTexture()
    {
        return customPortalTexture > -1;
    }
    
    public int getParticleColour()
    {
        return particleColour;
    }
    
    public void setParticleColour(int i)
    {
        particleColour = i;
    }
    
    public int getParticleType()
    {
        return particleType;
    }
    
    public void setParticleType(int i)
    {
        particleType = i;
    }
    
    public ItemStack getPortalItem()
    {
        return inventory[1];
    }
    
    public void setPortalItem(ItemStack s)
    {
        inventory[1] = s;
    }
    
    public ItemStack getFrameItem()
    {
        return inventory[0];
    }
    
    public void setFrameItem(ItemStack s)
    {
        inventory[0] = s;
    }
}
