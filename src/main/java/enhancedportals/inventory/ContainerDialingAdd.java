package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.GuiHandler;
import enhancedportals.portal.GlyphElement;
import enhancedportals.portal.GlyphIdentifier;
import enhancedportals.portal.PortalTextureManager;
import enhancedportals.tile.TileDialingDevice;

public class ContainerDialingAdd extends BaseContainer
{
    TileDialingDevice dial;

    public ContainerDialingAdd(TileDialingDevice d, InventoryPlayer p)
    {
        super(null, p);
        dial = d;
        hideInventorySlots();
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("uid") && tag.hasKey("texture") && tag.hasKey("name"))
        {
            PortalTextureManager ptm = new PortalTextureManager();
            ptm.readFromNBT(tag, "texture");
            dial.glyphList.add(new GlyphElement(tag.getString("name"), new GlyphIdentifier(tag.getString("uid")), ptm));
            player.openGui(EnhancedPortals.instance, GuiHandler.DIALING_DEVICE_A, dial.getWorldObj(), dial.xCoord, dial.yCoord, dial.zCoord);
        }
    }
}
