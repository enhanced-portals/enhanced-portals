package cofh.gui.element;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;
import cofh.gui.GuiBase;
import cofh.render.RenderHelper;

public class ElementFluidTank extends ElementBase {

	public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(GuiBase.PATH_ELEMENTS + "FluidTank.png");
	public static final int DEFAULT_SCALE = 60;

	protected IFluidTank tank;
	protected int gaugeType;

	public ElementFluidTank(GuiBase gui, int posX, int posY, IFluidTank tank) {

		super(gui, posX, posY);
		this.tank = tank;

		this.texture = DEFAULT_TEXTURE;
		this.texW = 64;
		this.texH = 64;

		this.sizeX = 16;
		this.sizeY = DEFAULT_SCALE;
	}

	public ElementFluidTank(GuiBase gui, int posX, int posY, IFluidTank tank, String texture) {

		super(gui, posX, posY);
		this.tank = tank;

		this.texture = new ResourceLocation(texture);
		this.texW = 64;
		this.texH = 64;

		this.sizeX = 16;
		this.sizeY = DEFAULT_SCALE;
	}

	public ElementFluidTank setGauge(int gaugeType) {

		this.gaugeType = gaugeType;
		return this;
	}

	@Override
	public void draw() {

		if (!visible) {
			return;
		}
		int amount = getScaled();
		gui.drawFluid(posX, posY + sizeY - amount, tank.getFluid(), sizeX, amount);
		RenderHelper.bindTexture(texture);
		drawTexturedModalRect(posX, posY, 32 + gaugeType * 16, 1, sizeX, sizeY);
	}

	@Override
	public String getTooltip() {

		return "" + tank.getFluidAmount() + " / " + tank.getCapacity() + " mB";
	}

	@Override
	public boolean handleMouseClicked(int x, int y, int mouseButton) {

		return false;
	}

	int getScaled() {

		return tank.getFluidAmount() * sizeY / tank.getCapacity();
	}

}
