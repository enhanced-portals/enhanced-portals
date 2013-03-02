package alz.mods.enhancedportals.block;

import alz.mods.enhancedportals.common.EnhancedPortals;
import alz.mods.enhancedportals.common.Reference;
import alz.mods.enhancedportals.helpers.PortalHelper;
import net.minecraft.world.World;

public class BlockFire extends net.minecraft.block.BlockFire
{
	public BlockFire()	
	{
		super(51, 31);
		setHardness(0.0F);
		setLightValue(1.0F);
		setStepSound(soundWoodFootstep);
		setBlockName("fire");
		disableStats();
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		if (world.getBlockId(x, y - 1, z) == EnhancedPortals.instance.blockObsidian.blockID || (Reference.allowModifiers && world.getBlockId(x, y - 1, z) == EnhancedPortals.instance.blockPortalModifier.blockID))
		{
			PortalHelper.createPortal(world, x, y, z, 0);
			
			return;
		}
		
		super.onBlockAdded(world, x, y, z);
	}
}