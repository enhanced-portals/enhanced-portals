package alz.mods.enhancedportals.block;

import alz.mods.enhancedportals.helpers.PortalHelper;
import alz.mods.enhancedportals.reference.BlockID;
import alz.mods.enhancedportals.reference.Settings;
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
		if (world.getBlockId(x, y - 1, z) == BlockID.Obsidian || (Settings.AllowModifiers && world.getBlockId(x, y - 1, z) == BlockID.PortalModifier))
		{
			PortalHelper.createPortal(world, x, y, z, 0);
			
			return;
		}
		
		super.onBlockAdded(world, x, y, z);
	}
}