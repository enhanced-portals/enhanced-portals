package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import enhancedportals.inventory.ContainerTransferItem;
import enhancedportals.tileentity.portal.TileTransferItem;

public class GuiTransferItem extends BaseGui
{
    public static final int CONTAINER_SIZE = 50;
    TileTransferItem itme;

    public GuiTransferItem(TileTransferItem i, EntityPlayer p)
    {
        super(new ContainerTransferItem(i, p.inventory), CONTAINER_SIZE);
    }

}
