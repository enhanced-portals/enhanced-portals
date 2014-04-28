package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferEnergy;
import enhancedportals.inventory.ContainerTransferEnergy;

public class GuiTransferEnergy extends BaseGui
{
    public static final int CONTAINER_SIZE = 50;
    TileTransferEnergy energy;

    public GuiTransferEnergy(TileTransferEnergy e, EntityPlayer p)
    {
        super(new ContainerTransferEnergy(e, p.inventory), CONTAINER_SIZE);
    }

}
