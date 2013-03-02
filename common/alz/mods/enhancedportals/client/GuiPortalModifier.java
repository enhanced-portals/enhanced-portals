package alz.mods.enhancedportals.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import alz.mods.enhancedportals.common.ContainerPortalModifier;
import alz.mods.enhancedportals.common.Reference;
import alz.mods.enhancedportals.common.TileEntityPortalModifier;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GuiPortalModifier extends GuiContainer
{
	GuiTextField textBox;
	GuiButton button;
	public TileEntityPortalModifier Modifier;
	
	String originalText;
	
	public GuiPortalModifier(InventoryPlayer player, TileEntityPortalModifier modifier)
	{
		super(new ContainerPortalModifier(player, modifier));
		Modifier = modifier;
	}

	public FontRenderer getFontRenderer()
	{
		return fontRenderer;
	}
	
	public RenderEngine getRenderEngine()
	{
		return mc.renderEngine;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		textBox = new GuiTextField(fontRenderer, this.guiLeft + 8, this.guiTop + 21, 52, 16);
		textBox.setFocused(true);
		textBox.setText("" + Modifier.Frequency);
		originalText = "" + Modifier.Frequency;
		button = new GuiButton(1, this.guiLeft + this.xSize - 44, this.guiTop + (this.ySize / 2) - 25, 37, 20, "Save");
		controlList.add(button);
		button.enabled = false;
	}
	
	protected void actionPerformed(GuiButton button)
	{
		if (button.id == 1 && button.enabled == true)
		{
			textBox.setText(String.format("%6s", textBox.getText()).replace(' ', '0'));
			Modifier.Frequency = Integer.parseInt(textBox.getText());
			button.enabled = false;
			originalText = textBox.getText();
			ClientNetworking.SendBlockUpdate(Modifier.Frequency, -1, Modifier.xCoord, Modifier.yCoord, Modifier.zCoord, Modifier.worldObj.provider.dimensionId);
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2)
	{
		if (!textBox.isFocused() || par2 == FMLClientHandler.instance().getClient().gameSettings.keyBindInventory.keyCode || par2 == 1)
		{
			super.keyTyped(par1, par2);
			return;
		}
		
		if (par2 == 28)
		{
			textBox.setFocused(false);
			return;
		}
		
		if (!Character.isDigit(par1) && par2 != 14  && par2 != 211 && par2 != 203 && par2 != 205)
			return;
		
		if (textBox.getText().length() > 5 && par2 != 14  && par2 != 211 && par2 != 203 && par2 != 205)
			return;
		
		if (!button.enabled)
			button.enabled = true;
		
		textBox.textboxKeyTyped(par1, par2);
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		textBox.mouseClicked(par1, par2, par3);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		int swappedColour = Modifier.Colour;
		
		if (swappedColour == 0)
			swappedColour = 5;
		else if (swappedColour == 5)
			swappedColour = 0;
		
		fontRenderer.drawString(Reference.STR_PortalModifierTitle, 8, 6, 4210752);
				
		fontRenderer.drawString(Reference.STR_Frequency, 70, 25, 4210752);
		fontRenderer.drawString(Reference.STR_Upgrades, 70, 50, 4210752);
		
		//fontRenderer.drawString(ItemDye.dyeColorNames[swappedColour].substring(0, 1).toUpperCase() + ItemDye.dyeColorNames[swappedColour].substring(1), 125, 6, ItemDye.dyeColors[swappedColour]);
		
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		textBox.updateCursorCounter();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		int texture = mc.renderEngine.getTexture(Reference.textureGuiDirectory + "portalModifier.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);		
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		textBox.drawTextBox();
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		
		Modifier.worldObj.markBlockForRenderUpdate(Modifier.xCoord, Modifier.yCoord, Modifier.zCoord);
	}
}
