package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.container.ContainerScanner;
import uk.co.shadeddimensions.ep3.container.InventoryScanner;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import uk.co.shadeddimensions.library.gui.GuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementRedstoneFlux;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.ItemEnergyContainer;

public class GuiScanner extends GuiBase
{
    ItemStack stack;
    EnergyStorage storage = new EnergyStorage(2000, 250, 250);

    public GuiScanner(InventoryScanner scanner, EntityPlayer player, ItemStack s)
    {
        super(new ContainerScanner(scanner, player, s), new ResourceLocation("enhancedportals", "textures/gui/scanner.png"));
        stack = s;
    }

    @Override
    public void addElements()
    {
        if (GeneralUtils.hasEnergyCost())
        {
            storage.setEnergyStored(((ItemEnergyContainer) stack.getItem()).getEnergyStored(stack));
            addElement(new ElementRedstoneFlux(this, xSize - 22, 22, storage.getEnergyStored(), storage.getMaxEnergyStored()));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        fontRenderer.drawString(Localization.getGuiString("scanner"), 7, 7, 0x404040);
        super.drawGuiContainerForegroundLayer(x, y);
    }
}
