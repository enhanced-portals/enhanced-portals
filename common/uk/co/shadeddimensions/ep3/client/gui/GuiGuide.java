package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

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
        drawInventory = false;
    }

    @Override
    protected void drawBackgroundTexture()
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
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, 0, 0, 0, xSize, 7);
        drawTexturedModalRect(guiLeft, height - 10, 0, 229, xSize, 10);
    }

    @Override
    public void addElements()
    {
        scrollPanel = new ElementScrollPanel(this, 11, -guiTop + 7, xSize - 22, ySize - 17);
        addElement(scrollPanel);
        addElement(new ElementScrollBar(this, xSize - 9, -guiTop + 7, 5, ySize - 16, scrollPanel));
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
                addTab(new TabToggleButton(this, 1, "blockFrame", "Portal Frame", new ItemStack(CommonProxy.blockFrame)));
                addTab(new TabToggleButton(this, 1, "blockController", "Portal Controller", new ItemStack(CommonProxy.blockFrame, 1, 1)));
                addTab(new TabToggleButton(this, 1, "blockRedstoneInterface", "Redstone Interface", new ItemStack(CommonProxy.blockFrame, 1, 2)));
                addTab(new TabToggleButton(this, 1, "blockNetworkInterface", "Network Interface", new ItemStack(CommonProxy.blockFrame, 1, 3)));
                addTab(new TabToggleButton(this, 1, "blockDiallingDevice", "Dialling Device", new ItemStack(CommonProxy.blockFrame, 1, 4)));
                addTab(new TabToggleButton(this, 1, "blockBiometric", "Biometric Identifier", new ItemStack(CommonProxy.blockFrame, 1, 5)));
                addTab(new TabToggleButton(this, 1, "blockModule", "Module Manipulator", new ItemStack(CommonProxy.blockFrame, 1, 6)));
                addTab(new TabToggleButton(this, 1, "blockDbs", "DBS", new ItemStack(CommonProxy.blockStabilizer)));
                addTab(new TabToggleButton(this, 1, "blockDecoration", "Decoration", new ItemStack(CommonProxy.blockDecoration)));
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

            scrollPanel.addElement(craft);
            scrollPanel.addElement(new ElementText(this, 5, 90 + offset, Localization.getGuiString("information"), null, 0x44AAFF, true));
            scrollPanel.addElement(new ElementTextBox(this, 8, 103 + offset, Localization.getGuiString(page + ".information"), scrollPanel.getWidth() - 14, 0xFFFFFF, false));
        }
    }
}
