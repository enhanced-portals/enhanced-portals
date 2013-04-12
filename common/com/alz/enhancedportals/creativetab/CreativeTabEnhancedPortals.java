package com.alz.enhancedportals.creativetab;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import com.alz.enhancedportals.reference.BlockIds;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabEnhancedPortals extends CreativeTabs
{
    public static final CreativeTabs tabEnhancedPortals = new CreativeTabEnhancedPortals("enhancedportals");

    public CreativeTabEnhancedPortals(String label)
    {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(Block.blocksList[BlockIds.PORTAL_MODIFIER]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel()
    {
        return "Enhanced Portals";
    }
}
