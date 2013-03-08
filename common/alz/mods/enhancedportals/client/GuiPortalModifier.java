package alz.mods.enhancedportals.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import alz.mods.enhancedportals.common.ContainerPortalModifier;
import alz.mods.enhancedportals.common.TileEntityPortalModifier;
import alz.mods.enhancedportals.reference.IO;
import alz.mods.enhancedportals.reference.Language;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GuiPortalModifier extends GuiContainer
{
	GuiTextField textBox;
	GuiButton button, button2;
	public TileEntityPortalModifier Modifier;
		
	public GuiPortalModifier(InventoryPlayer player, TileEntityPortalModifier modifier)
	{
		super(new ContainerPortalModifier(player, modifier));
		Modifier = modifier;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		
		textBox = new GuiIntegerBox(fontRenderer, (((this.width - 20) / 2) - (75 / 2)), this.guiTop + 42, 75, 16);
		textBox.setFocused(true);
		textBox.setText("" + Modifier.Frequency);
		
		button = new GuiButton(1, (((this.width - 20) / 2) - (40 / 2)) + 61, this.guiTop + 40, 20, 20, "§2S");
		button2 = new GuiButton(2, (((this.width - 20) / 2) - (40 / 2)) - 40, this.guiTop + 40, 20, 20, "§cC");
		
		button.enabled = false;
		button2.enabled = Integer.parseInt(textBox.getText()) != 0;
				
		controlList.add(button);
		controlList.add(button2);
	}
	
	protected void actionPerformed(GuiButton button)
	{
		if (!button.enabled)
			return;
		
		if (button.id == 1)
		{
			textBox.setText(String.format("%1s", textBox.getText()).replace(' ', '0'));
			this.button.enabled = false;
			
			Modifier.Frequency = Integer.parseInt(textBox.getText());			
			ClientNetworking.SendBlockUpdate(Modifier.Frequency, -1, Modifier.xCoord, Modifier.yCoord, Modifier.zCoord, Modifier.worldObj.provider.dimensionId);
		}
		else if (button.id == 2)
		{
			textBox.setText("0");
			this.button.enabled = false;
			button2.enabled = false;
			
			Modifier.Frequency = Integer.parseInt(textBox.getText());
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
		
		if (!button.enabled)
			button.enabled = true;
		
		if (textBox.getText() == "0")
			button2.enabled = false;
		else
			button2.enabled = true;
		
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
		fontRenderer.drawString(Language.PortalModifierTitle, 8, 6, 4210752);
						
		fontRenderer.drawString(Language.Frequency, (((this.width - 20) / 2) - (fontRenderer.getStringWidth(Language.Frequency) / 2)) - this.guiLeft, 30, 4210752);
		//fontRenderer.drawString(Language.Upgrades, (this.xSize - 7) - fontRenderer.getStringWidth(Language.Upgrades), 6, 4210752);
		
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
		int texture = mc.renderEngine.getTexture(IO.GuiTextureDirectory + "portalModifier.png");
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
