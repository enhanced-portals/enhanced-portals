package enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;

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
}
