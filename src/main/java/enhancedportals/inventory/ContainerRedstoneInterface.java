package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiRedstoneInterface;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.tileentity.TileRedstoneInterface;

public class ContainerRedstoneInterface extends BaseContainer
{
    TileRedstoneInterface ri;
    int firstState = -100, secondState = -100;

    public ContainerRedstoneInterface(TileRedstoneInterface i, InventoryPlayer p)
    {
        super(null, p, GuiRedstoneInterface.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        ri = i;
        hideInventorySlots();
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        int id = tag.getInteger("id");

        if (id == 0)
        {
            ri.isOutput = !ri.isOutput;
            ri.setState((byte) 0);
        }
        else
        {
            int s = ri.state + 1;

            if (ri.isOutput)
            {
                if (s >= TileRedstoneInterface.MAX_OUTPUT_STATE)
                {
                    s = 0;
                }
            }
            else if (!ri.isOutput)
            {
                if (s >= TileRedstoneInterface.MAX_INPUT_STATE)
                {
                    s = 0;
                }
            }

            ri.setState((byte) s);
        }

        ri.markDirty();
    }
    
    @Override
    public void updateProgressBar(int id, int val)
    {
        
    }
    
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        int fs = ri.isOutput ? 1 : 0, ss = ri.state;
        
        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting) crafters.get(i);
            
            if (firstState != fs || secondState != ss)
            {
                EnhancedPortals.packetPipeline.sendTo(new PacketGui(ri), (EntityPlayerMP) icrafting);
            }
        }
        
        firstState = fs;
        secondState = ss;
    }
}
