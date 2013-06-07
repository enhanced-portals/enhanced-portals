package enhancedportals.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.ItemIds;
import enhancedportals.lib.Localization;
import enhancedportals.portal.Portal;

public class BlockObsidian extends net.minecraft.block.BlockObsidian
{
    public BlockObsidian()
    {
        super(BlockIds.Obsidian);
        setHardness(50.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Localization.Obsidian_Name);
    }

    @Override
    public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        ItemStack current = player.inventory.mainInventory[player.inventory.currentItem];

        if (current != null && (current.itemID == Item.flintAndSteel.itemID || current.itemID == ItemIds.EnhancedFlintAndSteel + 256))
        {
            if (new Portal(x, y + 1, z, worldObj).createPortal(current) || new Portal(x, y - 1, z, worldObj).createPortal(current) || new Portal(x + 1, y, z, worldObj).createPortal(current) || new Portal(x - 1, y, z, worldObj).createPortal(current) || new Portal(x, y, z + 1, worldObj).createPortal(current) || new Portal(x, y, z - 1, worldObj).createPortal(current))
            {
                current.damageItem(1, player);                
            }
            
            return true;
        }

        return false;
    }
}
