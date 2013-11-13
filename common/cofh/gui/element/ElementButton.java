package cofh.gui.element;

import cofh.gui.GuiBase;
import cofh.render.RenderHelper;

public class ElementButton extends ElementBase {

	int sheetX;
	int sheetY;
	int hoverX;
	int hoverY;
	String tooltip;

	public ElementButton(GuiBase gui, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int sizeX, int sizeY, String texture) {

		super(gui, posX, posY);
		setName(name);
		setSize(sizeX, sizeY);
		setTexture(texture, texW, texH);
		this.sheetX = sheetX;
		this.sheetY = sheetY;
		this.hoverX = hoverX;
		this.hoverY = hoverY;
	}

	public String toolTip;

	@Override
	public void draw() {

		RenderHelper.bindTexture(texture);
		if (intersectsWith(gui.getMouseX(), gui.getMouseY())) {
			drawTexturedModalRect(posX, posY, hoverX, hoverY, sizeX, sizeY);
		} else {
			drawTexturedModalRect(posX, posY, sheetX, sheetY, sizeX, sizeY);
		}
	}

	@Override
	public String getTooltip() {

		return tooltip;
	}

	public ElementButton setToolTip(String toolTip) {

		tooltip = toolTip;
		return this;
	}

	@Override
	public boolean handleMouseClicked(int x, int y, int mouseButton) {

		gui.handleElementButtonClick(getName(), mouseButton);
		return true;
	}

	public void setSheetX(int pos) {

		sheetX = pos;
	}

	public void setSheetY(int pos) {

		sheetY = pos;
	}

	public void setHoverX(int pos) {

		hoverX = pos;
	}

	public void setHoverY(int pos) {

		hoverY = pos;
	}

}
