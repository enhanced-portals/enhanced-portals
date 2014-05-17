package enhancedportals.portal;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import cpw.mods.fml.common.network.ByteBufUtils;
import enhancedportals.tileentity.TileController;

public class PortalTextureManager
{
    int frameColour, customFrameTexture;
    int portalColour, customPortalTexture;
    int particleColour, particleType;
    ItemStack[] inventory;
    TileController controller;

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
        controller = p.controller;
    }

    public PortalTextureManager(TileController controller)
    {
        this();
        this.controller = controller;
    }

    public int getCustomFrameTexture()
    {
        return customFrameTexture;
    }

    public int getCustomPortalTexture()
    {
        return customPortalTexture;
    }

    public int getFrameColour()
    {
        return frameColour;
    }

    public ItemStack getFrameItem()
    {
        return inventory[0];
    }

    public int getParticleColour()
    {
        return particleColour;
    }

    public int getParticleType()
    {
        return particleType;
    }

    public int getPortalColour()
    {
        return portalColour;
    }

    public ItemStack getPortalItem()
    {
        return inventory[1];
    }

    public boolean hasCustomFrameTexture()
    {
        return customFrameTexture > -1;
    }

    public boolean hasCustomPortalTexture()
    {
        return customPortalTexture > -1;
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
        NBTTagList l = t.getTagList("Inventory", 10);

        for (int i = 0; i < inventory.length; i++)
        {
            NBTTagCompound T = l.getCompoundTagAt(i);
            inventory[i] = ItemStack.loadItemStackFromNBT(T);
        }
    }

    public void setCustomFrameTexture(int i)
    {
        customFrameTexture = i;

        if (controller != null)
        {
            controller.markDirty();
        }
    }

    public void setCustomPortalTexture(int i)
    {
        customPortalTexture = i;

        if (controller != null)
        {
            controller.markDirty();
        }
    }

    public void setFrameColour(int i)
    {
        frameColour = i;

        if (controller != null)
        {
            controller.markDirty();
        }
    }

    public void setFrameItem(ItemStack s)
    {
        inventory[0] = s;

        if (controller != null)
        {
            controller.markDirty();
        }
    }

    public void setParticleColour(int i)
    {
        particleColour = i;

        if (controller != null)
        {
            controller.markDirty();
        }
    }

    public void setParticleType(int i)
    {
        particleType = i;

        if (controller != null)
        {
            controller.markDirty();
        }
    }

    public void setPortalColour(int i)
    {
        portalColour = i;

        if (controller != null)
        {
            controller.markDirty();
        }
    }

    public void setPortalItem(ItemStack s)
    {
        inventory[1] = s;

        if (controller != null)
        {
            controller.markDirty();
        }
    }

    public void usePacket(ByteBuf buffer)
    {
        frameColour = buffer.readInt();
        portalColour = buffer.readInt();
        particleColour = buffer.readInt();
        particleType = buffer.readInt();
        customFrameTexture = buffer.readInt();
        customPortalTexture = buffer.readInt();

        for (int i = 0; i < inventory.length; i++)
        {
            inventory[i] = ByteBufUtils.readItemStack(buffer);
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

        for (ItemStack element : inventory)
        {
            NBTTagCompound T = new NBTTagCompound();

            if (element != null)
            {
                element.writeToNBT(T);
            }

            l.appendTag(T); // Make sure we still write it when it's null, so inventory slots don't get shifted when reloading
        }

        t.setTag("Inventory", l);
        tag.setTag(tagName, t);
    }

    public void writeToPacket(ByteBuf buffer)
    {
        buffer.writeInt(frameColour);
        buffer.writeInt(portalColour);
        buffer.writeInt(particleColour);
        buffer.writeInt(particleType);
        buffer.writeInt(customFrameTexture);
        buffer.writeInt(customPortalTexture);

        for (ItemStack element : inventory)
        {
            ByteBufUtils.writeItemStack(buffer, element);
        }
    }
}
