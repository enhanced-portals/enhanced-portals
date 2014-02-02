package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Icon;
import net.minecraft.world.ChunkCoordIntPair;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.item.ItemLocationCard;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.network.packet.PacketRerender;
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
import uk.co.shadeddimensions.ep3.util.PortalDialException;
import uk.co.shadeddimensions.ep3.util.PortalTextureManager;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;
import uk.co.shadeddimensions.library.util.ItemHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;

public class TilePortalController extends TilePortalFrameSpecial implements IPeripheral
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

    public boolean activate(EntityPlayer player)
    {
        ItemStack item = player.inventory.getCurrentItem();
        boolean isReconfiguring = false;
        WorldCoordinates dbs = null;

        if (worldObj.isRemote)
        {
            return item == null ? false : true;
        }

        if (isFullyInitialized())
        {
            if (item == null)
            {
                return false;
            }
            
            if (item.itemID == CommonProxy.itemLocationCard.itemID && ItemLocationCard.hasDBSLocation(item))
            {
                WorldCoordinates stabilizer = ItemLocationCard.getDBSLocation(item);

                if (!(stabilizer.getBlockTileEntity() instanceof TileStabilizerMain))
                {
                    ItemLocationCard.clearDBSLocation(item);
                    player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("voidLinkCard")));
                }
                else
                {
                    if (!stabilizer.equals(blockManager.getDimensionalBridgeStabilizer()))
                    {
                        item.stackSize--;

                        if (item.stackSize <= 0)
                        {
                            player.inventory.mainInventory[player.inventory.currentItem] = null;
                        }

                        blockManager.setDimensionalBridgeStabilizer(stabilizer);
                        player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("reconfigureSuccess")));
                    }
                }
            }
            else if (ItemHelper.isWrench(item))
            {
                CommonProxy.openGui(player, GUIs.PortalController, this);
            }
            else if (item.itemID == CommonProxy.itemPaintbrush.itemID)
            {
                CommonProxy.openGui(player, GUIs.TexturesFrame, this);
            }

            return true;
        }
        
        if (portalState == 2)
        {
            if (ItemHelper.isWrench(item))
            {
                isReconfiguring = true;
                dbs = blockManager.getDimensionalBridgeStabilizer();
            }
            else
            {
                return false;
            }
        }
        else if (item == null || item.itemID != CommonProxy.itemLocationCard.itemID)
        {
            return false;
        }

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

            if (tile == null || !(tile instanceof TilePortalPart)) // Should really be null, but the check is there for invisible blocks
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
            else if (tile instanceof TileBiometricIdentifier)
            {
                blockManager.setBiometricIdentifier(c);
            }

            ((TilePortalPart) tile).portalController = getChunkCoordinates();
            CommonProxy.sendUpdatePacketToAllAround((TileEnhancedPortals) tile);
        }

        portalState = 1;
        isPortalActive = false;

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

        CommonProxy.sendUpdatePacketToAllAround(this);
        return true;
    }

    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        if (oldBlockID == -1 && oldMetadata == -1)
        {
            partBroken(false);
            return; // This is from placing down a block. We don't want to terminate the entire thing
        }
        
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

    /***
     * Creates a portal using the set network, if available. Called via redstone interface.
     */
    public void createPortal()
    {
        if (worldObj.isRemote || blockManager.getHasDialDevice())
        {
            return;
        }

        try
        {
            GlyphIdentifier uID = getUniqueIdentifier(), nID = getNetworkIdentifier();

            if (uID == null)
            {
                throw new PortalDialException("uniqueIdentifierNotSet");
            }
            else if (nID == null)
            {
                throw new PortalDialException("networkIdentifierNotSet");
            }

            TileStabilizerMain dbs = blockManager.getDimensionalBridgeStabilizerTile();

            if (dbs == null)
            {
                throw new PortalDialException("stabilizerNotFound");
            }

            dbs.setupNewConnection(uID, CommonProxy.networkManager.getDestination(uID, nID), null);
        }
        catch (PortalDialException e)
        {
            EntityPlayer player = worldObj.getClosestPlayer(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128);

            if (player != null)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getErrorString(e.getMessage())));
            }
        }
    }

    public void dialRequest(GlyphIdentifier id, PortalTextureManager m, EntityPlayer player)
    {
        if (worldObj.isRemote || blockManager.getHasNetworkInterface())
        {
            return;
        }

        try
        {
            GlyphIdentifier uID = getUniqueIdentifier();

            if (uID == null)
            {
                throw new PortalDialException("uniqueIdentifierNotSet");
            }
            else if (!blockManager.getHasDialDevice())
            {
                throw new PortalDialException("dialDeviceNotFound");
            }

            TileStabilizerMain dbs = blockManager.getDimensionalBridgeStabilizerTile();

            if (dbs == null)
            {
                throw new PortalDialException("stabilizerNotFound");
            }

            dbs.setupNewConnection(uID, id, m);
        }
        catch (PortalDialException e)
        {
            if (player == null)
            {
                player = worldObj.getClosestPlayer(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128);
            }

            if (player != null)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getErrorString(e.getMessage())));
            }
        }
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
    public Icon getBlockTexture(int side, int pass)
    {
        if (pass == 0)
        {
            if (isFullyInitialized())
            {
                if (activeTextureData.hasCustomFrameTexture())
                {
                    return ClientProxy.customFrameTextures.get(activeTextureData.getCustomFrameTexture());
                }
                else if (activeTextureData.getFrameItem() != null && activeTextureData.getFrameItem().getItem() instanceof ItemBlock)
                {
                    return Block.blocksList[((ItemBlock) activeTextureData.getFrameItem().getItem()).getBlockID()].getIcon(side, activeTextureData.getFrameItem().getItemDamage());
                }
                
                return BlockFrame.connectedTextures.getIconForSide(worldObj, xCoord, yCoord, zCoord, side);
            }
            
            return BlockFrame.connectedTextures.getBaseIcon();
        }

        return CommonProxy.forceShowFrameOverlays || wearingGoggles ? BlockFrame.overlayIcons[1] : BlockFrame.overlayIcons[0];
    }
    
    @Override
    public int getColourMultiplier()
    {
        return isFullyInitialized() ? activeTextureData.getFrameColour() : 0xFFFFFF;
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

    @Override
    public TilePortalController getPortalController()
    {
        return this;
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

    void setUniqueIdentifier(GlyphIdentifier id)
    {
        if (CommonProxy.networkManager.getPortalLocation(id) != null) // Check to see if we already have a portal with this ID
        {
            if (hasUniqueIdentifier() && getUniqueIdentifier().equals(id)) // Make sure it's not already us
            {
                return;
            }

            return; // If we do, void this request
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
        }
        else if (id.size() > 0) // Otherwise if the new identifier isn't blank
        {
            CommonProxy.networkManager.addPortal(id, getWorldCoordinates()); // Add the portal
        }

        sendUpdatePacket(false);
    }

    void setNetworkIdentifier(GlyphIdentifier id)
    {
        if (!hasUniqueIdentifier())
        {
            return;
        }

        GlyphIdentifier uID = getUniqueIdentifier();
        TileStabilizerMain dbs = blockManager.getDimensionalBridgeStabilizerTile();

        if (dbs != null && isPortalActive)
        {
            try
            {
                dbs.terminateExistingConnection(getUniqueIdentifier());
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        if (hasNetworkIdentifier())
        {
            CommonProxy.networkManager.removePortalFromNetwork(uID, getNetworkIdentifier());
        }

        if (id.size() > 0)
        {
            CommonProxy.networkManager.addPortalToNetwork(uID, id);
        }

        sendUpdatePacket(false);
    }

    void setFrameColour(int colour)
    {
        activeTextureData.setFrameColour(colour);
        sendUpdatePacket(true);
    }

    void setPortalColour(int colour)
    {
        activeTextureData.setPortalColour(colour);
        sendUpdatePacket(true);
    }

    void setParticleColour(int colour)
    {
        activeTextureData.setParticleColour(colour);
        sendUpdatePacket(false); // Particles are generated by querying this
    }

    void setParticleType(int type)
    {
        activeTextureData.setParticleType(type);
        sendUpdatePacket(false); // Particles are generated by querying this
    }

    void setCustomFrameTexture(int tex)
    {
        activeTextureData.setCustomFrameTexture(tex);
        sendUpdatePacket(true);
    }

    void setCustomPortalTexture(int tex)
    {
        activeTextureData.setCustomPortalTexture(tex);
        sendUpdatePacket(true);
    }

    void setPortalItem(int ID, int Meta)
    {
        ItemStack s = ID == 0 ? null : new ItemStack(ID, 1, Meta);
        activeTextureData.setPortalItem(s);        
        sendUpdatePacket(true);
    }

    void setFrameItem(int ID, int Meta)
    {
        ItemStack s = ID == 0 ? null : new ItemStack(ID, 1, Meta);
        activeTextureData.setFrameItem(s);        
        sendUpdatePacket(true);
    }

    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        super.guiActionPerformed(payload, player);

        if (payload.data.hasKey("uniqueIdentifier"))
        {
            setUniqueIdentifier(new GlyphIdentifier(payload.data.getString("uniqueIdentifier")));
        }

        if (payload.data.hasKey("networkIdentifier"))
        {
            setNetworkIdentifier(new GlyphIdentifier(payload.data.getString("networkIdentifier")));
        }

        if (payload.data.hasKey("frameColour"))
        {
            setFrameColour(payload.data.getInteger("frameColour"));
        }

        if (payload.data.hasKey("portalColour"))
        {
            setPortalColour(payload.data.getInteger("portalColour"));
        }

        if (payload.data.hasKey("particleColour"))
        {
            setParticleColour(payload.data.getInteger("particleColour"));
        }

        if (payload.data.hasKey("particleType"))
        {
            setParticleType(payload.data.getInteger("particleType"));
        }

        if (payload.data.hasKey("customFrameTexture"))
        {
            setCustomFrameTexture(payload.data.getInteger("customFrameTexture"));
        }

        if (payload.data.hasKey("customPortalTexture"))
        {
            setCustomPortalTexture(payload.data.getInteger("customPortalTexture"));
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

            sendUpdatePacket(false);
        }

        if (payload.data.hasKey("portalItemID"))
        {
            setPortalItem(payload.data.getInteger("portalItemID"), payload.data.getInteger("portalItemMeta"));
        }

        if (payload.data.hasKey("frameItemID"))
        {
            setFrameItem(payload.data.getInteger("frameItemID"), payload.data.getInteger("frameItemMeta"));
        }
    }

    void sendUpdatePacket(boolean updateChunks)
    {
        CommonProxy.sendUpdatePacketToAllAround(this);

        if (updateChunks)
        {
            ArrayList<ChunkCoordIntPair> chunks = new ArrayList<ChunkCoordIntPair>();
            chunks.add(new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4));

            for (ChunkCoordinates c : blockManager.getPortalFrames())
            {
                if (!chunks.contains(new ChunkCoordIntPair(c.posX >> 4, c.posZ >> 4)))
                {
                    CommonProxy.sendPacketToAllAround(this, new PacketRerender(c.posX, c.posY, c.posZ).getPacket());
                    chunks.add(new ChunkCoordIntPair(c.posX >> 4, c.posZ >> 4));
                }
            }
        }
    }

    public boolean hasNetworkIdentifier()
    {
        return worldObj.isRemote ? networkID != null : hasUniqueIdentifier() ? CommonProxy.networkManager.hasNetwork(getUniqueIdentifier()) : false;
    }

    public boolean hasUniqueIdentifier()
    {
        return worldObj.isRemote ? uniqueID != null : CommonProxy.networkManager.hasIdentifier(getWorldCoordinates());
    }

    /***
     * Checks to see if this portal is ready for use.
     */
    public boolean isFullyInitialized()
    {
        return portalState == 1;
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

            if (bio != null && !bio.canEntityTravel(entity))
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

            if (dbs != null)
            {
                dbs.onEntityEnterPortal(uID, entity, portal);
            }
            else
            {
                blockManager.setDimensionalBridgeStabilizer(null);
                PortalUtils.removePortalFrom(this);
                portalState = 2;
            }
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

        if (isPortalActive)
        {
            removePortal();
        }
        
        portalState = 2; // Set the portal to inactive - this portal is no longer functional
        WorldCoordinates dbs = blockManager.getDimensionalBridgeStabilizer();

        for (ChunkCoordinates c : blockManager.getPortals())
        {
            worldObj.setBlockToAir(c.posX, c.posY, c.posZ);
        }

        for (ChunkCoordinates c : blockManager.getPortalFrames())
        {
            TileEntity t = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
            
            if (t instanceof TilePortalPart)
            {
                TilePortalPart tile = (TilePortalPart) t;
                tile.portalController = null;
                CommonProxy.sendUpdatePacketToAllAround(tile);
            }
        }

        for (ChunkCoordinates c : blockManager.getRedstoneInterfaces())
        {
            TileEntity t = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);
            
            if (t instanceof TilePortalPart)
            {
                TilePortalPart tile = (TilePortalPart) t;
                tile.portalController = null;
                CommonProxy.sendUpdatePacketToAllAround(tile);
            }
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
        CommonProxy.sendUpdatePacketToAllAround(this);
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

        try
        {
            dbs.terminateExistingConnection(uID);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
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

    public void swapTextureData(PortalTextureManager newManager)
    {
        inactiveTextureData = new PortalTextureManager(activeTextureData);
        activeTextureData = newManager;
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

        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
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

    /* IPeripheral */
    @Override
    public String getType()
    {
        return "Portal Controller";
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "isPortalActive", "getUniqueIdentifier", "setUniqueIdentifier", "getFrameColour", "setFrameColour", "getPortalColour", "setPortalColour", "getParticleColour", "setParticleColour" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        if (method == 0) // isPortalActive
        {
            return new Object[] { isPortalActive };
        }
        else if (method == 1) // getUniqueIdentifier
        {
            GlyphIdentifier identifier = getUniqueIdentifier();

            if (identifier == null || identifier.isEmpty())
            {
                return new Object[] { "" };
            }
            else
            {
                return new Object[] { identifier.getGlyphString() };
            }
        }
        else if (method == 2) // setUniqueIdentifier
        {
            if (arguments.length == 0)
            {
                setUniqueIdentifier(new GlyphIdentifier());
                return callMethod(computer, context, 1, null);
            }
            else if (arguments.length == 1)
            {
                String s = arguments[0].toString();
                s = s.replace(" ", GlyphIdentifier.GLYPH_SEPERATOR);

                if (s.length() == 0)
                {
                    throw new Exception("Glyph ID is not formatted correctly");
                }
                
                try
                {
                    if (s.contains(GlyphIdentifier.GLYPH_SEPERATOR))
                    {
                        String[] nums = s.split(GlyphIdentifier.GLYPH_SEPERATOR);

                        if (nums.length > 9)
                        {
                            throw new Exception("Glyph ID is too long. Must be a maximum of 9 IDs");
                        }

                        for (String num : nums)
                        {

                            int n = Integer.parseInt(num);

                            if (n < 0 || n > 27)
                            {
                                throw new Exception("Glyph ID must be between 0 and 27 (inclusive)");
                            }
                        }
                    }
                    else
                    {
                        int n = Integer.parseInt(s);

                        if (n < 0 || n > 27)
                        {
                            throw new Exception("Glyph ID must be between 0 and 27 (inclusive)");
                        }
                    }
                }
                catch (NumberFormatException ex)
                {
                    throw new Exception("Glyph ID is not formatted correctly");
                }

                setUniqueIdentifier(new GlyphIdentifier(s));
            }
            else
            {
                throw new Exception("Invalid arguments");
            }

            return callMethod(computer, context, 1, null);
        }
        else if (method == 3) // getFrameColour
        {
            return new Object[] { activeTextureData.getFrameColour() };
        }
        else if (method == 4) // setFrameColour
        {
            if (arguments.length > 1 || arguments.length == 1 && arguments[0].toString().length() == 0)
            {
                throw new Exception("Invalid arguments");
            }
            
            try
            {
                int hex = Integer.parseInt(arguments.length == 1 ? arguments[0].toString() : "FFFFFF", 16);
                setFrameColour(hex);
            }
            catch (NumberFormatException ex)
            {
                throw new Exception("Couldn't parse input as hexidecimal");
            }
        }
        else if (method == 5) // getPortalColour
        {
            return new Object[] { activeTextureData.getPortalColour() };
        }
        else if (method == 6) // setPortalColour
        {
            if (arguments.length > 1 || arguments.length == 1 && arguments[0].toString().length() == 0)
            {
                throw new Exception("Invalid arguments");
            }
            
            try
            {
                int hex = Integer.parseInt(arguments.length == 1 ? arguments[0].toString() : "FFFFFF", 16);
                setPortalColour(hex);
            }
            catch (NumberFormatException ex)
            {
                throw new Exception("Couldn't parse input as hexidecimal");
            }
        }
        else if (method == 7) // getParticleColour
        {
            return new Object[] { activeTextureData.getParticleColour() };
        }
        else if (method == 8) // setParticleColour
        {
            if (arguments.length > 1 || arguments.length == 1 && arguments[0].toString().length() == 0)
            {
                throw new Exception("Invalid arguments");
            }
            
            try
            {
                int hex = Integer.parseInt(arguments.length == 1 ? arguments[0].toString() : "FFFFFF", 16); // TODO default particle colour
                setParticleColour(hex);
            }
            catch (NumberFormatException ex)
            {
                throw new Exception("Couldn't parse input as hexidecimal");
            }
        }

        return null;
    }

    @Override
    public boolean canAttachToSide(int side)
    {
        return true;
    }

    @Override
    public void attach(IComputerAccess computer)
    {

    }

    @Override
    public void detach(IComputerAccess computer)
    {

    }
}
