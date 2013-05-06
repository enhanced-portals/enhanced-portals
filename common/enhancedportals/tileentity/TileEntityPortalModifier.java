package enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Portal;
import enhancedportals.lib.PortalTexture;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;
import enhancedportals.network.packet.PacketTEUpdate;

public class TileEntityPortalModifier extends TileEntityEnhancedPortals
{
    public PortalTexture texture;
    public byte thickness, redstoneSetting;
    public int frequency;
    public boolean particles, sounds, oldRedstoneState;

    public TileEntityPortalModifier()
    {
        texture = new PortalTexture(0);
        thickness = 0;
        redstoneSetting = 0;
        frequency = 0;
        particles = true;
        sounds = true;
        oldRedstoneState = false;
    }
    
    @Override
    public PacketData getPacketData()
    {
        PacketData data = new PacketData();
        data.integerData = new int[] { texture.colour == null ? -1 : texture.colour.ordinal(), texture.blockID, texture.metaData, sounds ? 1 : 0, particles ? 1 : 0, thickness };

        return data;
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
        
        Portal portal = new Portal(xCoord, yCoord, zCoord, worldObj);
        portal.updateTexture(newTexture);
        portal.updateData(sound, particles, portalThickness);
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

    public void handleRedstoneChanges(boolean currentRedstoneState)
    {
        WorldLocation portalLocation = new WorldLocation(xCoord, yCoord, zCoord, worldObj).getOffset(ForgeDirection.getOrientation(getBlockMetadata()));
                
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
                    thick++;
                else
                    thick = 0;
                
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

        return false;
    }

    private boolean isTextureValid(int itemID)
    {
        return new Portal().isTextureValid(itemID);
    }
}
