package alz.mods.enhancedportals.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import alz.mods.enhancedportals.EnhancedPortals;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;
import alz.mods.enhancedportals.tileentity.TileEntityDialDevice;

public class BlockDialDevice extends BlockContainer
{
	public BlockDialDevice()
	{
		super(Reference.BlockIDs.DialDevice, Material.rock);
		setHardness(50.0F);
		setResistance(2000.0F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName(Strings.DialDevice_Name);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (player.isSneaking())
			return false;

		player.openGui(EnhancedPortals.instance, Reference.GuiIDs.DialDevice, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityDialDevice();
	}
}
