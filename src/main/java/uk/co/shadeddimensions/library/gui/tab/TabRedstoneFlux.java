package uk.co.shadeddimensions.library.gui.tab;

import net.minecraft.item.Item;
import uk.co.shadeddimensions.library.gui.IGuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementRedstoneFlux;

public class TabRedstoneFlux extends TabBase
{
    protected ElementRedstoneFlux elementFlux;

    public TabRedstoneFlux(IGuiBase gui, ElementRedstoneFlux flux)
    {
        this(gui, 1, flux);
    }

    public TabRedstoneFlux(IGuiBase gui, int side, ElementRedstoneFlux flux)
    {
        super(gui, side);
        elementFlux = flux;
        name = "Energy";
        backgroundColor = 0xDD6600;
        icon = Item.redstone.getIconFromDamage(0);
        titleColour = 0xDDDD00;
        maxHeight = 75;
    }

    @Override
    public void draw()
    {
        super.draw();

        if (isFullyOpened() && elementFlux != null)
        {
            gui.getFontRenderer().drawStringWithShadow("Maximum Power:", posX + 10, posY + 20, 0xAAAAAA);
            gui.getFontRenderer().drawString(elementFlux.getMaximum() + " RF", posX + 17, posY + 32, 0x000000);

            gui.getFontRenderer().drawStringWithShadow("Energy Stored:", posX + 10, posY + 45, 0xAAAAAA);
            gui.getFontRenderer().drawString(elementFlux.getProgress() + " RF", posX + 17, posY + 57, 0x000000);
        }
    }
}
