package uk.co.shadeddimensions.enhancedportals.network;

import java.util.EnumSet;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class GoggleTickHandler implements ITickHandler
{
    int tickTimer = 0;
    
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {

    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        if (FMLClientHandler.instance().getClient().currentScreen != null)
        {
            return;
        }
        
        if (tickTimer >= 20)
        {
            ItemStack helmet = FMLClientHandler.instance().getClient().thePlayer.inventory.armorInventory[3];        
            ClientProxy.isWearingGoggles = helmet != null && helmet.itemID == Item.helmetGold.itemID;
            tickTimer = -1;
        }
        
        tickTimer++;
    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel()
    {
        return "EP2 Goggles";
    }
}
