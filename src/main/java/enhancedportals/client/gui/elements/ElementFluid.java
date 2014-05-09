package enhancedportals.client.gui.elements;

import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;

import org.lwjgl.opengl.GL11;

import enhancedportals.client.gui.BaseGui;

public class ElementFluid extends BaseElement
{
    FluidTank t;
    int scale = 1;

    public ElementFluid(BaseGui gui, int x, int y, FluidTank tank)
    {
        super(gui, x, y, 18, 62);
        t = tank;
        texture = new ResourceLocation("enhancedportals", "textures/gui/elements.png");
    }

    @Override
    public void addTooltip(List<String> list)
    {
        list.add(t.getFluidAmount() + " / " + t.getCapacity() + " mB");
    }

    @Override
    protected void drawContent()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        parent.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(posX, posY, 210, 0, sizeX, sizeY);

        if (t.getFluid() != null)
        {
            int height = 0;
            if (t.getFluidAmount() > 0)
            {
                height = Math.round((float) t.getFluidAmount() * sizeY / t.getCapacity());
                height = Math.min(height, sizeY - 2);
            }
    
            IIcon icon = t.getFluid().getFluid().getIcon();
            int colour = t.getFluid().getFluid().getColor();
            parent.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            GL11.glColor3ub((byte) (colour >> 16 & 0xFF), (byte) (colour >> 8 & 0xFF), (byte) (colour & 0xFF));
    
            for (int i = 0; i < height; i += 16)
            {
                drawScaledTexturedModelRectFromIcon(posX + 1, posY + sizeY - height - 1 + i, icon, 16, Math.min(height - i, 16));
            }
        }

        if (scale != 0)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            parent.getTextureManager().bindTexture(texture);
            drawTexturedModalRect(posX + 1, posY + (scale == 1 ? 6 : 14), scale == 1 ? 228 : 210, scale == 1 ? 42 : 62, sizeX, sizeY);
        }
    }

    @Override
    public void update()
    {

    }
    
    void drawScaledTexturedModelRectFromIcon(int x, int y, IIcon icon, int width, int height)
    {
        if (icon == null)
        {
            return;
        }

        double minU = icon.getMinU();
        double maxU = icon.getMaxU();
        double minV = icon.getMinV();
        double maxV = icon.getMaxV();
        int zLevel = 0;
        Tessellator tessellator = Tessellator.instance;
        
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, zLevel, minU, minV + (maxV - minV) * height / 16F);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F);
        tessellator.addVertexWithUV(x + width, y + 0, zLevel, minU + (maxU - minU) * width / 16F, minV);
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, minU, minV);
        tessellator.draw();
    }
}
