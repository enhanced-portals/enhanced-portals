package alz.mods.enhancedportals.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import alz.mods.enhancedportals.helpers.PortalHelper;
import alz.mods.enhancedportals.reference.BlockID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockStairsObsidian extends BlockStairs
{
	Icon texture;
	
	public BlockStairsObsidian()
	{
		super(BlockID.ObsidianStairs, Block.obsidian, 0);
		setHardness(50.0F);
		setResistance(2000.0F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName("stairsObsidian");
		setLightOpacity(0);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		PortalHelper.createPortalAround(world, x, y, z, player);
		
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94332_a(IconRegister iconRegister)
	{
		texture = iconRegister.func_94245_a("obsidian");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		return texture;
	}
}
