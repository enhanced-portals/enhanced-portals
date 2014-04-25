package enhancedportals.client.gui;

import java.awt.Color;

import com.sun.istack.internal.localization.Localizable;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.library.gui.button.GuiBetterSlider;
import uk.co.shadeddimensions.library.gui.button.GuiRGBSlider;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.tabs.TabColour;
import enhancedportals.client.gui.tabs.TabTip;
import enhancedportals.inventory.ContainerTextureFrame;

public class GuiTextureFrame extends BaseGui
{
    public static final int CONTAINER_SIZE = 75;
    TileController controller;
    GuiRGBSlider sliderR, sliderG, sliderB;
    GuiButton buttonReset, buttonSave;

    public GuiTextureFrame(TileController c, EntityPlayer p)
    {
        super(new ContainerTextureFrame(c, p.inventory), CONTAINER_SIZE);
        controller = c;
        name = "gui.frame";
        setCombinedInventory();
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
            PacketHandlerClient.sendGuiPacket(tag);
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        
        Color c = new Color(controller.activeTextureData.getFrameColour());
        sliderR = new GuiRGBSlider(100, guiLeft + xSize + 4, guiTop + 24, EnhancedPortals.localize("gui.red"), c.getRed() / 255f);
        sliderG = new GuiRGBSlider(101, guiLeft + xSize + 4, guiTop + 45, EnhancedPortals.localize("gui.green"), c.getGreen() / 255f);
        sliderB = new GuiRGBSlider(102, guiLeft + xSize + 4, guiTop + 66, EnhancedPortals.localize("gui.blue"), c.getBlue() / 255f);
        
        buttonList.add(sliderR);
        buttonList.add(sliderG);
        buttonList.add(sliderB);

        buttonSave = new GuiButton(110, guiLeft + xSize + 4, guiTop + 87, 57, 20, EnhancedPortals.localize("gui.save"));
        buttonReset = new GuiButton(111, guiLeft + xSize + 61, guiTop + 87, 57, 20, EnhancedPortals.localize("gui.reset"));

        buttonList.add(buttonSave);
        buttonList.add(buttonReset);

        addTab(new TabColour(this, sliderR, sliderG, sliderB, buttonSave, buttonReset));
        addTab(new TabTip(this, "colourTip"));
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
}
