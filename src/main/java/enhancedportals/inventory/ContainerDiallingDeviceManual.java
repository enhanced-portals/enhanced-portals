package enhancedportals.inventory;

import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice.GlyphElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerDiallingDeviceManual extends BaseContainer
{
    TileDiallingDevice dial;

    public ContainerDiallingDeviceManual(TileDiallingDevice d, InventoryPlayer p)
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
