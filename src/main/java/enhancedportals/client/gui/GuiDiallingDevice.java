package enhancedportals.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.network.packet.PacketRequestGui;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementScrollDiallingDevice;
import enhancedportals.client.gui.tabs.TabTip;
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
        texture = new ResourceLocation("enhancedportals", "textures/gui/diallingDevice.png");
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
        
        buttonDial = new GuiButton(1, guiLeft + xSize - 147, guiTop + ySize - 27, 140, 20, EnhancedPortals.localize("gui.terminate"));
        buttonDial.enabled = controller.isPortalActive();
        buttonList.add(new GuiButton(0, guiLeft + 7, guiTop + ySize - 27, 100, 20, EnhancedPortals.localize("gui.manualEntry")));
        buttonList.add(buttonDial);
        
        addElement(new ElementScrollDiallingDevice(this, dial, 7, 28));
        addTab(new TabTip(this, "dialling"));
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        buttonDial.enabled = controller.isPortalActive();
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
        }
    }

    public void onEntrySelected(int entry)
    {
        if (!controller.isPortalActive())
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("dial", entry);
            PacketHandlerClient.sendGuiPacket(tag);
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
    }
    
    public void onEntryEdited(int entry)
    {
        ClientProxy.editingID = entry;
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("edit", entry);
        PacketHandlerClient.sendGuiPacket(tag);
    }
    
    public void onEntryDeleted(int entry)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("delete", entry);
        PacketHandlerClient.sendGuiPacket(tag);
    }
}
