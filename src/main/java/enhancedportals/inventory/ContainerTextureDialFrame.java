package enhancedportals.inventory;

import enhancedportals.tileentity.portal.TileController;
import enhancedportals.tileentity.portal.TileDiallingDevice;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerTextureDialFrame extends ContainerTextureFrame
{
    TileDiallingDevice dial;
    
    public ContainerTextureDialFrame(TileDiallingDevice d, InventoryPlayer p)
    {
        super(d.getPortalController(), p);
        dial = d;
    }
}
