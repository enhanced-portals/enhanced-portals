package com.alz.enhancedportals.core.blocks;

import com.alz.enhancedportals.helpers.WorldLocation;
import com.alz.enhancedportals.portals.PortalHandler;

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
        ItemStack current = player.inventory.mainInventory[player.inventory.currentItem];

        if (current != null && current.itemID == Item.flintAndSteel.itemID)
        {
            if (PortalHandler.createPortalAroundBlock(new WorldLocation(x, y, z, world)))
            {
                current.damageItem(1, player);
            }

            return true;
        }

        return false;
    }
}
