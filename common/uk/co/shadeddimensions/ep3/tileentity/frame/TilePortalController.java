package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.portal.PortalUtils;
import uk.co.shadeddimensions.ep3.tileentity.TileFrame;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
import uk.co.shadeddimensions.ep3.util.ChunkCoordinateUtils;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.ep3.util.StackHelper;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TilePortalController extends TilePortalPart
{
    public List<WorldCoordinates> frameBasic, frameRedstone, frameFluid, framePower, portals;
    public WorldCoordinates frameModule, frameDialler, frameNetwork, frameBiometric, bridgeStabilizer;
    public boolean hasConfigured, waitingForCard, isPortalActive, processing;

    @SideOnly(Side.CLIENT)
    public GlyphIdentifier uniqueID;
    @SideOnly(Side.CLIENT)
    public GlyphIdentifier networkID;

    public int frameColour, customFrameTexture;
    public int portalColour, customPortalTexture, portalType;
    public int particleColour, particleType;

    ItemStack[] inventory;

    @SideOnly(Side.CLIENT)
    public int intBasic;
    @SideOnly(Side.CLIENT)
    public int intRedstone;
    @SideOnly(Side.CLIENT)
    public int intFluid;
    @SideOnly(Side.CLIENT)
    public int intPower;
    @SideOnly(Side.CLIENT)
    public int intPortal;
    @SideOnly(Side.CLIENT)
    public boolean boolDialler;
    @SideOnly(Side.CLIENT)
    public boolean boolNetwork;
    @SideOnly(Side.CLIENT)
    public boolean boolBiometric;
    @SideOnly(Side.CLIENT)
    public int connectedPortals;

    public TilePortalController()
    {
        frameBasic = new ArrayList<WorldCoordinates>();
        frameRedstone = new ArrayList<WorldCoordinates>();
        frameFluid = new ArrayList<WorldCoordinates>();
        framePower = new ArrayList<WorldCoordinates>();
        portals = new ArrayList<WorldCoordinates>();
        frameModule = null;
        frameDialler = null;
        frameNetwork = null;
        frameBiometric = null;
        bridgeStabilizer = null;
        hasConfigured = waitingForCard = processing = false;

        frameColour = portalColour = 0xffffff;
        particleColour = 0x0077D8;
        particleType = portalType = 0;

        customFrameTexture = customPortalTexture = -1;

        inventory = new ItemStack[2];
    }

    @Override
    public TilePortalController getPortalController()
    {
        return this;
    }

    public void configure(List<WorldCoordinates> tiles, int pType, EntityPlayer player)
    {
        if (CommonProxy.isClient() || hasConfigured)
        {
            return;
        }

        System.out.println("Configuring " + tiles.size() + " blocks!");

        frameBasic.clear();
        frameRedstone.clear();
        frameFluid.clear();
        framePower.clear();
        portals.clear();
        frameModule = null;
        frameDialler = null;
        frameNetwork = null;
        frameBiometric = null;
        bridgeStabilizer = null;

        for (WorldCoordinates c : tiles)
        {
            TileEntity t = c.getBlockTileEntity();

            if (!(t instanceof TilePortalPart))
            {
                portals.add(c);
                continue;
            }

            TilePortalPart tile = (TilePortalPart) t;

            if (tile instanceof TileFrame)
            {
                frameBasic.add(c);
            }
            else if (tile instanceof TileRedstoneInterface)
            {
                frameRedstone.add(c);
            }
            else if (tile instanceof TileModuleManipulator)
            {
                frameModule = c;
            }
            else if (tile instanceof TileDiallingDevice)
            {
                frameDialler = c;
            }
            else if (tile instanceof TileNetworkInterface)
            {
                frameNetwork = c;
            }
            else if (tile instanceof TileBiometricIdentifier)
            {
                frameBiometric = c;
            }

            tile.portalController = getWorldCoordinates();
            CommonProxy.sendUpdatePacketToAllAround(tile);
        }

        portalType = pType;
        waitingForCard = true;
        player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.nextStage"));
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);

        GlyphIdentifier uID = getUniqueIdentifier(), nID = getNetworkIdentifier();

        stream.writeBoolean(hasConfigured);
        stream.writeUTF(uID != null ? uID.getGlyphString() : "");
        stream.writeUTF(nID != null ? nID.getGlyphString() : "");

        stream.writeInt(portals.size());
        stream.writeInt(frameBasic.size());
        stream.writeInt(frameRedstone.size());
        stream.writeInt(frameFluid.size());
        stream.writeInt(framePower.size());
        stream.writeInt(nID != null ? CommonProxy.networkManager.getNetworkSize(nID) : -1);

        stream.writeBoolean(frameDialler != null);
        stream.writeBoolean(frameNetwork != null);
        stream.writeBoolean(frameBiometric != null);
        stream.writeBoolean(waitingForCard);

        stream.writeInt(frameColour);
        stream.writeInt(portalColour);
        stream.writeInt(particleColour);
        stream.writeInt(particleType);
        stream.writeInt(portalType);

        stream.writeInt(customPortalTexture);
        stream.writeInt(customFrameTexture);

        if (frameModule != null)
        {
            stream.writeInt(frameModule.posX);
            stream.writeInt(frameModule.posY);
            stream.writeInt(frameModule.posZ);
        }
        else
        {
            stream.writeInt(0);
            stream.writeInt(-1);
            stream.writeInt(0);
        }

        for (int i = 0; i < getSizeInventory(); i++)
        {
            if (getStackInSlot(i) != null)
            {
                stream.writeInt(getStackInSlot(i).itemID);
                stream.writeInt(getStackInSlot(i).getItemDamage());
            }
            else
            {
                stream.writeInt(0);
                stream.writeInt(0);
            }
        }
    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);

        hasConfigured = stream.readBoolean();
        uniqueID = new GlyphIdentifier(stream);
        networkID = new GlyphIdentifier(stream);

        intPortal = stream.readInt();
        intBasic = stream.readInt();
        intRedstone = stream.readInt();
        intFluid = stream.readInt();
        intPower = stream.readInt();
        connectedPortals = stream.readInt();

        boolDialler = stream.readBoolean();
        boolNetwork = stream.readBoolean();
        boolBiometric = stream.readBoolean();
        waitingForCard = stream.readBoolean();

        frameColour = stream.readInt();
        portalColour = stream.readInt();
        particleColour = stream.readInt();
        particleType = stream.readInt();
        portalType = stream.readInt();

        customPortalTexture = stream.readInt();
        customFrameTexture = stream.readInt();

        WorldCoordinates c = new WorldCoordinates(stream.readInt(), stream.readInt(), stream.readInt(), worldObj.provider.dimensionId);        
        frameModule = c.posY > -1 ? c : null;

        for (int i = 0; i < getSizeInventory(); i++)
        {
            int id = stream.readInt(), meta = stream.readInt();

            if (id != 0)
            {
                setInventorySlotContents(i, new ItemStack(id, 1, meta));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setBoolean("hasConfigured", hasConfigured);
        tag.setBoolean("waitingForCard", waitingForCard);

        tag.setInteger("frameColour", frameColour);
        tag.setInteger("portalColour", portalColour);
        tag.setInteger("particleColour", particleColour);
        tag.setInteger("particleType", particleType);
        tag.setInteger("portalType", portalType);
        tag.setBoolean("isPortalActive", isPortalActive);
        tag.setInteger("customPortalTexture", customPortalTexture);
        tag.setInteger("customFrameTexture", customFrameTexture);

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

        tag.setTag("inventory", list);

        ChunkCoordinateUtils.saveWorldCoordList(tag, portals, "portals");
        ChunkCoordinateUtils.saveWorldCoordList(tag, frameBasic, "frameBasic");
        ChunkCoordinateUtils.saveWorldCoordList(tag, frameRedstone, "frameRedstone");
        ChunkCoordinateUtils.saveWorldCoord(tag, frameBiometric, "frameBiometric");
        ChunkCoordinateUtils.saveWorldCoord(tag, frameDialler, "frameDialler");
        ChunkCoordinateUtils.saveWorldCoord(tag, frameModule, "frameModule");
        ChunkCoordinateUtils.saveWorldCoord(tag, frameNetwork, "frameNetwork");
        ChunkCoordinateUtils.saveWorldCoord(tag, bridgeStabilizer, "bridgeStabilizer");
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        hasConfigured = tag.getBoolean("hasConfigured");
        waitingForCard = tag.getBoolean("waitingForCard");

        frameColour = tag.getInteger("frameColour");
        portalColour = tag.getInteger("portalColour");
        particleColour = tag.getInteger("particleColour");
        particleType = tag.getInteger("particleType");
        portalType = tag.getInteger("portalType");
        isPortalActive = tag.getBoolean("isPortalActive");
        customPortalTexture = tag.getInteger("customPortalTexture");
        customFrameTexture = tag.getInteger("customFrameTexture");

        NBTTagList list = tag.getTagList("inventory");
        for (int i = 0; i < list.tagCount(); i++)
        {
            inventory[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) list.tagAt(i));
        }

        portals = ChunkCoordinateUtils.loadWorldCoordList(tag, "portals");
        frameBasic = ChunkCoordinateUtils.loadWorldCoordList(tag, "frameBasic");
        frameRedstone = ChunkCoordinateUtils.loadWorldCoordList(tag, "frameRedstone");
        frameBiometric = ChunkCoordinateUtils.loadWorldCoord(tag, "frameBiometric");
        frameDialler = ChunkCoordinateUtils.loadWorldCoord(tag, "frameDialler");
        frameModule = ChunkCoordinateUtils.loadWorldCoord(tag, "frameModule");
        frameNetwork = ChunkCoordinateUtils.loadWorldCoord(tag, "frameNetwork");
        bridgeStabilizer = ChunkCoordinateUtils.loadWorldCoord(tag, "bridgeStabilizer");
    }

    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        super.guiActionPerformed(payload, player);
        boolean sendUpdatePacket = false;

        if (payload.data.hasKey("DialRequest"))
        {
            dialRequest(new GlyphIdentifier(payload.data.getString("DialRequest")));
        }
        else if (payload.data.hasKey("DialTerminateRequest"))
        {
            removePortal();
        }

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

        if (payload.data.hasKey("GlyphName") && payload.data.hasKey("Glyphs"))
        {
            TileDiallingDevice dial = (TileDiallingDevice) frameDialler.getBlockTileEntity();

            if (dial != null)
            {
                dial.glyphList.add(dial.new GlyphElement(payload.data.getString("GlyphName"), new GlyphIdentifier(payload.data.getString("Glyphs"))));
                CommonProxy.sendUpdatePacketToAllAround(dial);
            }
        }

        if (payload.data.hasKey("DeleteGlyph"))
        {
            TileDiallingDevice dial = (TileDiallingDevice) frameDialler.getBlockTileEntity();

            if (dial != null)
            {
                dial.glyphList.remove(payload.data.getInteger("DeleteGlyph"));
                CommonProxy.sendUpdatePacketToAllAround(dial);
            }
        }

        if (payload.data.hasKey("frameColour"))
        {
            frameColour = payload.data.getInteger("frameColour");
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("portalColour"))
        {
            portalColour = payload.data.getInteger("portalColour");
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("particleColour"))
        {
            particleColour = payload.data.getInteger("particleColour");
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("particleType"))
        {
            particleType = payload.data.getInteger("particleType");
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("customFrameTexture"))
        {
            customFrameTexture = payload.data.getInteger("customFrameTexture");
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("customPortalTexture"))
        {
            customPortalTexture = payload.data.getInteger("customPortalTexture");
            sendUpdatePacket = true;
        }

        if (payload.data.hasKey("resetSlot"))
        {
            setInventorySlotContents(payload.data.getInteger("resetSlot"), null);
            sendUpdatePacket = true;
        }

        if (sendUpdatePacket)
        {
            CommonProxy.sendUpdatePacketToAllAround(this);
        }
    }

    @Override
    public boolean activate(EntityPlayer player)
    {
        if (super.activate(player))
        {
            return true;
        }

        if (!hasConfigured && waitingForCard && CommonProxy.isServer())
        {
            if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID != CommonProxy.itemLocationCard.itemID)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.nextStage"));
            }           

            return false;
        }

        if (CommonProxy.isClient() || hasConfigured)
        {
            return false;
        }

        System.out.println("Configuring controller...");

        Queue<WorldCoordinates> portalBlocks = new LinkedList<WorldCoordinates>();
        int pType = 0;

        outerloop:
            for (int j = 0; j < 6; j++)
            {
                for (int i = 1; i < 4; i++)
                {
                    portalBlocks = PortalUtils.ghostPortalAt(getWorldCoordinates().offset(ForgeDirection.getOrientation(j)), i);

                    if (!portalBlocks.isEmpty())
                    {
                        pType = i;
                        break outerloop;
                    }
                }
            }

        System.out.println("Found " + portalBlocks.size() + " portal blocks!");

        if (!portalBlocks.isEmpty())
        {
            List<WorldCoordinates> portalParts = new ArrayList<WorldCoordinates>();
            Queue<WorldCoordinates> toProcess = new LinkedList<WorldCoordinates>();
            Queue<WorldCoordinates> processed = new LinkedList<WorldCoordinates>();
            toProcess.add(getWorldCoordinates());

            int biometricCounter = 0, dialCounter = 0, networkCounter = 0, moduleCounter = 0;

            while (!toProcess.isEmpty())
            {
                WorldCoordinates c = toProcess.remove();

                if (!processed.contains(c))
                {
                    processed.add(c);
                    TileEntity t = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);

                    if (portalBlocks.contains(c) || t instanceof TilePortalPart)
                    {
                        if (t != null)
                        {
                            if (t instanceof TileNetworkInterface)
                            {
                                if (networkCounter == 1)
                                {
                                    player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.multipleNetworkInterfaces"));
                                    return false;
                                }
                                else if (dialCounter == 1)
                                {
                                    player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.networkInterfaceAndDialDevice"));
                                    return false;
                                }

                                networkCounter++;
                            }
                            else if (t instanceof TileDiallingDevice)
                            {
                                if (dialCounter == 1)
                                {
                                    player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.multipleDiallingDevices"));
                                    return false;
                                }
                                else if (networkCounter == 1)
                                {
                                    player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.networkInterfaceAndDialDevice"));
                                    return false;
                                }

                                dialCounter++;
                            }
                            else if (t instanceof TileBiometricIdentifier)
                            {
                                if (biometricCounter == 1)
                                {
                                    player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.multipleBiometricIdentifiers"));
                                    return false;
                                }

                                biometricCounter++;
                            }
                            else if (t instanceof TileModuleManipulator)
                            {
                                if (moduleCounter == 1)
                                {
                                    player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.multipleModuleManipulators"));
                                    return false;
                                }

                                moduleCounter++;
                            }
                            else if (t instanceof TilePortalController && processed.size() > 1)
                            {
                                player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.multipleControllers"));
                                return false;
                            }
                        }

                        portalParts.add(c);
                        PortalUtils.addNearbyBlocks(c, 0, toProcess);
                    }
                }
            }

            configure(portalParts, pType, player);
        }
        else
        {
            player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.noPortals"));
        }

        return false;
    }

    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        partBroken();
    }

    public TileStabilizer getStabilizer()
    {
        if (bridgeStabilizer == null)
        {
            return null;
        }

        TileEntity t = bridgeStabilizer.getBlockTileEntity();

        if (t instanceof TileStabilizer)
        {
            return (TileStabilizer) t;
        }

        return null;
    }

    public void dialRequest(GlyphIdentifier id)
    {
        if (CommonProxy.networkManager.hasIdentifier(getWorldCoordinates()) && frameDialler != null)
        {
            TileStabilizer dbs = getStabilizer();

            if (dbs == null || !dbs.hasConfigured)
            {
                bridgeStabilizer = null;
                waitingForCard = true;
                hasConfigured = false;
                CommonProxy.sendUpdatePacketToAllAround(this);
                return;
            }

            dbs.setupNewConnection(getUniqueIdentifier(), id);
        }
    }

    /***
     * Creates a portal using the set network, if available. Called via redstone interface.
     */
    public void createPortal()
    {
        if (CommonProxy.isClient())
        {
            return;
        }

        if (CommonProxy.networkManager.hasIdentifier(getWorldCoordinates()) && CommonProxy.networkManager.hasNetwork(getWorldCoordinates()))
        {
            TileStabilizer dbs = getStabilizer();

            if (dbs == null || !dbs.hasConfigured)
            {
                bridgeStabilizer = null;
                waitingForCard = true;
                hasConfigured = false;
                CommonProxy.sendUpdatePacketToAllAround(this);
                return;
            }

            GlyphIdentifier identifier = getUniqueIdentifier(), dest = CommonProxy.networkManager.getDestination(identifier, CommonProxy.networkManager.getPortalNetwork(identifier));
            dbs.setupNewConnection(identifier, dest);
        }
    }

    /***
     * Removes a portal and resets the destination portal.
     */
    public void removePortal()
    {
        if (CommonProxy.isClient())
        {
            return;
        }

        if (CommonProxy.networkManager.hasIdentifier(getWorldCoordinates()))
        {
            TileStabilizer dbs = getStabilizer();
            GlyphIdentifier identifier = CommonProxy.networkManager.getPortalIdentifier(getWorldCoordinates());

            if (dbs == null || !dbs.hasConfigured)
            {
                bridgeStabilizer = null;
                waitingForCard = true;
                hasConfigured = false;
                CommonProxy.sendUpdatePacketToAllAround(this);
                return;
            }

            if (frameDialler != null)
            {
                dbs.terminateExistingConnection(identifier);
            }
            else if (frameNetwork != null && CommonProxy.networkManager.hasNetwork(getWorldCoordinates()))
            {                
                GlyphIdentifier dest = CommonProxy.networkManager.getDestination(identifier, CommonProxy.networkManager.getPortalNetwork(identifier));
                dbs.terminateExistingConnection(identifier, dest);
            }
        }
    }

    public TileModuleManipulator getModuleManipulator()
    {
        return frameModule != null ? (TileModuleManipulator) worldObj.getBlockTileEntity(frameModule.posX, frameModule.posY, frameModule.posZ) : null;
    }

    public List<WorldCoordinates> getAllPortalBlocks()
    {
        return portals;
    }

    /* IInventory */
    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack s = getStackInSlot(i);
        s.stackSize -= j;

        return s;
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

    public void setPortalActive(boolean b)
    {
        isPortalActive = b;

        for (WorldCoordinates c : frameRedstone)
        {
            if (b)
            {
                ((TileRedstoneInterface) c.getBlockTileEntity()).portalCreated();
            }
            else
            {
                ((TileRedstoneInterface) c.getBlockTileEntity()).portalRemoved();
            }
        }

        CommonProxy.sendUpdatePacketToAllAround(this);
    }

    public void partBroken()
    {
        if (!processing && hasConfigured)
        {
            processing = true;

            for (WorldCoordinates c : frameBasic)
            {
                TileFrame frame = (TileFrame) c.getBlockTileEntity();
                frame.portalController = null;
                CommonProxy.sendUpdatePacketToAllAround(frame);
            }

            for (WorldCoordinates c : frameRedstone)
            {
                TileRedstoneInterface frame = (TileRedstoneInterface) c.getBlockTileEntity();
                frame.portalController = null;
                CommonProxy.sendUpdatePacketToAllAround(frame);
            }

            for (WorldCoordinates c : portals)
            {
                worldObj.setBlock(c.posX, c.posY, c.posZ, 0, 0, 2);
            }

            if (frameModule != null)
            {
                TileModuleManipulator frame = (TileModuleManipulator) frameModule.getBlockTileEntity();
                frame.portalController = null;
                CommonProxy.sendUpdatePacketToAllAround(frame);
            }

            if (frameDialler != null)
            {
                TileDiallingDevice frame = (TileDiallingDevice) frameDialler.getBlockTileEntity();
                frame.portalController = null;
                CommonProxy.sendUpdatePacketToAllAround(frame);
            }

            if (frameNetwork != null)
            {
                TileNetworkInterface frame = (TileNetworkInterface) frameNetwork.getBlockTileEntity();
                frame.portalController = null;
                CommonProxy.sendUpdatePacketToAllAround(frame);
            }

            if (frameBiometric != null)
            {
                TileBiometricIdentifier frame = (TileBiometricIdentifier) frameBiometric.getBlockTileEntity();
                frame.portalController = null;
                CommonProxy.sendUpdatePacketToAllAround(frame);
            }

            frameBasic.clear();
            frameRedstone.clear();
            portals.clear();
            frameModule = null;
            frameDialler = null;
            frameNetwork = null;
            frameBiometric = null;

            CommonProxy.networkManager.removePortalFromNetwork(getUniqueIdentifier(), getNetworkIdentifier());
            CommonProxy.networkManager.removePortal(getWorldCoordinates());

            isPortalActive = false;
            hasConfigured = false;
            processing = false;

            CommonProxy.sendUpdatePacketToAllAround(this);
        }
    }

    public boolean hasUniqueIdentifier()
    {
        return CommonProxy.networkManager.hasIdentifier(getWorldCoordinates());
    }

    public boolean hasNetworkIdentifier()
    {
        return hasUniqueIdentifier() ? CommonProxy.networkManager.hasNetwork(getUniqueIdentifier()) : false;
    }

    /***
     * Gets this controllers unique identifier.
     * @return Null if one is not set
     */
    public GlyphIdentifier getUniqueIdentifier()
    {
        if (CommonProxy.isClient())
        {
            return uniqueID;
        }

        return CommonProxy.networkManager.getPortalIdentifier(getWorldCoordinates());
    }

    /***
     * Gets this controllers network identifier.
     * @return Null if one is not set
     */
    public GlyphIdentifier getNetworkIdentifier()
    {
        if (CommonProxy.isClient())
        {
            return networkID;
        }

        return CommonProxy.networkManager.getPortalNetwork(getUniqueIdentifier());
    }

    public void onEntityEnterPortal(Entity entity)
    {
        GlyphIdentifier uID = getUniqueIdentifier();

        if (uID == null)
        {
            return;
        }
        else
        {
            TileStabilizer dbs = getStabilizer();
            dbs.onEntityEnterPortal(uID, entity);
        }
    }
}