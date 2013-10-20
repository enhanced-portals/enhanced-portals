package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.portal.PortalUtils;
import uk.co.shadeddimensions.ep3.portal.StackHelper;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.util.ChunkCoordinateUtils;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TilePortalController extends TilePortalPart
{
    public List<WorldCoordinates> frameBasic, frameRedstone, frameFluid, framePower, portals;
    public WorldCoordinates frameModule, frameDialler, frameNetwork, frameBiometric;
    public boolean hasConfigured;
    public boolean isPortalActive;
    public String uniqueIdentifier, networkIdentifier;
    
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
        hasConfigured = false;
        
        frameColour = portalColour = 0xffffff;
        particleColour = 0xB336A1;
        particleType = portalType = 0;
        
        uniqueIdentifier = networkIdentifier = NetworkManager.BLANK_IDENTIFIER;
        customFrameTexture = customPortalTexture = -1;
        
        inventory = new ItemStack[2];
    }
    
    @Override
    public TilePortalController getPortalController()
    {
        return this;
    }
    
    public void configure(List<WorldCoordinates> tiles, int pType)
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
        
        for (WorldCoordinates c : tiles)
        {
            TilePortalPart tile = (TilePortalPart) c.getBlockTileEntity();
            
            if (tile == null)
            {
                portals.add(c);
            }
            else
            {
                if (tile instanceof TilePortalFrame)
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
        }
        
        portalType = pType;
        hasConfigured = true;
    }
    
    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);
        
        stream.writeBoolean(hasConfigured);
        
        stream.writeInt(portals.size());
        stream.writeInt(frameBasic.size());
        stream.writeInt(frameRedstone.size());
        stream.writeInt(frameFluid.size());
        stream.writeInt(framePower.size());
        
        stream.writeBoolean(frameDialler != null);
        stream.writeBoolean(frameNetwork != null);
        stream.writeBoolean(frameBiometric != null);
        
        stream.writeUTF(uniqueIdentifier);
        stream.writeUTF(networkIdentifier);
        
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
        
        intPortal = stream.readInt();
        intBasic = stream.readInt();
        intRedstone = stream.readInt();
        intFluid = stream.readInt();
        intPower = stream.readInt();
        
        boolDialler = stream.readBoolean();
        boolNetwork = stream.readBoolean();
        boolBiometric = stream.readBoolean();
        
        uniqueIdentifier = stream.readUTF();
        networkIdentifier = stream.readUTF();
        
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
        
        tag.setString("uniqueIdentifier", uniqueIdentifier);
        tag.setString("networkIdentifier", networkIdentifier);
        
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
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        
        hasConfigured = tag.getBoolean("hasConfigured");
        
        uniqueIdentifier = tag.getString("uniqueIdentifier");
        networkIdentifier = tag.getString("networkIdentifier");
        
        frameColour = tag.getInteger("frameColour");
        portalColour = tag.getInteger("portalColour");
        particleColour = tag.getInteger("particleColour");
        particleType = tag.getInteger("particleType");
        portalType = tag.getInteger("portalType");
        isPortalActive = tag.getBoolean("isPortalActive");
        customPortalTexture = tag.getInteger("customPortalTexture");
        customFrameTexture = tag.getInteger("customFrameTexture");
        
        NBTTagList list = tag.getTagList("inventory");
        for (int i = 0; i < list.tagList.size(); i++)
        {
            inventory[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) list.tagList.get(i));
        }
        
        portals = ChunkCoordinateUtils.loadWorldCoordList(tag, "portals");
        frameBasic = ChunkCoordinateUtils.loadWorldCoordList(tag, "frameBasic");
        frameRedstone = ChunkCoordinateUtils.loadWorldCoordList(tag, "frameRedstone");
        frameBiometric = ChunkCoordinateUtils.loadWorldCoord(tag, "frameBiometric");
        frameDialler = ChunkCoordinateUtils.loadWorldCoord(tag, "frameDialler");
        frameModule = ChunkCoordinateUtils.loadWorldCoord(tag, "frameModule");
        frameNetwork = ChunkCoordinateUtils.loadWorldCoord(tag, "frameNetwork");
    }
    
    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        super.guiActionPerformed(payload, player);
        boolean sendUpdatePacket = false;
        
        if (payload.data.hasKey("uniqueIdentifier"))
        {
            String identifier = payload.data.getString("uniqueIdentifier");
            
            if (identifier.equals(NetworkManager.BLANK_IDENTIFIER) && !uniqueIdentifier.equals(NetworkManager.BLANK_IDENTIFIER)) // If the identifier is blank and previously it wasn't
            {
                CommonProxy.networkManager.removePortal(uniqueIdentifier);
            }
            else if (!identifier.equals(NetworkManager.BLANK_IDENTIFIER) && uniqueIdentifier.equals(NetworkManager.BLANK_IDENTIFIER)) // If the identifier isn't blank and previously it was
            {
                CommonProxy.networkManager.addNewPortal(identifier, getWorldCoordinates());
            }
            else if (!identifier.equals(NetworkManager.BLANK_IDENTIFIER) && !uniqueIdentifier.equals(NetworkManager.BLANK_IDENTIFIER)) // If the identifier isn't blank and previously it wasn't
            {
                CommonProxy.networkManager.updateExistingPortal(uniqueIdentifier, identifier);
                
                if (!networkIdentifier.equals(NetworkManager.BLANK_IDENTIFIER)) // Remove old identifier from network and add the new one
                {
                    CommonProxy.networkManager.removePortalFromNetwork(uniqueIdentifier, networkIdentifier);
                    CommonProxy.networkManager.addPortalToNetwork(identifier, networkIdentifier);
                }
            }
            
            uniqueIdentifier = identifier;
            sendUpdatePacket = true;
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
                                    // TODO: ERROR MESSAGE
                                    return false;
                                }
                                else if (dialCounter == 1)
                                {
                                    // TODO: ERROR MESSAGE
                                    return false;
                                }
                                
                                networkCounter++;
                            }
                            else if (t instanceof TileDiallingDevice)
                            {
                                if (dialCounter == 1)
                                {
                                    // TODO: ERROR MESSAGE
                                    return false;
                                }
                                else if (networkCounter == 1)
                                {
                                    // TODO: ERROR MESSAGE
                                    return false;
                                }
                                
                                dialCounter++;
                            }
                            else if (t instanceof TileBiometricIdentifier)
                            {
                                if (biometricCounter == 1)
                                {
                                    // TODO: ERROR MESSAGE
                                    return false;
                                }
                                
                                biometricCounter++;
                            }
                            else if (t instanceof TileModuleManipulator)
                            {
                                if (moduleCounter == 1)
                                {
                                    // TODO: ERROR MESSAGE
                                    return false;
                                }
                                
                                moduleCounter++;
                            }
                            else if (t instanceof TilePortalController && processed.size() > 1)
                            {
                                // ERROR MESSAGE
                                return false;
                            }
                        }
                        
                        portalParts.add(c);
                        PortalUtils.addNearbyBlocks(c, 0, toProcess);
                    }
                }
            }

            configure(portalParts, pType);
        }
                
        return false;
    }
    
    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        // TODO destroy portal
    }
    
    public void createPortal()
    {
        PortalUtils.createPortalFrom(this);
    }
    
    public void removePortal()
    {
        PortalUtils.removePortalFrom(this);
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
        
        // TODO notify all redstone identifiers
        CommonProxy.sendUpdatePacketToAllAround(this);
    }
}