package enhancedportals.inventory;

import enhancedportals.EnhancedPortals;
import enhancedportals.network.GuiHandler;
import enhancedportals.portal.GlyphElement;
import enhancedportals.portal.GlyphIdentifier;
import enhancedportals.portal.PortalTextureManager;
import enhancedportals.tileentity.portal.TileDiallingDevice;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerDiallingDeviceEdit extends ContainerDiallingDeviceSave
{
    public ContainerDiallingDeviceEdit(TileDiallingDevice d, InventoryPlayer p)
    {
        super(d, p);
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("id") && tag.hasKey("uid") && tag.hasKey("texture") && tag.hasKey("name"))
        {
            PortalTextureManager ptm = new PortalTextureManager();
            ptm.readFromNBT(tag, "texture");
            dial.glyphList.set(tag.getInteger("id"), new GlyphElement(tag.getString("name"), new GlyphIdentifier(tag.getString("uid")), ptm));
            player.openGui(EnhancedPortals.instance, GuiHandler.DIALLING_DEVICE_A,  dial.getWorldObj(), dial.xCoord, dial.yCoord, dial.zCoord);
        }
    }
}
