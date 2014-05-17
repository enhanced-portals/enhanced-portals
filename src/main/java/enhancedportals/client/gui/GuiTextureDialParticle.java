package enhancedportals.client.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.ClientProxy;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.packet.PacketRequestGui;
import enhancedportals.portal.PortalTextureManager;
import enhancedportals.tileentity.TileDiallingDevice;

public class GuiTextureDialParticle extends GuiTextureParticle
{
    TileDiallingDevice dial;
    boolean didSave, returnToEdit;
    
    public GuiTextureDialParticle(TileDiallingDevice d, EntityPlayer p)
    {
        this(d, p, false);
    }
    
    public GuiTextureDialParticle(TileDiallingDevice d, EntityPlayer p, boolean r)
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

        Color c = new Color(getPTM().getParticleColour());
        sliderR.sliderValue = c.getRed() / 255f;
        sliderG.sliderValue = c.getGreen() / 255f;
        sliderB.sliderValue = c.getBlue() / 255f;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == buttonSave.id)
        {
            getPTM().setParticleColour(Integer.parseInt(String.format("%02x%02x%02x", sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), 16));
        }
        else if (button.id == buttonReset.id)
        {
            int colour = 0xffffff;
            getPTM().setParticleColour(colour);

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
            EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(dial, returnToEdit ? GuiHandler.TEXTURE_DIALLING_EDIT_A : GuiHandler.TEXTURE_DIALLING_SAVE_A));
        }
        else if (button.id == 501)
        {
            didSave = true;
            EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(dial, returnToEdit ? GuiHandler.TEXTURE_DIALLING_EDIT_B : GuiHandler.TEXTURE_DIALLING_SAVE_B));
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
    public void particleSelected(int particle)
    {
        getPTM().setParticleType(particle);
    }
    
    @Override
    public PortalTextureManager getPTM()
    {
        return ClientProxy.saveTexture;
    }
}
