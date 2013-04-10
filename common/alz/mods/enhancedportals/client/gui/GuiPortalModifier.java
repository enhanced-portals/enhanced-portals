package alz.mods.enhancedportals.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import alz.mods.enhancedportals.client.ClientProxy;
import alz.mods.enhancedportals.common.ContainerPortalModifier;
import alz.mods.enhancedportals.reference.Localizations;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import cpw.mods.fml.client.FMLClientHandler;

public class GuiPortalModifier extends GuiContainer
{
    GuiTextField textBox;
    GuiButton button, button2;
    public TileEntityPortalModifier Modifier;

    public GuiPortalModifier(InventoryPlayer player, TileEntityPortalModifier modifier)
    {
        super(new ContainerPortalModifier(player, modifier));
        Modifier = modifier;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (!button.enabled)
        {
            return;
        }

        if (button.id == 1)
        {
            textBox.setText(String.format("%1s", textBox.getText()).replace(' ', '0'));
            this.button.enabled = false;

            Modifier.setNetwork(textBox.getText());
            ClientProxy.SendBlockUpdate(Modifier);
        }
        else if (button.id == 2)
        {
            textBox.setText("0");
            this.button.enabled = false;
            button2.enabled = false;

            Modifier.setNetwork(textBox.getText());
            ClientProxy.SendBlockUpdate(Modifier);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Reference.File.GuiTextureDirectory + "portalModifier.png");
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String freq = Localizations.getLocalizedString(Strings.GUI_Frequency);

        fontRenderer.drawString(Localizations.getLocalizedString("tile." + Strings.PortalModifier_Name + ".name"), 8, 6, 4210752);
        fontRenderer.drawString(freq, (width - 20) / 2 - fontRenderer.getStringWidth(freq) / 2 - guiLeft, 30, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        textBox.drawTextBox();

        if (isPointInRegion(19, 40, 18, 20, par1, par2))
        {
            drawCreativeTabHoveringText(Localizations.getLocalizedString(Strings.GUI_ClearFrequency), par1, par2);
        }
        else if (isPointInRegion(120, 40, 18, 20, par1, par2))
        {
            drawCreativeTabHoveringText(Localizations.getLocalizedString(Strings.GUI_SaveFrequency), par1, par2);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        textBox = new GuiImprovedTextBox(fontRenderer, (width - 20) / 2 - 75 / 2, guiTop + 42, 75, 16, 10, true, true);
        textBox.setText(Modifier.getNetwork());

        button = new GuiButton(1, (width - 20) / 2 - 40 / 2 + 61, guiTop + 40, 20, 20, Localizations.getLocalizedString(Strings.GUI_SaveCharacter));
        button2 = new GuiButton(2, (width - 20) / 2 - 40 / 2 - 40, guiTop + 40, 20, 20, Localizations.getLocalizedString(Strings.GUI_ClearCharacter));

        button.enabled = false;
        button2.enabled = Integer.parseInt(textBox.getText()) != 0;

        buttonList.add(button);
        buttonList.add(button2);
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (!textBox.isFocused() || par2 == FMLClientHandler.instance().getClient().gameSettings.keyBindInventory.keyCode || par2 == 1)
        {
            super.keyTyped(par1, par2);
            return;
        }

        if (par2 == 28)
        {
            textBox.setFocused(false);
            return;
        }

        if (!button.enabled)
        {
            button.enabled = true;
        }

        if (textBox.getText() == "0")
        {
            button2.enabled = false;
        }
        else
        {
            button2.enabled = true;
        }

        textBox.textboxKeyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        textBox.mouseClicked(par1, par2, par3);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();

        Modifier.worldObj.markBlockForRenderUpdate(Modifier.xCoord, Modifier.yCoord, Modifier.zCoord);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        textBox.updateCursorCounter();
    }
}
