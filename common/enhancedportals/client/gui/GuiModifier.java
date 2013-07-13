package enhancedportals.client.gui;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.gui.EnhancedCoreContainer;
import enhancedcore.gui.GuiEnhancedCore;
import enhancedcore.gui.GuiItemStackButton;
import enhancedcore.util.Properties;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Strings;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.network.packet.PacketRedstoneControl;
import enhancedportals.network.packet.PacketThickness;
import enhancedportals.network.packet.PacketUpgrade;
import enhancedportals.portal.upgrades.Upgrade;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class GuiModifier extends GuiEnhancedCore
{
	private class RedstoneControlLedger extends Ledger
	{
		int headerColour = 0xe1c92f;
		int subheaderColour = 0xaaafb8;
		int textColour = 0x000000;
		int selected = 0, selected2 = 0;
		
		TileEntityPortalModifier modifier;
		
		public RedstoneControlLedger(TileEntityPortalModifier theModifier)
		{
			modifier = theModifier;
			overlayColor = 0xd46c1f;
			maxHeight = 47;
		}

		@Override
		public void draw(int x, int y)
		{
			drawBackground(x, y);
			
			Properties.bindTexture(FMLClientHandler.instance().getClient().renderEngine, 1);
			drawIcon(new ItemStack(Item.redstone).getIconIndex(), x + 3, y + 3);
			
			if (!isFullyOpened())
			{
				return;
			}
			
			int y2 = y + 22, xLoc = x + (maxWidth / 2) - (22 * 4 / 2);
			fontRenderer.drawStringWithShadow("Redstone Control", x + 22, y + 8, headerColour);
			Properties.bindTexture(FMLClientHandler.instance().getClient().renderEngine, "epcore", "textures/gui/buttons.png");
			GL11.glColor3f(1F, 1F, 1F);
			
            int setting = modifier.redstoneSetting;
			
			for (int i = 0; i < 4; i++)
			{
				int x2 = xLoc + 3 + 22 * i;
				boolean isActive = false;
				
				if (i == 0)
				{
				    isActive = setting == 0;
				    selected = isActive ? 0 : selected;
				}
				else if (i == 1)
				{
				    isActive = setting == 1;
				    selected = isActive ? 1 : selected;
				}
				else if (i == 2)
				{
				    isActive = setting >= 2;
				    selected2 = isActive ? setting - 2 : 0;
				    selected = isActive ? 2 : selected;
				}
				else if (i == 3)
				{
				    isActive = setting == -1;
				    selected = isActive ? 3 : selected;
				}
				
				drawTexturedModalRect(x2, y2, 0, isActive ? 40 : 20, 10, 10);
				drawTexturedModalRect(x2 + 10, y2, 190, isActive ? 40 : 20, 10, 10);
				drawTexturedModalRect(x2, y2 + 10, 0, isActive ? 50 : 30, 10, 10);
				drawTexturedModalRect(x2 + 10, y2 + 10, 190, isActive ? 50 : 30, 10, 10);
			}
			
			Properties.bindTexture(FMLClientHandler.instance().getClient().renderEngine, 0);
			drawIcon(new ItemStack(Block.torchRedstoneActive).getIconIndex(), xLoc + 5, y + 22);
			drawIcon(new ItemStack(Block.torchRedstoneIdle).getIconIndex(), xLoc + 27, y + 22);
			
			Properties.bindTexture(FMLClientHandler.instance().getClient().renderEngine, 1);
			drawIcon(new ItemStack(Item.redstone).getIconIndex(), xLoc + 49, y + 24);
			drawIcon(new ItemStack(Item.gunpowder).getIconIndex(), xLoc + 71, y + 24);
			
			if (selected2 != 0)
			{
				fontRenderer.drawStringWithShadow("" + selected2, xLoc + 49 + 17 - fontRenderer.getStringWidth("" + selected2), y + 33, 0xFFFFFFFF);
			}
		}
				
		@Override
		public boolean handleMouseClicked(int x, int y, int mouseButton)
		{
			int xLoc = currentShiftX + (maxWidth / 2) - (22 * 4 / 2), y2 = currentShiftY + 22;
			boolean changed = false;
			
			for (int i = 0; i < 4; i++)
			{
				int x2 = xLoc + 3 + 22 * i;
				
				if (x >= x2 && x <= x2 + 19 && y >= y2 && y <= y2 + 19)
				{
					if (i == 2 && selected == 2)
					{
						if (mouseButton == 0)
						{
							selected2++;
						}
						else
						{
							selected2--;
						}
						
						if (selected2 > 15)
						{
							selected2 = 1;
						}
						else if (selected2 < 1)
						{
							selected2 = 15;
						}
						
						changed = true;
					}
					else if (mouseButton == 0 && selected != 2 && i == 2)
					{
						selected2 = 1;
						selected = i;
						changed = true;
					}
					else if (mouseButton == 0)
					{
						selected = i;
						changed = true;
					}
					
					if (changed)
					{
					    int num = selected;
					    
					    if (selected == 3)
					    {
					        num = -1;
					    }
					    else if (selected == 2)
					    {
					        num = 2 + selected2;
					    }
					    
					    modifier.redstoneSetting = (byte) num;
					    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketRedstoneControl(modifier)));
					    mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					}
					
					return true;
				}
			}
						
			return false;
		}
		
		@Override
		public ArrayList<String> getTooltip()
		{
			ArrayList<String> strList = new ArrayList<String>();
			
			if (!isOpen())
			{
			    int num = modifier.redstoneSetting;
			    
				strList.add("Redstone Control");
				strList.add(EnumChatFormatting.GRAY + (num == 0 ? "Normal" : num == 1 ? "Inverted" : num >= 2 ? "Analogue (" + (num - 2) + ")" : num == -1 ? "Disabled" : "Unknown"));
			}
			
			return strList;
		}
	}
	
	private class TipLedger extends Ledger
	{
		int headerColour = 0xe1c92f;
		int subheaderColour = 0xaaafb8;
		int textColour = 0x000000;
		int tip = -1;
		
		String[][] tipsList = new String[][]
			{
				{ "You can camouflage", "the portal with any", "block or fluid" },
				{ "You can control your", "portals with Redstone", "or ComputerCraft" },
				{ "The portal modifier", "does not have to be", "inside a portal frame" }
			};
		
		public TipLedger()
		{			
			overlayColor = 0x5396da;			
		}
		
		@Override
		public boolean handleMouseClicked(int x, int y, int mouseButton)
		{
			if (!isOpen())
			{
				tip++;
				
				if (tip >= tipsList.length)
				{
					tip = 0;
				}
				
				maxHeight = 25 + tipsList[tip].length * 10;
			}
			
			return super.handleMouseClicked(x, y, mouseButton);
		}
		
		@Override
		public void draw(int x, int y)
		{
			drawBackground(x, y);
			
			if (!isFullyOpened())
			{
				return;
			}
			
			fontRenderer.drawStringWithShadow("Did you know...", x + 22, y + 8, headerColour);
			
			if (tip == -1)
			{
			    tip = 0;
			    maxHeight = 25 + tipsList[tip].length * 10;
			}
			
			for (int i = 0; i < tipsList[tip].length; i++)
			{
				fontRenderer.drawString(tipsList[tip][i], x + 5, y + 20 + (i * 10), textColour);
			}
		}

		@Override
		public ArrayList<String> getTooltip()
		{
			ArrayList<String> strList = new ArrayList<String>();
			
			if (!isOpen())
			{
				strList.add("Did you know...");			
			}
			
			return strList;
		}		
	}

	TileEntityPortalModifier modifier;
	
	public GuiModifier(EnhancedCoreContainer container, IInventory inventory)
	{
		super(container, inventory);
		
		modifier = (TileEntityPortalModifier) inventory;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		ArrayList<String> strList = new ArrayList<String>();
		strList.add(EnumChatFormatting.WHITE + "Thickness");
		strList.add(EnumChatFormatting.GRAY + "Normal");
		
		buttonList.add(new GuiItemStackButton(1, guiLeft + 134, guiTop + 18, new ItemStack(Block.portal, 1), true, strList, "" + modifier.thickness, true));
		
		for (int i = 0; i < modifier.upgradeHandler.getInstalledUpgrades().length; i++)
		{
			Upgrade u = modifier.upgradeHandler.getUpgrade(i);			
			buttonList.add(new GuiItemStackButton(100 + i, guiLeft + 8 + (18 * i), guiTop + 18, u.getDisplayItemStack(), true, u.getText(true), true));
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button.id == 1)
		{
			GuiItemStackButton btn = (GuiItemStackButton) button;
			int num = Integer.parseInt(btn.displayString) + 1;
			
			if (num >= 4)
			{
				num = 0;
			}
			
			modifier.thickness = (byte) num;
			PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketThickness(modifier)));			
		}
		else if (button.id >= 100)
		{
		    int size = modifier.upgradeHandler.getUpgrades().size();
		    Upgrade upgrade = modifier.upgradeHandler.getUpgrade(MathHelper.clamp_int(button.id - 100, 0, size > 0 ? size - 1 : size));
		    
		    if (upgrade != null)
		    {
		        PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketUpgrade(modifier, (byte) 1, upgrade.getUpgradeID())));
		        buttonList.remove(button);
		        modifier.upgradeHandler.removeUpgrade(upgrade, modifier);
		    }
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		Properties.bindTexture(mc.renderEngine, "ep2", "textures/gui/portalModifier.png");
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
		
		if (modifier.getStackInSlot(0) == null)
		{
			// Render default portal if no item in slot
			itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(Block.portal), guiLeft + 152, guiTop + 18);
		}
		
		boolean draw = modifier.isRemotelyControlled() ? modifier.dialDeviceNetwork.equals("") : modifier.modifierNetwork.equals("");

        if (draw)
        {
        	// Draw error/message
            String str = modifier.isRemotelyControlled() ? Strings.ClickToSetIdentifier.toString() : Strings.ClickToSetNetwork.toString();

            if (!EnhancedPortals.proxy.isIdentifierTaken)
            {
                fontRenderer.drawStringWithShadow(str, guiLeft + xSize / 2 - fontRenderer.getStringWidth(str) / 2, guiTop + 54, 0xFF00FF00);
            }
            else
            {
                fontRenderer.drawStringWithShadow(Strings.IdentifierInUse.toString(), guiLeft + xSize / 2 - fontRenderer.getStringWidth(Strings.IdentifierInUse.toString()) / 2, guiTop + 54, 0xEE0000);
            }
        }
        else
        {
        	// Render network icons
        	String network = modifier.isRemotelyControlled() ? modifier.dialDeviceNetwork : modifier.modifierNetwork;

            if (!network.equals(""))
            {
                String[] split = network.split(Reference.glyphSeperator);
                int theSize = guiLeft + (xSize / 2) - ((split.length * 18) / 2) + 1;

                for (int i1 = 0; i1 < split.length; i1++)
                {
                    for (int j1 = 0; j1 < Reference.glyphItems.size(); j1++)
                    {
                        if (Reference.glyphItems.get(j1).getItemName().replace("item.", "").equalsIgnoreCase(split[i1]))
                        {
                        	ItemStack stack = Reference.glyphItems.get(j1);
                        	Icon icon = stack.getIconIndex();
                        	
                        	Properties.bindTexture(mc.renderEngine, stack.getItem().getSpriteNumber());
                        	itemRenderer.renderIcon(theSize + i1 * 18, guiTop + 50, icon, 16, 16);
                        }
                    }
                }
            }
        }
	}
	
	@Override
	public void updateScreen()
	{
	    super.updateScreen();
	    
	    ((GuiItemStackButton) buttonList.get(0)).displayString = "" + modifier.thickness;
	    ((GuiItemStackButton) buttonList.get(0)).hoverText.set(1, EnumChatFormatting.GRAY + (modifier.thickness == 0 ? "Normal" : modifier.thickness == 1 ? "Thick" : modifier.thickness == 2 ? "Thicker" : "Full Block"));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRenderer.drawStringWithShadow("Portal Modifier", xSize / 2 - fontRenderer.getStringWidth("Portal Modifier") / 2, -12, 0xFFFFFFFF);
		
		fontRenderer.drawString("Upgrades", 8, 6, 0xFF444444);
		fontRenderer.drawString("Modifications", xSize - 6 - fontRenderer.getStringWidth("Modifications"), 6, 0xFF444444);
		fontRenderer.drawString("Network", 8, 38, 0xFF444444);
		fontRenderer.drawString("Inventory", 8, 70, 0xFF444444);
		
		super.drawGuiContainerForegroundLayer(par1, par2);		
	}
	
	@Override
	protected void initLedgers(IInventory inventory)
	{
		super.initLedgers(inventory);
		
		ledgerManager.add(new TipLedger());
		ledgerManager.add(new RedstoneControlLedger((TileEntityPortalModifier) inventory));
	}
	
	@Override
	protected void mouseClicked(int x, int y, int mouseButton)
	{
		super.mouseClicked(x, y, mouseButton);
		
		if (x >= guiLeft + 8 && x <= width - guiLeft - 10 && y >= guiTop + 49 && y <= guiTop + 65 && mouseButton == 0)
		{
		    mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketGui(modifier, GuiIds.PortalModifierNetwork)));
		}
	}
}
