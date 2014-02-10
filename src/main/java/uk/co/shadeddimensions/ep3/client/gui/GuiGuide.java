package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.block.BlockDecoration;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.block.BlockStabilizer;
import uk.co.shadeddimensions.ep3.item.ItemEntityCard;
import uk.co.shadeddimensions.ep3.item.ItemGoggles;
import uk.co.shadeddimensions.ep3.item.ItemHandheldScanner;
import uk.co.shadeddimensions.ep3.item.ItemLocationCard;
import uk.co.shadeddimensions.ep3.item.ItemMisc;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.item.ItemPortalModule;
import uk.co.shadeddimensions.ep3.item.ItemUpgrade;
import uk.co.shadeddimensions.ep3.item.ItemWrench;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.library.gui.GuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementCrafting;
import uk.co.shadeddimensions.library.gui.element.ElementScrollBar;
import uk.co.shadeddimensions.library.gui.element.ElementScrollPanel;
import uk.co.shadeddimensions.library.gui.element.ElementText;
import uk.co.shadeddimensions.library.gui.element.ElementTextBox;
import uk.co.shadeddimensions.library.gui.tab.TabBase;
import uk.co.shadeddimensions.library.gui.tab.TabToggleButton;

public class GuiGuide extends GuiBase
{
	ElementScrollPanel scrollPanel;

	public GuiGuide()
	{
		super(new ResourceLocation("enhancedportals", "textures/gui/guide.png"));
		xSize = 256;
		ySize = 240;
	}

