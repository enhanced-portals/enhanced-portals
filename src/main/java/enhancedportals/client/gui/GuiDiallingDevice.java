package enhancedportals.client.gui;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.network.packet.PacketRequestGui;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import enhancedportals.EnhancedPortals;
import enhancedportals.inventory.ContainerDiallingDevice;

public class GuiDiallingDevice extends BaseGui
{
    public static final int CONTAINER_SIZE = 175, CONTAINER_WIDTH = 256;
    TileDiallingDevice dial;
    TileController controller;
    GuiButton buttonDial;

    public GuiDiallingDevice(TileDiallingDevice d, EntityPlayer p)
    {
        super(new ContainerDiallingDevice(d, p.inventory), CONTAINER_SIZE);
        xSize = CONTAINER_WIDTH;
        dial = d;
        controller = dial.getPortalController();
        name = "gui.dialDevice";
        setHidePlayerInventory();
    }

    @Override
    public void initGui()
    {
        super.initGui();
        
        buttonDial = new GuiButton(1, guiLeft + xSize - 147, guiTop + ySize - 27, 140, 20, EnhancedPortals.localize("gui." + (controller.isPortalActive() ? "terminate" : "dial")));
        buttonList.add(new GuiButton(0, guiLeft + 7, guiTop + ySize - 27, 100, 20, EnhancedPortals.localize("gui.manualEntry")));
        buttonList.add(buttonDial);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        fontRenderer.drawString(EnhancedPortals.localize("gui.storedIdentifiers"), 7, 18, 0x404040);
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            PacketDispatcher.sendPacketToServer(new PacketRequestGui(dial, GuiHandler.DIALLING_DEVICE_B).getPacket());
        }
        else if (button.id == 1)
        {
            if (controller.isPortalActive())
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setBoolean("terminate", true);
                PacketHandlerClient.sendGuiPacket(tag);
            }
            else
            {
                // TODO: Check if has one selected, and dial it if there is
            }
        }
    }
}
