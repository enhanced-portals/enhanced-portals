package uk.co.shadeddimensions.ep3.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.library.util.ThermalExpansionHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class ThermalExpansion
{
    public static void registerRecipes()
    {
        ItemStack machineFrame = getItemStack("machineFrame"), coilGold = getItemStack("powerCoilGold"), electrum = getItemStack("ingotElectrum");
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.blockFrame, 4, 0), "SQS", "QMQ", "SQS", 'S', new ItemStack(Block.stone), 'Q', new ItemStack(Item.netherQuartz), 'M', machineFrame));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.blockCrafting, 1, 0), " E ", "EME", "CCC", 'M', machineFrame, 'C', coilGold, 'E', electrum));
    }
    
    public static void registerMachineRecipes()
    {
        ThermalExpansionHelper.addTransposerFill(10000, new ItemStack(CommonProxy.blockFrame, 1, 0), new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.REDSTONE_INTERFACE), new FluidStack(FluidRegistry.getFluidID("redstone"), 500), false);
        ThermalExpansionHelper.addTransposerFill(10000, new ItemStack(CommonProxy.itemMisc, 1, 1), new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 0), new FluidStack(FluidRegistry.getFluidID("redstone"), 500), false);
        
        ThermalExpansionHelper.addTransposerFill(15000, new ItemStack(CommonProxy.blockFrame, 1, 0), new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.NETWORK_INTERFACE), new FluidStack(FluidRegistry.getFluidID("ender"), 125), false);
        ThermalExpansionHelper.addTransposerFill(15000, new ItemStack(CommonProxy.itemMisc, 1, 1), new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 1), new FluidStack(FluidRegistry.getFluidID("ender"), 125), false);
        
        ThermalExpansionHelper.addTransposerFill(25000, new ItemStack(CommonProxy.blockCrafting, 1, 0), new ItemStack(CommonProxy.blockStabilizer, 1, 0), new FluidStack(FluidRegistry.getFluidID("ender"), 150), false);
        
    }
    
    static ItemStack getItemStack(String ID)
    {
        return GameRegistry.findItemStack("ThermalExpansion", ID, 1);
    }
}
