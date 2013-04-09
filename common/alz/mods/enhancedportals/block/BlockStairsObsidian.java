package alz.mods.enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import alz.mods.enhancedportals.helpers.PortalHelper;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStairsObsidian extends BlockStairs
{
    Icon texture;

    public BlockStairsObsidian()
    {
        super(Reference.BlockIDs.ObsidianStairs, Block.obsidian, 0);
        setHardness(50.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Strings.ObsidianStairs_Name);
        setLightOpacity(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        return texture;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        PortalHelper.createPortalAround(world, x, y, z, player);

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        texture = iconRegister.registerIcon("obsidian");
    }
}
