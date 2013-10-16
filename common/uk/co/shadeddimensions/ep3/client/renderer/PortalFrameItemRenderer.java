package uk.co.shadeddimensions.ep3.client.renderer;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.network.CommonProxy;

public class PortalFrameItemRenderer implements IItemRenderer
{
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        switch (type)
        {
            case ENTITY:
                return true;

            case EQUIPPED:
                return true;

            case EQUIPPED_FIRST_PERSON:
                return true;

            case INVENTORY:
                return true;

            default:
                return false;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch (type)
        {
            case ENTITY:
                renderPortalItem((RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
                break;

            case EQUIPPED:
                renderPortalItem((RenderBlocks) data[0], item, -0.4f, 0.50f, 0.35f);
                break;

            case EQUIPPED_FIRST_PERSON:
                renderPortalItem((RenderBlocks) data[0], item, -0.4f, 0.2f, 0.2f);
                break;

            case INVENTORY:
                renderPortalItem((RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
                break;

            default:
        }
    }

    private void renderPortalItem(RenderBlocks renderer, ItemStack item, float x, float y, float z)
    {
        Tessellator tessellator = Tessellator.instance;

        Icon icon = CommonProxy.blockFrame.getIcon(0, 0);
        renderer.setRenderBoundsFromBlock(CommonProxy.blockFrame);

        GL11.glTranslatef(x, y, z);

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        renderer.renderFaceYNeg(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        renderer.renderFaceZNeg(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();

        /*if (item.getItemDamage() >= 1) // TODO
        {
            icon = BlockFrame.typeOverlayIcons[item.getItemDamage()];

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderer.renderFaceYNeg(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderer.renderFaceZNeg(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(CommonProxy.blockFrame, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
        }*/
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }
}
