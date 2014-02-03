package uk.co.shadeddimensions.library.gui.element;

import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementCrafting extends ElementBaseContainer
{
    int type;

    /**
     * 
     * @param parent
     * @param x
     * @param y
     * @param t
     *            0 = 3x3 Crafting Grid, 1 = Furnace, 2 = Potion
     */
    public ElementCrafting(IGuiBase parent, int x, int y, int t)
    {
        super(parent, x, y, t == 0 ? 18 : t == 1 ? 54 : 64, t == 2 ? 55 : 54);
        type = t;

        if (t == 0)
        {
            for (int i = 0; i < 9; i++)
            {
                int xPos = i % 3 * 18, yPos = i / 3 * 18;
                elements.add(new ElementItemIconWithSlot(parent, xPos, yPos, null));
            }

            elements.add(new ElementIcon(parent, 61, 19, 2));
            elements.add(new ElementItemIconWithSlotAndCount(parent, 90, 14, null, true));
            elements.get(elements.size() - 2).setVisible(false);
            elements.get(elements.size() - 1).setVisible(false);
        }
        else if (t == 1)
        {
            elements.add(new ElementItemIconWithSlot(parent, 0, 0, null));
            elements.add(new ElementFire(parent, 2, 21, 0, 1000, false));
            elements.add(new ElementItemIconWithSlot(parent, 0, 36, null));

            elements.add(new ElementIcon(parent, 25, 19, 2));
            elements.add(new ElementItemIconWithSlotAndCount(parent, 56, 13, null, true));
            elements.get(elements.size() - 2).setVisible(false);
            elements.get(elements.size() - 1).setVisible(false);
        }
        else if (t == 2)
        {
            elements.add(new ElementItemIcon(parent, 24, 2, null));
            elements.add(new ElementItemIcon(parent, 1, 31, null));
            elements.add(new ElementItemIcon(parent, 24, 38, null));
            elements.add(new ElementItemIcon(parent, 47, 31, null));
            elements.add(new ElementBubbles(parent, 10, 0, 100));
            elements.add(new ElementDownArrow(parent, 43, 2, 1000));
        }
    }

    public ElementCrafting addAllGridSlots(ItemStack[] stacks)
    {
        if (type == 0)
        {
            for (int i = 0; i < stacks.length; i++)
            {
                ((ElementItemIconWithSlot) elements.get(i)).setItem(stacks[i]);
            }
        }

        return this;
    }

    public ElementCrafting addBothFurnaceSlots(ItemStack[] stacks)
    {
        if (type == 1)
        {
            ((ElementItemIconWithSlot) elements.get(0)).setItem(stacks[0]);
            ((ElementItemIconWithSlot) elements.get(2)).setItem(stacks[1]);
        }

        return this;
    }

    public ElementCrafting addOutputSlot(ItemStack stack)
    {
        if (type == 2)
        {
            ((ElementItemIcon) elements.get(1)).setItem(stack);
            ((ElementItemIcon) elements.get(2)).setItem(stack);
            ((ElementItemIcon) elements.get(3)).setItem(stack);
        }
        else
        {
            sizeX = type == 1 ? 82 : 116;
            elements.get(elements.size() - 2).setVisible(true);
            elements.get(elements.size() - 1).setVisible(true);
            ((ElementItemIconWithSlotAndCount) elements.get(elements.size() - 1)).setItem(stack);
        }

        return this;
    }

    public ElementCrafting addPotionIngredient(ItemStack stack)
    {
        if (type == 2)
        {
            ((ElementItemIcon) elements.get(0)).setItem(stack);
        }

        return this;
    }

    public ElementCrafting addTopFurnaceSlot(ItemStack stack)
    {
        if (type == 1)
        {
            ((ElementItemIconWithSlot) elements.get(0)).setItem(stack);
        }

        return this;
    }

    @Override
    public void draw()
    {
        if (type == 2)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            gui.getTextureManager().bindTexture(texture);
            drawTexturedModalRect(posX, posY, 146, 0, sizeX, sizeY);
        }

        super.draw();
    }
}
