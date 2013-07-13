package enhancedportals.client.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import enhancedcore.gui.slots.SlotPhantom;
import enhancedcore.gui.tooltips.ToolTip;
import enhancedcore.gui.tooltips.ToolTipLine;
import enhancedportals.lib.Textures;
import enhancedportals.portal.PortalTexture;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class SlotPhantomTexture extends SlotPhantom
{
	public SlotPhantomTexture(IInventory iinventory, int slotIndex, int posX, int posY)
	{
		super(iinventory, slotIndex, posX, posY);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return Textures.getTextureFromItemStack(stack) != null;
	}
	
	@Override
	public ToolTip getToolTip()
	{
		ToolTip toolTip = new ToolTip();
		toolTip.add(new ToolTipLine(EnumChatFormatting.WHITE + "Facade"));
		
		return toolTip;
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
	
	@Override
	public void onSlotChanged()
	{
		super.onSlotChanged();
		PortalTexture tex = Textures.getTextureFromItemStack(getStack());
        
        if (inventory instanceof TileEntityPortalModifier)
        {
            if (tex != null)
            {
                ((TileEntityPortalModifier) inventory).texture = tex.getID();
            }
            else
            {
                ((TileEntityPortalModifier) inventory).texture = "";
            }
        }
	}
}
