package enhancedportals.client.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.ClientProxy;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.packet.PacketRequestGui;
import enhancedportals.portal.PortalTextureManager;
import enhancedportals.tileentity.TileDiallingDevice;

public class GuiTextureDialPortal extends GuiTexturePortal
{
    TileDiallingDevice dial;
    boolean didSave, returnToEdit;
    
    public GuiTextureDialPortal(TileDiallingDevice d, EntityPlayer p)
    {
        this(d, p, false);
    }
    
    public GuiTextureDialPortal(TileDiallingDevice d, EntityPlayer p, boolean r)
    {
        super(d.getPortalController(), p);
        dial = d;
        returnToEdit = r;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.add(new GuiButton(1000, guiLeft + 7, guiTop + ySize + 3, xSize - 14, 20, "Save"));

        Color c = new Color(getPTM().getPortalColour());
        sliderR.sliderValue = c.getRed() / 255f;
        sliderG.sliderValue = c.getGreen() / 255f;
        sliderB.sliderValue = c.getBlue() / 255f;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == buttonSave.id)
        {
            getPTM().setPortalColour(Integer.parseInt(String.format("%02x%02x%02x", sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), 16));
        }
        else if (button.id == buttonReset.id)
        {
            int colour = 0xffffff;
            getPTM().setPortalColour(colour);

            Color c = new Color(colour);
            sliderR.sliderValue = c.getRed() / 255f;
            sliderG.sliderValue = c.getGreen() / 255f;
            sliderB.sliderValue = c.getBlue() / 255f;
        }
        else if (button.id == 1000)
        {
            didSave = true;
            EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(dial, returnToEdit ? GuiHandler.DIALLING_DEVICE_D : GuiHandler.DIALLING_DEVICE_C));
        }
        else if (button.id == 500)
        {
            didSave = true;
            EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(dial, returnToEdit? GuiHandler.TEXTURE_DIALLING_EDIT_A  : GuiHandler.TEXTURE_DIALLING_SAVE_A));
        }
        else if (button.id == 501)
        {
            didSave = true;
            EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(dial, returnToEdit ? GuiHandler.TEXTURE_DIALLING_EDIT_C : GuiHandler.TEXTURE_DIALLING_SAVE_C));
        }
    }
    
    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        
        if (!didSave)
        {
            ClientProxy.saveGlyph = null;
            ClientProxy.saveName = null;
            ClientProxy.saveTexture = null;
        }
    }
    
    @Override
    public void iconSelected(int icon)
    {
        getPTM().setCustomPortalTexture(icon);
    }
    
    @Override
    public void onItemChanged(ItemStack newItem)
    {
        getPTM().setPortalItem(newItem);
    }
    
    @Override
    public PortalTextureManager getPTM()
    {
        return ClientProxy.saveTexture;
    }
}
