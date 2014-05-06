package enhancedportals.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import enhancedportals.tileentity.portal.TileDiallingDevice;

public class ContainerTextureDialPortal extends ContainerTexturePortal
{
    TileDiallingDevice dial;

    public ContainerTextureDialPortal(TileDiallingDevice d, InventoryPlayer p)
    {
        super(d.getPortalController(), p);
        dial = d;
    }
}
