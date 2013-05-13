package enhancedportals.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderItem;

import org.lwjgl.opengl.GL11;

import enhancedportals.portal.Upgrade;

public class GuiUpgradeButton extends Gui
{
    public int ID;
    public boolean active;
    public Upgrade parent;

    public GuiUpgradeButton(int id, Upgrade parentUpgrade, boolean active)
    {
        ID = id;
        this.active = active;
        parent = parentUpgrade;
    }

    public void drawButton(int x, int y, int mouseX, int mouseY, FontRenderer fontRenderer, RenderItem itemRenderer, RenderEngine renderEngine)
    {
        GL11.glDisable(GL11.GL_LIGHTING);

        if (active)
        {
            drawRect(x, y, x + 16, y + 16, 0xFF00CC00);
        }
        else
        {
            drawRect(x, y, x + 16, y + 16, 0xFFCC0000);
        }

        if (parent.getItemStack(ID) != null)
        {
            itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, renderEngine, parent.getItemStack(ID), x, y);

            if (isPointInRegion(x, y, 16, 16, mouseX, mouseY))
            {
                parent.parent.drawText(parent.getHoverText(ID), mouseX, mouseY);
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    boolean isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
    }

    public void mouseClicked(int x, int y, int mouseX, int mouseY, int buttonClicked)
    {
        if (isPointInRegion(x, y, 16, 16, mouseX, mouseY))
        {
            parent.onElementClicked(this, buttonClicked);
        }
    }
}
