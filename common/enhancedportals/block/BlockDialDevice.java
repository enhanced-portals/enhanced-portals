package enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.tileentity.TileEntityDialDevice;

public class BlockDialDevice extends BlockEnhancedPortals
{
    public BlockDialDevice()
    {
        super(BlockIds.DialHomeDevice, Material.rock);
        setCreativeTab(Reference.CREATIVE_TAB);
        setCanRotate();
        setHardness(25.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Localization.DialDevice_Name);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityDialDevice();
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        return Block.blocksList[BlockIds.Obsidian].getIcon(par1, par2);
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z)
    {
        return new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST };
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {

    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
}