	@Override
	public void drawBackgroundTexture()
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);

		int bgHeight = 0;
		while (bgHeight < height)
		{
			drawTexturedModalRect(guiLeft, bgHeight, 0, 15, xSize, 206);
			bgHeight += 206;
		}
	}

	@Override
	public void drawGuiBackgroundLayer(float f, int mouseX, int mouseY)
	{
		super.drawGuiBackgroundLayer(f, mouseX, mouseY);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiLeft, 0, 0, 0, xSize, 7);
		drawTexturedModalRect(guiLeft, height - 10, 0, 229, xSize, 10);
	}

	@Override
	public void addElements()
	{
		scrollPanel = new ElementScrollPanel(this, 11, -guiTop + 7, xSize - 22, height - 17);
		addElement(scrollPanel);
		addElement(new ElementScrollBar(this, xSize - 9, -guiTop + 7, 5, height - 16, scrollPanel));
		updateScrollPanel();
	}

	@Override
	public void addTabs()
	{
		addTab(new TabToggleButton(this, "mainMain", "Main", null));
		addTab(new TabToggleButton(this, "mainBlocks", "Blocks", new ItemStack(BlockFrame.instance)));
		addTab(new TabToggleButton(this, "mainItems", "Items", new ItemStack(ItemGoggles.instance)));

		if (ClientProxy.manualPage.startsWith("block"))
		{
			tabs.get(1).setFullyOpen();
			addTabsForPage("mainBlocks");
		}
		else if (ClientProxy.manualPage.startsWith("item"))
		{
			tabs.get(2).setFullyOpen();
			addTabsForPage("mainItems");
		}

		for (TabBase tab : tabs)
		{
			TabToggleButton t = (TabToggleButton) tab;

			if (t.ID.equals(ClientProxy.manualPage))
			{
				t.setFullyOpen();
			}
		}

		handleElementButtonClick(ClientProxy.manualPage, 1);
	}

	void addTabsForPage(String page)
	{
		if (page.startsWith("main"))
		{
			for (int i = tabs.size() - 1; i > 2; i--)
			{
				tabs.remove(i);
			}
		}
		
		if (page.equals("mainBlocks"))
		{
			addTab(new TabToggleButton(this, 1, "blockFrame", Localization.getGuiString("blockFrame"), new ItemStack(BlockFrame.instance)));
			addTab(new TabToggleButton(this, 1, "blockController", Localization.getGuiString("blockController"), new ItemStack(BlockFrame.instance, 1, 1)));
			addTab(new TabToggleButton(this, 1, "blockRedstoneInterface", Localization.getGuiString("blockRedstoneInterface"), new ItemStack(BlockFrame.instance, 1, 2)));
			addTab(new TabToggleButton(this, 1, "blockNetworkInterface", Localization.getGuiString("blockNetworkInterface"), new ItemStack(BlockFrame.instance, 1, 3)));
			addTab(new TabToggleButton(this, 1, "blockDiallingDevice", Localization.getGuiString("blockDiallingDevice"), new ItemStack(BlockFrame.instance, 1, 4)));
			addTab(new TabToggleButton(this, 1, "blockBiometric", Localization.getGuiString("blockBiometric"), new ItemStack(BlockFrame.instance, 1, 5)));
			addTab(new TabToggleButton(this, 1, "blockModule", Localization.getGuiString("blockModule"), new ItemStack(BlockFrame.instance, 1, 6)));
			addTab(new TabToggleButton(this, 1, "blockDbs", Localization.getGuiString("blockDBS"), new ItemStack(BlockStabilizer.instance)));
			addTab(new TabToggleButton(this, 1, "blockDecoration", Localization.getGuiString("blockDecoration"), new ItemStack(BlockDecoration.instance)));
		}
		else if (page.equals("mainItems"))
		{
			addTab(new TabToggleButton(this, 1, "itemGlasses", Localization.getGuiString("itemGlasses"), new ItemStack(ItemGoggles.instance)));
			addTab(new TabToggleButton(this, 1, "itemWrench", Localization.getGuiString("itemWrench"), new ItemStack(ItemWrench.instance)));
			addTab(new TabToggleButton(this, 1, "itemNanobrush", Localization.getGuiString("itemNanobrush"), new ItemStack(ItemPaintbrush.instance)));
			addTab(new TabToggleButton(this, 1, "itemLocationCard", Localization.getGuiString("itemLocationCard"), new ItemStack(ItemLocationCard.instance)));
			addTab(new TabToggleButton(this, 1, "itemIdCard", Localization.getGuiString("itemIdCard"), new ItemStack(ItemEntityCard.instance)));
			addTab(new TabToggleButton(this, 1, "itemScanner", Localization.getGuiString("itemScanner"), new ItemStack(ItemHandheldScanner.instance)));
			addTab(new TabToggleButton(this, 1, "itemPortalModules", Localization.getGuiString("itemPortalModules"), new ItemStack(ItemMisc.instance, 1, 0)));
			addTab(new TabToggleButton(this, 1, "itemUpgrades", Localization.getGuiString("itemUpgrades"), new ItemStack(ItemMisc.instance, 1, 1)));
		}
	}

	@Override
	public void handleElementButtonClick(String buttonName, int mouseButton)
	{
		ClientProxy.manualPage = buttonName;
		updateScrollPanel();
		addTabsForPage(ClientProxy.manualPage);
	}

	void updateScrollPanel()
	{
		scrollPanel.clear();

		if (ClientProxy.manualPage == null)
		{
			return;
		}

		scrollPanel.addElement(new ElementText(this, scrollPanel.getWidth() / 2 - getFontRenderer().getStringWidth(Localization.getGuiString(ClientProxy.manualPage)) / 2, 5, Localization.getGuiString(ClientProxy.manualPage), null, 0xFFFF00, true));
		int offset = 0;

		if (ClientProxy.manualPage.startsWith("block") || ClientProxy.manualPage.startsWith("item"))
		{
			scrollPanel.addElement(new ElementText(this, 5, 18, Localization.getGuiString("crafting"), null, 0x44AAFF, true));
			ElementCrafting craft = new ElementCrafting(this, scrollPanel.getWidth() / 2 - 58, 30, 0);

			if (ClientProxy.manualPage.equals("blockFrame"))
			{
				craft.addOutputSlot(new ItemStack(BlockFrame.instance));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(Block.stone), new ItemStack(Item.ingotIron), new ItemStack(Block.stone), new ItemStack(Item.ingotIron), new ItemStack(Block.blockNetherQuartz), new ItemStack(Item.ingotIron), new ItemStack(Block.stone), new ItemStack(Item.ingotIron), new ItemStack(Block.stone) });
			}
			else if (ClientProxy.manualPage.equals("blockController"))
			{
				craft.addOutputSlot(new ItemStack(BlockFrame.instance, 0, 1));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(BlockFrame.instance), new ItemStack(Item.diamond) });
			}
			else if (ClientProxy.manualPage.equals("blockRedstoneInterface"))
			{
				craft.addOutputSlot(new ItemStack(BlockFrame.instance, 0, 2));
				craft.addAllGridSlots(new ItemStack[] { null, new ItemStack(Item.redstone), null, new ItemStack(Item.redstone), new ItemStack(BlockFrame.instance), new ItemStack(Item.redstone), null, new ItemStack(Item.redstone), null });
			}
			else if (ClientProxy.manualPage.equals("blockNetworkInterface"))
			{
				craft.addOutputSlot(new ItemStack(BlockFrame.instance, 0, 3));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(BlockFrame.instance), new ItemStack(Item.enderPearl) });
			}
			else if (ClientProxy.manualPage.equals("blockDiallingDevice"))
			{
				craft.addOutputSlot(new ItemStack(BlockFrame.instance, 0, 4));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(BlockFrame.instance, 0, 3), new ItemStack(Item.diamond) });
			}
			else if (ClientProxy.manualPage.equals("blockBiometric"))
			{
				craft.addOutputSlot(new ItemStack(BlockFrame.instance, 0, 5));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.porkCooked), new ItemStack(Item.beefCooked), new ItemStack(Item.chickenCooked), new ItemStack(Item.blazePowder), new ItemStack(BlockFrame.instance), new ItemStack(Item.blazePowder) });

				ElementCrafting craft2 = new ElementCrafting(this, scrollPanel.getWidth() / 2 - 58, 86, 0);
				craft2.addOutputSlot(new ItemStack(BlockFrame.instance, 0, 5));
				craft2.addAllGridSlots(new ItemStack[] { new ItemStack(Item.porkRaw), new ItemStack(Item.beefRaw), new ItemStack(Item.chickenRaw), new ItemStack(Item.blazePowder), new ItemStack(BlockFrame.instance), new ItemStack(Item.blazePowder) });
				scrollPanel.addElement(craft2);
				offset += craft2.getHeight();
			}
			else if (ClientProxy.manualPage.equals("blockModule"))
			{
				craft.addOutputSlot(new ItemStack(BlockFrame.instance, 0, 6));
				craft.addAllGridSlots(new ItemStack[] { null, new ItemStack(ItemMisc.instance), null, new ItemStack(Item.emerald), new ItemStack(BlockFrame.instance), new ItemStack(Item.diamond) });
			}
			else if (ClientProxy.manualPage.equals("blockDbs"))
			{
				craft.addOutputSlot(new ItemStack(BlockStabilizer.instance, 6));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(Block.blockIron), new ItemStack(Item.enderPearl), new ItemStack(Block.blockIron), new ItemStack(Item.enderPearl), new ItemStack(Item.diamond), new ItemStack(Item.enderPearl), new ItemStack(Block.blockIron), new ItemStack(Item.enderPearl), new ItemStack(Block.blockIron) });
			}
			else if (ClientProxy.manualPage.equals("blockDecoration"))
			{
				craft.addOutputSlot(new ItemStack(BlockDecoration.instance, 8, 0));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(Block.stone), new ItemStack(Block.blockNetherQuartz), new ItemStack(Block.stone), new ItemStack(Block.blockNetherQuartz), new ItemStack(Block.blockNetherQuartz), new ItemStack(Block.blockNetherQuartz), new ItemStack(Block.stone), new ItemStack(Block.blockNetherQuartz), new ItemStack(Block.stone) });

				ElementCrafting craft2 = new ElementCrafting(this, scrollPanel.getWidth() / 2 - 58, 86, 0);
				craft2.addOutputSlot(new ItemStack(BlockDecoration.instance, 10, 1));
				craft2.addAllGridSlots(new ItemStack[] { null, new ItemStack(Item.ingotIron), null, new ItemStack(Item.ingotIron), new ItemStack(Block.blockIron), new ItemStack(Item.ingotIron), null, new ItemStack(Item.ingotIron), null });
				scrollPanel.addElement(craft2);
				offset += craft2.getHeight();
			}
			else if (ClientProxy.manualPage.equals("itemGlasses"))
			{
				craft.addOutputSlot(new ItemStack(ItemGoggles.instance));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.dyePowder, 0, 1), null, new ItemStack(Item.dyePowder, 0, 6), new ItemStack(Block.thinGlass), new ItemStack(Item.leather), new ItemStack(Block.thinGlass), new ItemStack(Item.leather), null, new ItemStack(Item.leather) });
			}
			else if (ClientProxy.manualPage.equals("itemWrench"))
			{
				craft.addOutputSlot(new ItemStack(ItemWrench.instance));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.ingotIron), null, new ItemStack(Item.ingotIron), null, new ItemStack(Item.netherQuartz), null, null, new ItemStack(Item.ingotIron) });
			}
			else if (ClientProxy.manualPage.equals("itemNanobrush"))
			{
				craft.addOutputSlot(new ItemStack(ItemPaintbrush.instance));
				craft.addAllGridSlots(new ItemStack[] { null, new ItemStack(Item.silk), new ItemStack(Block.cloth), null, new ItemStack(Item.stick), new ItemStack(Item.silk), new ItemStack(Item.stick) });
			}
			else if (ClientProxy.manualPage.equals("itemLocationCard"))
			{
				craft.addOutputSlot(new ItemStack(ItemLocationCard.instance, 16));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.ingotIron), new ItemStack(Item.paper), new ItemStack(Item.ingotIron), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.ingotIron), new ItemStack(Item.dyePowder, 0, 4), new ItemStack(Item.ingotIron) });
			}
			else if (ClientProxy.manualPage.equals("itemIdCard"))
			{
				craft.addOutputSlot(new ItemStack(ItemEntityCard.instance, 8));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.ingotGold), new ItemStack(Item.paper), new ItemStack(Item.ingotGold), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.ingotGold), new ItemStack(Item.dyePowder, 0, 10), new ItemStack(Item.ingotGold) });
			}
			else if (ClientProxy.manualPage.equals("itemScanner"))
			{
				craft.addOutputSlot(new ItemStack(ItemHandheldScanner.instance));
				craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.ingotGold), new ItemStack(Item.redstone), new ItemStack(Item.ingotGold), new ItemStack(Item.ingotIron), new ItemStack(Item.netherQuartz), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron), new ItemStack(ItemEntityCard.instance), new ItemStack(Item.ingotIron) });
			}
			else if (ClientProxy.manualPage.equals("itemPortalModules"))
			{
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30, 0).addOutputSlot(new ItemStack(ItemMisc.instance)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget), new ItemStack(Item.ingotIron), new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget) }));
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 57, 0).addOutputSlot(new ItemStack(ItemPortalModule.instance)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.redstone), new ItemStack(ItemMisc.instance), new ItemStack(Item.gunpowder) }));
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 114, 0).addOutputSlot(new ItemStack(ItemPortalModule.instance, 0, 1)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.dyePowder, 0, 1), new ItemStack(Item.dyePowder, 0, 2), new ItemStack(Item.dyePowder, 0, 4), null, new ItemStack(ItemMisc.instance), null, new ItemStack(Item.dyePowder, 0, 4), new ItemStack(Item.dyePowder, 0, 2), new ItemStack(Item.dyePowder, 0, 1) }));
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 171, 0).addOutputSlot(new ItemStack(ItemPortalModule.instance, 0, 2)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.redstone), new ItemStack(ItemMisc.instance), new ItemStack(Block.music) }));
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 228, 0).addOutputSlot(new ItemStack(ItemPortalModule.instance, 0, 3)).addAllGridSlots(new ItemStack[] { new ItemStack(Block.anvil), new ItemStack(ItemMisc.instance), new ItemStack(Item.feather) }));
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 285, 0).addOutputSlot(new ItemStack(ItemPortalModule.instance, 0, 5)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.dyePowder, 0, 15), new ItemStack(ItemMisc.instance), new ItemStack(Item.dyePowder) }));
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 342, 0).addOutputSlot(new ItemStack(ItemPortalModule.instance, 0, 7)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.feather), new ItemStack(Item.feather), new ItemStack(Item.feather), new ItemStack(Item.feather), new ItemStack(ItemMisc.instance), new ItemStack(Item.feather), new ItemStack(Item.feather), new ItemStack(Item.feather), new ItemStack(Item.feather) }));

				craft = null;
				offset += 57 * 6;
			}
			else
			{
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30, 0).addOutputSlot(new ItemStack(ItemMisc.instance, 1, 1)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.diamond), null, null, new ItemStack(Item.paper), null, null, new ItemStack(Item.dyePowder, 0, 1) }));
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 57, 0).addOutputSlot(new ItemStack(ItemUpgrade.instance)).addAllGridSlots(new ItemStack[] { null, new ItemStack(Item.redstone), null, new ItemStack(Item.redstone), new ItemStack(ItemMisc.instance, 1, 1), new ItemStack(Item.redstone), null, new ItemStack(Item.redstone) }));
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 114, 0).addOutputSlot(new ItemStack(ItemUpgrade.instance, 1, 1)).addAllGridSlots(new ItemStack[] { new ItemStack(ItemMisc.instance, 1, 1), new ItemStack(Item.enderPearl) }));
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 171, 0).addOutputSlot(new ItemStack(ItemUpgrade.instance, 1, 2)).addAllGridSlots(new ItemStack[] { new ItemStack(ItemUpgrade.instance, 1, 1), new ItemStack(Item.diamond) }));
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 228, 0).addOutputSlot(new ItemStack(ItemUpgrade.instance, 1, 3)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.porkCooked), new ItemStack(Item.beefCooked), new ItemStack(Item.chickenCooked), new ItemStack(Item.blazePowder), new ItemStack(ItemMisc.instance, 1, 1), new ItemStack(Item.blazePowder) }));
				scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 285, 0).addOutputSlot(new ItemStack(ItemUpgrade.instance, 1, 4)).addAllGridSlots(new ItemStack[] { null, new ItemStack(ItemMisc.instance), null, new ItemStack(Item.emerald), new ItemStack(ItemMisc.instance, 1, 1), new ItemStack(Item.diamond) }));

				craft = null;
				offset += 57 * (BlockFrame.FRAME_TYPES - 2);
			}

			if (craft != null)
			{
				scrollPanel.addElement(craft);
			}

			scrollPanel.addElement(new ElementText(this, 5, 90 + offset, Localization.getGuiString("information"), null, 0x44AAFF, true));
			scrollPanel.addElement(new ElementTextBox(this, 8, 103 + offset, Localization.getGuiString(ClientProxy.manualPage + ".information"), scrollPanel.getWidth() - 14, 0xFFFFFF, false));
		}
		else
		{
			scrollPanel.addElement(new ElementTextBox(this, 8, 18, Localization.getGuiString(ClientProxy.manualPage + ".information"), scrollPanel.getWidth() - 14, 0xFFFFFF, false));
		}
	}
}
