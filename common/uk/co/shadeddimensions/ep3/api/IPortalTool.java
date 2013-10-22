package uk.co.shadeddimensions.ep3.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;

public abstract interface IPortalTool
{
    public abstract boolean openGui(TilePortalPart portalPart, ItemStack stack, boolean isSneaking, EntityPlayer player);
}
