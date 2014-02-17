package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.library.gui.GuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementBase;
import uk.co.shadeddimensions.library.gui.element.ElementBaseContainer;
import uk.co.shadeddimensions.library.gui.element.ElementButton;
import uk.co.shadeddimensions.library.gui.element.ElementScrollBar;
import uk.co.shadeddimensions.library.gui.element.ElementScrollPanel;
import uk.co.shadeddimensions.library.gui.element.ElementText;
import uk.co.shadeddimensions.library.util.GuiUtils;

public class GuiConfig extends GuiBase
{
    @Override
    public void drawGuiBackgroundLayer(float f, int mouseX, int mouseY)
    {
        double left = 0, right = width, top = 35, b0 = 0, bottom = height - 50;
        Tessellator tessellator = Tessellator.instance;

        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f1 = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(2105376);
        tessellator.addVertexWithUV((double)left, (double)bottom, 0.0D, (double)((float)left / f1), (double)((float)(bottom) / f1));
        tessellator.addVertexWithUV((double)right, (double)bottom, 0.0D, (double)((float)right / f1), (double)((float)(bottom) / f1));
        tessellator.addVertexWithUV((double)right, (double)top, 0.0D, (double)((float)right / f1), (double)((float)(top) / f1));
        tessellator.addVertexWithUV((double)left, (double)top, 0.0D, (double)((float)left / f1), (double)((float)(top) / f1));
        tessellator.draw();

        super.drawGuiBackgroundLayer(f, mouseX, mouseY);

        this.overlayBackground(0, 35, 255, 255);
        this.overlayBackground(height - 50, height, 255, 255);
    }

    @Override
    public void drawGuiForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiForegroundLayer(mouseX, mouseY);

