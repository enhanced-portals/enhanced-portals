package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementManualCraftingGrid;
import enhancedportals.client.gui.elements.ElementManualTextButton;
import enhancedportals.inventory.ContainerManual;
import enhancedportals.network.ClientProxy;

public class GuiManual extends BaseGui
{
    public static final int CONTAINER_SIZE = 180, CONTAINER_WIDTH = 279;
    static ResourceLocation textureB = new ResourceLocation("enhancedportals", "textures/gui/manualB.png");
    ElementManualCraftingGrid craftingGrid;
    ElementManualTextButton[] textButtons;

    public GuiManual(EntityPlayer p)
    {
        super(new ContainerManual(p.inventory), CONTAINER_SIZE);
        xSize = CONTAINER_WIDTH;
        setHidePlayerInventory();
        texture = new ResourceLocation("enhancedportals", "textures/gui/manualA.png");
    }

    @Override
    protected void drawBackgroundTexture()
    {
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 140, ySize);
        mc.renderEngine.bindTexture(textureB);
        drawTexturedModalRect(guiLeft + 140, guiTop, 0, 0, 139, ySize);

        boolean mouseHeight = guiTop + mouseY >= guiTop + CONTAINER_SIZE + 3 && guiTop + mouseY < guiTop + CONTAINER_SIZE + 13, rightButton = mouseHeight && guiLeft + mouseX >= guiLeft + CONTAINER_WIDTH - 23 && guiLeft + mouseX < guiLeft + CONTAINER_WIDTH - 5, leftButton = mouseHeight && guiLeft + mouseX >= guiLeft + 5 && guiLeft + mouseX < guiLeft + 23, middleButton = mouseHeight && guiLeft + mouseX >= guiLeft + (xSize / 2 - 10) && guiLeft + mouseX < guiLeft + (xSize / 2 - 10) + 21;

        if (ClientProxy.manualEntryHasPage(ClientProxy.manualPage + 2))
        {
            drawTexturedModalRect(guiLeft + CONTAINER_WIDTH - 23, guiTop + CONTAINER_SIZE + 3, rightButton ? 23 : 0, 233, 18, 10);
        }

        if (ClientProxy.manualPage > 0)
        {
            drawTexturedModalRect(guiLeft + 5, guiTop + CONTAINER_SIZE + 3, leftButton ? 23 : 0, 246, 18, 10);
        }
        
