package alz.mods.enhancedportals.client.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import alz.mods.enhancedportals.client.ClientProxy;
import alz.mods.enhancedportals.common.ContainerDialDeviceAdd;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityDialDevice;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;

public class GuiDialDeviceAdd extends GuiContainer
{
	private TileEntityDialDevice DialDevice;
	private EntityPlayer Player;
	
	private GuiImprovedTextBox[] textBoxes;
	private int bump;
	
	public GuiDialDeviceAdd(EntityPlayer player, TileEntityDialDevice dialDevice)
	{
		super(new ContainerDialDeviceAdd(player.inventory, dialDevice));
		Player = player;
		DialDevice = dialDevice;
		textBoxes = new GuiImprovedTextBox[10];
		bump = 15;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		
		textBoxes[0] = new GuiImprovedTextBox(mc.fontRenderer, guiLeft, guiTop, width - guiLeft * 2, 16, 35, false, true, 0, 0);
		textBoxes[1] = new GuiImprovedTextBox(mc.fontRenderer, guiLeft, guiTop + 20, 16, 16, 35, false, false, 0, 20);
		textBoxes[2] = new GuiImprovedTextBox(mc.fontRenderer, width - guiLeft * 2 - 16, guiTop + 20, 16, 16, 35, false, false, width - guiLeft * 2 - 16, 20);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		drawBackground(0);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Reference.File.GuiTextureDirectory + "dialDeviceInventory.png");
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String nameString = "Name", scrollString = "Scroll", modifierString = "Modifier", moanA = "The Teleportation Scroll is invalid", moanB = "An upgrade is required";
		
		//drawString(mc.fontRenderer, nameString, (width / 2) - guiLeft - (mc.fontRenderer.getStringWidth(nameString)) - 80 + bump, 0, 0xFFFFFF);
		drawString(mc.fontRenderer, scrollString, (width / 2) - guiLeft - (mc.fontRenderer.getStringWidth(scrollString)) - 50 + bump, 24, 0xFFFFFF);
		drawString(mc.fontRenderer, modifierString, (width / 2) - guiLeft - (mc.fontRenderer.getStringWidth(modifierString)) + 52 + bump, 24, 0xFFFFFF);

		drawString(mc.fontRenderer, moanA, (width / 2) - guiLeft - (mc.fontRenderer.getStringWidth(moanA) / 2), 50, 0xDD0000);
		drawString(mc.fontRenderer, moanB, (width / 2) - guiLeft - (mc.fontRenderer.getStringWidth(moanB) / 2), 60, 0x990000);
		
		for (GuiImprovedTextBox textBox : textBoxes)
		{
			if (textBox == null)
			{
				continue;
			}
			
			textBox.drawTextBox();
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2)
	{
		for (GuiImprovedTextBox textBox : textBoxes)
		{
			if (textBox == null)
			{
				continue;
			}
			
			if (textBox.isFocused())
			{
				textBox.textboxKeyTyped(par1, par2);
				return;
			}
		}
		
		if (par2 == FMLClientHandler.instance().getClient().gameSettings.keyBindInventory.keyCode || par2 == 1)
		{
			ClientProxy.OpenGuiFromLocal(Player, DialDevice, Reference.GuiIDs.DialDevice);
			return;
		}
		
		super.keyTyped(par1, par2);
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		
		for (GuiImprovedTextBox textBox : textBoxes)
		{
			if (textBox == null)
			{
				continue;
			}
			
			textBox.mouseClicked(par1, par2, par3);
		}
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		
		for (GuiImprovedTextBox textBox : textBoxes)
		{
			if (textBox == null)
			{
				continue;
			}
			
			textBox.updateCursorCounter();
		}
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		
		//RenderHelper.enableGUIStandardItemLighting();
		
		
	}	
}
