package alz.mods.enhancedportals.block;

import alz.mods.enhancedportals.helpers.PortalHelper;
import alz.mods.enhancedportals.helpers.TeleportData;
import alz.mods.enhancedportals.item.ItemScroll;
import alz.mods.enhancedportals.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockObsidian extends net.minecraft.block.BlockObsidian
{
	public BlockObsidian()
	{
		super(49);
		setHardness(50.0F);
		setResistance(2000.0F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName("obsidian");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		// PortalHelper.createPortalAround(world, x, y, z, player);
	    ItemStack currentItem = player.inventory.mainInventory[player.inventory.currentItem];
        
        if (currentItem != null && currentItem.itemID == Reference.ItemIDs.ItemScroll + 256)
        {
            ItemScroll Scroll = (ItemScroll) currentItem.getItem();
            Scroll.setLocationData(currentItem, new TeleportData(x, y, z, 0));
        }
		return false;
	}
}
