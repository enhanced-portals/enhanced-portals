package uk.co.shadeddimensions.ep3.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.container.ContainerTransferEnergy;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferEnergy;
import uk.co.shadeddimensions.library.gui.GuiBaseContainer;
import uk.co.shadeddimensions.library.gui.element.ElementRedstoneFlux;

public class GuiTransferEnergy extends GuiBaseContainer
{
    TileTransferEnergy energy;
    
    public GuiTransferEnergy(TileTransferEnergy energy)
    {
        super(new ContainerTransferEnergy(energy), new ResourceLocation("enhancedportals", "textures/gui/transfer.png"));
        this.energy = energy;
        ySize = 52;
        name = BlockFrame.instance.getUnlocalizedName() + ".energy.name";
        drawInventory = false;
        drawName = false;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        getFontRenderer().drawStringWithShadow(StatCollector.translateToLocal(name), xSize / 2 - getFontRenderer().getStringWidth(StatCollector.translateToLocal(name)) / 2, -15, 0xFFFFFF);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
    
    @Override
    public void drawBackgroundTexture()
    {
        if (texture != null)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            getMinecraft().renderEngine.bindTexture(texture);
            drawTexturedModalRect(guiLeft, guiTop, 0, 72, xSize, ySize);
        }
    }
    
    @Override
    public void addElements()
    {
        addElement(new ElementRedstoneFlux(this, 5, 5, energy.storage));
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("state", true);
        PacketHandlerClient.sendGuiPacket(tag);
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.add(new GuiButton(0, guiLeft + 23, guiTop + ySize / 2 - 10, xSize - 28, 20, energy.isSending ? Localization.getGuiString("sending") : Localization.getGuiString("receiving")));
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        ((GuiButton) buttonList.get(0)).displayString = energy.isSending ? Localization.getGuiString("sending") : Localization.getGuiString("receiving");
    }
}
