package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.packet.PacketTextureData;
import enhancedportals.portal.GlyphElement;
import enhancedportals.tileentity.portal.TileDiallingDevice;

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

            if (dial.glyphList.size() > id)
            {
                GlyphElement e = dial.glyphList.get(id);
                dial.getPortalController().connectionDial(e.identifier, e.texture, player);
            }
        }
        else if (tag.hasKey("edit"))
        {
            int id = tag.getInteger("edit");

            if (dial.glyphList.size() > id)
            {
                GlyphElement e = dial.glyphList.get(id);
                player.openGui(EnhancedPortals.instance, GuiHandler.DIALLING_DEVICE_D, dial.getWorldObj(), dial.xCoord, dial.yCoord, dial.zCoord);
                EnhancedPortals.packetPipeline.sendTo(new PacketTextureData(e.name, e.identifier.getGlyphString(), e.texture), (EntityPlayerMP) player);
            }
        }
        else if (tag.hasKey("delete"))
        {
            int id = tag.getInteger("delete");

            if (dial.glyphList.size() > id)
            {
                dial.glyphList.remove(id);
            }

            // PacketHandlerServer.sendGuiPacketToPlayer(dial, player); // TODO
        }
    }
}
