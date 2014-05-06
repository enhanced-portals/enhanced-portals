package enhancedportals.inventory;

import enhancedportals.tileentity.portal.TileController;
import enhancedportals.tileentity.portal.TileDiallingDevice;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerTextureDialPortal extends ContainerTexturePortal
{
    TileDiallingDevice dial;
    
    public ContainerTextureDialPortal(TileDiallingDevice d, InventoryPlayer p)
    {
        super(d.getPortalController(), p);
        dial = d;
    }
}
