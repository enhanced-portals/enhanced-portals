package uk.co.shadeddimensions.enhancedportals.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.WorldServer;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.lib.GuiIds;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.portal.PortalUtils;
import uk.co.shadeddimensions.enhancedportals.util.NBTHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TilePortalFrameController extends TilePortalFrame implements IInventory
{
    public String UniqueIdentifier;

    public int FrameColour;
    public int PortalColour;
    public int ParticleColour;
    public int ParticleType;

    public List<ChunkCoordinates> portalFrame;
    public List<ChunkCoordinates> portalFrameRedstone;
    public List<ChunkCoordinates> portalBlocks;

    @SideOnly(Side.CLIENT)
    public int attachedFrames;
    @SideOnly(Side.CLIENT)
    public int attachedFrameRedstone;
    @SideOnly(Side.CLIENT)
    public int attachedPortals;

    boolean hasInitialized;

    ItemStack[] inventory;

    public TilePortalFrameController()
    {
        FrameColour = PortalColour = 0xffffff;
        ParticleColour = 0xB336A1;
        ParticleType = 0;

        portalFrame = new ArrayList<ChunkCoordinates>();
        portalFrameRedstone = new ArrayList<ChunkCoordinates>();
        portalBlocks = new ArrayList<ChunkCoordinates>();

        hasInitialized = false;
        UniqueIdentifier = "NOT_SET";

        inventory = new ItemStack[2];
    }

    public int getAttachedFrames()
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            return portalFrame.size();
        }
        else
        {
            return attachedFrames;
        }
    }

    public int getAttachedFrameRedstone()
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            return portalFrameRedstone.size();
        }
        else
        {
            return attachedFrameRedstone;
        }
    }

    public int getAttachedPortals()
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            return portalBlocks.size();
        }
        else
        {
            return attachedPortals;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        NBTHelper.saveCCList(tagCompound, portalFrame, "portalFrame");
        NBTHelper.saveCCList(tagCompound, portalFrameRedstone, "portalFrameRedstone");
        NBTHelper.saveCCList(tagCompound, portalBlocks, "portalBlocks");

        tagCompound.setBoolean("initialized", hasInitialized);
        tagCompound.setString("identifier", UniqueIdentifier);

        tagCompound.setInteger("FrameColour", FrameColour);
        tagCompound.setInteger("PortalColour", PortalColour);
        tagCompound.setInteger("ParticleColour", ParticleColour);
        tagCompound.setInteger("ParticleType", ParticleType);

        NBTTagList list = new NBTTagList();
        for (ItemStack s : inventory)
        {
            if (s == null)
            {
                continue;
            }

            NBTTagCompound compound = new NBTTagCompound();
            s.writeToNBT(compound);
            list.appendTag(compound);
        }

        tagCompound.setTag("Inventory", list);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        portalFrame = NBTHelper.loadCCList(tagCompound, "portalFrame");
        portalFrameRedstone = NBTHelper.loadCCList(tagCompound, "portalFrameRedstone");
        portalBlocks = NBTHelper.loadCCList(tagCompound, "portalBlocks");

        hasInitialized = tagCompound.getBoolean("initialized");
        UniqueIdentifier = tagCompound.getString("identifier");

        FrameColour = tagCompound.getInteger("FrameColour");
        PortalColour = tagCompound.getInteger("PortalColour");
        ParticleColour = tagCompound.getInteger("ParticleColour");
        ParticleType = tagCompound.getInteger("ParticleType");

        NBTTagList list = tagCompound.getTagList("Inventory");
        for (int i = 0; i < list.tagList.size(); i++)
        {
            inventory[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) list.tagList.get(i));
        }
    }

    @Override
    public Icon getTexture(int side, int renderpass)
    {
        if (renderpass == 1 && ClientProxy.isWearingGoggles)
        {
            return BlockFrame.typeOverlayIcons[1];
        }
        else
        {
            ItemStack s = getStackInSlot(0);

            if (s != null && s.getItemSpriteNumber() == 0 && s.itemID != CommonProxy.blockFrame.blockID)
            {
                return Block.blocksList[s.itemID].getIcon(side, s.getItemDamage());
            }
        }

        return null;
    }

    @Override
    public boolean activate(EntityPlayer player)
    {
        if (!worldObj.isRemote)
        {
            if (!hasInitialized)
            {
                byte status = PortalUtils.linkPortalController((WorldServer) worldObj, xCoord, yCoord, zCoord);

                if (status == 0)
                {
                    player.sendChatToPlayer(ChatMessageComponent.createFromText(EnumChatFormatting.GREEN + "Success: " + EnumChatFormatting.WHITE + String.format("Successfully linked %s frame and %s portal blocks", getAttachedFrames(), getAttachedPortals())));
                    hasInitialized = true;
                }
                else if (status == 1)
                {
                    player.sendChatToPlayer(ChatMessageComponent.createFromText(EnumChatFormatting.RED + "Error: " + EnumChatFormatting.WHITE + "An unknown error occurred"));
                }
                else if (status == 3)
                {
                    player.sendChatToPlayer(ChatMessageComponent.createFromText(EnumChatFormatting.RED + "Error: " + EnumChatFormatting.WHITE + "Another controller was found"));
                }
                else if (status == 4)
                {
                    player.sendChatToPlayer(ChatMessageComponent.createFromText(EnumChatFormatting.RED + "Error: " + EnumChatFormatting.WHITE + "Couldn't create a portal!"));
                }

                return true;
            }
            else if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == CommonProxy.itemWrench.itemID)
            {
                player.openGui(EnhancedPortals.instance, GuiIds.PORTAL_CONTROLLER, worldObj, xCoord, yCoord, zCoord);
                return true;
            }
        }

        return false;
    }

    public void createPortal()
    {
        if (portalBlocks.isEmpty())
        {
            PortalUtils.linkPortalController((WorldServer) worldObj, xCoord, yCoord, zCoord);
        }

        for (ChunkCoordinates c : portalBlocks)
        {
            if (worldObj.getBlockId(c.posX, c.posY, c.posZ) == CommonProxy.blockPortal.blockID)
            {
                int meta = worldObj.getBlockMetadata(c.posX, c.posY, c.posZ) - 6;

                if (meta >= 1 && meta <= 3)
                {
                    worldObj.setBlockMetadataWithNotify(c.posX, c.posY, c.posZ, meta, 3);
                }
            }
        }

        for (ChunkCoordinates c : portalFrameRedstone)
        {
            TileEntity tile = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);

            if (tile != null && tile instanceof TilePortalFrameRedstone)
            {
                TilePortalFrameRedstone redstone = (TilePortalFrameRedstone) tile;
                redstone.portalCreated();
            }
        }
    }

    public void removePortal()
    {
        for (ChunkCoordinates c : portalBlocks)
        {
            if (worldObj.getBlockId(c.posX, c.posY, c.posZ) == CommonProxy.blockPortal.blockID)
            {
                int meta = worldObj.getBlockMetadata(c.posX, c.posY, c.posZ) + 6;

                if (meta >= 7 && meta <= 9)
                {
                    worldObj.setBlockMetadataWithNotify(c.posX, c.posY, c.posZ, meta, 3);
                }
            }
        }

        for (ChunkCoordinates c : portalFrameRedstone)
        {
            TileEntity tile = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);

            if (tile != null && tile instanceof TilePortalFrameRedstone)
            {
                TilePortalFrameRedstone redstone = (TilePortalFrameRedstone) tile;
                redstone.portalRemoved();
            }
        }
    }

    public void destroyAllPortal()
    {
        destroyPortal();
        portalBlocks = new ArrayList<ChunkCoordinates>();
    }

    public void destroyPortal()
    {
        for (ChunkCoordinates c : portalBlocks)
        {
            if (worldObj.getBlockId(c.posX, c.posY, c.posZ) == CommonProxy.blockPortal.blockID)
            {
                worldObj.setBlockToAir(c.posX, c.posY, c.posZ);
            }
        }
    }

    public void removeFrame(TilePortalFrame frame)
    {
        portalFrame.remove(new ChunkCoordinates(frame.xCoord, frame.yCoord, frame.zCoord));

        if (frame instanceof TilePortalFrameRedstone)
        {
            portalFrameRedstone.remove(new ChunkCoordinates(frame.xCoord, frame.yCoord, frame.zCoord));
        }
    }

    @Override
    public void selfBroken()
    {
        for (ChunkCoordinates c : portalFrame)
        {
            if (worldObj.getBlockId(c.posX, c.posY, c.posZ) == CommonProxy.blockFrame.blockID)
            {
                TilePortalFrame frame = (TilePortalFrame) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
                frame.controller = new ChunkCoordinates(0, -1, 0);
            }
        }

        destroyPortal();
    }

    @Override
    public void actionPerformed(int id, String string, EntityPlayer player)
    {
        if (id == 0)
        {
            UniqueIdentifier = string;
        }

        CommonProxy.sendUpdatePacketToAllAround(this);
    }

    @Override
    public void actionPerformed(int id, int data, EntityPlayer player)
    {
        boolean sendUpdate = true;

        if (id == 0)
        {
            FrameColour = data;
        }
        else if (id == 1)
        {
            PortalColour = data;
        }
        else if (id == 2)
        {
            ParticleColour = data;
        }
        else if (id == 3)
        {
            ParticleType = data;
        }
        else if (id == 4)
        {
            setInventorySlotContents(data, null);
            sendUpdate = false;
        }

        if (sendUpdate)
        {
            CommonProxy.sendUpdatePacketToAllAround(this);
        }
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack s = getStackInSlot(i);
        s.stackSize -= j;

        return s;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return inventory[i];
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
    }

    @Override
    public String getInvName()
    {
        return "ep2.portalController";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return true;
    }
}
