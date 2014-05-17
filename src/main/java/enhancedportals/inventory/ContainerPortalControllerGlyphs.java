package enhancedportals.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.FMLCommonHandler;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiPortalControllerGlyphs;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.portal.GlyphIdentifier;
import enhancedportals.portal.PortalException;
import enhancedportals.tileentity.TileController;

public class ContainerPortalControllerGlyphs extends BaseContainer
{
    TileController controller;

    public ContainerPortalControllerGlyphs(TileController c, InventoryPlayer p)
    {
        super(null, p, GuiPortalControllerGlyphs.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        controller = c;
        hideInventorySlots();
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("uid"))
        {
            try
            {
                controller.setIdentifierUnique(new GlyphIdentifier(tag.getString("uid")));
                player.openGui(EnhancedPortals.instance, GuiHandler.PORTAL_CONTROLLER_A, controller.getWorldObj(), controller.xCoord, controller.yCoord, controller.zCoord);
            }
            catch (PortalException e)
            {
                NBTTagCompound errorTag = new NBTTagCompound();
                errorTag.setInteger("error", 0);
                EnhancedPortals.packetPipeline.sendTo(new PacketGuiData(errorTag), (EntityPlayerMP) player);
            }
        }
        else if (tag.hasKey("error") && FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            ((GuiPortalControllerGlyphs) Minecraft.getMinecraft().currentScreen).setWarningMessage();
        }
    }
}
