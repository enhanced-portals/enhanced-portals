package uk.co.shadeddimensions.library.gui.element;

import java.util.List;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;
import uk.co.shadeddimensions.library.util.GuiUtils;

public class ElementFluidTank extends ElementProgressBar
{
    byte scale;
    Fluid fluid;

    public ElementFluidTank(IGuiBase parent, int x, int y, int progress, int max, Fluid f, int scale)
    {
        super(parent, x, y, progress, max);
        sizeX = 18;
        sizeY = 62;
        this.scale = (byte) scale;
        fluid = f;
    }

    @Override
    public void addTooltip(List<String> list)
    {
        super.addTooltip(list);
        list.set(0, list.get(0) + " mB");
    }

    @Override
    public void draw()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        gui.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(posX, posY, 210, 0, sizeX, sizeY);

        if (!isDisabled() && fluid != null)
        {
            int height = 0;

            if (currentProgress > 0)
            {
                height = Math.round((float) currentProgress * sizeY / maxProgress);
                height = Math.min(height, sizeY - 2);
            }

            Icon icon = fluid.getIcon();
            int colour = fluid.getColor();
            gui.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            GL11.glColor3ub((byte) (colour >> 16 & 0xFF), (byte) (colour >> 8 & 0xFF), (byte) (colour & 0xFF));

            for (int i = 0; i < height; i += 16)
            {
                GuiUtils.drawScaledTexturedModelRectFromIcon(gui, posX + 1, posY + sizeY - height - 1 + i, icon, 16, Math.min(height - i, 16));
            }
        }

        if (scale != 0)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            gui.getTextureManager().bindTexture(texture);
            drawTexturedModalRect(posX + 1, posY + (scale == 1 ? 6 : 14), scale == 1 ? 228 : 210, scale == 1 ? 42 : 62, sizeX, sizeY);
        }
    }

    public void setFluid(Fluid f)
    {
        if (fluid == null)
        {
            currentProgress = 0;
        }

        fluid = f;
    }

    public void setFluid(FluidStack stack)
    {
        if (stack == null)
        {
            fluid = null;
            currentProgress = 0;
        }
        else
        {
            fluid = stack.getFluid();
            currentProgress = stack.amount;
        }
    }
}
