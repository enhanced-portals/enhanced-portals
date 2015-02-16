package enhancedportals.client.gui;

import java.util.Random;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementGlyphSelector;
import enhancedportals.client.gui.elements.ElementGlyphViewer;
import enhancedportals.client.gui.tabs.TabTip;
import enhancedportals.inventory.ContainerDialingEditIdentifier;
import enhancedportals.network.ClientProxy;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.packet.PacketRequestGui;
import enhancedportals.portal.GlyphIdentifier;
import enhancedportals.tile.TileDialingDevice;

public class GuiDialingEditIdentifier extends BaseGui {
	public static final int CONTAINER_SIZE = 135;
    TileDialingDevice dial;
    GuiButton buttonCancel, buttonSave;	
    ElementGlyphSelector selector;
    
    public GuiDialingEditIdentifier(TileDialingDevice d, EntityPlayer p)
    {
        super(new ContainerDialingEditIdentifier(d, p.inventory), CONTAINER_SIZE);
        dial = d;
        name = "gui.dialDevice";
        setHidePlayerInventory(	);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int buttonWidth = 80;
        buttonCancel = new GuiButton(0, guiLeft + 7, guiTop + containerSize - 27, buttonWidth, 20, EnhancedPortals.localize("gui.cancel"));
        buttonSave = new GuiButton(1, guiLeft + xSize - buttonWidth - 7, guiTop + containerSize - 27, buttonWidth, 20, EnhancedPortals.localize("gui.save"));
        buttonList.add(buttonCancel);
        buttonList.add(buttonSave);
        addTab(new TabTip(this, "glyphs"));
        selector = new ElementGlyphSelector(this, 7, 52);
        selector.setIdentifierTo(ClientProxy.saveGlyph);
        addElement(selector);
        addElement(new ElementGlyphViewer(this, 7, 29, selector));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (isShiftKeyDown())
        {
            if (button.id == buttonCancel.id) // Clear
            {
                selector.reset();
            }
            else if (button.id == buttonSave.id) // Random
            {
                Random random = new Random();
                GlyphIdentifier iden = new GlyphIdentifier();

                for (int i = 0; i < (isCtrlKeyDown() ? 9 : random.nextInt(8) + 1); i++)
                {
                    iden.addGlyph(random.nextInt(27));
                }

                selector.setIdentifierTo(iden);
            }
        }
        else
        {
            if (button.id == buttonCancel.id) // Cancel
            {
            	EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(dial, GuiHandler.DIALING_DEVICE_D));
            }
            else if (button.id == buttonSave.id) // Save Changes
            {
            	ClientProxy.saveGlyph = selector.getGlyphIdentifier();
            	EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(dial, GuiHandler.DIALING_DEVICE_D));
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        getFontRenderer().drawString(EnhancedPortals.localize("gui.uniqueIdentifier"), 7, 19, 0x404040);
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        
        if (isShiftKeyDown())
        {
            buttonCancel.displayString = EnumChatFormatting.AQUA + EnhancedPortals.localize("gui.clear");
            buttonSave.displayString = (isCtrlKeyDown() ? EnumChatFormatting.GOLD : EnumChatFormatting.AQUA) + EnhancedPortals.localize("gui.random");
        }
        else
        {
            buttonCancel.displayString = EnhancedPortals.localize("gui.cancel");
            buttonSave.displayString = EnhancedPortals.localize("gui.save");
        }
    }
}
