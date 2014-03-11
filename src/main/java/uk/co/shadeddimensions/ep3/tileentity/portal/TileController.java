package uk.co.shadeddimensions.ep3.tileentity.portal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.block.BlockPortal;
import uk.co.shadeddimensions.ep3.item.ItemLocationCard;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.PacketHandlerServer;
import uk.co.shadeddimensions.ep3.network.packet.PacketGuiData;
import uk.co.shadeddimensions.ep3.network.packet.PacketRerender;
import uk.co.shadeddimensions.ep3.portal.EntityManager;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.portal.PortalException;
import uk.co.shadeddimensions.ep3.portal.PortalUtilsNew;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.util.ComputerUtils;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import uk.co.shadeddimensions.ep3.util.PortalTextureManager;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;
import uk.co.shadeddimensions.ep3.util.WorldUtils;
import uk.co.shadeddimensions.library.util.ItemHelper;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;

public class TileController extends TileFrame implements IPeripheral, SimpleComponent
{
    enum ControlState
    {
        /**
         * Nothing has happened with me yet. Waiting for a Location Card
         */
        REQUIRES_LOCATION,
        /**
         * Portal was broken. Waiting to be wrenched.
         */
        REQUIRES_WRENCH,
        /**
         * All is OK
         */
        FINALIZED
    }

    ArrayList<ChunkCoordinates> portalFrames = new ArrayList<ChunkCoordinates>();
    ArrayList<ChunkCoordinates> portalBlocks = new ArrayList<ChunkCoordinates>();
    ArrayList<ChunkCoordinates> redstoneInterfaces = new ArrayList<ChunkCoordinates>();
    ArrayList<ChunkCoordinates> networkInterfaces = new ArrayList<ChunkCoordinates>();
    ArrayList<ChunkCoordinates> diallingDevices = new ArrayList<ChunkCoordinates>();
    ArrayList<ChunkCoordinates> transferFluids = new ArrayList<ChunkCoordinates>();
    ArrayList<ChunkCoordinates> transferItems = new ArrayList<ChunkCoordinates>();
    ArrayList<ChunkCoordinates> transferEnergy = new ArrayList<ChunkCoordinates>();

    ChunkCoordinates biometricIdentifier;
    ChunkCoordinates moduleManipulator;

    WorldCoordinates dimensionalBridgeStabilizer, temporaryDBS;

    public PortalTextureManager activeTextureData = new PortalTextureManager(), inactiveTextureData;

    ControlState portalState = ControlState.REQUIRES_LOCATION;

    public int connectedPortals = -1, instability = 0, portalType = 0;
    
    Random random = new Random();

    boolean processing, isPublic;
    
    GlyphIdentifier cachedDestinationUID;
    WorldCoordinates cachedDestinationLoc;

    @SideOnly(Side.CLIENT)
    int countPortals, countFrames, countRedstone, countNetwork, countDial, countFluids, countItems, countEnergy;

    @SideOnly(Side.CLIENT)
    boolean hasBio;

    @SideOnly(Side.CLIENT)
    GlyphIdentifier uID, nID;

    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
    	if (player.isSneaking())
		{
			return false;
		}
    	
