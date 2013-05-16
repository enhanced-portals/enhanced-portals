package enhancedportals.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;

public class BlockDialDeviceBasic extends BlockEnhancedPortals
{
    public BlockDialDeviceBasic()
    {
        super(BlockIds.DialHomeDeviceBasic, Material.rock);
        setCreativeTab(Reference.CREATIVE_TAB);
        setCanRotate();
        setHardness(25.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Localization.DialDeviceBasic_Name);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityDialDeviceBasic();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        
    }
    
    @Override
    public int getRenderType()
    {
        return -1;
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (!player.isSneaking())
        {
            player.openGui(EnhancedPortals.instance, GuiIds.DialDeviceBasic, worldObj, x, y, z);
        }
        
        return super.onBlockActivated(worldObj, x, y, z, player, par6, par7, par8, par9);
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        ((TileEntityDialDeviceBasic) world.getBlockTileEntity(x, y, z)).scheduledBlockUpdate();
    }
}
