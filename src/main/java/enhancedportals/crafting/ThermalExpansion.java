package enhancedportals.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.EnhancedPortals;
import enhancedportals.block.BlockFrame;
import enhancedportals.block.BlockStabilizer;
import enhancedportals.block.BlockStabilizerEmpty;
import enhancedportals.item.ItemBlankUpgrade;
import enhancedportals.item.ItemUpgrade;

public class ThermalExpansion
{
    static ItemStack getItemStack(String ID)
    {
        return GameRegistry.findItemStack(EnhancedPortals.MODID_THERMALEXPANSION, ID, 1);
    }

    // TODO this is a direct update from the old version. a lot of stuff needs tweaking as it wasn't finished back then
    
    public static void registerMachineRecipes()
    {
        ThermalExpansionHelper.addTransposerFill(10000, new ItemStack(BlockFrame.instance, 1, 0), new ItemStack(BlockFrame.instance, 1, BlockFrame.REDSTONE_INTERFACE), new FluidStack(FluidRegistry.getFluidID("redstone"), 200), false);
        ThermalExpansionHelper.addTransposerFill(10000, new ItemStack(ItemBlankUpgrade.instance, 1, 0), new ItemStack(ItemUpgrade.instance, 1, 0), new FluidStack(FluidRegistry.getFluidID("redstone"), 200), false);

        ThermalExpansionHelper.addTransposerFill(15000, new ItemStack(BlockFrame.instance, 1, 0), new ItemStack(BlockFrame.instance, 1, BlockFrame.NETWORK_INTERFACE), new FluidStack(FluidRegistry.getFluidID("ender"), 125), false);
        ThermalExpansionHelper.addTransposerFill(15000, new ItemStack(ItemBlankUpgrade.instance, 1, 1), new ItemStack(ItemUpgrade.instance, 1, 1), new FluidStack(FluidRegistry.getFluidID("ender"), 125), false);

        ThermalExpansionHelper.addTransposerFill(25000, new ItemStack(BlockStabilizerEmpty.instance, 1, 0), new ItemStack(BlockStabilizer.instance, 1, 0), new FluidStack(FluidRegistry.getFluidID("ender"), 150), false);
    }

    public static void registerRecipes()
    {
        ItemStack machineFrame = getItemStack("machineFrame"), coilGold = getItemStack("powerCoilGold"), electrum = getItemStack("ingotElectrum");

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockFrame.instance, 4, 0), "SQS", "QMQ", "SQS", 'S', new ItemStack(Blocks.stone), 'Q', new ItemStack(Items.quartz), 'M', machineFrame));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockStabilizerEmpty.instance, 1, 0), " E ", "EME", "CCC", 'M', machineFrame, 'C', coilGold, 'E', electrum));
    }
}
