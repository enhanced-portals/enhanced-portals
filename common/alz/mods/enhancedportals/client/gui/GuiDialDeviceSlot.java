package alz.mods.enhancedportals.client.gui;

import alz.mods.enhancedportals.portals.PortalData;
import alz.mods.enhancedportals.reference.Localizations;
import alz.mods.enhancedportals.reference.Strings;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

public class GuiDialDeviceSlot extends GuiSlot
{
	GuiDialDevice parentGui;
	Minecraft mc;
	FontRenderer fontRenderer;

	public GuiDialDeviceSlot(GuiDialDevice parentGui)
	{
		super(FMLClientHandler.instance().getClient(), parentGui.width, parentGui.height, 32, parentGui.height - 64, 36);
		this.parentGui = parentGui;
		
		mc = FMLClientHandler.instance().getClient();
		fontRenderer = mc.fontRenderer;
	}
	
	public GuiDialDeviceSlot()
	{		
		super(FMLClientHandler.instance().getClient(), FMLClientHandler.instance().getClient().currentScreen.width, FMLClientHandler.instance().getClient().currentScreen.height, 32, FMLClientHandler.instance().getClient().currentScreen.height - 64, 36);
		
		mc = FMLClientHandler.instance().getClient();
		fontRenderer = mc.fontRenderer;
	}

	@Override
	protected int getSize()
	{
		if (parentGui == null)
		{
			return 0;
		}
		
		return parentGui.getSize().size();
	}

	@Override
	protected void elementClicked(int i, boolean flag)
	{
		if (parentGui == null)
		{
			return;
		}
		
		parentGui.onElementSelected(i);
		
        if (flag)
        {
            parentGui.selectWorld(i);
        }
	}

	@Override
	protected boolean isSelected(int i)
	{
		if (parentGui == null)
		{
			return false;
		}
		
		return i == parentGui.getSelectedWorld();
	}

	@Override
	protected void drawBackground()
	{
		if (parentGui == null)
		{
			return;
		}
		
		parentGui.drawDefaultBackground();
	}

	@Override
	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator)
	{
		if (parentGui == null)
		{
			return;
		}
		
		PortalData currentEntry = parentGui.getSize().get(i);
		String doubleClick = Localizations.getLocalizedString(Strings.GUI_DialDevice_NotSelected), selected = Localizations.getLocalizedString(Strings.GUI_DialDevice_Selected);
		
		int mainColour = 16777215;
		boolean showString = false;
		String displayName = currentEntry.DisplayName;
		
		if (displayName.length() > 35)
			displayName = displayName.substring(0, 35);
		
		if (parentGui.getActive() == i)
			mainColour = 56162;
		else if (i != parentGui.getActive() && i == parentGui.getSelectedWorld())
			showString = true;
		
        parentGui.drawString(fontRenderer, displayName, j + 2, k + 1, mainColour);
        parentGui.drawString(fontRenderer, String.format(Localizations.getLocalizedString(Strings.GUI_DialDevice_Coords), currentEntry.TeleportData.getDimensionAsString(), currentEntry.TeleportData.getX(), currentEntry.TeleportData.getY(), currentEntry.TeleportData.getZ()), j + 2, k + 12, 8421504);
        parentGui.drawString(fontRenderer, currentEntry.GetTextureAsString(), j + 2, k + 12 + 10, 8421504);
                
        if (showString)
        {
        	parentGui.drawString(fontRenderer, doubleClick, (j + 215) - fontRenderer.getStringWidth(doubleClick), k + 22, 8421504 / 2);
        }
        else if (!showString && parentGui.getSelectedWorld() == i)
        {
        	parentGui.drawString(fontRenderer, selected, (j + 215) - fontRenderer.getStringWidth(selected), k + 22, 8421504 / 2);
        }
	}
}
