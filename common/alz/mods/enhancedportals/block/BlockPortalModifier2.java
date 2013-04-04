package alz.mods.enhancedportals.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier2;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortalModifier2 extends BlockContainer
{
	Icon[] activeFace;
	Icon sideFace;
	
	protected BlockPortalModifier2()
	{
		super(Reference.BlockIDs.DialDevice + 1, Material.rock);
		setHardness(50.0F);
		setResistance(2000.0F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName(Strings.PortalModifier_Name);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityPortalModifier2();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		activeFace = new Icon[16];
		sideFace = iconRegister.registerIcon(Strings.PortalModifier_Icon_Side);
		
		for (int i = 0; i < 16; i++)
		{
			activeFace[i] = iconRegister.registerIcon(String.format(Strings.PortalModifier_Icon_Active, i));
		}
	}
	
	// Placed in world
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
	{
		int meta = world.getBlockMetadata(x, y, z);
		
		return side == meta ? activeFace[0] : sideFace;
	}
	
	// For inventory
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		return side == 1 ? activeFace[0] : sideFace;
	}
}