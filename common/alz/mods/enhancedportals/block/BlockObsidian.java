package alz.mods.enhancedportals.block;

import alz.mods.enhancedportals.helpers.PortalHelper;
import net.minecraft.entity.player.EntityPlayer;
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
		PortalHelper.createPortalAround(world, x, y, z, player);
		
		return false;
	}
}
