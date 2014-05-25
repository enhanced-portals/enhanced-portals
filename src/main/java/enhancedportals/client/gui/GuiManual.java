package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import truetyper.FontHelper;
import truetyper.FontLoader;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementManualCraftingGrid;
import enhancedportals.inventory.ContainerManual;
import enhancedportals.network.ClientProxy;

public class GuiManual extends BaseGui
{
    public static final int CONTAINER_SIZE = 180, CONTAINER_WIDTH = 279;
    static ResourceLocation textureB = new ResourceLocation("enhancedportals", "textures/gui/manualB.png");
    ElementManualCraftingGrid craftingGrid;

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

        boolean mouseHeight = guiTop + mouseY >= guiTop + CONTAINER_SIZE + 3 && guiTop + mouseY < guiTop + CONTAINER_SIZE + 13, rightButton = mouseHeight && guiLeft + mouseX >= guiLeft + CONTAINER_WIDTH - 23 && guiLeft + mouseX < guiLeft + CONTAINER_WIDTH - 5, leftButton = mouseHeight && guiLeft + mouseX >= guiLeft + 5 && guiLeft + mouseX < guiLeft + 23;

        if (ClientProxy.manualEntryHasPage(ClientProxy.manualPage + 2))
        {
            drawTexturedModalRect(guiLeft + CONTAINER_WIDTH - 23, guiTop + CONTAINER_SIZE + 3, rightButton ? 23 : 0, 233, 18, 10);
        }

        if (ClientProxy.manualPage > 0)
        {
            drawTexturedModalRect(guiLeft + 5, guiTop + CONTAINER_SIZE + 3, leftButton ? 23 : 0, 246, 18, 10);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        if (ClientProxy.manualPage == 0)
        {
            int offset = drawSplitTrueType(17, 12, 255, EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".title"), 0,  0f, 0f, 0f, 1f);
            String sub = EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".subtitle");
            
            if (!sub.contains(".subtitle"))
            {
                drawSplitTrueType(17, offset + 12, 255, sub, 1, 0f, 0f, 0f, 0.4f);
            }

            drawSplitTrueType(147, 12, 255, EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".page.0"), 1, 0f, 0f, 0f, 1f);
        }
        else
        {
            drawSplitTrueType(15, 12, 255, EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".page." + ClientProxy.manualPage), 0, 0f, 0f, 0f, 1f);

            if (ClientProxy.manualEntryHasPage(ClientProxy.manualPage + 1))
            {
                drawSplitTrueType(148, 12, 255, EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".page." + (ClientProxy.manualPage + 1)), 1, 0f, 0f, 0f, 1f);
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

        if (mouseY >= guiTop + CONTAINER_SIZE + 3 && mouseY < guiTop + CONTAINER_SIZE + 13)
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
        }
    }

    void nextPage()
    {
        if (ClientProxy.manualEntryHasPage(ClientProxy.manualPage + 2))
        {
            ClientProxy.manualPage += ClientProxy.manualPage == 0 ? 1 : 2;
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
        }
    }

    void changeEntry(String e)
    {
        ClientProxy.manualChangeEntry(e);
        ItemStack[] stacks = ClientProxy.getCraftingRecipeForManualEntry();
        craftingGrid.setVisible(stacks != null);
        craftingGrid.setItems(stacks);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        craftingGrid = new ElementManualCraftingGrid(this, 70 - 33, 90 - 33, null);
        pageChanged();
        addElement(craftingGrid);
    }

    public void pageChanged()
    {
        ItemStack[] stacks = ClientProxy.getCraftingRecipeForManualEntry();
        craftingGrid.setVisible(stacks != null);
        craftingGrid.setItems(stacks);
    }
}
