package uk.co.shadeddimensions.ep3.tileentity.frame;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import uk.co.shadeddimensions.ep3.api.IPortalModule;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.BlockManager;
import uk.co.shadeddimensions.ep3.portal.ControllerLink;
import uk.co.shadeddimensions.ep3.portal.ControllerLink.LinkStatus;
import uk.co.shadeddimensions.ep3.portal.EntityManager;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.portal.PortalUtils;
import uk.co.shadeddimensions.ep3.portal.StackHelper;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

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
        UniqueIdentifier = NetworkManager.BLANK_IDENTIFIER;

        inventory = new ItemStack[2];
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
    public void actionPerformed(int id, String string, EntityPlayer player)
    {
        if (id == 0)
        {
            if (CommonProxy.networkManager.networkExists(string))
            {
                // TODO: REJECT
                return;
            }

            if (string.equals(NetworkManager.BLANK_IDENTIFIER) && !UniqueIdentifier.equals(NetworkManager.BLANK_IDENTIFIER))
            {
                TileNetworkInterface ni = blockManager.getNetworkInterface(worldObj);

                if (ni != null)
                {
                    CommonProxy.networkManager.removePortalFromNetwork(UniqueIdentifier, ni.NetworkIdentifier);
                }

                CommonProxy.networkManager.removePortal(UniqueIdentifier);
                UniqueIdentifier = NetworkManager.BLANK_IDENTIFIER;
                ni.NetworkIdentifier = NetworkManager.BLANK_IDENTIFIER;
            }
            else if (UniqueIdentifier.equals(NetworkManager.BLANK_IDENTIFIER))
            {
                CommonProxy.networkManager.addNewPortal(string, new WorldCoordinates(xCoord, yCoord, zCoord, player.worldObj.provider.dimensionId));
                UniqueIdentifier = string;
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
                UniqueIdentifier = string;
            }
        }

        CommonProxy.sendUpdatePacketToAllAround(this);
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

                TilePortal portal = (TilePortal) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
                portal.controller = getChunkCoordinates();
                CommonProxy.sendUpdatePacketToAllAround(portal);
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

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack s = getStackInSlot(i);
        s.stackSize -= j;

        return s;
    }

    public void entityEnteredPortal(Entity entity)
    {
        if (blockManager.getNetworkInterfaceCoord() != null)
        {
            if (entity instanceof EntityItem)
            {
                EntityItem entityItem = (EntityItem) entity;
                ItemStack stack = entityItem.getEntityItem();

                if (stack.getItem() instanceof IPortalModule)
                {
                    TileModuleManipulator module = blockManager.getModuleManipulator(worldObj);

                    if (module != null)
                    {
                        if (!module.hasModule(((IPortalModule) stack.getItem()).getID(entityItem.getEntityItem())))
                        {
                            ItemStack s = stack.copy();
                            s.stackSize = 1;

                            module.setInventorySlotContents(5, s);
                            CommonProxy.sendUpdatePacketToAllAround(module);

                            if (stack.stackSize > 1)
                            {
                                entityItem.getEntityItem().stackSize--;
                            }
                            else
                            {
                                entityItem.setDead();
                            }

                            return;
                        }
                    }
                }
            }

            TileNetworkInterface network = blockManager.getNetworkInterface(worldObj);

            if (network != null && !network.NetworkIdentifier.equals(NetworkManager.BLANK_IDENTIFIER) && EntityManager.isEntityFitForTravel(entity))
            {
                String destination = CommonProxy.networkManager.getNextDestination(network.NetworkIdentifier, UniqueIdentifier);

                if (destination == null || destination.equals(NetworkManager.BLANK_IDENTIFIER))
                {
                    CommonProxy.logger.fine("Error teleporting Entity (" + entity.getEntityName() + "). Invalid destination (" + destination + ")!");
                }
                else
                {
                    CommonProxy.logger.fine("Entity (" + entity.getEntityName() + ") is trying to teleport to " + destination + "!");
                    EntityManager.teleportEntity(entity, UniqueIdentifier, destination);
                }
            }

            EntityManager.setEntityPortalCooldown(entity);
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public String getInvName()
    {
        return "tile.ep3.portalFrame.controller.name";
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
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return inventory[i];
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack)
    {
        return i == 0 ? StackHelper.isItemStackValidForPortalFrameTexture(stack) : StackHelper.isItemStackValidForPortalTexture(stack);
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
        portalActive = tagCompound.getBoolean("PortalActive");

        NBTTagList list = tagCompound.getTagList("Inventory");
        for (int i = 0; i < list.tagList.size(); i++)
        {
            inventory[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) list.tagList.get(i));
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
            if (!UniqueIdentifier.equals(NetworkManager.BLANK_IDENTIFIER))
            {
                TileNetworkInterface network = blockManager.getNetworkInterface(worldObj);

                if (network != null)
                {
                    CommonProxy.networkManager.removePortalFromNetwork(UniqueIdentifier, network.NetworkIdentifier);
                    network.NetworkIdentifier = NetworkManager.BLANK_IDENTIFIER;
                }

                CommonProxy.networkManager.removePortal(UniqueIdentifier);
                UniqueIdentifier = NetworkManager.BLANK_IDENTIFIER;
            }

            blockManager.destroyAndClearAll(worldObj);
            portalActive = false;
            hasInitialized = false; // For when other blocks get destroyed/added - allows user to reinit portal
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
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
        tagCompound.setBoolean("PortalActive", portalActive);

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
}