        getFontRenderer().drawStringWithShadow("EnhancedPortals 3 Configuration", GuiUtils.getCenteredOffset(this, "EnhancedPortals 3 Configuration", xSize), -guiTop + 20, 0xFFFFFF);
    }

    private void overlayBackground(int p_148136_1_, int p_148136_2_, int p_148136_3_, int p_148136_4_)
    {
        int left = 0;
        Tessellator tessellator = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(4210752, p_148136_4_);
        tessellator.addVertexWithUV((double)left, (double)p_148136_2_, 0.0D, 0.0D, (double)((float)p_148136_2_ / f));
        tessellator.addVertexWithUV((double)(left + this.width), (double)p_148136_2_, 0.0D, (double)((float)this.width / f), (double)((float)p_148136_2_ / f));
        tessellator.setColorRGBA_I(4210752, p_148136_3_);
        tessellator.addVertexWithUV((double)(left + this.width), (double)p_148136_1_, 0.0D, (double)((float)this.width / f), (double)((float)p_148136_1_ / f));
        tessellator.addVertexWithUV((double)left, (double)p_148136_1_, 0.0D, 0.0D, (double)((float)p_148136_1_ / f));
        tessellator.draw();
    }

    @Override
    public void initGui()
    {
        super.initGui();
        ElementScrollPanel panel = new ElementScrollPanel(this, -90, -guiTop + 35, xSize * 2, height - 80);

        int y = 10;
        panel.addElement(new ElementText(this, 0, y, Localization.getConfigString("general"), null, 0xFFFFFF, true));
        panel.addElement(new ElementButton(this, 0, y + 15, 110, "glyphType", Localization.getConfigString("alternateGlyph." + CommonProxy.useAlternateGlyphs)));

        y += 50;
        panel.addElement(new ElementText(this, 0, y, Localization.getConfigString("portal"), null, 0xFFFFFF, true));
        panel.addElement(new ElementButton(this, 0, y + 15, 110, "sound", Localization.getConfigString("sounds." + !CommonProxy.disableSounds)));
        panel.addElement(new ElementButton(this, 115, y + 15, 110, "destroyBlocks", Localization.getConfigString("portalsDestroy." + CommonProxy.portalsDestroyBlocks)));
        panel.addElement(new ElementButton(this, 230, y + 15, 110, "forceOverlays", Localization.getConfigString("showOverlays." + CommonProxy.forceShowFrameOverlays)));
        panel.addElement(new ElementButton(this, 0, y + 40, 110, "particles", Localization.getConfigString("particles." + !CommonProxy.disableParticles)));

        y += 70;
        panel.addElement(new ElementText(this, 0, y, Localization.getConfigString("power"), null, 0xFFFFFF, true));
        panel.addElement(new ElementButton(this, 0, y + 15, 110, "powerRequired", Localization.getConfigString("requirePower." + CommonProxy.requirePower)));
        panel.addElement(new ElementButton(this, 115, y + 15, 110, "powerMultiplier", Localization.getConfigString("multiplier") + CommonProxy.powerMultiplier));

        y += 50;
        panel.addElement(new ElementText(this, 0, y, Localization.getConfigString("teleportation"), null, 0xFFFFFF, true));
        panel.addElement(new ElementButton(this, 0, y + 15, 110, "fastCooldown", Localization.getConfigString("fastCooldown." + CommonProxy.fasterPortalCooldown)));

        ElementBase lastElement = panel.getElements().get(panel.getElements().size() - 1);
        panel.addElement(new ElementText(this, 0, lastElement.getRelativeY() + lastElement.getHeight() + 10, "", null));
        addElement(panel);
        addElement(new ElementScrollBar(this, panel.getRelativeX() + panel.getWidth() + 5, -guiTop + 37, 5, panel.getHeight() - 7, panel, 0x33FFFFFF, 0x77FFFFFF));

        buttonList.add(new GuiButton(0, width / 2 - 100, height - 40, "Done"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            CommonProxy.saveConfigs();
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
    }

    ElementButton getButtonForID(String id)
    {
        for (ElementBase e : ((ElementBaseContainer) elements.get(0)).getElements())
        {
            if (e instanceof ElementButton)
            {
                if (((ElementButton) e).getID().equals(id))
                {
                    return (ElementButton) e;
                }
            }
        }
        
        return null;
    }

    @Override
    public void handleElementButtonClick(String id, int mouseButton)
    {
        ElementButton button = getButtonForID(id);

        if (button == null)
        {
            return;
        }
        
        if (button.getID().equals("glyphType"))
        {
            CommonProxy.useAlternateGlyphs = !CommonProxy.useAlternateGlyphs;
            button.setText(Localization.getConfigString("alternateGlyph." + CommonProxy.useAlternateGlyphs));
        }
        else if (button.getID().equals("sound"))
        {
            CommonProxy.disableSounds = !CommonProxy.disableSounds;
            button.setText(Localization.getConfigString("sounds." + !CommonProxy.disableSounds));
        }
        else if (button.getID().equals("destroyBlocks"))
        {
            CommonProxy.portalsDestroyBlocks = !CommonProxy.portalsDestroyBlocks;
            button.setText(Localization.getConfigString("portalsDestroy." + CommonProxy.portalsDestroyBlocks));
        }
        else if (button.getID().equals("forceOverlays"))
        {
            CommonProxy.forceShowFrameOverlays = !CommonProxy.forceShowFrameOverlays;
            button.setText(Localization.getConfigString("showOverlays." + CommonProxy.forceShowFrameOverlays));
        }
        else if (button.getID().equals("particles"))
        {
            CommonProxy.disableParticles = !CommonProxy.disableParticles;
            button.setText(Localization.getConfigString("particles." + !CommonProxy.disableParticles));
        }
        else if (button.getID().equals("powerRequired"))
        {
            CommonProxy.requirePower = !CommonProxy.requirePower;
            button.setText(Localization.getConfigString("requirePower." + CommonProxy.requirePower));
        }
        else if (button.getID().equals("powerMultiplier"))
        {
            if (CommonProxy.powerMultiplier < 10)
            {
                CommonProxy.powerMultiplier++;
            }
            else
            {
                CommonProxy.powerMultiplier = 1;
            }

            button.setText(Localization.getConfigString("multiplier") + CommonProxy.powerMultiplier);
        }
        else if (button.getID().equals("fastCooldown"))
        {
            CommonProxy.fasterPortalCooldown = !CommonProxy.fasterPortalCooldown;
            button.setText(Localization.getConfigString("fastCooldown." + CommonProxy.fasterPortalCooldown));
        }
    }
}
