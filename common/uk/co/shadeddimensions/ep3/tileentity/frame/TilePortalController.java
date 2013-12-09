package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import uk.co.shadeddimensions.ep3.item.ItemLocationCard;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.EntityManager;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.portal.PortalUtils;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.util.BlockManager;
import uk.co.shadeddimensions.ep3.util.ClientBlockManager;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.ep3.util.PortalTextureManager;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TilePortalController extends TilePortalPart
{
    public PortalTextureManager activeTextureData;
    PortalTextureManager inactiveTextureData;
    public BlockManager blockManager;
    byte portalState;
    public byte portalType;
    public boolean isPortalActive;
    public boolean processing;
    boolean processingPortal;
    public int connectedPortals;

    @SideOnly(Side.CLIENT)
    public GlyphIdentifier uniqueID, networkID;
  
    public TilePortalController()
    {
        blockManager = new BlockManager();
        portalType = 0;
        portalState = 0;
        connectedPortals = -1;
        activeTextureData = new PortalTextureManager();
        inactiveTextureData = null;
        isPortalActive = processing = processingPortal = false;
    }

    /***
     * Checks to see if this portal is ready for use.
     */
    public boolean isFullyInitialized()
    {
        return portalState == 1;
    }

    public void swapTextureData(PortalTextureManager newManager)
    {
        inactiveTextureData = new PortalTextureManager(activeTextureData);
        activeTextureData = newManager;
    }

    public void revertTextureData()
    {
        if (inactiveTextureData == null)
        {
            return;
        }

        activeTextureData = new PortalTextureManager(inactiveTextureData);
        inactiveTextureData = null;
        CommonProxy.sendUpdatePacketToAllAround(this);
    }

    @Override
    public TilePortalController getPortalController()
    {
        return this;
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        GlyphIdentifier nID = getNetworkIdentifier();

        GeneralUtils.writeGlyphIdentifier(stream, getUniqueIdentifier());
        GeneralUtils.writeGlyphIdentifier(stream, nID);
        blockManager.writeToPacket(stream);
        activeTextureData.writeToPacket(stream);
        stream.writeByte(portalState);
        stream.writeByte(portalType);
        stream.writeBoolean(isPortalActive);
        stream.writeInt(nID == null ? -1 : CommonProxy.networkManager.getNetworkSize(nID));
    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        uniqueID = GeneralUtils.readGlyphIdentifier(stream);
        networkID = GeneralUtils.readGlyphIdentifier(stream);
        blockManager = new ClientBlockManager().readFromPacket(stream);
        activeTextureData.usePacket(stream);
        portalState = stream.readByte();
        portalType = stream.readByte();
        isPortalActive = stream.readBoolean();
        connectedPortals = stream.readInt();
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        blockManager.writeToNBT(tagCompound);
        tagCompound.setByte("PortalState", portalState);
        tagCompound.setByte("PortalType", portalType);
        activeTextureData.writeToNBT(tagCompound, "ActiveTextureData");
        tagCompound.setBoolean("IsPortalActive", isPortalActive);

        if (inactiveTextureData != null)
        {
            inactiveTextureData.writeToNBT(tagCompound, "InactiveTextureData");
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        blockManager.readFromNBT(tagCompound);
        portalState = tagCompound.getByte("PortalState");
        portalType = tagCompound.getByte("PortalType");
        activeTextureData.readFromNBT(tagCompound, "ActiveTextureData");
        isPortalActive = tagCompound.getBoolean("IsPortalActive");

        if (tagCompound.hasKey("InactiveTextureData"))
        {
            inactiveTextureData = new PortalTextureManager();
            inactiveTextureData.readFromNBT(tagCompound, "InactiveTextureData");
        }
    }

    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        super.guiActionPerformed(payload, player);
        boolean sendUpdatePacket = false;

        if (payload.data.hasKey("uniqueIdentifier"))
        {
            GlyphIdentifier id = new GlyphIdentifier(payload.data.getString("uniqueIdentifier"));

            if (CommonProxy.networkManager.getPortalLocation(id) != null) // Check to see if we already have a portal with this ID
            {
                id = new GlyphIdentifier(); // If we do, void this request
            }

            if (hasUniqueIdentifier()) // If already have an identifier
            {
                GlyphIdentifier networkIdentifier = null;

                if (hasNetworkIdentifier()) // Check to see if it's in a network
                {
                    networkIdentifier = getNetworkIdentifier();
                    CommonProxy.networkManager.removePortalFromNetwork(getUniqueIdentifier(), networkIdentifier); // Remove it if it is
                }

                CommonProxy.networkManager.removePortal(getWorldCoordinates()); // Remove the old identifier

                if (id.size() > 0) // If the new identifier isn't blank
                {
                    CommonProxy.networkManager.addPortal(id, getWorldCoordinates()); // Add it

                    if (networkIdentifier != null)
                    {
                        CommonProxy.networkManager.addPortalToNetwork(id, networkIdentifier); // Re-add it to the network, if it was in one
                    }
                }

                sendUpdatePacket = true;
            }
            else if (id.size() > 0) // Otherwise if the new identifier isn't blank
            {
                CommonProxy.networkManager.addPortal(id, getWorldCoordinates()); // Add the portal
                sendUpdatePacket = true;
            }
        }

        if (payload.data.hasKey("networkIdentifier"))
        {
            GlyphIdentifier id = new GlyphIdentifier(payload.data.getString("networkIdentifier"));

            if (!hasUniqueIdentifier())
            {
                sendUpdatePacket = true;
            }
            else
            {
                GlyphIdentifier uID = getUniqueIdentifier();
                TileStabilizerMain dbs = blockManager.getDimensionalBridgeStabilizerTile();

                if (dbs != null)
                {
                    dbs.terminateExistingConnection(getUniqueIdentifier());
                }

                if (hasNetworkIdentifier())
                {
                    CommonProxy.networkManager.removePortalFromNetwork(uID, getNetworkIdentifier());
                    sendUpdatePacket = true;
                }

                if (id.size() > 0)
                {
                    CommonProxy.networkManager.addPortalToNetwork(uID, id);
                    sendUpdatePacket = true;
                }
            }
        }

        if (payload.data.hasKey("frameColour"))
        {
            activeTextureData.setFrameColour(payload.data.getInteger("frameColour"));
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("portalColour"))
        {
            activeTextureData.setPortalColour(payload.data.getInteger("portalColour"));
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("particleColour"))
        {
            activeTextureData.setParticleColour(payload.data.getInteger("particleColour"));
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("particleType"))
        {
            activeTextureData.setParticleType(payload.data.getInteger("particleType"));
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("customFrameTexture"))
        {
            activeTextureData.setCustomFrameTexture(payload.data.getInteger("customFrameTexture"));
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("customPortalTexture"))
        {
            activeTextureData.setCustomPortalTexture(payload.data.getInteger("customPortalTexture"));
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("resetSlot"))
        {
            int slot = payload.data.getInteger("resetSlot");

            if (slot == 0)
            {
                activeTextureData.setFrameItem(null);
            }
            else if (slot == 1)
            {
                activeTextureData.setPortalItem(null);
            }

            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("portalItemID"))
        {
            int id = payload.data.getInteger("portalItemID"), meta = payload.data.getInteger("portalItemMeta");
            ItemStack s = id == 0 ? null : new ItemStack(id, 1, meta);
            activeTextureData.setPortalItem(s);
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("frameItemID"))
        {
            int id = payload.data.getInteger("frameItemID"), meta = payload.data.getInteger("frameItemMeta");
            ItemStack s = id == 0 ? null : new ItemStack(id, 1, meta);
            activeTextureData.setFrameItem(s);
            sendUpdatePacket = true;
        }

        if (sendUpdatePacket)
        {
            CommonProxy.sendUpdatePacketToAllAround(this);
        }
    }

    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        partBroken(false);

        GlyphIdentifier uID = getUniqueIdentifier(), nID = getNetworkIdentifier();

        if (uID != null)
        {
            if (nID != null)
            {
                CommonProxy.networkManager.removePortalFromNetwork(uID, nID);
            }

            CommonProxy.networkManager.removePortal(uID);
        }
    }

    public void partBroken(boolean isPortal)
    {
        if (portalState != 1)
        {
            return;
        }

        if (isPortal)
        {
            if (!processingPortal)
            {
                processingPortal = true;
                removePortal();
                processingPortal = false;
            }

            return;
        }

        removePortal();
        portalState = 2; // Set the portal to inactive - this portal is no longer functional
        WorldCoordinates dbs = blockManager.getDimensionalBridgeStabilizer();

        for (ChunkCoordinates c : blockManager.getPortals())
        {
            worldObj.setBlockToAir(c.posX, c.posY, c.posZ);
        }

        for (ChunkCoordinates c : blockManager.getPortalFrames())
        {
            TilePortalPart tile = (TilePortalPart) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
            tile.portalController = null;
            CommonProxy.sendUpdatePacketToAllAround(tile);
        }

        for (ChunkCoordinates c : blockManager.getRedstoneInterfaces())
        {
            TilePortalPart tile = (TilePortalPart) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
            tile.portalController = null;
            CommonProxy.sendUpdatePacketToAllAround(tile);
        }

        TileNetworkInterface network = blockManager.getNetworkInterface(worldObj);

        if (network != null)
        {
            network.portalController = null;
            CommonProxy.sendUpdatePacketToAllAround(network);
        }

        TileDiallingDevice dial = blockManager.getDialDevice(worldObj);

        if (dial != null)
        {
            dial.portalController = null;
            CommonProxy.sendUpdatePacketToAllAround(dial);
        }

        TileBiometricIdentifier bio = blockManager.getBiometricIdentifier(worldObj);

        if (bio != null)
        {
            bio.portalController = null;
            CommonProxy.sendUpdatePacketToAllAround(bio);
        }

        TileModuleManipulator module = blockManager.getModuleManipulator(worldObj);

        if (module != null)
        {
            module.portalController = null;
            CommonProxy.sendUpdatePacketToAllAround(module);
        }

        blockManager = new BlockManager();
        blockManager.setDimensionalBridgeStabilizer(dbs);
    }

    @Override
    public boolean activate(EntityPlayer player)
    {
        ItemStack item = player.inventory.getCurrentItem();
        boolean isReconfiguring = false;
        WorldCoordinates dbs = null;

        if (worldObj.isRemote || isFullyInitialized() || item == null || portalState == 1)
        {
            if (!worldObj.isRemote)
            {
                super.activate(player);
            }

            return false;
        }

        if (portalState == 0)
        {
            if (item.itemID != CommonProxy.itemLocationCard.itemID || !ItemLocationCard.hasDBSLocation(item))
            {
                return false;
            }

            WorldCoordinates stabilizer = ItemLocationCard.getDBSLocation(item);

            if (!(stabilizer.getBlockTileEntity() instanceof TileStabilizerMain))
            {
                ItemLocationCard.clearDBSLocation(item);
                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("voidLinkCard")));
                return false;
            }
        }
        else if (portalState == 2)
        {
            if (item.itemID == CommonProxy.itemWrench.itemID)
            {
                isReconfiguring = true;
                dbs = blockManager.getDimensionalBridgeStabilizer();
            }
            else
            {
                return false;
            }
        }

        if (blockManager.getPortalCount() > 0 && blockManager.getDimensionalBridgeStabilizer() == null)
        {
            blockManager.setDimensionalBridgeStabilizer(ItemLocationCard.getDBSLocation(item));
            portalState = 1;

            item.stackSize--;

            if (item.stackSize <= 0)
            {
                player.inventory.mainInventory[player.inventory.currentItem] = null;
            }

            player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("reconfigureSuccess")));
        }
        else
        {
            blockManager = new BlockManager();
            Queue<ChunkCoordinates> portalBlocks = PortalUtils.getGhostedPortals(this);

            if (portalBlocks == null || portalBlocks.isEmpty() || portalType == 0)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("portalCouldNotBeCreatedHere")));
                return false;
            }

            Queue<ChunkCoordinates> allBlocks = PortalUtils.findAllAttachedPortalParts(this, portalBlocks, player);

            if (allBlocks == null || allBlocks.isEmpty())
            {
                return false;
            }

            for (ChunkCoordinates c : allBlocks)
            {
                TileEntity tile = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);

                if (tile == null || !(tile instanceof TilePortalPart)) // Should really be null, but the check is there for RC invisible blocks
                {
                    blockManager.addPortalBlock(c);
                    continue; // We don't want to try and set the blocks controller/send a packet for this
                }
                else if (tile instanceof TileFrame)
                {
                    blockManager.addBasicFrame(c);
                }
                else if (tile instanceof TileRedstoneInterface)
                {
                    blockManager.addRedstoneInterface(c);
                }
                else if (tile instanceof TileNetworkInterface)
                {
                    blockManager.setNetworkInterface(c);
                }
                else if (tile instanceof TileDiallingDevice)
                {
                    blockManager.setDialDevice(c);
                }
                else if (tile instanceof TileModuleManipulator)
                {
                    blockManager.setModuleManipulator(c);
                }

                ((TilePortalPart) tile).portalController = getChunkCoordinates();
                CommonProxy.sendUpdatePacketToAllAround((TileEnhancedPortals) tile);
            }

            portalState = 1;

            if (!isReconfiguring)
            {
                blockManager.setDimensionalBridgeStabilizer(ItemLocationCard.getDBSLocation(item));
                item.stackSize--;

                if (item.stackSize <= 0)
                {
                    player.inventory.mainInventory[player.inventory.currentItem] = null;
                }

                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("success")));
            }
            else
            {
                blockManager.setDimensionalBridgeStabilizer(dbs);
                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("reconfigureSuccess")));
            }
        }

        return true;
    }

    public void dialRequest(GlyphIdentifier id)
    {
        dialRequest(id, null);
    }

    public void dialRequest(GlyphIdentifier id, PortalTextureManager m)
    {
        GlyphIdentifier uID = getUniqueIdentifier();

        if (uID != null && blockManager.getHasDialDevice())
        {
            TileStabilizerMain dbs = blockManager.getDimensionalBridgeStabilizerTile();

            if (dbs == null)
            {
                portalState = 0;
                return;
            }

            dbs.setupNewConnection(uID, id, m);
        }
    }

    /***
     * Creates a portal using the set network, if available. Called via redstone interface.
     */
    public void createPortal()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        GlyphIdentifier uID = getUniqueIdentifier(), nID = getNetworkIdentifier();

        if (uID == null || nID == null)
        {
            return;
        }

        TileStabilizerMain dbs = blockManager.getDimensionalBridgeStabilizerTile();

        if (dbs == null)
        {
            portalState = 0;
            return;
        }

        dbs.setupNewConnection(uID, CommonProxy.networkManager.getDestination(uID, nID), null);
    }

    /***
     * Removes a portal and resets the destination portal.
     */
    public void removePortal()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        GlyphIdentifier uID = getUniqueIdentifier();

        if (uID == null)
        {
            return;
        }

        TileStabilizerMain dbs = blockManager.getDimensionalBridgeStabilizerTile();

        if (dbs == null)
        {
            portalState = 1;
            return;
        }

        dbs.terminateExistingConnection(uID);
    }

    public void setPortalActive(boolean b)
    {
        isPortalActive = b;

        for (ChunkCoordinates c : blockManager.getRedstoneInterfaces())
        {
            if (b)
            {
                ((TileRedstoneInterface) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ)).portalCreated();
            }
            else
            {
                ((TileRedstoneInterface) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ)).portalRemoved();
            }
        }

        CommonProxy.sendUpdatePacketToAllAround(this);
    }

    public boolean hasUniqueIdentifier()
    {
        return worldObj.isRemote ? uniqueID != null : CommonProxy.networkManager.hasIdentifier(getWorldCoordinates());
    }

    public boolean hasNetworkIdentifier()
    {
        return worldObj.isRemote ? networkID != null : hasUniqueIdentifier() ? CommonProxy.networkManager.hasNetwork(getUniqueIdentifier()) : false;
    }

    /***
     * Gets this controllers unique identifier.
     * 
     * @return Null if one is not set
     */
    public GlyphIdentifier getUniqueIdentifier()
    {
        if (worldObj.isRemote)
        {
            return uniqueID;
        }

        return CommonProxy.networkManager.getPortalIdentifier(getWorldCoordinates());
    }

    /***
     * Gets this controllers network identifier.
     * 
     * @return Null if one is not set
     */
    public GlyphIdentifier getNetworkIdentifier()
    {
        if (worldObj.isRemote)
        {
            return networkID;
        }

        return CommonProxy.networkManager.getPortalNetwork(getUniqueIdentifier());
    }

    public void onEntityEnterPortal(Entity entity, TilePortal portal)
    {
        GlyphIdentifier uID = getUniqueIdentifier();

        if (EntityManager.isEntityFitForTravel(entity)) // Make sure we're not spamming this. We only want it to happen once per entity.
        {
            for (ChunkCoordinates c : blockManager.getRedstoneInterfaces())
            {
                ((TileRedstoneInterface) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ)).entityTeleport(entity);
            }
        }

        if (uID != null)
        {
            TileBiometricIdentifier bio = blockManager.getBiometricIdentifier(worldObj);
            TileModuleManipulator module = blockManager.getModuleManipulator(worldObj);

            if (bio != null && !bio.canEntityBeSent(entity))
            {
                EntityManager.setEntityPortalCooldown(entity);
                return;
            }

            if (module != null && !module.canSendEntity(entity))
            {
                EntityManager.setEntityPortalCooldown(entity);
                return;
            }

            TileStabilizerMain dbs = blockManager.getDimensionalBridgeStabilizerTile();
            if (dbs != null) {
              dbs.onEntityEnterPortal(uID, entity, portal);
            }
        }
    }

    /* IInventory */
    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if (i == 0)
        {
            activeTextureData.setFrameItem(itemstack);
        }
        else if (i == 1)
        {
            activeTextureData.setPortalItem(itemstack);
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack stack = getStackInSlot(i);

        if (stack != null)
        {
            if (stack.stackSize <= j)
            {
                setInventorySlotContents(i, null);
            }
            else
            {
                stack = stack.splitStack(j);

                if (stack.stackSize == 0)
                {
                    setInventorySlotContents(i, null);
                }
            }
        }

        return stack;
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
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return i == 0 ? activeTextureData.getFrameItem() : i == 1 ? activeTextureData.getPortalItem() : null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return i == 0 ? activeTextureData.getFrameItem() : i == 1 ? activeTextureData.getPortalItem() : null;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack)
    {
        return false;
    }
}
