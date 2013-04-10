package alz.mods.enhancedportals.tileentity;

import java.util.HashMap;
import java.util.Map;

import alz.mods.enhancedportals.networking.ITileEntityNetworking;
import alz.mods.enhancedportals.networking.PacketTileUpdate;
import alz.mods.enhancedportals.portals.PortalTexture;
import alz.mods.enhancedportals.portals.PortalUpgrade;
import alz.mods.enhancedportals.teleportation.TeleportData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPortalModifier extends TileEntity implements ITileEntityNetworking, IInventory
{
    private PortalTexture Texture;
    private String Network;
    private TeleportData TeleportData;
    public boolean HadPower;
    public byte PowerState;
    private ItemStack[] tempInventory;    
    private Map<PortalUpgrade, Boolean> upgrades;
    
    public TileEntityPortalModifier()
    {
        Texture = PortalTexture.PURPLE;
        TeleportData = null;
        Network = "undefined";
        tempInventory = new ItemStack[2];
        
        upgrades = new HashMap<PortalUpgrade, Boolean>();
        
        for (PortalUpgrade upgrade : PortalUpgrade.values())
        {
            upgrades.put(upgrade, false);
        }
    }
    
    public void setTexture(PortalTexture texture)
    {
        Texture = texture;
        
        // TODO Computercraft event?
    }
    
    public PortalTexture getTexture()
    {
        return Texture;
    }
    
    public void setNetwork(String ID)
    {
        Network = ID;
        
        // TODO Computercraft event?
    }
    
    public String getNetwork()
    {
        return Network;
    }
    
    public void setTeleportData(TeleportData data)
    {
        TeleportData = data;
        
        // TODO Computercraft event?
    }
    
    public TeleportData getTeleportData()
    {
        return TeleportData;
    }
    
    public boolean hasUpgrade(PortalUpgrade upgrade)
    {
        return upgrades.get(upgrade);
    }
    
    public boolean hasUpgrade(int upgradeID)
    {
        return hasUpgrade(PortalUpgrade.getPortalUpgrade(upgradeID));
    }
    
    public boolean addUpgrade(PortalUpgrade upgrade)
    {
        if (hasUpgrade(upgrade))
        {
            return false;
        }
        
        upgrades.remove(upgrade);
        upgrades.put(upgrade, true);
        return true;
    }
    
    public boolean removeUpgrade(PortalUpgrade upgrade)
    {
        if (!hasUpgrade(upgrade))
        {
            return false;
        }
        
        upgrades.remove(upgrade);
        upgrades.put(upgrade, false);
        return true;
    }

    @Override
    public PacketTileUpdate getUpdatePacket()
    {
        PacketTileUpdate packet = new PacketTileUpdate();
        packet.xCoord = xCoord;
        packet.yCoord = yCoord;
        packet.zCoord = zCoord;
        packet.data = new int[] { Texture.ordinal() };
        
        return packet;
    }

    @Override
    public void parseUpdatePacket(PacketTileUpdate packet)
    {
        Texture = PortalTexture.getPortalTexture(packet.data[0]);
    }

    @Override
    public int getSizeInventory()
    {
        return tempInventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return tempInventory[i];
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
        return tempInventory[i];
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        tempInventory[i] = itemstack;
    }

    @Override
    public String getInvName()
    {
        return "tile.portalModifier.name";
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
    { }

    @Override
    public void closeChest()
    { }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }
}
