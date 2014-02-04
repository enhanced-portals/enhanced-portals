package uk.co.shadeddimensions.library.gui.element;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementCraftingGridLarge extends ElementBaseContainer
{

    public ElementCraftingGridLarge(IGuiBase parent, int x, int y, ItemStack output)
    {
        super(parent, x, y, 0, 0);
        
        for (Object o : CraftingManager.getInstance().getRecipeList())
        {
            IRecipe recipe = (IRecipe) o;
            
            if (recipe.getRecipeOutput().equals(output))
            {
                
            }
        }
    }

}
