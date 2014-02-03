package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
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
    String page = "mainMain";

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
        addTab(new TabToggleButton(this, "mainBlocks", "Blocks", new ItemStack(CommonProxy.blockFrame)));
        addTab(new TabToggleButton(this, "mainItems", "Items", new ItemStack(CommonProxy.itemGoggles)));

        if (page.startsWith("block"))
        {
            tabs.get(1).setFullyOpen();
            addTabsForPage("mainBlocks");
        }
        else if (page.startsWith("item"))
        {
            tabs.get(2).setFullyOpen();
            addTabsForPage("mainItems");
        }

        for (TabBase tab : tabs)
        {
            TabToggleButton t = (TabToggleButton) tab;

            if (t.ID.equals(page))
            {
                t.setFullyOpen();
            }
        }

        handleElementButtonClick(page, 1);
    }

    void addTabsForPage(String page)
    {
        if (page.startsWith("main"))
        {
            for (int i = tabs.size() - 1; i > 2; i--)
            {
                tabs.remove(i);
            }

            if (page.equals("mainBlocks"))
            {
                addTab(new TabToggleButton(this, 1, "blockFrame", Localization.getGuiString("blockFrame"), new ItemStack(CommonProxy.blockFrame)));
                addTab(new TabToggleButton(this, 1, "blockController", Localization.getGuiString("blockController"), new ItemStack(CommonProxy.blockFrame, 1, 1)));
                addTab(new TabToggleButton(this, 1, "blockRedstoneInterface", Localization.getGuiString("blockRedstoneInterface"), new ItemStack(CommonProxy.blockFrame, 1, 2)));
                addTab(new TabToggleButton(this, 1, "blockNetworkInterface", Localization.getGuiString("blockNetworkInterface"), new ItemStack(CommonProxy.blockFrame, 1, 3)));
                addTab(new TabToggleButton(this, 1, "blockDiallingDevice", Localization.getGuiString("blockDiallingDevice"), new ItemStack(CommonProxy.blockFrame, 1, 4)));
                addTab(new TabToggleButton(this, 1, "blockBiometric", Localization.getGuiString("blockBiometric"), new ItemStack(CommonProxy.blockFrame, 1, 5)));
                addTab(new TabToggleButton(this, 1, "blockModule", Localization.getGuiString("blockModule"), new ItemStack(CommonProxy.blockFrame, 1, 6)));
                addTab(new TabToggleButton(this, 1, "blockDbs", Localization.getGuiString("blockDBS"), new ItemStack(CommonProxy.blockStabilizer)));
                addTab(new TabToggleButton(this, 1, "blockDecoration", Localization.getGuiString("blockDecoration"), new ItemStack(CommonProxy.blockDecoration)));
            }
            else if (page.equals("mainItems"))
            {
                addTab(new TabToggleButton(this, 1, "itemGlasses", Localization.getGuiString("itemGlasses"), new ItemStack(CommonProxy.itemGoggles)));
                addTab(new TabToggleButton(this, 1, "itemWrench", Localization.getGuiString("itemWrench"), new ItemStack(CommonProxy.itemWrench)));
                addTab(new TabToggleButton(this, 1, "itemNanobrush", Localization.getGuiString("itemNanobrush"), new ItemStack(CommonProxy.itemPaintbrush)));
                addTab(new TabToggleButton(this, 1, "itemLocationCard", Localization.getGuiString("itemLocationCard"), new ItemStack(CommonProxy.itemLocationCard)));
                addTab(new TabToggleButton(this, 1, "itemSynchronizer", Localization.getGuiString("itemSynchronizer"), new ItemStack(CommonProxy.itemSynchronizer)));
                addTab(new TabToggleButton(this, 1, "itemIdCard", Localization.getGuiString("itemIdCard"), new ItemStack(CommonProxy.itemEntityCard)));
                addTab(new TabToggleButton(this, 1, "itemScanner", Localization.getGuiString("itemScanner"), new ItemStack(CommonProxy.itemScanner)));
                addTab(new TabToggleButton(this, 1, "itemPortalModules", Localization.getGuiString("itemPortalModules"), new ItemStack(CommonProxy.itemMisc, 1, 0)));
                addTab(new TabToggleButton(this, 1, "itemUpgrades", Localization.getGuiString("itemUpgrades"), new ItemStack(CommonProxy.itemMisc, 1, 1)));
            }
        }
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        page = buttonName;
        updateScrollPanel();
        addTabsForPage(page);
    }

    void updateScrollPanel()
    {
        scrollPanel.clear();

        if (page == null)
        {
            return;
        }

        scrollPanel.addElement(new ElementText(this, scrollPanel.getWidth() / 2 - getFontRenderer().getStringWidth(Localization.getGuiString(page)) / 2, 5, Localization.getGuiString(page), null, 0xFFFF00, true));
        int offset = 0;
        
        if (page.startsWith("block") || page.startsWith("item"))
        {
            scrollPanel.addElement(new ElementText(this, 5, 18, Localization.getGuiString("crafting"), null, 0x44AAFF, true));
            ElementCrafting craft = new ElementCrafting(this, scrollPanel.getWidth() / 2 - 58, 30, 0);

            if (page.equals("blockFrame"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.blockFrame));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(Block.stone), new ItemStack(Item.ingotIron), new ItemStack(Block.stone), new ItemStack(Item.ingotIron), new ItemStack(Block.blockNetherQuartz), new ItemStack(Item.ingotIron), new ItemStack(Block.stone), new ItemStack(Item.ingotIron), new ItemStack(Block.stone) });
            }
            else if (page.equals("blockController"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.blockFrame, 0, 1));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(CommonProxy.blockFrame), new ItemStack(Item.diamond) });
            }
            else if (page.equals("blockRedstoneInterface"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.blockFrame, 0, 2));
                craft.addAllGridSlots(new ItemStack[] { null, new ItemStack(Item.redstone), null, new ItemStack(Item.redstone), new ItemStack(CommonProxy.blockFrame), new ItemStack(Item.redstone), null, new ItemStack(Item.redstone), null });
            }
            else if (page.equals("blockNetworkInterface"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.blockFrame, 0, 3));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(CommonProxy.blockFrame), new ItemStack(Item.enderPearl) });
            }
            else if (page.equals("blockDiallingDevice"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.blockFrame, 0, 4));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(CommonProxy.blockFrame, 0, 3), new ItemStack(Item.diamond) });
            }
            else if (page.equals("blockBiometric"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.blockFrame, 0, 5));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.porkCooked), new ItemStack(Item.beefCooked), new ItemStack(Item.chickenCooked), new ItemStack(Item.blazePowder), new ItemStack(CommonProxy.blockFrame), new ItemStack(Item.blazePowder) });
                
                ElementCrafting craft2 = new ElementCrafting(this, scrollPanel.getWidth() / 2 - 58, 86, 0);
                craft2.addOutputSlot(new ItemStack(CommonProxy.blockFrame, 0, 5));
                craft2.addAllGridSlots(new ItemStack[] { new ItemStack(Item.porkRaw), new ItemStack(Item.beefRaw), new ItemStack(Item.chickenRaw), new ItemStack(Item.blazePowder), new ItemStack(CommonProxy.blockFrame), new ItemStack(Item.blazePowder) });
                scrollPanel.addElement(craft2);
                offset += craft2.getHeight();
            }
            else if (page.equals("blockModule"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.blockFrame, 0, 6));
                craft.addAllGridSlots(new ItemStack[] { null, new ItemStack(CommonProxy.itemMisc), null, new ItemStack(Item.emerald), new ItemStack(CommonProxy.blockFrame), new ItemStack(Item.diamond) });
            }
            else if (page.equals("blockDbs"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.blockStabilizer, 6));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(Block.blockIron), new ItemStack(Item.enderPearl), new ItemStack(Block.blockIron), new ItemStack(Item.enderPearl), new ItemStack(Item.diamond), new ItemStack(Item.enderPearl), new ItemStack(Block.blockIron), new ItemStack(Item.enderPearl), new ItemStack(Block.blockIron) });
            }
            else if (page.equals("blockDecoration"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.blockDecoration, 8, 0));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(Block.stone), new ItemStack(Block.blockNetherQuartz), new ItemStack(Block.stone), new ItemStack(Block.blockNetherQuartz), new ItemStack(Block.blockNetherQuartz), new ItemStack(Block.blockNetherQuartz), new ItemStack(Block.stone), new ItemStack(Block.blockNetherQuartz), new ItemStack(Block.stone) });
                
                ElementCrafting craft2 = new ElementCrafting(this, scrollPanel.getWidth() / 2 - 58, 86, 0);
                craft2.addOutputSlot(new ItemStack(CommonProxy.blockDecoration, 10, 1));
                craft2.addAllGridSlots(new ItemStack[] { null, new ItemStack(Item.ingotIron), null, new ItemStack(Item.ingotIron), new ItemStack(Block.blockIron), new ItemStack(Item.ingotIron), null, new ItemStack(Item.ingotIron), null });
                scrollPanel.addElement(craft2);
                offset += craft2.getHeight();
            }
            else if (page.equals("itemGlasses"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.itemGoggles));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.dyePowder, 0, 1), null, new ItemStack(Item.dyePowder, 0, 6), new ItemStack(Block.thinGlass), new ItemStack(Item.leather), new ItemStack(Block.thinGlass), new ItemStack(Item.leather), null, new ItemStack(Item.leather) });
            }
            else if (page.equals("itemWrench"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.itemWrench));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.ingotIron), null, new ItemStack(Item.ingotIron), null, new ItemStack(Item.netherQuartz), null, null, new ItemStack(Item.ingotIron) });
            }
            else if (page.equals("itemNanobrush"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.itemPaintbrush));
                craft.addAllGridSlots(new ItemStack[] { null, new ItemStack(Item.silk), new ItemStack(Block.cloth), null, new ItemStack(Item.stick), new ItemStack(Item.silk), new ItemStack(Item.stick) });
            }
            else if (page.equals("itemLocationCard"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.itemLocationCard, 16));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.ingotIron), new ItemStack(Item.paper), new ItemStack(Item.ingotIron), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.ingotIron), new ItemStack(Item.dyePowder, 0, 4), new ItemStack(Item.ingotIron) });
            }
            else if (page.equals("itemSynchronizer"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.itemSynchronizer));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.ingotGold), new ItemStack(Item.ingotGold), new ItemStack(Item.ingotGold), new ItemStack(Item.ingotIron), new ItemStack(Item.netherQuartz), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron) });
            }
            else if (page.equals("itemIdCard"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.itemEntityCard, 8));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.ingotGold), new ItemStack(Item.paper), new ItemStack(Item.ingotGold), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.ingotGold), new ItemStack(Item.dyePowder, 0, 10), new ItemStack(Item.ingotGold) });
            }
            else if (page.equals("itemScanner"))
            {
                craft.addOutputSlot(new ItemStack(CommonProxy.itemScanner));
                craft.addAllGridSlots(new ItemStack[] { new ItemStack(Item.ingotGold), new ItemStack(Item.redstone), new ItemStack(Item.ingotGold), new ItemStack(Item.ingotIron), new ItemStack(Item.netherQuartz), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron), new ItemStack(CommonProxy.itemEntityCard), new ItemStack(Item.ingotIron) });
            }
            else if (page.equals("itemPortalModules"))
            {
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30, 0).addOutputSlot(new ItemStack(CommonProxy.itemMisc)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget), new ItemStack(Item.ingotIron), new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget) }));
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 57, 0).addOutputSlot(new ItemStack(CommonProxy.itemPortalModule)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.redstone), new ItemStack(CommonProxy.itemMisc), new ItemStack(Item.gunpowder) }));
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 114, 0).addOutputSlot(new ItemStack(CommonProxy.itemPortalModule, 0, 1)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.dyePowder, 0, 1), new ItemStack(Item.dyePowder, 0, 2), new ItemStack(Item.dyePowder, 0, 4), null, new ItemStack(CommonProxy.itemMisc), null, new ItemStack(Item.dyePowder, 0, 4), new ItemStack(Item.dyePowder, 0, 2), new ItemStack(Item.dyePowder, 0, 1) }));
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 171, 0).addOutputSlot(new ItemStack(CommonProxy.itemPortalModule, 0, 2)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.redstone), new ItemStack(CommonProxy.itemMisc), new ItemStack(Block.music) }));
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 228, 0).addOutputSlot(new ItemStack(CommonProxy.itemPortalModule, 0, 3)).addAllGridSlots(new ItemStack[] { new ItemStack(Block.anvil), new ItemStack(CommonProxy.itemMisc), new ItemStack(Item.feather) }));
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 285, 0).addOutputSlot(new ItemStack(CommonProxy.itemPortalModule, 0, 5)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.dyePowder, 0, 15), new ItemStack(CommonProxy.itemMisc), new ItemStack(Item.dyePowder) }));
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 342, 0).addOutputSlot(new ItemStack(CommonProxy.itemPortalModule, 0, 7)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.feather), new ItemStack(Item.feather), new ItemStack(Item.feather), new ItemStack(Item.feather), new ItemStack(CommonProxy.itemMisc), new ItemStack(Item.feather), new ItemStack(Item.feather), new ItemStack(Item.feather), new ItemStack(Item.feather) }));
                
                craft = null;
                offset += 57 * 6;
            }
            else
            {
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30, 0).addOutputSlot(new ItemStack(CommonProxy.itemMisc, 1, 1)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.diamond), null, null, new ItemStack(Item.paper), null, null, new ItemStack(Item.dyePowder, 0, 1) }));
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 57, 0).addOutputSlot(new ItemStack(CommonProxy.itemInPlaceUpgrade)).addAllGridSlots(new ItemStack[] { null, new ItemStack(Item.redstone), null, new ItemStack(Item.redstone), new ItemStack(CommonProxy.itemMisc, 1, 1), new ItemStack(Item.redstone), null, new ItemStack(Item.redstone) }));
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 114, 0).addOutputSlot(new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 1)).addAllGridSlots(new ItemStack[] { new ItemStack(CommonProxy.itemMisc, 1, 1), new ItemStack(Item.enderPearl) }));
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 171, 0).addOutputSlot(new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 2)).addAllGridSlots(new ItemStack[] { new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 1), new ItemStack(Item.diamond) }));
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 228, 0).addOutputSlot(new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 3)).addAllGridSlots(new ItemStack[] { new ItemStack(Item.porkCooked), new ItemStack(Item.beefCooked), new ItemStack(Item.chickenCooked), new ItemStack(Item.blazePowder), new ItemStack(CommonProxy.itemMisc, 1, 1), new ItemStack(Item.blazePowder) }));
                scrollPanel.addElement(new ElementCrafting(this, craft.getRelativeX(), 30 + 285, 0).addOutputSlot(new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 4)).addAllGridSlots(new ItemStack[] { null, new ItemStack(CommonProxy.itemMisc), null, new ItemStack(Item.emerald), new ItemStack(CommonProxy.itemMisc, 1, 1), new ItemStack(Item.diamond) }));
                
                craft = null;
                offset += 57 * (BlockFrame.FRAME_TYPES - 2);
            }

            if (craft != null)
            {
                scrollPanel.addElement(craft);
            }
            
            scrollPanel.addElement(new ElementText(this, 5, 90 + offset, Localization.getGuiString("information"), null, 0x44AAFF, true));
            scrollPanel.addElement(new ElementTextBox(this, 8, 103 + offset, Localization.getGuiString(page + ".information"), scrollPanel.getWidth() - 14, 0xFFFFFF, false));
        }
        else
        {
            scrollPanel.addElement(new ElementTextBox(this, 8, 18, Localization.getGuiString(page + ".information"), scrollPanel.getWidth() - 14, 0xFFFFFF, false));
        }
    }
}
