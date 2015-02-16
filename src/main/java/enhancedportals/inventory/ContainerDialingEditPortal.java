package enhancedportals.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import enhancedportals.tile.TileDialingDevice;

public class ContainerDialingEditPortal extends ContainerTexturePortal
{
    TileDialingDevice dial;

    public ContainerDialingEditPortal(TileDialingDevice d, InventoryPlayer p)
    {
        super(d.getPortalController(), p);
        dial = d;
    }
}
