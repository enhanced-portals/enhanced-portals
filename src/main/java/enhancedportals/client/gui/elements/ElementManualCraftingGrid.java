package enhancedportals.client.gui.elements;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import enhancedportals.client.gui.GuiManual;
import enhancedportals.network.ClientProxy;

public class ElementManualCraftingGrid extends BaseElement
{
    int offset = 7;
    ItemStack[] items;
    
    public ElementManualCraftingGrid(GuiManual gui, int x, int y, ItemStack[] i)
    {
        super(gui, x, y, 66, 66);
        texture = new ResourceLocation("enhancedportals", "textures/gui/crafting.png");
        items = i;
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (items == null)
        {
            return false;
        }
        
        x = x - posX + parent.getGuiLeft();
        y = y - posY + parent.getGuiTop();
        
        for (int i = 0; i < 9; i++)
        {
            if (i >= items.length || items[i] == null)
            {
                break;
            }
            
            int X = i % 3 * 18, Y = i / 3 * 18;
            
            if (x >= offset + X && x < offset + X + 16 && y >= offset + Y && y < offset + Y + 16)
            {
                ClientProxy.setManualPageFromItem(items[i]);
                ((GuiManual) parent).pageChanged();
                break;
            }
        }
        
        return false;
    }
    
    @Override
    public void addTooltip(List<String> list)
    {
        if (items == null)
        {
            return;
        }
        
        List l = null;
        int x = parent.getMouseX() + parent.getGuiLeft() - posX, y = parent.getMouseY() + parent.getGuiTop() - posY;

        for (int i = 0; i < 9; i++)
        {
            if (i >= items.length)
            {
                break;
            }
            
            int X = i % 3 * 18, Y = i / 3 * 18;
            
            if (x >= offset + X && x < offset + X + 16 && y >= offset + Y && y < offset + Y + 16)
            {
                l = items[i].getTooltip(parent.getMinecraft().thePlayer, false);
                break;
            }
        }
        
        if (l != null)
        {
            list.add("offset");
            
            for (Object o : l)
            {
                list.add("" + o);
            }
        }
    }

    @Override
    protected void drawContent()
    {
        if (items == null)
        {
            return;
        }
        
        for (int i = 0; i < 9; i++)
        {
            if (i >= items.length)
            {
                break;
            }
            
            parent.drawItemStack(items[i], posX + offset + (i % 3 * 18), posY + offset + (i / 3 * 18));
        }
    }

    @Override
    public void update()
    {

    }

    public void setItems(ItemStack[] s)
    {
        items = s;
    }
}
