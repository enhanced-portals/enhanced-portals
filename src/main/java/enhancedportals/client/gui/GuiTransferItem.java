package enhancedportals.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import enhancedportals.EnhancedPortals;
import enhancedportals.inventory.ContainerTransferItem;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.tileentity.portal.TileTransferItem;

public class GuiTransferItem extends BaseGui
{
    public static final int CONTAINER_SIZE = 47;
    TileTransferItem item;

    public GuiTransferItem(TileTransferItem i, EntityPlayer p)
    {
        super(new ContainerTransferItem(i, p.inventory), CONTAINER_SIZE);
        name = "gui.transferItem";
        item = i;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(playerInventoryTexture);
        drawTexturedModalRect(guiLeft + xSize - 18 - 7, guiTop + 22, 7, 7, 18, 18);
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            EnhancedPortals.packetPipeline.sendToServer(new PacketGuiData(new NBTTagCompound()));
        }
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.add(new GuiButton(1, guiLeft + 7, guiTop + 21, 140, 20, EnhancedPortals.localize("gui." + (item.isSending ? "sending" : "receiving"))));
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        ((GuiButton) buttonList.get(0)).displayString = EnhancedPortals.localize("gui." + (item.isSending ? "sending" : "receiving"));
    }
}
