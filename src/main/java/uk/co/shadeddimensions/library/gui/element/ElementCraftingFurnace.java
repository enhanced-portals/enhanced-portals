package uk.co.shadeddimensions.library.gui.element;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementCraftingFurnace extends ElementBaseContainer
{
    ItemStack input, output;

    public ElementCraftingFurnace(IGuiBase parent, int x, int y, ItemStack input)
    {
        super(parent, x, y, 54, 54);
        this.input = input;
        this.output = FurnaceRecipes.smelting().getSmeltingResult(input);
        
        elements.add(new ElementItemIconWithSlot(parent, 0, 0, input));
        elements.add(new ElementFire(parent, 2, 21, 0, 1000, false).setAutoIncrement(1));
        elements.add(new ElementItemIconWithSlot(parent, 0, 36, null));

        elements.add(new ElementIcon(parent, 25, 19, 2));
        elements.add(new ElementItemIconWithSlotAndCount(parent, 56, 13, output, true));
    }
}
