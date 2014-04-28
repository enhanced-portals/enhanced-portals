package enhancedportals.inventory;

import enhancedportals.EnhancedPortals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.portal.GlyphElement;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.portal.PortalTextureManager;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;

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
            player.openGui(EnhancedPortals.instance, GuiHandler.DIALLING_DEVICE_A,  dial.worldObj, dial.xCoord, dial.yCoord, dial.zCoord);
        }
    }
}
