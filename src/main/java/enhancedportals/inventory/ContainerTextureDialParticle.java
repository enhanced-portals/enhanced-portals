package enhancedportals.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import enhancedportals.tileentity.TileDiallingDevice;

public class ContainerTextureDialParticle extends ContainerTextureParticle
{
    TileDiallingDevice dial;

    public ContainerTextureDialParticle(TileDiallingDevice d, InventoryPlayer p)
    {
        super(d.getPortalController(), p);
        dial = d;
    }
}
