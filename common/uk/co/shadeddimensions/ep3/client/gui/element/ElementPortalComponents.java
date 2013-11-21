package uk.co.shadeddimensions.ep3.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;

public class ElementPortalComponents extends ElementBase
{
    TilePortalController controller;
    RenderItem itemRenderer = new RenderItem();
    
    public ElementPortalComponents(GuiBase gui, TilePortalController control, int posX, int posY)
    {
        super(gui, posX + 2, posY + 2);
        controller = control;
    }

    @Override
    public void draw()
    {
        if (!isVisible())
        {
            return;
        }
        
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        TextureManager textureManager = Minecraft.getMinecraft().renderEngine;
        
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, textureManager, new ItemStack(CommonProxy.blockFrame), posX, posY);
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, textureManager, new ItemStack(CommonProxy.blockFrame, 1, 2), posX, posY + 20);
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, textureManager, new ItemStack(CommonProxy.blockFrame, 1, 3), posX, posY + 40);
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, textureManager, new ItemStack(CommonProxy.blockFrame, 1, 4), posX, posY + 60);
        
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, textureManager, new ItemStack(CommonProxy.blockPortal), posX + 80, posY);
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, textureManager, new ItemStack(CommonProxy.blockFrame, 1, 5), posX + 80, posY + 20);
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, textureManager, new ItemStack(CommonProxy.blockFrame, 1, 6), posX + 80, posY + 40);
        //itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, textureManager, new ItemStack(CommonProxy.blockFrame, 1, 7), posX + 80, posY + 60);
                
        fontRenderer.drawString("" + controller.blockManager.getFrameCount(), posX + 20, posY + 4, 0x404040);
        fontRenderer.drawString("" + controller.blockManager.getRedstoneInterfaceCount(), posX + 20, posY + 24, 0x404040);
        fontRenderer.drawString(controller.blockManager.getHasNetworkInterface() ? Localization.getGuiString("initialized") : "0", posX + 20, posY + 44, 0x404040);
        fontRenderer.drawString(controller.blockManager.getHasDialDevice() ? Localization.getGuiString("initialized") : "0", posX + 20, posY + 64, 0x404040);
        
        fontRenderer.drawString("" + controller.blockManager.getPortalCount(), posX + 100, posY + 4, 0x404040);
        fontRenderer.drawString(controller.blockManager.getHasBiometricIdentifier() ? Localization.getGuiString("initialized") : "0", posX + 100, posY + 24, 0x404040);
        fontRenderer.drawString(controller.blockManager.getHasModuleManipulator() ? Localization.getGuiString("initialized") : "0", posX + 100, posY + 44, 0x404040);
    }

    @Override
    public String getTooltip()
    {
        return null;
    }
}
