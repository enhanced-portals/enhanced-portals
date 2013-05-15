package enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.portal.Portal;

public class BlockObsidianStairs extends BlockStairs
{
    public BlockObsidianStairs()
    {
        super(BlockIds.ObsidianStairs, Block.obsidian, 0);
        setHardness(50.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Localization.ObsidianStairs_Name);
        setCreativeTab(Reference.CREATIVE_TAB);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return Block.blocksList[BlockIds.Obsidian].getIcon(par1, par2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        // Stops from registering NULL.
    }
    
    @Override
    public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        ItemStack current = player.inventory.mainInventory[player.inventory.currentItem];

        if (current != null && current.itemID == Item.flintAndSteel.itemID)
        {
            if (new Portal(x, y + 1, z, worldObj).createPortal() || new Portal(x, y - 1, z, worldObj).createPortal() || new Portal(x + 1, y, z, worldObj).createPortal() || new Portal(x - 1, y, z, worldObj).createPortal() || new Portal(x, y, z + 1, worldObj).createPortal() || new Portal(x, y, z - 1, worldObj).createPortal())
            {
                current.damageItem(1, player);
            }

            return true;
        }

        return false;
    }
}
