package enhancedportals.inventory;

import enhancedportals.tileentity.portal.TileController;
import enhancedportals.tileentity.portal.TileDiallingDevice;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerTextureDialParticle extends ContainerTextureParticle
{
    TileDiallingDevice dial;
    
    public ContainerTextureDialParticle(TileDiallingDevice d, InventoryPlayer p)
    {
        super(d.getPortalController(), p);
        dial = d;
    }
}
