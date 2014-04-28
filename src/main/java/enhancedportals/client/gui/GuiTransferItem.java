package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferItem;
import enhancedportals.inventory.ContainerTransferItem;

public class GuiTransferItem extends BaseGui
{
    public static final int CONTAINER_SIZE = 50;
    TileTransferItem itme;

    public GuiTransferItem(TileTransferItem i, EntityPlayer p)
    {
        super(new ContainerTransferItem(i, p.inventory), CONTAINER_SIZE);
    }

}