        try
        {
            if (stack != null)
            {
                if (portalState == ControlState.REQUIRES_LOCATION)
                {
                    if (stack.itemID == ItemLocationCard.ID && !worldObj.isRemote)
                    {
                        boolean reconfiguring = dimensionalBridgeStabilizer != null;
                        setDBS(player, stack);
                        configurePortal();
                        player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getSuccessString(!reconfiguring ? "create" : "reconfigure")));
                    }

                    return true;
                }
                else if (portalState == ControlState.REQUIRES_WRENCH)
                {
                    if (ItemHelper.isWrench(stack) && !worldObj.isRemote)
                    {
                        configurePortal();
                        player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getSuccessString("reconfigure")));
                    }

                    return true;
                }
                else if (portalState == ControlState.FINALIZED)
                {
                    if (ItemHelper.isWrench(stack))
                    {
                        GuiHandler.openGui(player, this, GuiHandler.PORTAL_CONTROLLER);
                        return true;
                    }
                    else if (stack.itemID == ItemPaintbrush.ID)
                    {
                        GuiHandler.openGui(player, this, GuiHandler.TEXTURE_FRAME);
                        return true;
                    }
                }
            }
        }
        catch (PortalException e)
        {
            player.sendChatToPlayer(ChatMessageComponent.createFromText(e.getMessage()));
        }

        return false;
    }

    public void addDialDevice(ChunkCoordinates chunkCoordinates)
	{
		diallingDevices.add(chunkCoordinates);
	}

    public void addNetworkInterface(ChunkCoordinates chunkCoordinates)
	{
		networkInterfaces.add(chunkCoordinates);
	}

    public void addRedstoneInterface(ChunkCoordinates chunkCoordinates)
	{
		redstoneInterfaces.add(chunkCoordinates);
	}
    
    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
    	try
    	{
    		deconstruct();
    		setIdentifierNetwork(new GlyphIdentifier());
			setIdentifierUnique(new GlyphIdentifier());
		}
    	catch (PortalException e)
		{
			e.printStackTrace();
		}
    }

    /**
     * Updates the data of the destination portal.
     * 
     * @param id
     * @param wc
     */
    public void cacheDestination(GlyphIdentifier id, WorldCoordinates wc)
    {
        cachedDestinationUID = id;
        cachedDestinationLoc = wc;
        onInventoryChanged();
        WorldUtils.markForUpdate(this);
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    void configurePortal() throws PortalException
    {
        ArrayList<ChunkCoordinates> portalStructure = PortalUtilsNew.getAllPortalComponents(this);

        for (ChunkCoordinates c : portalStructure)
        {
            TileEntity tile = worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);

            if (tile instanceof TileController)
            {

            }
            else if (tile instanceof TileFrameBasic)
            {
                portalFrames.add(c);
            }
            else if (tile instanceof TileRedstoneInterface)
            {
                redstoneInterfaces.add(c);
            }
            else if (tile instanceof TileNetworkInterface)
            {
                networkInterfaces.add(c);
            }
            else if (tile instanceof TileDiallingDevice)
            {
                diallingDevices.add(c);
            }
            else if (tile instanceof TileBiometricIdentifier)
            {
                biometricIdentifier = c;
            }
            else if (tile instanceof TileModuleManipulator)
            {
                moduleManipulator = c;
            }
            else if (tile instanceof TileTransferFluid)
            {
                transferFluids.add(c);
            }
            else if (tile instanceof TileTransferItem)
            {
                transferItems.add(c);
            }
            else if (tile instanceof TileTransferEnergy)
            {
                transferEnergy.add(c);
            }
            else
            {
                portalBlocks.add(c);
                continue;
            }

            ((TilePortalPart) tile).setPortalController(getChunkCoordinates());
        }

        portalState = ControlState.FINALIZED;
        onInventoryChanged();
        WorldUtils.markForUpdate(this);
    }

    public void connectionDial()
    {
    	if (worldObj.isRemote || getIdentifierNetwork() == null)
        {
            return;
        }
    	
    	try
        {
            TileStabilizerMain dbs = getDimensionalBridgeStabilizer();

            if (dbs == null)
            {
                throw new PortalException("stabilizerNotFound");
            }
            
            dbs.setupNewConnection(getIdentifierUnique(), CommonProxy.networkManager.getDestination(getIdentifierUnique(),  getIdentifierNetwork()), null);
        }
        catch (PortalException e)
        {
        	EntityPlayer player = worldObj.getClosestPlayer(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128);
            
            if (player != null)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText(e.getMessage()));
            }
        }
    	
    	onInventoryChanged();
	}

    public void connectionDial(GlyphIdentifier id, PortalTextureManager m, EntityPlayer player)
    {
        if (worldObj.isRemote)
        {
            return;
        }

        try
        {
            TileStabilizerMain dbs = getDimensionalBridgeStabilizer();

            if (dbs == null)
            {
                throw new PortalException("stabilizerNotFound");
            }

            dbs.setupNewConnection(getIdentifierUnique(), id, m);
        }
        catch (PortalException e)
        {
            if (player == null)
            {
                player = worldObj.getClosestPlayer(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128);
            }

            if (player != null)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText(e.getMessage()));
            }
        }
        
        onInventoryChanged();
    }

    public void connectionTerminate()
    {
        if (worldObj.isRemote || processing)
        {
            return;
        }
        
        try
        {
            TileStabilizerMain dbs = getDimensionalBridgeStabilizer();

            if (dbs == null)
            {
                throw new PortalException("stabilizerNotFound");
            }

            dbs.terminateExistingConnection(getIdentifierUnique());
        }
        catch (PortalException e)
        {
            System.out.println(e.getMessage());
        }
        
        temporaryDBS = null;
        onInventoryChanged();
    }

    /**
     * Deconstructs the portal structure.
     */
    public void deconstruct()
    {
        if (processing)
        {
            return;
        }
        
        if (isPortalActive())
        {
        	connectionTerminate();
        }
        
        for (ChunkCoordinates c : portalFrames)
        {
            TileEntity t = WorldUtils.getTileEntity(worldObj, c);

            if (t != null && t instanceof TilePortalPart)
            {
                ((TilePortalPart) t).setPortalController(null);
            }
        }

        for (ChunkCoordinates c : redstoneInterfaces)
        {
            TileEntity t = WorldUtils.getTileEntity(worldObj, c);

            if (t != null && t instanceof TilePortalPart)
            {
                ((TilePortalPart) t).setPortalController(null);
            }
        }

        for (ChunkCoordinates c : networkInterfaces)
        {
            TileEntity t = WorldUtils.getTileEntity(worldObj, c);

            if (t != null && t instanceof TilePortalPart)
            {
                ((TilePortalPart) t).setPortalController(null);
            }
        }

        for (ChunkCoordinates c : diallingDevices)
        {
            TileEntity t = WorldUtils.getTileEntity(worldObj, c);

            if (t != null && t instanceof TilePortalPart)
            {
                ((TilePortalPart) t).setPortalController(null);
            }
        }

        for (ChunkCoordinates c : transferFluids)
        {
            TileEntity t = WorldUtils.getTileEntity(worldObj, c);

            if (t != null && t instanceof TilePortalPart)
            {
                ((TilePortalPart) t).setPortalController(null);
            }
        }

        for (ChunkCoordinates c : transferItems)
        {
            TileEntity t = WorldUtils.getTileEntity(worldObj, c);

            if (t != null && t instanceof TilePortalPart)
            {
                ((TilePortalPart) t).setPortalController(null);
            }
        }

        for (ChunkCoordinates c : transferEnergy)
        {
            TileEntity t = WorldUtils.getTileEntity(worldObj, c);

            if (t != null && t instanceof TilePortalPart)
            {
                ((TilePortalPart) t).setPortalController(null);
            }
        }

        if (biometricIdentifier != null)
        {
            TileEntity t = WorldUtils.getTileEntity(worldObj, biometricIdentifier);

            if (t != null && t instanceof TilePortalPart)
            {
                ((TilePortalPart) t).setPortalController(null);
            }
        }

        if (moduleManipulator != null)
        {
            TileEntity t = WorldUtils.getTileEntity(worldObj, moduleManipulator);

            if (t != null && t instanceof TilePortalPart)
            {
                ((TilePortalPart) t).setPortalController(null);
            }
        }

        portalBlocks.clear();
        portalFrames.clear();
        redstoneInterfaces.clear();
        networkInterfaces.clear();
        diallingDevices.clear();
        transferFluids.clear();
        transferItems.clear();
        transferEnergy.clear();
        biometricIdentifier = null;
        moduleManipulator = null;
        portalState = ControlState.REQUIRES_WRENCH;
        onInventoryChanged();
        WorldUtils.markForUpdate(this);
    }

    public TileBiometricIdentifier getBiometricIdentifier()
    {
        if (biometricIdentifier != null)
        {
            TileEntity tile = worldObj.getBlockTileEntity(biometricIdentifier.posX, biometricIdentifier.posY, biometricIdentifier.posZ);

            if (tile instanceof TileBiometricIdentifier)
            {
                return (TileBiometricIdentifier) tile;
            }
        }

        return null;
    }

    /**
     * @return Returns the destination portal UID.
     */
    public GlyphIdentifier getDestination()
    {
        return cachedDestinationUID;
    }

    /**
     * @return Returns the location of the TilePortalController of the destination portal.
     */
    public WorldCoordinates getDestinationLocation()
    {
        return cachedDestinationLoc;
    }

    public TileDiallingDevice getDialDeviceRandom()
	{
		ChunkCoordinates dial = null;
		 
		if (diallingDevices.isEmpty())
		{
			return null;
		}
		else if (diallingDevices.size() == 1)
		{
			dial = diallingDevices.get(0);
		}
		else
		{
			dial = diallingDevices.get(random.nextInt(diallingDevices.size()));
		}
		
		TileEntity tile = WorldUtils.getTileEntity(worldObj, dial);
		
		if (tile != null && tile instanceof TileDiallingDevice)
		{
			return (TileDiallingDevice) tile;
		}
		
		return null;
	}

    public int getDiallingDeviceCount()
    {
        if (worldObj.isRemote)
        {
            return countDial;
        }
        else
        {
            return getDiallingDevices().size();
        }
    }

    public ArrayList<ChunkCoordinates> getDiallingDevices()
    {
        return diallingDevices;
    }

    public TileStabilizerMain getDimensionalBridgeStabilizer()
    {
        if (temporaryDBS != null)
        {
            World w = temporaryDBS.getWorld();
            TileEntity tile = w.getBlockTileEntity(temporaryDBS.posX, temporaryDBS.posY, temporaryDBS.posZ);

            if (tile instanceof TileStabilizerMain)
            {
                return (TileStabilizerMain) tile;
            }
            else if (tile instanceof TileStabilizer)
            {
                TileStabilizer t = (TileStabilizer) tile;
                TileStabilizerMain m = t.getMainBlock();

                if (m != null)
                {
                    temporaryDBS = m.getWorldCoordinates();
                    return m;
                }
            }

            temporaryDBS = null;
        }
        
        if (dimensionalBridgeStabilizer != null)
        {
            World w = dimensionalBridgeStabilizer.getWorld();
            TileEntity tile = w.getBlockTileEntity(dimensionalBridgeStabilizer.posX, dimensionalBridgeStabilizer.posY, dimensionalBridgeStabilizer.posZ);

            if (tile instanceof TileStabilizerMain)
            {
                return (TileStabilizerMain) tile;
            }
            else if (tile instanceof TileStabilizer)
            {
                TileStabilizer t = (TileStabilizer) tile;
                TileStabilizerMain m = t.getMainBlock();

                if (m != null)
                {
                    dimensionalBridgeStabilizer = m.getWorldCoordinates();
                    return m;
                }
            }

            dimensionalBridgeStabilizer = null;
        }

        return null;
    }

    public int getFrameCount()
    {
        if (worldObj.isRemote)
        {
            return countFrames;
        }
        else
        {
            return getFrames().size();
        }
    }

    public ArrayList<ChunkCoordinates> getFrames()
    {
        return portalFrames;
    }

    public boolean getHasBiometricIdentifier()
    {
        if (worldObj.isRemote)
        {
            return hasBio;
        }
        else
        {
            return getBiometricIdentifier() != null;
        }
    }

    /***
     * @return Returns if this portal has a NID.
     */
    public boolean getHasIdentifierNetwork()
    {
        return getIdentifierNetwork() != null;
    }

    /***
     * @return Returns if this portal has a UID.
     */
    public boolean getHasIdentifierUnique()
    {
        return getIdentifierUnique() != null;
    }

    public boolean getHasModuleManipulator()
    {
        return moduleManipulator != null;
    }

    /**
     * @return Returns the NID of the network this portal is connected to.
     */
    public GlyphIdentifier getIdentifierNetwork()
    {
        if (worldObj.isRemote)
        {
            return nID;
        }
        
        return CommonProxy.networkManager.getPortalNetwork(getIdentifierUnique());
    }

    /**
     * @return Returns the UID of this portal.
     */
    public GlyphIdentifier getIdentifierUnique()
    {
        if (worldObj.isRemote)
        {
            return uID;
        }
        
        return CommonProxy.networkManager.getPortalIdentifier(getWorldCoordinates());
    }

    public TileModuleManipulator getModuleManipulator()
    {
        if (moduleManipulator != null)
        {
            TileEntity tile = worldObj.getBlockTileEntity(moduleManipulator.posX, moduleManipulator.posY, moduleManipulator.posZ);

            if (tile instanceof TileModuleManipulator)
            {
                return (TileModuleManipulator) tile;
            }
        }

        return null;
    }

    public int getNetworkInterfaceCount()
    {
        if (worldObj.isRemote)
        {
            return countNetwork;
        }
        else
        {
            return getNetworkInterfaces().size();
        }
    }

    public ArrayList<ChunkCoordinates> getNetworkInterfaces()
    {
        return networkInterfaces;
    }

    @Override
    public TileController getPortalController()
    {
        return isFinalized() ? this : null;
    }

    public int getPortalCount()
    {
        if (worldObj.isRemote)
        {
            return countPortals;
        }
        else
        {
            return getPortals().size();
        }
    }

    public ArrayList<ChunkCoordinates> getPortals()
    {
        return portalBlocks;
    }

    public int getRedstoneInterfaceCount()
    {
        if (worldObj.isRemote)
        {
            return countRedstone;
        }
        else
        {
            return getRedstoneInterfaces().size();
        }
    }

    public ArrayList<ChunkCoordinates> getRedstoneInterfaces()
    {
        return redstoneInterfaces;
    }

    public ArrayList<ChunkCoordinates> getTransferEnergy()
    {
        return transferEnergy;
    }

    public int getTransferEnergyCount()
    {
        if (worldObj.isRemote)
        {
            return countEnergy;
        }
        else
        {
            return getTransferEnergy().size();
        }
    }

    public int getTransferFluidCount()
    {
        if (worldObj.isRemote)
        {
            return countFluids;
        }
        else
        {
            return getTransferFluids().size();
        }
    }
    
    public ArrayList<ChunkCoordinates> getTransferFluids()
    {
        return transferFluids;
    }

    public int getTransferItemCount()
    {
        if (worldObj.isRemote)
        {
            return countItems;
        }
        else
        {
            return getTransferItems().size();
        }
    }

    public ArrayList<ChunkCoordinates> getTransferItems()
    {
        return transferItems;
    }

    public boolean isFinalized()
    {
        return portalState == ControlState.FINALIZED;
    }
    
    /**
     * @return Portal active state.
     */
    public boolean isPortalActive()
    {
        return cachedDestinationUID != null;
    }

    public void onEntityEnterPortal(Entity entity, TilePortal tilePortal)
    {
        if (cachedDestinationLoc == null)
        {
            return;
        }
        
        onEntityTouchPortal(entity);
        TileEntity tile = cachedDestinationLoc.getBlockTileEntity();

        if (tile != null && tile instanceof TileController)
        {
            TileController control = (TileController) tile;
            
            try
            {
                EntityManager.transferEntity(entity, this, control);
                control.onEntityTeleported(entity);
                control.onEntityTouchPortal(entity);
            }
            catch (PortalException e)
            {
                if (entity instanceof EntityPlayer)
                {
                	((EntityPlayer) entity).sendChatToPlayer(ChatMessageComponent.createFromText(e.getMessage()));
                }
            }
        }
    }

    public void onEntityTeleported(Entity entity)
    {
        TileModuleManipulator module = getModuleManipulator();

        if (module != null)
        {
            module.onEntityTeleported(entity);
        }
    }

    public void onEntityTouchPortal(Entity entity)
    {
        for (ChunkCoordinates c : getRedstoneInterfaces())
        {
            ((TileRedstoneInterface) worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ)).onEntityTeleport(entity);
        }
    }

    public void onPartFrameBroken()
    {
        deconstruct();
    }

    @Override
    public void addDataToPacket(NBTTagCompound tag)
    {
        tag.setByte("PortalState", (byte) portalState.ordinal());
        tag.setBoolean("PortalActive", isPortalActive());
        tag.setInteger("Instability", instability);
        
        if (isPortalActive())
        {
            tag.setString("DestUID", cachedDestinationUID.getGlyphString());
            tag.setInteger("destX", cachedDestinationLoc.posX);
            tag.setInteger("destY", cachedDestinationLoc.posY);
            tag.setInteger("destZ", cachedDestinationLoc.posZ);
            tag.setInteger("destD", cachedDestinationLoc.dimension);
        }
        
        activeTextureData.writeToNBT(tag, "Texture");
        
        if (moduleManipulator != null)
        {
            tag.setInteger("ModX", moduleManipulator.posX);
            tag.setInteger("ModY", moduleManipulator.posY);
            tag.setInteger("ModZ", moduleManipulator.posZ);
        }
    }
    
    @Override
    public void onDataPacket(NBTTagCompound tag)
    {
        portalState = ControlState.values()[tag.getByte("PortalState")];

        if (tag.hasKey("DestUID"))
        {
            cachedDestinationUID = new GlyphIdentifier(tag.getString("DestUID"));
            cachedDestinationLoc = new WorldCoordinates(tag.getInteger("destX"), tag.getInteger("destY"), tag.getInteger("destZ"), tag.getInteger("destD"));
        }
        else
        {
            cachedDestinationUID = null;
            cachedDestinationLoc = null;
        }

        activeTextureData.readFromNBT(tag, "Texture");
        instability = tag.getInteger("Instability");

        if (tag.hasKey("ModX"))
        {
            moduleManipulator = new ChunkCoordinates(tag.getInteger("ModX"), tag.getInteger("ModY"), tag.getInteger("ModZ"));
        }
    }

    @Override
    public void packetGui(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("uid"))
        {
            try
            {
                setIdentifierUnique(new GlyphIdentifier(tag.getString("uid")));
            }
            catch (PortalException e)
            {
                NBTTagCompound errorTag = new NBTTagCompound();
                errorTag.setInteger("controller", 0);
                PacketDispatcher.sendPacketToPlayer(new PacketGuiData(errorTag).getPacket(), (Player) player);
            }
        }
        else if (tag.hasKey("nid"))
        {
            setIdentifierNetwork(new GlyphIdentifier(tag.getString("nid")));
            PacketHandlerServer.sendGuiPacketToPlayer(this, player);
        }
        
        if (tag.hasKey("frameColour"))
        {
            setFrameColour(tag.getInteger("frameColour"));
        }

        if (tag.hasKey("portalColour"))
        {
            setPortalColour(tag.getInteger("portalColour"));
        }

        if (tag.hasKey("particleColour"))
        {
            setParticleColour(tag.getInteger("particleColour"));
        }

        if (tag.hasKey("particleType"))
        {
            setParticleType(tag.getInteger("particleType"));
        }

        if (tag.hasKey("customFrameTexture"))
        {
            setCustomFrameTexture(tag.getInteger("customFrameTexture"));
        }

        if (tag.hasKey("customPortalTexture"))
        {
            setCustomPortalTexture(tag.getInteger("customPortalTexture"));
        }

        if (tag.hasKey("resetSlot"))
        {
            int slot = tag.getInteger("resetSlot");

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

        if (tag.hasKey("portalItemID"))
        {
            setPortalItem(tag.getInteger("portalItemID"), tag.getInteger("portalItemMeta"));
        }

        if (tag.hasKey("frameItemID"))
        {
            setFrameItem(tag.getInteger("frameItemID"), tag.getInteger("frameItemMeta"));
        }
        
        if (tag.hasKey("public"))
        {
            isPublic = !isPublic;
            PacketHandlerServer.sendGuiPacketToPlayer(this, player);
        }
    }

    @Override
    public void packetGuiFill(DataOutputStream stream) throws IOException
    {
        stream.writeUTF(getHasIdentifierUnique() ? getIdentifierUnique().getGlyphString() : "");
        stream.writeUTF(getHasIdentifierNetwork() ? getIdentifierNetwork().getGlyphString() : "");
        stream.writeInt(getPortalCount());
        stream.writeInt(getFrameCount());
        stream.writeInt(getRedstoneInterfaceCount());
        stream.writeInt(getNetworkInterfaceCount());
        stream.writeInt(getDiallingDeviceCount());
        stream.writeBoolean(getHasBiometricIdentifier());
        stream.writeInt(getTransferEnergyCount());
        stream.writeInt(getTransferFluidCount());
        stream.writeInt(getTransferItemCount());
        stream.writeInt(getHasIdentifierNetwork() ? CommonProxy.networkManager.getNetworkSize(getIdentifierNetwork()) : -1);
        stream.writeBoolean(isPublic);
    }

    @Override
    public void packetGuiUse(DataInputStream stream) throws IOException
    {
        uID = new GlyphIdentifier(stream.readUTF());
        nID = new GlyphIdentifier(stream.readUTF());
        countPortals = stream.readInt();
        countFrames = stream.readInt();
        countRedstone = stream.readInt();
        countNetwork = stream.readInt();
        countDial = stream.readInt();
        hasBio = stream.readBoolean();
        countEnergy = stream.readInt();
        countFluids = stream.readInt();
        countItems = stream.readInt();
        connectedPortals = stream.readInt();
        isPublic = stream.readBoolean();
    }
    
    /**
     * Creates the portal block. Throws an {@link PortalException} if an error occurs.
     */
    public void portalCreate() throws PortalException
    {
        for (ChunkCoordinates c : portalBlocks) // Check all spots first
        {
            if (!worldObj.isAirBlock(c.posX, c.posY, c.posZ))
            {
                if (CommonProxy.portalsDestroyBlocks)
                {
                    worldObj.destroyBlock(c.posX, c.posY, c.posZ, true);
                }
                else
                {
                    throw new PortalException("failedToCreatePortal");
                }
            }
        }

        for (ChunkCoordinates c : portalBlocks)
        {
            worldObj.setBlock(c.posX, c.posY, c.posZ, BlockPortal.ID, portalType, 2);
            
            TilePortal portal = (TilePortal) WorldUtils.getTileEntity(worldObj, c);
            portal.portalController = getChunkCoordinates();
        }
        
        for (ChunkCoordinates c : getRedstoneInterfaces())
        {
            TileRedstoneInterface ri = (TileRedstoneInterface) WorldUtils.getTileEntity(worldObj, c);
            ri.onPortalCreated();
        }
    }

    public void portalRemove()
    {
        if (processing)
        {
            return;
        }
        
        processing = true;
        
        for (ChunkCoordinates c : portalBlocks)
        {
            worldObj.setBlockToAir(c.posX, c.posY, c.posZ);
        }
        
        for (ChunkCoordinates c : getRedstoneInterfaces())
        {
            TileRedstoneInterface ri = (TileRedstoneInterface) WorldUtils.getTileEntity(worldObj, c);
            ri.onPortalRemoved();
        }
        
        processing = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        portalState = ControlState.values()[tagCompound.getInteger("PortalState")];
        instability = tagCompound.getInteger("Instability");
        portalType = tagCompound.getInteger("PortalType");

        portalFrames = GeneralUtils.loadChunkCoordList(tagCompound, "Frames");
        portalBlocks = GeneralUtils.loadChunkCoordList(tagCompound, "Portals");
        redstoneInterfaces = GeneralUtils.loadChunkCoordList(tagCompound, "RedstoneInterfaces");
        networkInterfaces = GeneralUtils.loadChunkCoordList(tagCompound, "NetworkInterface");
        diallingDevices = GeneralUtils.loadChunkCoordList(tagCompound, "DialDevice");
        transferEnergy = GeneralUtils.loadChunkCoordList(tagCompound, "TransferEnergy");
        transferFluids = GeneralUtils.loadChunkCoordList(tagCompound, "TransferFluid");
        transferItems = GeneralUtils.loadChunkCoordList(tagCompound, "TransferItems");
        biometricIdentifier = GeneralUtils.loadChunkCoord(tagCompound, "BiometricIdentifier");
        moduleManipulator = GeneralUtils.loadChunkCoord(tagCompound, "ModuleManipulator");
        dimensionalBridgeStabilizer = GeneralUtils.loadWorldCoord(tagCompound, "DimensionalBridgeStabilizer");
        temporaryDBS = GeneralUtils.loadWorldCoord(tagCompound, "TemporaryDBS");

        activeTextureData.readFromNBT(tagCompound, "ActiveTextureData");

        if (tagCompound.hasKey("InactiveTextureData"))
        {
            inactiveTextureData = new PortalTextureManager();
            inactiveTextureData.readFromNBT(tagCompound, "InactiveTextureData");
        }
        
        if (tagCompound.hasKey("CachedDestinationUID"))
        {
            cachedDestinationLoc = GeneralUtils.loadWorldCoord(tagCompound, "CachedDestinationLoc");
            cachedDestinationUID = new GlyphIdentifier(tagCompound.getString("CachedDestinationUID"));
        }
    }

    public void removeFrame(ChunkCoordinates chunkCoordinates)
	{
		portalFrames.remove(chunkCoordinates);
	}

    public void revertTextureData()
    {
        if (inactiveTextureData == null)
        {
            return;
        }

        activeTextureData = new PortalTextureManager(inactiveTextureData);
        inactiveTextureData = null;
    }

    /***
     * Sends an update packet for the TilePortalController. Also sends one packet per chunk to notify the client it needs to re-render its portal/frame blocks.
     * 
     * @param updateChunks
     *            Should we send packets to re-render the portal/frame blocks?
     */
    void sendUpdatePacket(boolean updateChunks)
    {
        WorldUtils.markForUpdate(this);

        if (updateChunks)
        {
            ArrayList<ChunkCoordIntPair> chunks = new ArrayList<ChunkCoordIntPair>();
            chunks.add(new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4));

            for (ChunkCoordinates c : getFrames())
            {
                if (!chunks.contains(new ChunkCoordIntPair(c.posX >> 4, c.posZ >> 4)))
                {
                    PacketHandlerServer.sendPacketToAllAround(this, new PacketRerender(c.posX, c.posY, c.posZ).getPacket());
                    chunks.add(new ChunkCoordIntPair(c.posX >> 4, c.posZ >> 4));
                }
            }
        }
    }

    public void setBiometricIdentifier(ChunkCoordinates chunkCoordinates)
	{
		biometricIdentifier = chunkCoordinates;
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

    void setDBS(EntityPlayer player, ItemStack stack) throws PortalException
    {
        WorldCoordinates stabilizer = ItemLocationCard.getDBSLocation(stack);

        if (stabilizer == null || !(stabilizer.getBlockTileEntity() instanceof TileStabilizerMain))
        {
            ItemLocationCard.clearDBSLocation(stack);
            throw new PortalException("voidLinkCard");
        }
        else
        {
            if (!stabilizer.equals(getDimensionalBridgeStabilizer()))
            {
                if (!player.capabilities.isCreativeMode)
                {
                    stack.stackSize--;

                    if (stack.stackSize <= 0)
                    {
                        player.inventory.mainInventory[player.inventory.currentItem] = null;
                    }
                }

                dimensionalBridgeStabilizer = stabilizer;
            }
        }
        
        onInventoryChanged();
    }

    void setFrameColour(int colour)
    {
        activeTextureData.setFrameColour(colour);
        onInventoryChanged();
        sendUpdatePacket(true);
    }

    void setFrameItem(int ID, int Meta)
    {
        ItemStack s = ID == 0 ? null : new ItemStack(ID, 1, Meta);
        activeTextureData.setFrameItem(s);    
        onInventoryChanged();
        sendUpdatePacket(true);
    }

    public void setIdentifierNetwork(GlyphIdentifier id)
    {
        if (!getHasIdentifierUnique())
        {
            return;
        }

        if (isPortalActive())
        {
            connectionTerminate();
        }
        
        GlyphIdentifier uID = getIdentifierUnique();
        
        if (getHasIdentifierNetwork())
        {
            CommonProxy.networkManager.removePortalFromNetwork(uID, getIdentifierNetwork());
        }

        if (id.size() > 0)
        {
            CommonProxy.networkManager.addPortalToNetwork(uID, id);
        }
    }
    
    public void setIdentifierUnique(GlyphIdentifier id) throws PortalException
    {
        if (CommonProxy.networkManager.getPortalLocation(id) != null) // Check to see if we already have a portal with this ID
        {
            if (getHasIdentifierUnique() && getIdentifierUnique().equals(id)) // Make sure  it's  not  already  us
            {
                return;
            }

            throw new PortalException("");
        }

        if (isPortalActive())
        {
            connectionTerminate();
        }
        
        if (getHasIdentifierUnique()) // If already have an identifier
        {
            GlyphIdentifier networkIdentifier = null;

            if (getHasIdentifierNetwork()) // Check to see if it's in a network
            {
                networkIdentifier = getIdentifierNetwork();
                CommonProxy.networkManager.removePortalFromNetwork(getIdentifierUnique(), networkIdentifier); // Remove it if it  is
            }

            CommonProxy.networkManager.removePortal(getWorldCoordinates()); // Remove the old identifier

            if (id.size() > 0) // If the new identifier isn't blank
            {
                CommonProxy.networkManager.addPortal(id, getWorldCoordinates()); // Add it

                if (networkIdentifier != null)
                {
                    CommonProxy.networkManager.addPortalToNetwork(id, networkIdentifier); // Re-add it to the network, if it was in  one
                }
            }
        }
        else if (id.size() > 0) // Otherwise if the new identifier isn't blank
        {
            CommonProxy.networkManager.addPortal(id, getWorldCoordinates()); // Add  the  portal
        }
    }

    /***
     * Sets the instability of the portal to the specified value. Value is only used clientside, to display effects.
     * 
     * @param instabil
     *            Portal instability level.
     */
    public void setInstability(int instabil)
    {
        instability = instabil;
        onInventoryChanged();
        sendUpdatePacket(true);
    }

	public void setModuleManipulator(ChunkCoordinates chunkCoordinates)
	{
		moduleManipulator = chunkCoordinates;
		onInventoryChanged();
	}

	void setParticleColour(int colour)
    {
        activeTextureData.setParticleColour(colour);
        onInventoryChanged();
        sendUpdatePacket(false); // Particles are generated by querying this
    }

	void setParticleType(int type)
    {
        activeTextureData.setParticleType(type);
        onInventoryChanged();
        sendUpdatePacket(false); // Particles are generated by querying this
    }

	void setPortalColour(int colour)
    {
        activeTextureData.setPortalColour(colour);
        onInventoryChanged();
        sendUpdatePacket(true);
    }

	void setPortalItem(int ID, int Meta)
    {
        ItemStack s = ID == 0 ? null : new ItemStack(ID, 1, Meta);
        activeTextureData.setPortalItem(s);    
        onInventoryChanged();
        sendUpdatePacket(true);
    }

	public void swapTextureData(PortalTextureManager textureManager)
    {
        inactiveTextureData = new PortalTextureManager(activeTextureData);
        activeTextureData = textureManager;
        onInventoryChanged();
    }

	@Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("PortalState", portalState.ordinal());
        tagCompound.setInteger("Instability", instability);
        tagCompound.setInteger("PortalType", portalType);

        GeneralUtils.saveChunkCoordList(tagCompound, getFrames(), "Frames");
        GeneralUtils.saveChunkCoordList(tagCompound, getPortals(), "Portals");
        GeneralUtils.saveChunkCoordList(tagCompound, getRedstoneInterfaces(), "RedstoneInterfaces");
        GeneralUtils.saveChunkCoordList(tagCompound, getNetworkInterfaces(), "NetworkInterface");
        GeneralUtils.saveChunkCoordList(tagCompound, getDiallingDevices(), "DialDevice");
        GeneralUtils.saveChunkCoordList(tagCompound, getTransferEnergy(), "TransferEnergy");
        GeneralUtils.saveChunkCoordList(tagCompound, getTransferFluids(), "TransferFluid");
        GeneralUtils.saveChunkCoordList(tagCompound, getTransferItems(), "TransferItems");
        GeneralUtils.saveChunkCoord(tagCompound, biometricIdentifier, "BiometricIdentifier");
        GeneralUtils.saveChunkCoord(tagCompound, moduleManipulator, "ModuleManipulator");
        GeneralUtils.saveWorldCoord(tagCompound, dimensionalBridgeStabilizer, "DimensionalBridgeStabilizer");
        GeneralUtils.saveWorldCoord(tagCompound, temporaryDBS, "TemporaryDBS");

        activeTextureData.writeToNBT(tagCompound, "ActiveTextureData");

        if (inactiveTextureData != null)
        {
            inactiveTextureData.writeToNBT(tagCompound, "InactiveTextureData");
        }
        
        if (cachedDestinationLoc != null)
        {
            GeneralUtils.saveWorldCoord(tagCompound, cachedDestinationLoc, "CachedDestinationLoc");
            tagCompound.setString("CachedDestinationUID", cachedDestinationUID.getGlyphString());
        }
    }

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
            return new Object[] { isPortalActive() };
        }
        else if (method == 1) // getUniqueIdentifier
        {
            GlyphIdentifier identifier = getIdentifierUnique();

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
                setIdentifierUnique(new GlyphIdentifier());
                return callMethod(computer, context, 1, null);
            }
            else if (arguments.length == 1)
            {
                String s = arguments[0].toString();
                s = s.replace(" ", GlyphIdentifier.GLYPH_SEPERATOR);

                String error = ComputerUtils.verifyGlyphArguments(s);
                if(error != null) throw new Exception(error);

                setIdentifierUnique(new GlyphIdentifier(s));
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
    
    public void setupTemporaryDBS(TileStabilizerMain sA)
    {
        temporaryDBS = sA.getWorldCoordinates();
        onInventoryChanged();
    }
    
    public boolean isPublic()
    {
        return isPublic;
    }

    // OpenComputers
    
	@Override
	public String getComponentName() {
		return "ep_controller";
	}
	
	@Callback(direct = true)
	public Object[] isPortalActive(Context context, Arguments args) {
		return new Object[] { isPortalActive() };
	}
	
	@Callback(direct = true)
	public Object[] getUniqueIdentifier(Context context, Arguments args) throws Exception {
        return callMethod(null, null, 1, ComputerUtils.argsToArray(args));
    }
	
	@Callback
	public Object[] setUniqueIdentifier(Context context, Arguments args) throws Exception {
        return callMethod(null, null, 2, ComputerUtils.argsToArray(args));
    }
	
	@Callback(direct = true)
	public Object[] getFrameColour(Context context, Arguments args) throws Exception {
        return callMethod(null, null, 3, ComputerUtils.argsToArray(args));
    }
	
	@Callback
	public Object[] setFrameColour(Context context, Arguments args) throws Exception {
        return callMethod(null, null, 4, ComputerUtils.argsToArray(args));
    }
	
	@Callback(direct = true)
	public Object[] getPortalColour(Context context, Arguments args) throws Exception {
        return callMethod(null, null, 5, ComputerUtils.argsToArray(args));
    }
	
	@Callback
	public Object[] setPortalColour(Context context, Arguments args) throws Exception {
        return callMethod(null, null, 6, ComputerUtils.argsToArray(args));
    }
	
	@Callback(direct = true)
	public Object[] getParticleColour(Context context, Arguments args) throws Exception {
        return callMethod(null, null, 7, ComputerUtils.argsToArray(args));
    }
	
	@Callback
	public Object[] setParticleColour(Context context, Arguments args) throws Exception {
        return callMethod(null, null, 8, ComputerUtils.argsToArray(args));
    }
}
