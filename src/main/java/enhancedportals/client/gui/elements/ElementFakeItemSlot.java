package enhancedportals.client.gui.elements;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.common.IFakeSlotHandler;

public class ElementFakeItemSlot extends BaseElement
{
    ItemStack s;

    public ElementFakeItemSlot(BaseGui gui, int x, int y)
    {
        this(gui, x, y, null);
    }

    public ElementFakeItemSlot(BaseGui gui, int x, int y, ItemStack stack)
    {
        super(gui, x, y, 16, 16);
        s = stack;
    }

    @Override
    public void addTooltip(List<String> list)
    {
        if (s != null)
        {
            for (Object o : s.getTooltip(parent.getMinecraft().thePlayer, false))
            {
                list.add((String) o);
            }
        }
    }
    
    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        ItemStack st = parent.getMinecraft().thePlayer.inventory.getItemStack();
        
        if (((IFakeSlotHandler) parent).isItemValid(st))
        {
            s = st;
            ((IFakeSlotHandler) parent).onItemChanged(s);
        }
        
        return true;
    }

    @Override
    protected void drawContent()
    {
        if (intersectsWith(parent.getMouseX(), parent.getMouseY()))
        {
            parent.drawRect(posX, posY, posX + sizeX, posY + sizeY, 0x99FFFFFF);
        }
        
        if (s != null)
        {
            parent.drawItemStack(s, posX, posY);
        }
    }

    @Override
    public void update()
    {

    }
}
