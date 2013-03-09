package alz.mods.enhancedportals.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiIntegerBox extends GuiTextField
{
	int xPos, width, yPos, height;

	public GuiIntegerBox(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5)
	{
		super(par1FontRenderer, par2, par3, par4, par5);
		
		xPos = par2;
		yPos = par3;
		width = par4;
		height = par5;
		
		setMaxStringLength(10);
	}
	
	@Override
	public boolean textboxKeyTyped(char par1, int par2)
	{
		if (!Character.isDigit(par1) && par2 != 14 && par2 != 211 && par2 != 203 && par2 != 205)
			return false;
		
		return super.textboxKeyTyped(par1, par2);
	}
	
	@Override
	public void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		
		boolean clickedInside = par1 >= this.xPos && par1 < this.xPos + this.width && par2 >= this.yPos && par2 < this.yPos + this.height;
		
		if (!clickedInside)
			return;
		
		if (getText().length() > 0 && (getText() == "0" || Integer.parseInt(getText()) == 0))
		{
			setText("");
		}
	}
}
