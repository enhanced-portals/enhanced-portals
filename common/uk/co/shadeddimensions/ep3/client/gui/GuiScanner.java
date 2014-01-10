package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.container.ContainerScanner;
import uk.co.shadeddimensions.ep3.container.InventoryScanner;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.library.gui.GuiBase;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.ItemEnergyContainer;

public class GuiScanner extends GuiBase
{
    ItemStack stack;

    public GuiScanner(InventoryScanner scanner, EntityPlayer player, ItemStack s)
    {
        super(new ContainerScanner(scanner, player, s), new ResourceLocation("enhancedportals", "textures/gui/scanner.png"));
        stack = s;
        //drawInventory = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        fontRenderer.drawString(Localization.getGuiString("scanner"), 7, 7, 0x404040);
        fontRenderer.drawString(Localization.getGuiString("inventory"), 7, 70, 0x404040);

        super.drawGuiContainerForegroundLayer(x, y);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        EnergyStorage storage = new EnergyStorage(2000, 250, 250);
        storage.setEnergyStored(((ItemEnergyContainer) stack.getItem()).getEnergyStored(stack));
        //addElement(new ElementEnergyStored(this, xSize - 22, 22, storage));
    }
}
