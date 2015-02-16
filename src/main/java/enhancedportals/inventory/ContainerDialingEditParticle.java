package enhancedportals.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import enhancedportals.tile.TileDialingDevice;

public class ContainerDialingEditParticle extends ContainerTextureParticle
{
    TileDialingDevice dial;

    public ContainerDialingEditParticle(TileDialingDevice d, InventoryPlayer p)
    {
        super(d.getPortalController(), p);
        dial = d;
    }
}
