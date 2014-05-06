package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiRedstoneInterface;
import enhancedportals.tileentity.portal.TileRedstoneInterface;

public class ContainerRedstoneInterface extends BaseContainer
{
    TileRedstoneInterface ri;

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
                if (s >= ri.MAX_OUTPUT_STATE)
                {
                    s = 0;
                }
            }
            else if (!ri.isOutput)
            {
                if (s >= ri.MAX_INPUT_STATE)
                {
                    s = 0;
                }
            }
            
            ri.setState((byte) s);
        }

        ri.markDirty();
        //PacketHandlerServer.sendGuiPacketToPlayer(ri, player);  // TODO
    }
}
