package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.portal.StackHelper;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TilePortalController extends TilePortalPart
{
    public List<WorldCoordinates> frameBasic, frameRedstone, frameFluid, framePower, portals;
    public WorldCoordinates frameModule, frameDialler, frameNetwork, frameBiometric;
    public int powerCost;
    public boolean hasConfigured;
    public boolean isPortalActive;
    public String uniqueIdentifier, networkIdentifier;
    
    public int frameColour;
    public int portalColour, portalType;
    public int particleColour;
    public int particleType;
    
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
        powerCost = 0;
        hasConfigured = false;
        
        frameColour = portalColour = 0xffffff;
        particleColour = 0xB336A1;
        particleType = portalType = 0;
        
        uniqueIdentifier = networkIdentifier = NetworkManager.BLANK_IDENTIFIER;
        
        inventory = new ItemStack[2];
    }
    
    @Override
    public TilePortalController getPortalController()
    {
        return this;
    }

    @Override
    public int getPowerDrainPerTick()
    {
        return 3;
    }
    
    public void configure(List<WorldCoordinates> tiles)
    {
        if (CommonProxy.isClient() || hasConfigured)
        {
            return;
        }
        
        frameBasic.clear();
        frameRedstone.clear();
        frameFluid.clear();
        framePower.clear();
        portals.clear();
        frameModule = null;
        frameDialler = null;
        frameNetwork = null;
        frameBiometric = null;
        powerCost = getPowerDrainPerTick();
        
        for (WorldCoordinates c : tiles)
        {
            TilePortalPart tile = (TilePortalPart) c.getBlockTileEntity();
            
            powerCost += tile.getPowerDrainPerTick();
            
            // TODO: init frame block and add to correct frame list, init and add portals to portal lists
        }
        
        powerCost *= EnhancedPortals.config.getInteger("powerCostMultiplier");
        hasConfigured = true;
    }
    
    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);
        
        stream.writeInt(powerCost);
        stream.writeBoolean(hasConfigured);
        
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
        
        powerCost = stream.readInt();
        hasConfigured = stream.readBoolean();
        
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
        
        tag.setInteger("powerCost", powerCost);
        tag.setBoolean("hasConfigured", hasConfigured);
        
        tag.setString("uniqueIdentifier", uniqueIdentifier);
        tag.setString("networkIdentifier", networkIdentifier);
        
        tag.setInteger("frameColour", frameColour);
        tag.setInteger("portalColour", portalColour);
        tag.setInteger("particleColour", particleColour);
        tag.setInteger("particleType", particleType);
        tag.setInteger("portalType", portalType);
        tag.setBoolean("isPortalActive", isPortalActive);
        
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
        
        // TODO Save lists
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        
        powerCost = tag.getInteger("powerCost");
        hasConfigured = tag.getBoolean("hasConfigured");
        uniqueIdentifier = tag.getString("uniqueIdentifier");
        networkIdentifier = tag.getString("networkIdentifier");
        
        frameColour = tag.getInteger("frameColour");
        portalColour = tag.getInteger("portalColour");
        particleColour = tag.getInteger("particleColour");
        particleType = tag.getInteger("particleType");
        portalType = tag.getInteger("portalType");
        isPortalActive = tag.getBoolean("isPortalActive");
        
        NBTTagList list = tag.getTagList("inventory");
        for (int i = 0; i < list.tagList.size(); i++)
        {
            inventory[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) list.tagList.get(i));
        }
        
        // TODO Load lists
    }
    
    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        // TODO
    }
    
    @Override
    public boolean activate(EntityPlayer player)
    {
        // TODO init portal
        
        return false;
    }
    
    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        // TODO destroy portal
    }
    
    public void createPortal()
    {
        // TODO
    }
    
    public void removePortal()
    {
        // TODO
    }
    
    public TileModuleManipulator getModuleManipulator()
    {
        return (TileModuleManipulator) worldObj.getBlockTileEntity(frameModule.posX, frameModule.posY, frameModule.posZ);
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
}