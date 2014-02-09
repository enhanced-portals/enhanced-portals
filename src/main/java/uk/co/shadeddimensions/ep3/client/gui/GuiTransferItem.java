package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.container.ContainerTransferItem;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferItem;
import uk.co.shadeddimensions.library.gui.GuiBaseContainer;
import uk.co.shadeddimensions.library.gui.element.ElementItemIconWithSlotAndCount;

public class GuiTransferItem extends GuiBaseContainer
{
    TileTransferItem item;
    
    public GuiTransferItem(TileTransferItem item)
    {
        super(new ContainerTransferItem(item), new ResourceLocation("enhancedportals", "textures/gui/transfer.png"));
        this.item = item;
        ySize = 44;
        name = BlockFrame.instance.getUnlocalizedName() + ".item.name";
        drawInventory = false;
    }
    
    @Override
    public void drawBackgroundTexture()
    {
        if (texture != null)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            getMinecraft().renderEngine.bindTexture(texture);
            drawTexturedModalRect(guiLeft, guiTop, 0, 124, xSize, ySize);
        }
    }
    
    @Override
    public void addElements()
    {
        addElement(new ElementItemIconWithSlotAndCount(this, 5, 20, item.getStackInSlot(0)));
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
        buttonList.add(new GuiButton(0, guiLeft + 27, guiTop + ySize - 25, xSize - 32, 20, item.isSending ? "Sending" : "Receiving"));
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        ((ElementItemIconWithSlotAndCount) elements.get(0)).setItem(item.getStackInSlot(0));
        ((GuiButton) buttonList.get(0)).displayString = item.isSending ? "Sending" : "Receiving";
    }
}
