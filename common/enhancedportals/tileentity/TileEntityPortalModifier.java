package enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Portal;
import enhancedportals.lib.PortalTexture;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;
import enhancedportals.network.packet.PacketRequestSync;
import enhancedportals.network.packet.PacketTEUpdate;

public class TileEntityPortalModifier extends TileEntityEnhancedPortals implements IInventory
{
    public PortalTexture texture;
    public byte thickness, redstoneSetting;
    public int frequency;
    public boolean particles, sounds, oldRedstoneState;
    public ItemStack[] inventory;

    public TileEntityPortalModifier()
    {
        texture = new PortalTexture(0);
        thickness = 0;
        redstoneSetting = 0;
        frequency = 0;
        particles = true;
        sounds = true;
        oldRedstoneState = false;
        inventory = new ItemStack[1];
    }

    @Override
    public PacketData getPacketData()
    {
        PacketData data = new PacketData();
        data.integerData = new int[] { texture.colour == null ? -1 : texture.colour.ordinal(), texture.blockID, texture.metaData, sounds ? 1 : 0, particles ? 1 : 0, thickness };

        return data;
    }

    public boolean handleBlockActivation(EntityPlayer player)
    {
        ItemStack item = player.inventory.mainInventory[player.inventory.currentItem];

        if (item != null)
        {
            if (item.itemID == Item.dyePowder.itemID)
            {
                if (updateTexture(new PortalTexture(PortalTexture.swapColours(item.getItemDamage()))) && !player.capabilities.isCreativeMode)
                {
                    item.stackSize--;
                }

                return true;
            }
            else if (item.itemID == Item.bucketLava.itemID)
            {
                PortalTexture pTexture = new PortalTexture(Block.lavaStill.blockID, 0);
                boolean consumeLiquid = true;

                if (texture.isEqualTo(pTexture))
                {
                    pTexture = new PortalTexture(Block.lavaMoving.blockID, 0);
                    consumeLiquid = false;
                }
                else if (texture.isEqualTo(new PortalTexture(Block.lavaMoving.blockID, 0)))
                {
                    consumeLiquid = false;
                }

                if (updateTexture(pTexture) && !player.capabilities.isCreativeMode && consumeLiquid)
                {
                    item.stackSize--;
                    player.inventory.addItemStackToInventory(new ItemStack(Item.bucketEmpty, 1));
                }

                return true;
            }
            else if (item.itemID == Item.bucketWater.itemID)
            {
                PortalTexture pTexture = new PortalTexture(Block.waterStill.blockID, 0);
                boolean consumeLiquid = true;

                if (texture.isEqualTo(pTexture))
                {
                    pTexture = new PortalTexture(Block.waterMoving.blockID, 0);
                    consumeLiquid = false;
                }
                else if (texture.isEqualTo(new PortalTexture(Block.waterMoving.blockID, 0)))
                {
                    consumeLiquid = false;
                }

                if (updateTexture(pTexture) && !player.capabilities.isCreativeMode && consumeLiquid)
                {
                    item.stackSize--;
                    player.inventory.addItemStackToInventory(new ItemStack(Item.bucketEmpty, 1));
                }

                return true;
            }
            else if (item.itemID == Item.redstone.itemID)
            {
                updateData(!sounds, particles, thickness);
            }
            else if (item.itemID == Item.stick.itemID)
            {
                updateData(sounds, !particles, thickness);
            }
            else if (item.itemID == Item.arrow.itemID)
            {
                byte thick = thickness;

                if (thick < 4)
                {
                    thick++;
                }
                else
                {
                    thick = 0;
                }

                updateData(sounds, particles, thick);
            }
            else if (item.itemID < 4096 && Block.blocksList[item.itemID] != null && item.itemID != BlockIds.NetherPortal)
            {
                Block potentialBlock = Block.blocksList[item.itemID];

                if (new ItemStack(potentialBlock, 1, item.getItemDamage()).isItemEqual(item) && isTextureValid(item.itemID))
                {
                    PortalTexture texture = new PortalTexture(item.itemID, item.getItemDamage());

                    if (updateTexture(texture) && !player.capabilities.isCreativeMode)
                    {
                        item.stackSize--;
                    }

                    return true;
                }
            }
        }
        
        if (!player.isSneaking())
        {
            player.openGui(EnhancedPortals.instance, GuiIds.PortalModifier, worldObj, xCoord, yCoord, zCoord);
            return true;
        }

        return false;
    }

