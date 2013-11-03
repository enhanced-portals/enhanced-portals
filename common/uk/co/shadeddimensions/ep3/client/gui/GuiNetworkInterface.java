package uk.co.shadeddimensions.ep3.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.base.GuiResizable;
import uk.co.shadeddimensions.ep3.client.gui.elements.GuiGlyphIdentifierSelector;
import uk.co.shadeddimensions.ep3.client.gui.elements.GuiGlyphIdentifierViewer;
import uk.co.shadeddimensions.ep3.container.ContainerNetworkInterface;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class GuiNetworkInterface extends GuiResizable
{
    GuiGlyphIdentifierSelector glyphSelector;
    GuiGlyphIdentifierViewer glyphViewer;

    TilePortalController portalController;

    public GuiNetworkInterface(TilePortalController tile, EntityPlayer player)
    {
        super(new ContainerNetworkInterface(tile, player), tile, 176, 75, 176, 143);
        portalController = tile;
        glyphSelector = new GuiGlyphIdentifierSelector(7, 57, this);
        glyphViewer = new GuiGlyphIdentifierViewer(7, 20, this, glyphSelector);

        glyphSelector.setGlyphsToIdentifier(portalController.getNetworkIdentifier());
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (isShiftKeyDown())
        {
            if (button.id == 0) // Clear
            {
                glyphSelector.clearSelection();
            }
            else if (button.id == 1) // Random
            {
                glyphSelector.randomize(isCtrlKeyDown());
            }
        }
        else
        {
            if (button.id == 0) // Reset Changes
            {
                glyphSelector.setGlyphsToIdentifier(portalController.getNetworkIdentifier());
                toggleState();
            }
            else if (button.id == 1) // Save Changes
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setString("networkIdentifier", glyphSelector.getSelectedIdentifier().getGlyphString());        
                ClientProxy.sendGuiPacket(payload);                
                toggleState();
            }
        }
    }

    @Override
    protected void drawBackground(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(guiLeft + 7, guiTop + 20, 0, 54, 162, 18);

        glyphViewer.drawBackground(i, j);
    }

    @Override
    protected void drawBackgroundExpanded(float f, int i, int j)
    {
        glyphSelector.drawBackground(i, j);
    }

    @Override
    protected void drawForeground(int par1, int par2)
    {
        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile." + Reference.SHORT_ID + ".portalFrame.networkInterface.name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("tile." + Reference.SHORT_ID + ".portalFrame.networkInterface.name")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkIdentifier"), 8, 8, 0x404040);
    }

    @Override
    protected void drawForegroundExpanded(int par1, int par2)
    {
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".glyphs"), 8, 44, 0x404040);
    }

    @Override
    protected void drawForegroundShrunk(int par1, int par2)
    {
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkInfo"), 8, 44, 0x404040);

        if (par1 >= guiLeft + 7 && par1 <= guiLeft + xSize - 8)
        {
            if (par2 >= guiTop + 20 && par2 <= guiTop + 37)
            {
                List<String> list = new ArrayList<String>();
                list.add(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".clickToModify"));

                drawHoveringText(list, par1 - guiLeft, par2 - guiTop, fontRenderer);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
        }

        String s1 = portalController.connectedPortals == -1 ? StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkIdentifier.notSet") : "" + portalController.connectedPortals;
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkIdentifier.networkedPortals"), 12, 57, 0x777777);
        fontRenderer.drawString(s1, xSize - 12 - fontRenderer.getStringWidth(s1), 57, 0x404040);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.add(new GuiButton(0, guiLeft + 7, guiTop + 115, (xSize - 14) / 2 - 5, 20, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.cancel")));
        buttonList.add(new GuiButton(1, guiLeft + xSize - (xSize - 14) / 2 - 2, guiTop + 115, (xSize - 14) / 2 - 5, 20, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.save")));

        ((GuiButton) buttonList.get(0)).drawButton = ((GuiButton) buttonList.get(1)).drawButton = expanded;
    }

    @Override
    protected void mouseClickExpanded(int par1, int par2, int mouseButton)
    {
        glyphViewer.mouseClicked(par1, par2, mouseButton);
        glyphSelector.mouseClicked(par1, par2, mouseButton);
    }

    @Override
    protected void mouseClickShrunk(int par1, int par2, int mouseButton)
    {
        if (par1 >= guiLeft + 7 && par1 <= guiLeft + xSize - 8)
        {
            if (par2 >= guiTop + 20 && par2 <= guiTop + 37)
            {
                toggleState();
                mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            }
        }
    }

    @Override
    protected void onExpandGui()
    {
        ((GuiButton) buttonList.get(0)).drawButton = ((GuiButton) buttonList.get(1)).drawButton = true;
    }

    @Override
    protected void onShrinkGui()
    {
        ((GuiButton) buttonList.get(0)).drawButton = ((GuiButton) buttonList.get(1)).drawButton = false;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (expanded)
        {
            if (isShiftKeyDown())
            {
                ((GuiButton) buttonList.get(0)).displayString = EnumChatFormatting.AQUA + StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.clear");
                ((GuiButton) buttonList.get(1)).displayString = (isCtrlKeyDown() ? EnumChatFormatting.GOLD : EnumChatFormatting.AQUA) + StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.random");
            }
            else
            {
                ((GuiButton) buttonList.get(0)).displayString = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.cancel");
                ((GuiButton) buttonList.get(1)).displayString = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.save");
            }
        }
    }
}
