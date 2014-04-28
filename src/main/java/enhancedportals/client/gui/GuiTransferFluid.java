package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferFluid;
import enhancedportals.inventory.ContainerTransferFluid;

public class GuiTransferFluid extends BaseGui
{
    public static final int CONTAINER_SIZE = 50;
    TileTransferFluid fluid;

    public GuiTransferFluid(TileTransferFluid f, EntityPlayer p)
    {
        super(new ContainerTransferFluid(f, p.inventory), CONTAINER_SIZE);
    }

}
