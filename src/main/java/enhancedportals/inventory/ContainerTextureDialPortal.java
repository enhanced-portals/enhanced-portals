package enhancedportals.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;

public class ContainerTextureDialPortal extends ContainerTexturePortal
{
    TileDiallingDevice dial;
    
    public ContainerTextureDialPortal(TileDiallingDevice d, InventoryPlayer p)
    {
        super(d.getPortalController(), p);
        dial = d;
    }
}
