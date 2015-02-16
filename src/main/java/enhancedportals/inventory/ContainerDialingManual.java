package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.portal.GlyphIdentifier;
import enhancedportals.tile.TileDialingDevice;

public class ContainerDialingManual extends BaseContainer
{
    TileDialingDevice dial;

    public ContainerDialingManual(TileDialingDevice d, InventoryPlayer p)
    {
        super(null, p);
        dial = d;
        hideInventorySlots();
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("dial"))
        {
            dial.getPortalController().connectionDial(new GlyphIdentifier(tag.getString("dial")), null, player);
        }
    }
}
