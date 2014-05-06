package enhancedportals.crafting;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ThermalExpansion
{
    public static void registerRecipes()
    {
        //ItemStack machineFrame = getItemStack("machineFrame"), coilGold = getItemStack("powerCoilGold"), electrum = getItemStack("ingotElectrum");
        
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockFrame.instance, 4, 0), "SQS", "QMQ", "SQS", 'S', new ItemStack(Block.stone), 'Q', new ItemStack(Item.netherQuartz), 'M', machineFrame));
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCrafting.instance, 1, 0), " E ", "EME", "CCC", 'M', machineFrame, 'C', coilGold, 'E', electrum));
    }
    
    public static void registerMachineRecipes()
    {
        //ThermalExpansionHelper.addTransposerFill(10000, new ItemStack(BlockFrame.instance, 1, 0), new ItemStack(BlockFrame.instance, 1, BlockFrame.REDSTONE_INTERFACE), new FluidStack(FluidRegistry.getFluidID("redstone"), 200), false);
        //ThermalExpansionHelper.addTransposerFill(10000, new ItemStack(ItemMisc.instance, 1, 1), new ItemStack(ItemUpgrade.instance, 1, 0), new FluidStack(FluidRegistry.getFluidID("redstone"), 200), false);
        
        //ThermalExpansionHelper.addTransposerFill(15000, new ItemStack(BlockFrame.instance, 1, 0), new ItemStack(BlockFrame.instance, 1, BlockFrame.NETWORK_INTERFACE), new FluidStack(FluidRegistry.getFluidID("ender"), 125), false);
        //ThermalExpansionHelper.addTransposerFill(15000, new ItemStack(ItemMisc.instance, 1, 1), new ItemStack(ItemUpgrade.instance, 1, 1), new FluidStack(FluidRegistry.getFluidID("ender"), 125), false);
        
        //ThermalExpansionHelper.addTransposerFill(25000, new ItemStack(BlockCrafting.instance, 1, 0), new ItemStack(BlockStabilizer.instance, 1, 0), new FluidStack(FluidRegistry.getFluidID("ender"), 150), false);
    }
    
    static ItemStack getItemStack(String ID)
    {
        return GameRegistry.findItemStack("ThermalExpansion", ID, 1);
    }
}
