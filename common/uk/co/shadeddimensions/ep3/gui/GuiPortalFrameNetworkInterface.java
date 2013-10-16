package uk.co.shadeddimensions.ep3.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.container.ContainerNetworkInterface;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class GuiPortalFrameNetworkInterface extends GuiResizable
{
    GuiGlyphSelectorNetwork glyphSelector;
    GuiGlyphViewer glyphViewer;

    TileNetworkInterface networkInterface;

    public GuiPortalFrameNetworkInterface(TileNetworkInterface tile)
    {
        super(new ContainerNetworkInterface(tile), tile, 176, 85, 176, 143);

        networkInterface = tile;

        glyphSelector = new GuiGlyphSelectorNetwork(7, 57, 0xffffff, this);
        glyphViewer = new GuiGlyphViewer(7, 20, 0xffffff, this, glyphSelector);

        glyphSelector.setSelectedToIdentifier(networkInterface.getPortalController().networkIdentifier);
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
                glyphSelector.setSelectedToIdentifier(networkInterface.getPortalController().networkIdentifier);

                toggleState();
            }
            else if (button.id == 1) // Save Changes
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setString("networkIdentifier", glyphViewer.getSelectedIdentifier());        
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

        glyphViewer.drawForeground(par1, par2);
    }

    @Override
    protected void drawForegroundExpanded(int par1, int par2)
    {
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".glyphs"), 8, 44, 0x404040);

        glyphSelector.drawForeground(par1, par2);
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

        String s = EnhancedPortals.config.getBoolean("randomTeleportMode") ? StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkIdentifier.random") : StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkIdentifier.sequential"), s1 = networkInterface.connectedPortals == -1 ? StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkIdentifier.notSet") : "" + networkInterface.connectedPortals;

        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkIdentifier.connectedPortals"), 12, 57, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkIdentifier.teleportMode"), 12, 67, 0x777777);

        fontRenderer.drawString(s1, xSize - 12 - fontRenderer.getStringWidth(s1), 57, 0x404040);
        fontRenderer.drawString(s, xSize - 12 - fontRenderer.getStringWidth(s), 67, 0x404040);
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
        ((GuiButton) buttonList.get(0)).drawButton = ((GuiButton) buttonList.get(1)).drawButton = glyphSelector.canEdit = glyphViewer.canEdit = true;
    }

    @Override
    protected void onShrinkGui()
    {
        ((GuiButton) buttonList.get(0)).drawButton = ((GuiButton) buttonList.get(1)).drawButton = glyphSelector.canEdit = glyphViewer.canEdit = false;
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
