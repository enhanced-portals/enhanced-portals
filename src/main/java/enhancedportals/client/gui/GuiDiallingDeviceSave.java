package enhancedportals.client.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.packet.PacketRequestGui;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementGlyphDisplay;
import enhancedportals.inventory.ContainerDiallingDeviceManual;

public class GuiDiallingDeviceSave extends BaseGui
{
    public static final int CONTAINER_SIZE = 131;
    TileDiallingDevice dial;
    GuiTextField text;

    public GuiDiallingDeviceSave(TileDiallingDevice d, EntityPlayer p)
    {
        super(new ContainerDiallingDeviceManual(d, p.inventory), CONTAINER_SIZE);
        dial = d;
        name = "gui.dialDevice";
        setHidePlayerInventory();
        allowUserInput = true;
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (!text.textboxKeyTyped(par1, par2))
        {
            super.keyTyped(par1, par2);
        }
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        text.updateCursorCounter();
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        
        text = new GuiTextField(fontRenderer, guiLeft + 7, guiTop + 18, 162, 20);
        text.setText(ClientProxy.saveName);
        
        addElement(new ElementGlyphDisplay(this, guiLeft + 7, guiTop + 52, ClientProxy.saveGlyph));
        
        buttonList.add(new GuiButton(0, guiLeft + 7, guiTop + ySize - 27, 80, 20, EnhancedPortals.localize("gui.cancel")));
        buttonList.add(new GuiButton(1, guiLeft + xSize - 87, guiTop + ySize - 27, 80, 20, EnhancedPortals.localize("gui.save")));
        
        buttonList.add(new GuiButton(2, guiLeft + 57, guiTop + 83, 20, 20, ""));
        buttonList.add(new GuiButton(3, guiLeft + (xSize / 2 - 10), guiTop + 83, 20, 20, ""));
        buttonList.add(new GuiButton(4, guiLeft + 99, guiTop + 83, 20, 20, ""));
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        text.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        text.drawTextBox();
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        fontRenderer.drawString(EnhancedPortals.localize("gui.uniqueIdentifier"), 7, 43, 0x404040);
        fontRenderer.drawString(EnhancedPortals.localize("gui.textures"), 7, 73, 0x404040);
    }
    
    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        ClientProxy.saveGlyph = null;
        ClientProxy.saveName = null;
        ClientProxy.saveTexture = null;
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0) // cancel
        {
            PacketDispatcher.sendPacketToServer(new PacketRequestGui(dial, GuiHandler.DIALLING_DEVICE_B).getPacket());
        }
        else if (button.id == 1) // save
        {
            
        }
    }
}
