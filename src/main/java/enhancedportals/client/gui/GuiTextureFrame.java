package enhancedportals.client.gui;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidContainerRegistry;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.button.GuiBetterSlider;
import enhancedportals.client.gui.button.GuiRGBSlider;
import enhancedportals.client.gui.elements.ElementFakeItemSlot;
import enhancedportals.client.gui.elements.ElementScrollFrameIcons;
import enhancedportals.client.gui.tabs.TabColour;
import enhancedportals.client.gui.tabs.TabTip;
import enhancedportals.client.gui.tabs.TabTipSecondary;
import enhancedportals.common.IFakeSlotHandler;
import enhancedportals.inventory.ContainerTextureFrame;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.tileentity.portal.TileController;

public class GuiTextureFrame extends BaseGui implements IFakeSlotHandler
{
    public static final int CONTAINER_SIZE = 92, CONTAINER_WIDTH = 190;
    protected TileController controller;
    protected GuiRGBSlider sliderR, sliderG, sliderB;
    protected GuiButton buttonReset, buttonSave;

    public GuiTextureFrame(TileController c, EntityPlayer p)
    {
        super(new ContainerTextureFrame(c, p.inventory), CONTAINER_SIZE);
        controller = c;
        xSize = CONTAINER_WIDTH;
        name = "gui.frame";
        texture = new ResourceLocation("enhancedportals", "textures/gui/textures.png");
        leftNudge = 7;
        hasSingleTexture = true;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        String s = EnhancedPortals.localize("gui.facade");
        getFontRenderer().drawString(s, xSize - 30 - getFontRenderer().getStringWidth(s), containerSize - 12, 0x404040);
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == buttonSave.id || button.id == buttonReset.id)
        {
            if (button.id == buttonSave.id)
            {
                int hex = Integer.parseInt(String.format("%02x%02x%02x", sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), 16);
                controller.activeTextureData.setFrameColour(hex);
            }
            else if (button.id == buttonReset.id)
            {
                int colour = 0xffffff;
                controller.activeTextureData.setFrameColour(colour);
    
                Color c = new Color(colour);
                sliderR.sliderValue = c.getRed() / 255f;
                sliderG.sliderValue = c.getGreen() / 255f;
                sliderB.sliderValue = c.getBlue() / 255f;
            }
            
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("colour", Integer.parseInt(String.format("%02x%02x%02x", sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), 16));
            EnhancedPortals.packetPipeline.sendToServer(new PacketGuiData(tag));
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        
        Color c = new Color(controller.activeTextureData.getFrameColour());
        sliderR = new GuiRGBSlider(100, guiLeft + xSize + 4, guiTop + 25, EnhancedPortals.localize("gui.red"), c.getRed() / 255f, 105);
        sliderG = new GuiRGBSlider(101, guiLeft + xSize + 4, guiTop + 46, EnhancedPortals.localize("gui.green"), c.getGreen() / 255f, 105);
        sliderB = new GuiRGBSlider(102, guiLeft + xSize + 4, guiTop + 67, EnhancedPortals.localize("gui.blue"), c.getBlue() / 255f, 105);
        
        buttonList.add(sliderR);
        buttonList.add(sliderG);
        buttonList.add(sliderB);

        buttonSave = new GuiButton(110, guiLeft + xSize + 4, guiTop + 88, 53, 20, EnhancedPortals.localize("gui.save"));
        buttonReset = new GuiButton(111, guiLeft + xSize + 57, guiTop + 88, 53, 20, EnhancedPortals.localize("gui.reset"));

        buttonList.add(buttonSave);
        buttonList.add(buttonReset);

        addTab(new TabColour(this, sliderR, sliderG, sliderB, buttonSave, buttonReset));
        addTab(new TabTip(this, "colourTip"));
        addTab(new TabTipSecondary(this, "frameCustomTexture"));
        addElement(new ElementScrollFrameIcons(this, 7, 17, texture));
        addElement(new ElementFakeItemSlot(this, xSize - 24, containerSize - 16, controller.activeTextureData.getFrameItem()));
    }

    @Override
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        super.mouseMovedOrUp(par1, par2, par3);

        if (par3 == 0)
        {
            for (Object o : buttonList)
            {
                if (o instanceof GuiBetterSlider)
                {
                    GuiBetterSlider slider = (GuiBetterSlider) o;
                    slider.mouseReleased(par1, par2);
                }
            }
        }
    }
    
    public void iconSelected(int icon)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("custom", icon);
        EnhancedPortals.packetPipeline.sendToServer(new PacketGuiData(tag));
    }

    public int getSelectedIcon()
    {
        return controller.activeTextureData.getCustomFrameTexture();
    }

    @Override
    public void onItemChanged(ItemStack newItem)
    {
        NBTTagCompound tag = new NBTTagCompound();
        
        if (newItem != null)
        {
            newItem.writeToNBT(tag);
        }
        else
        {
            tag.setBoolean("removeItem", true);
        }
        
        EnhancedPortals.packetPipeline.sendToServer(new PacketGuiData(tag));
    }

    @Override
    public boolean isItemValid(ItemStack s)
    {
        if (s == null)
        {
            return true;
        }
        
        Block b = Block.getBlockFromItem(s.getItem());
        
        if (b == Blocks.air)
        {
            return false;
        }
        else if (!b.isNormalCube())
        {
            return false;
        }
        
        return true;
    }
}
