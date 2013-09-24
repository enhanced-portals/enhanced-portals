package uk.co.shadeddimensions.enhancedportals.tileentity.frame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import uk.co.shadeddimensions.enhancedportals.lib.Reference;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.portal.BlockManager;
import uk.co.shadeddimensions.enhancedportals.portal.ControllerLink;
import uk.co.shadeddimensions.enhancedportals.portal.ControllerLink.LinkStatus;
import uk.co.shadeddimensions.enhancedportals.portal.PortalUtils;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.util.WorldCoordinates;

public class TilePortalController extends TilePortalFrame implements IInventory
{
    public String UniqueIdentifier;

    public int FrameColour;
    public int PortalColour;
    public int ParticleColour;
    public int ParticleType;
    public int PortalType;

    public BlockManager blockManager;
    
    public boolean hasInitialized, portalActive;

    ItemStack[] inventory;

    public TilePortalController()
    {
        FrameColour = PortalColour = 0xffffff;
        ParticleColour = 0xB336A1;
        ParticleType = PortalType = 0;

        blockManager = new BlockManager();
        
        hasInitialized = portalActive = false;
        UniqueIdentifier = "";

        inventory = new ItemStack[2];
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        
        blockManager.saveData(tagCompound);
        
        tagCompound.setBoolean("initialized", hasInitialized);
        tagCompound.setString("identifier", UniqueIdentifier);

        tagCompound.setInteger("FrameColour", FrameColour);
        tagCompound.setInteger("PortalColour", PortalColour);
        tagCompound.setInteger("ParticleColour", ParticleColour);
        tagCompound.setInteger("ParticleType", ParticleType);
        tagCompound.setInteger("PortalType", PortalType);

        NBTTagList list = new NBTTagList();
        for (ItemStack s : inventory)
        {
            NBTTagCompound compound = new NBTTagCompound();
            
            if (s != null)
            {
                s.writeToNBT(compound);
            }
            
            list.appendTag(compound);
        }

        tagCompound.setTag("Inventory", list);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        blockManager.loadData(tagCompound);

        hasInitialized = tagCompound.getBoolean("initialized");
        UniqueIdentifier = tagCompound.getString("identifier");

        FrameColour = tagCompound.getInteger("FrameColour");
        PortalColour = tagCompound.getInteger("PortalColour");
        ParticleColour = tagCompound.getInteger("ParticleColour");
        ParticleType = tagCompound.getInteger("ParticleType");
        PortalType = tagCompound.getInteger("PortalType");
        
        NBTTagList list = tagCompound.getTagList("Inventory");
        for (int i = 0; i < list.tagList.size(); i++)
        {
            inventory[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) list.tagList.get(i));
        }
    }

    @Override
    public boolean activate(EntityPlayer player)
    {
        if (!worldObj.isRemote)
        {
            if (!hasInitialized && (player.inventory.getCurrentItem() == null || player.inventory.getCurrentItem().itemID != CommonProxy.itemWrench.itemID))
            {
                if (!PortalUtils.createPortalForController(this))
                {
                    player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("chat." + Reference.SHORT_ID + ".portalLink.couldNotMakePortal"));
                    return true;
                }
                
                LinkStatus status = new ControllerLink(this).doLink();

                if (status == LinkStatus.SUCCESS)
                {
                    player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("chat." + Reference.SHORT_ID + ".portalLink." + status.toString().toLowerCase()));
                    hasInitialized = true;
                }
                else
                {
                    PortalUtils.removePortalForController(this);
                    player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("chat." + Reference.SHORT_ID + ".portalLink." + status.toString().toLowerCase().replace("_", ".")));
                }
                
                return true;
            }
        }

        return false;
    }

    public void createPortal()
    {
        if (!portalActive)
        {
            for (ChunkCoordinates c : blockManager.getPortalsCoord())
            {
                if (!worldObj.isAirBlock(c.posX, c.posY, c.posZ))
                {
                    worldObj.destroyBlock(c.posX, c.posY, c.posZ, true);
                }
                
                worldObj.setBlock(c.posX, c.posY, c.posZ, CommonProxy.blockPortal.blockID, PortalType, 2);
            }

            for (ChunkCoordinates c : blockManager.getRedstoneCoord())
            {
                TileEntity tile = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);

                if (tile != null && tile instanceof TileRedstoneInterface)
                {
                    TileRedstoneInterface redstone = (TileRedstoneInterface) tile;
                    redstone.portalCreated();
                }
            }
            
            portalActive = true;
        }
    }

    public void removePortal()
    {
        if (portalActive)
        {
            for (ChunkCoordinates c : blockManager.getPortalsCoord())
            {
                worldObj.setBlockToAir(c.posX, c.posY, c.posZ);
            }
    
            for (ChunkCoordinates c : blockManager.getRedstoneCoord())
            {
                TileEntity tile = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
    
                if (tile != null && tile instanceof TileRedstoneInterface)
                {
                    TileRedstoneInterface redstone = (TileRedstoneInterface) tile;
                    redstone.portalRemoved();
                }
            }
            
            portalActive = false;
        }
    }

    @Override
    public void selfBroken()
    {
        if (!worldObj.isRemote)
        {
            blockManager.destroyAndClearAll(worldObj);
            
            if (!UniqueIdentifier.equals(""))
            {
                if (blockManager.getNetworkInterfaceCoord() != null)
                {
                    CommonProxy.networkManager.removePortalFromNetwork(UniqueIdentifier, blockManager.getNetworkInterface(worldObj).NetworkIdentifier);
                }
                
                CommonProxy.networkManager.removePortal(UniqueIdentifier);
                UniqueIdentifier = "";
            }
            
            portalActive = false;
            hasInitialized = false; // For when other blocks get destroyed - allows user to reinit portal
        }
    }

    @Override
    public void actionPerformed(int id, String string, EntityPlayer player)
    {
        if (id == 0)
        {
            if (CommonProxy.networkManager.networkExists(string))
            {
                // TODO: REJECT
                return;
            }
            
            if (UniqueIdentifier.equals(""))
            {
                CommonProxy.networkManager.addNewPortal(string, new WorldCoordinates(xCoord, yCoord, zCoord, player.worldObj.provider.dimensionId));
            }
            else
            {
                TileNetworkInterface ni = blockManager.getNetworkInterface(worldObj);

                if (ni != null)
                {
                    CommonProxy.networkManager.removePortalFromNetwork(UniqueIdentifier, ni.NetworkIdentifier);
                    CommonProxy.networkManager.addPortalToNetwork(string, ni.NetworkIdentifier);
                }
                
                CommonProxy.networkManager.updateExistingPortal(UniqueIdentifier, string);
            }
            
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
