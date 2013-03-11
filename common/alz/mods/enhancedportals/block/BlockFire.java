package alz.mods.enhancedportals.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import alz.mods.enhancedportals.helpers.PortalHelper;
import alz.mods.enhancedportals.reference.BlockID;
import alz.mods.enhancedportals.reference.Settings;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockFire extends net.minecraft.block.BlockFire
{
	Icon[] texture;
	
	public BlockFire()	
	{
		super(51);
		setHardness(0.0F);
		setLightValue(1.0F);
		setStepSound(soundWoodFootstep);
		setUnlocalizedName("fire");
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
	
	@SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
        this.texture = new Icon[] {par1IconRegister.func_94245_a("fire_0"), par1IconRegister.func_94245_a("fire_1")};
    }

    @SideOnly(Side.CLIENT)
    public Icon func_94438_c(int par1)
    {
        return this.texture[par1];
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return this.texture[0];
    }
}