    public void handleRedstoneChanges(boolean currentRedstoneState)
    {
        WorldLocation portalLocation = new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata()));
        // TODO CHANGE TO USE REDSTONE SETTING
        if (!oldRedstoneState && currentRedstoneState)
        {
            new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).createPortal();
        }
        else if (oldRedstoneState && !currentRedstoneState)
        {
            new Portal(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord, worldObj, this).removePortal();
        }

        oldRedstoneState = currentRedstoneState;
    }

    public boolean isActive()
    {
        return new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata())).getBlockId() == BlockIds.NetherPortal;
    }

    private boolean isTextureValid(int itemID)
    {
        return new Portal().isTextureValid(itemID);
    }

    @Override
    public void parsePacketData(PacketData data)
    {
        if (data == null || data.integerData == null || data.integerData.length != 6)
        {
            System.out.println("Unexpected packet recieved. " + data);
            return;
        }

        PortalTexture newTexture;
        boolean sound, particles;
        byte portalThickness;

        if (data.integerData[0] != -1)
        {
            newTexture = new PortalTexture(data.integerData[0]);
        }
        else
        {
            newTexture = new PortalTexture(data.integerData[1], data.integerData[2]);
        }

        sound = data.integerData[3] == 1;
        particles = data.integerData[4] == 1;
        portalThickness = (byte) data.integerData[5];
        updateTexture(newTexture);
        updateData(sound, particles, portalThickness);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        int colour = tagCompound.getInteger("Colour"), blockID = tagCompound.getInteger("BlockID"), metadata = tagCompound.getInteger("Metadata");

        if (colour != -1)
        {
            texture = new PortalTexture(colour);
        }
        else
        {
            texture = new PortalTexture(blockID, metadata);
        }

        thickness = tagCompound.getByte("Thickness");
        redstoneSetting = tagCompound.getByte("RedstoneSetting");
        frequency = tagCompound.getInteger("Frequency");
        particles = tagCompound.getBoolean("Particles");
        sounds = tagCompound.getBoolean("Sounds");
        oldRedstoneState = tagCompound.getBoolean("OldRedstoneState");
    }

    public boolean updateData(boolean sound, boolean part, byte thick)
    {
        if (sound == sounds && part == particles && thick == thickness)
        {
            return false;
        }

        sounds = sound;
        particles = part;
        thickness = thick;
        return true;
    }

    public boolean updateTexture(PortalTexture text)
    {
        if (text.isEqualTo(texture))
        {
            return false;
        }

        texture = text;
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);

        if (!worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 256, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
        }

        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("Colour", texture.colour == null ? -1 : texture.colour.ordinal());
        tagCompound.setInteger("BlockID", texture.blockID);
        tagCompound.setInteger("Metadata", texture.metaData);
        tagCompound.setByte("Thickness", thickness);
        tagCompound.setByte("RedstoneSetting", redstoneSetting);
        tagCompound.setInteger("Frequency", frequency);
        tagCompound.setBoolean("Particles", particles);
        tagCompound.setBoolean("Sounds", sounds);
        tagCompound.setBoolean("OldRedstoneState", oldRedstoneState);
    }
    
    @Override
    public void validate()
    {
        super.validate();
        
        if (worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToServer(new PacketRequestSync(this).getPacket());
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
        ItemStack stack = getStackInSlot(i);
        stack.stackSize -= j;
        
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return inventory[i];
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if (isStackValidForSlot(i, itemstack))
        {
            inventory[i] = itemstack;
        }
    }

    @Override
    public String getInvName()
    {
        return Localization.PortalModifier_Name;
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
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void openChest()
    {
        
    }

    @Override
    public void closeChest()
    {
        
    }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack stack)
    {
        if (stack == null)
        {
            return true;
        }
        
        return EnhancedPortals.instance.isValidItemForPortalTexture(stack) && !PortalTexture.getTextureFromItemStack(stack).isEqualTo(texture);
    }
    
    @Override
    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        
        if (getStackInSlot(0) != null)
        {
            ItemStack stack = getStackInSlot(0);
            
            if (EnhancedPortals.instance.isValidItemForPortalTexture(stack))
            {
                PortalTexture newTexture = PortalTexture.getTextureFromItemStack(stack);
                boolean isLiquid = false;
                
                if (newTexture.isEqualTo(new PortalTexture(Block.lavaStill.blockID)))
                {
                    if (newTexture.isEqualTo(this.texture))
                    {
                        newTexture = new PortalTexture(Block.lavaMoving.blockID, 0);
                    }
                    
                    isLiquid = true;
                }
                else if (newTexture.isEqualTo(new PortalTexture(Block.waterStill.blockID)))
                {
                    if (newTexture.isEqualTo(this.texture))
                    {
                        newTexture = new PortalTexture(Block.waterMoving.blockID, 0);
                    }
                    
                    isLiquid = true;
                }
                
                texture = newTexture;
                worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
                setInventorySlotContents(0, null);
                                
                if (isLiquid && !worldObj.isRemote)
                {
                    EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, new ItemStack(Item.bucketEmpty, 1));
                    worldObj.spawnEntityInWorld(entityItem);
                }
            }
        }
    }
}
