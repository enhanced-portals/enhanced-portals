package uk.co.shadeddimensions.enhancedportals.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.WorldServer;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.util.NBTHelper;
import uk.co.shadeddimensions.enhancedportals.util.PortalTexture;
import uk.co.shadeddimensions.enhancedportals.util.PortalUtils;
import uk.co.shadeddimensions.enhancedportals.util.Texture;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TilePortalFrameController extends TilePortalFrame
{
    public Texture frameTexture;
    public PortalTexture portalTexture;
    
    public List<ChunkCoordinates> portalFrame;
    public List<ChunkCoordinates> portalFrameRedstone;
    public List<ChunkCoordinates> portalBlocks;
        
    @SideOnly(Side.CLIENT)
    public int attachedFrames;
    @SideOnly(Side.CLIENT)
    public int attachedFrameRedstone;
    @SideOnly(Side.CLIENT)
    public int attachedPortals;
    
    public TilePortalFrameController()
    {
        frameTexture = new Texture();
        portalTexture = new PortalTexture();
        
        portalFrame = new ArrayList<ChunkCoordinates>();
        portalFrameRedstone = new ArrayList<ChunkCoordinates>();
        portalBlocks = new ArrayList<ChunkCoordinates>();
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
        
        frameTexture.writeToNBT(tagCompound);
        portalTexture.writeToNBT(tagCompound);
        
        NBTHelper.saveCCList(tagCompound, portalFrame, "portalFrame");
        NBTHelper.saveCCList(tagCompound, portalFrameRedstone, "portalFrameRedstone");
        NBTHelper.saveCCList(tagCompound, portalBlocks, "portalBlocks");
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        
        frameTexture = new Texture(tagCompound);
        portalTexture = new PortalTexture(tagCompound);
        
        portalFrame = NBTHelper.loadCCList(tagCompound, "portalFrame");
        portalFrameRedstone = NBTHelper.loadCCList(tagCompound, "portalFrameRedstone");
        portalBlocks = NBTHelper.loadCCList(tagCompound, "portalBlocks");
    }
    
    @Override
    public Icon getTexture(int side, int renderpass)
    {
        if (renderpass == 1 && ClientProxy.isWearingGoggles)
        {
            return BlockFrame.controllerOverlay;
        }

        return null;
    }
    
    @Override
    public boolean activate(EntityPlayer player)
    {
        if (player.inventory.getCurrentItem() != null)
        {        
            if (worldObj.isRemote)
            {
                return super.activate(player);
            }
            
            byte status = PortalUtils.performControllerLink((WorldServer) worldObj, xCoord, yCoord, zCoord);
            
            if (status == 0)
            {                
                player.sendChatToPlayer(ChatMessageComponent.func_111066_d(EnumChatFormatting.GREEN + "Success: " + EnumChatFormatting.WHITE + String.format("Successfully linked %s frame and %s portal blocks", getAttachedFrames(), getAttachedPortals())));
                return true;
            }
            else if (status == 1)
            {
                player.sendChatToPlayer(ChatMessageComponent.func_111066_d(EnumChatFormatting.RED + "Error: " + EnumChatFormatting.WHITE + "An unknown error occurred"));
            }
            else if (status == 3)
            {
                player.sendChatToPlayer(ChatMessageComponent.func_111066_d(EnumChatFormatting.RED + "Error: " + EnumChatFormatting.WHITE + "Another controller was found"));
            }
        }
        else
        {
            player.openGui(EnhancedPortals.instance, Identifiers.Gui.FRAME_CONTROLLER, worldObj, xCoord, yCoord, zCoord);
        }
        
        return false;
    }
    
    public boolean createPortal()
    {
        System.out.println("Creating portal from controller!");
        return false;
    }
    
    public void removePortal()
    {
        System.out.println("Removing portal from controller!");
    }
}
