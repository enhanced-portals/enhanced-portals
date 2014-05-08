package enhancedportals.common;

import net.minecraft.item.ItemStack;

public interface IFakeSlotHandler
{
    public void onItemChanged(ItemStack newItem);
    public boolean isItemValid(ItemStack s);
}
