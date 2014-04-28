package enhancedportals.client.gui;

import java.awt.Color;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.packet.PacketRequestGui;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;

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

        buttonList.add(new GuiButton(1000, guiLeft + 7, guiTop + ySize + 7, xSize - 14, 20, "Save"));

        Color c = new Color(ClientProxy.saveTexture.getPortalColour());
        sliderR.sliderValue = c.getRed() / 255f;
        sliderG.sliderValue = c.getGreen() / 255f;
        sliderB.sliderValue = c.getBlue() / 255f;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == buttonSave.id)
        {
            ClientProxy.saveTexture.setPortalColour(Integer.parseInt(String.format("%02x%02x%02x", sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), 16));
        }
        else if (button.id == buttonReset.id)
        {
            int colour = 0xffffff;
            ClientProxy.saveTexture.setPortalColour(colour);

            Color c = new Color(colour);
            sliderR.sliderValue = c.getRed() / 255f;
            sliderG.sliderValue = c.getGreen() / 255f;
            sliderB.sliderValue = c.getBlue() / 255f;
        }
        else if (button.id == 1000)
        {
            didSave = true;
            PacketDispatcher.sendPacketToServer(new PacketRequestGui(dial, returnToEdit ? GuiHandler.DIALLING_DEVICE_D : GuiHandler.DIALLING_DEVICE_C).getPacket());
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
}
