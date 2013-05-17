package enhancedportals.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.ItemIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.tileentity.TileEntityAutomaticDialler;

public class BlockAutomaticDialler extends BlockEnhancedPortals
{
    public BlockAutomaticDialler()
    {
        super(BlockIds.AutomaticDialler, Material.rock);
        setCreativeTab(Reference.CREATIVE_TAB);
        setHardness(25.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Localization.AutomaticDialler_Name);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityAutomaticDialler();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return EnhancedPortals.proxy.blockPortalModifier.texture;
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
        ((TileEntityAutomaticDialler) world.getBlockTileEntity(x, y, z)).handleNeighborUpdate();
    }
    
    @Override
    public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == ItemIds.NetworkCard + 256)
        {
            return false;
        }
        
        if (!player.isSneaking())
        {
            player.openGui(EnhancedPortals.instance, GuiIds.AutoDiallerBasic, worldObj, x, y, z);
            return true;
        }
        
        return super.onBlockActivated(worldObj, x, y, z, player, par6, par7, par8, par9);
    }
}
