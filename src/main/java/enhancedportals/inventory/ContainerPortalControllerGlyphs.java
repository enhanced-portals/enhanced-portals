package enhancedportals.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.packet.PacketGuiData;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.portal.PortalException;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiPortalControllerGlyphs;

public class ContainerPortalControllerGlyphs extends BaseContainer
{
    TileController controller;

    public ContainerPortalControllerGlyphs(TileController c, InventoryPlayer p)
    {
        super(null, p, GuiPortalControllerGlyphs.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        controller = c;
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("uid"))
        {
            try
            {
                controller.setIdentifierUnique(new GlyphIdentifier(tag.getString("uid")));
                player.openGui(EnhancedPortals.instance, GuiHandler.PORTAL_CONTROLLER_A, controller.worldObj, controller.xCoord, controller.yCoord, controller.zCoord);
            }
            catch (PortalException e)
            {
                NBTTagCompound errorTag = new NBTTagCompound();
                errorTag.setInteger("error", 0);
                PacketDispatcher.sendPacketToPlayer(new PacketGuiData(errorTag).getPacket(), (Player) player);
            }
        }
        else if (tag.hasKey("error") && FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            ((GuiPortalControllerGlyphs) Minecraft.getMinecraft().currentScreen).setWarningMessage();
        }
    }
}
