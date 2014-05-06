package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import enhancedportals.inventory.ContainerTransferEnergy;
import enhancedportals.tileentity.portal.TileTransferEnergy;

public class GuiTransferEnergy extends BaseGui
{
    public static final int CONTAINER_SIZE = 50;
    TileTransferEnergy energy;

    public GuiTransferEnergy(TileTransferEnergy e, EntityPlayer p)
    {
        super(new ContainerTransferEnergy(e, p.inventory), CONTAINER_SIZE);
    }

}
