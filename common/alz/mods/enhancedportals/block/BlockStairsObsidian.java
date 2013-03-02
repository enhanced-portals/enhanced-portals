package alz.mods.enhancedportals.block;

import alz.mods.enhancedportals.common.Reference;
import alz.mods.enhancedportals.helpers.PortalHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockStairsObsidian extends BlockStairs
{
	public BlockStairsObsidian()
	{
		super(Reference.IDObsidianStairs, Block.obsidian, 0);
		setHardness(50.0F);
		setResistance(2000.0F);
		setStepSound(soundStoneFootstep);
		setBlockName("blockStairsObsidian");
		setLightOpacity(0);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		PortalHelper.createPortalAround(world, x, y, z, player);
		
		return false;
	}
}
