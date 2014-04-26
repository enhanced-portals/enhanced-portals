package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.portal.GlyphElement;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;

public class ContainerDiallingDevice extends BaseContainer
{
    TileDiallingDevice dial;

    public ContainerDiallingDevice(TileDiallingDevice d, InventoryPlayer p)
    {
        super(null, p);
        dial = d;
        hideInventorySlots();
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("terminate"))
        {
            dial.getPortalController().connectionTerminate();
        }
        else if (tag.hasKey("dial"))
        {
            int id = tag.getInteger("dial");
            GlyphElement e = dial.glyphList.get(id);
            dial.getPortalController().connectionDial(e.identifier, e.texture, player);
        }
    }
}