        if (!ClientProxy.manualEntry.equals("main"))
        {
            drawTexturedModalRect(guiLeft + (xSize / 2 - 10), guiTop + CONTAINER_SIZE + 3, middleButton ? 21 : 0, 222, 21, 10);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        int pageWidth = 120;
        
        if (ClientProxy.manualEntry.equals("main"))
        {
            if (ClientProxy.manualPage == 0)
            {
                int offset = drawSplitString(17, 12, pageWidth, EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".title"), 0x000000);
                getFontRenderer().drawSplitString(EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".page.0"), 17, 15 + offset, pageWidth, 0x000000);
            }
        }
        else if (ClientProxy.manualPage == 0)
        {
            int offset = drawSplitString(17, 12, pageWidth, EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".title"), 0x000000);
            String sub = EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".subtitle");

            if (!sub.contains(".subtitle"))
            {
                getFontRenderer().drawSplitString(sub, 17, offset + 13, pageWidth, 0x888888);
            }

            getFontRenderer().drawSplitString(EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".page.0"), 147, 12, pageWidth, 0x000000);
        }
        else
        {
            getFontRenderer().drawSplitString(EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".page." + ClientProxy.manualPage), 15, 12, pageWidth, 0x000000);

            if (ClientProxy.manualEntryHasPage(ClientProxy.manualPage + 1))
            {
                getFontRenderer().drawSplitString(EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".page." + (ClientProxy.manualPage + 1)), 148, 12, pageWidth, 0x000000);
            }
        }

        super.drawGuiContainerForegroundLayer(par1, par2);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 4)
        {
            nextPage();
            return;
        }
        else if (mouseButton == 3)
        {
            prevPage();
            return;
        }
        else if (mouseY >= guiTop + CONTAINER_SIZE + 3 && mouseY < guiTop + CONTAINER_SIZE + 13)
        {
            if (mouseX >= guiLeft + CONTAINER_WIDTH - 23 && mouseX < guiLeft + CONTAINER_WIDTH - 5)
            {
                nextPage();
                return;
            }
            else if (mouseX >= guiLeft + 5 && mouseX < guiLeft + 23)
            {
                prevPage();
                return;
            }
            else if (mouseX >= guiLeft + (xSize / 2 - 10) && mouseX < guiLeft + (xSize / 2 - 10) + 21)
            {
                changeEntry("main");
                return;
            }
        }
    }

    void nextPage()
    {
        if (ClientProxy.manualEntryHasPage(ClientProxy.manualPage + 2))
        {
            ClientProxy.manualPage += ClientProxy.manualPage == 0 ? 1 : 2;
            pageChanged();
        }
    }

    void prevPage()
    {
        if (ClientProxy.manualPage > 0)
        {
            ClientProxy.manualPage -= 2;

            if (ClientProxy.manualPage < 0)
            {
                ClientProxy.manualPage = 0;
            }

            pageChanged();
        }
    }

    void changeEntry(String e)
    {
        ClientProxy.manualChangeEntry(e);
        pageChanged();
    }

    @Override
    public void initGui()
    {
        super.initGui();

        textButtons = new ElementManualTextButton[30];
        for (int i = 0; i < textButtons.length; i++)
        {
            textButtons[i] = new ElementManualTextButton(this, i < 15 ? 17 : 147, 15 + (i >= 15 ? (i - 15) * 10 : i * 10), "main");
            textButtons[i].setVisible(false);
            addElement(textButtons[i]);
        }

        craftingGrid = new ElementManualCraftingGrid(this, 70 - 33, 90 - 33, null);
        pageChanged();
        addElement(craftingGrid);
    }

    public void pageChanged()
    {
        if (ClientProxy.manualEntry.equals("main"))
        {
            craftingGrid.setVisible(false);
            craftingGrid.setItems(null);

            for (int i = 0; i < textButtons.length; i++)
            {
                textButtons[i].setVisible(ClientProxy.manualPage == 0 && i < 15 ? false : true);
            }

            switch (ClientProxy.manualPage)
            {
                case 0:
                    textButtons[15].updateEntry("portal");
                    textButtons[16].updateEntry("frame0");
                    textButtons[17].updateEntry("frame1");
                    textButtons[18].updateEntry("frame2");
                    textButtons[19].updateEntry("frame3");
                    textButtons[20].updateEntry("frame4");
                    textButtons[21].updateEntry("frame5");
                    textButtons[22].updateEntry("frame6");
                    textButtons[23].updateEntry("frame7");
                    textButtons[24].updateEntry("frame8");
                    textButtons[25].updateEntry("frame9");
                    textButtons[26].updateEntry("dbs");
                    textButtons[27].updateEntry("decorStabilizer");
                    textButtons[28].updateEntry("decorBorderedQuartz");
                    textButtons[29].updateEntry("dbsEmpty");
                    break;

                case 1:
                    textButtons[0].updateEntry("wrench");
                    textButtons[1].updateEntry("nanobrush");
                    textButtons[2].updateEntry("glasses");
                    textButtons[3].updateEntry("location_card");
                    textButtons[4].updateEntry(null);
                    textButtons[5].updateEntry("blank_module");
                    textButtons[6].updateEntry("module0");
                    textButtons[7].updateEntry("module1");
                    textButtons[8].updateEntry("module2");
                    textButtons[9].updateEntry("module3");
                    textButtons[10].updateEntry("module4");
                    textButtons[11].updateEntry("module5");
                    textButtons[12].updateEntry("module6");
                    textButtons[13].updateEntry("module7");
                    textButtons[14].updateEntry(null);
                    textButtons[15].updateEntry("blank_upgrade");
                    textButtons[16].updateEntry("upgrade0");
                    textButtons[17].updateEntry("upgrade1");
                    textButtons[18].updateEntry("upgrade2");
                    textButtons[19].updateEntry("upgrade3");
                    textButtons[20].updateEntry("upgrade4");
                    textButtons[21].updateEntry("upgrade5");
                    textButtons[22].updateEntry("upgrade6");
                    textButtons[23].updateEntry("upgrade7");
                    textButtons[24].updateEntry(null);
                    textButtons[25].updateEntry(null);
                    textButtons[26].updateEntry(null);
                    textButtons[27].updateEntry(null);
                    textButtons[28].updateEntry(null);
                    textButtons[29].updateEntry(null);
                    break;

                default:
                    break;
            }
        }
        else
        {
            for (int i = 0; i < textButtons.length; i++)
            {
                textButtons[i].setVisible(false);
            }

            ItemStack[] stacks = ClientProxy.getCraftingRecipeForManualEntry();
            craftingGrid.setVisible(stacks != null);
            craftingGrid.setItems(stacks);
        }
    }
}
