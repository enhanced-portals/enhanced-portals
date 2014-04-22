package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiNetworkInterface;

public class ContainerNetworkInterface extends BaseContainer
{
    TileController controller;

    public ContainerNetworkInterface(TileController c, InventoryPlayer p)
    {
        super(null, p, GuiNetworkInterface.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        controller = c;
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {

    }
}
