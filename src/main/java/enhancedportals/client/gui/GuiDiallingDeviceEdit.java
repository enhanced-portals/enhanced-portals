package enhancedportals.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.network.PacketDispatcher;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.packet.PacketGuiData;
import uk.co.shadeddimensions.ep3.network.packet.PacketRequestGui;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.portal.PortalTextureManager;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import enhancedportals.EnhancedPortals;
import enhancedportals.inventory.ContainerDiallingDeviceEdit;

public class GuiDiallingDeviceEdit extends GuiDiallingDeviceSave
{
    boolean receivedData = false;

    public GuiDiallingDeviceEdit(TileDiallingDevice d, EntityPlayer p)
    {
        super(new ContainerDiallingDeviceEdit(d, p.inventory), CONTAINER_SIZE);
        dial = d;
        name = "gui.dialDevice";
        setHidePlayerInventory();
        allowUserInput = true;
        Keyboard.enableRepeatEvents(true);
        
        if (ClientProxy.saveTexture == null)
        {
            ClientProxy.saveTexture = new PortalTextureManager();
        }
    }

    @Override
    public void initGui()
    {
        if (ClientProxy.saveName == null)
        {
            ClientProxy.saveName = "";
            ClientProxy.saveGlyph = new GlyphIdentifier();
            ClientProxy.saveTexture = new PortalTextureManager();
        }
        else
        {
            receivedData = true;
        }
        
        super.initGui();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (receivedData)
        {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (receivedData)
        {
            super.keyTyped(par1, par2);
        }
        else
        {
            if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.keyCode)
            {
                this.mc.thePlayer.closeScreen();
            }
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        
        if (!receivedData) // Just in case the users connection is very slow
        {
            drawRect(0, 0, xSize, ySize, 0xCC000000);
            String s = EnhancedPortals.localize("gui.waitingForDataFromServer");
            fontRenderer.drawSplitString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, ySize / 2 - fontRenderer.FONT_HEIGHT / 2, xSize, 0xFF0000);
        }
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0) // cancel
        {
            PacketDispatcher.sendPacketToServer(new PacketRequestGui(dial, GuiHandler.DIALLING_DEVICE_A).getPacket());
        }
        else if (button.id == 1) // save
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("id", ClientProxy.editingID);
            tag.setString("name", text.getText());
            tag.setString("uid", ClientProxy.saveGlyph.getGlyphString());
            ClientProxy.saveTexture.writeToNBT(tag, "texture");
            PacketDispatcher.sendPacketToServer(new PacketGuiData(tag).getPacket());
        }
        else if (button.id == 100)
        {
            isEditing = true;
            PacketDispatcher.sendPacketToServer(new PacketRequestGui(dial, GuiHandler.TEXTURE_DIALLING_A2).getPacket());
        }
        else if (button.id == 101)
        {
            isEditing = true;
            PacketDispatcher.sendPacketToServer(new PacketRequestGui(dial, GuiHandler.TEXTURE_DIALLING_B2).getPacket());
        }
        else if (button.id == 102)
        {
            isEditing = true;
            PacketDispatcher.sendPacketToServer(new PacketRequestGui(dial, GuiHandler.TEXTURE_DIALLING_C2).getPacket());
        }
    }
    
    public void receivedData()
    {
        receivedData = true;
        text.setText(ClientProxy.saveName);
        display.setIdentifier(ClientProxy.saveGlyph);
    }
}
