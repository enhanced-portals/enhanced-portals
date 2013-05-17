package enhancedportals.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.ItemIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.lib.WorldLocation;
import enhancedportals.portal.PortalTexture;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class BlockPortalModifier extends BlockEnhancedPortals
{
    Icon texture;

    public BlockPortalModifier()
    {
        super(BlockIds.PortalModifier, Material.rock);
        setCreativeTab(Reference.CREATIVE_TAB);
        setCanRotate();
        setCanRotateVertically();
        setHardness(25.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Localization.PortalModifier_Name);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        if (world.isRemote)
        {
            return;
        }

        TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);

        if (modifier.network != "" && modifier.network != "undefined")
        {
            EnhancedPortals.proxy.ModifierNetwork.removeFromAllNetworks(new WorldLocation(x, y, z, world.provider.dimensionId));
        }
        
        for (int i = 0; i < modifier.upgrades.length; i++)
        {
            if (modifier.upgrades[i])
            {
                ItemStack stack = new ItemStack(EnhancedPortals.proxy.portalModifierUpgrade, 1, i);
                EntityItem entity = new EntityItem(modifier.worldObj, modifier.xCoord + 0.5, modifier.yCoord + 0.5, modifier.zCoord + 0.5, stack);
                modifier.worldObj.spawnEntityInWorld(entity);                
                modifier.upgrades[i] = false;
            }
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntityPortalModifier();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntityPortalModifier modifier = (TileEntityPortalModifier) blockAccess.getBlockTileEntity(x, y, z);

        return side == blockAccess.getBlockMetadata(x, y, z) ? modifier.texture.getModifierIcon() : texture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        return side == 1 ? new PortalTexture((byte) 0).getModifierIcon() : texture;
    }

    @Override
    public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z)
    {
        return ForgeDirection.VALID_DIRECTIONS;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (!player.isSneaking())
        {
            if (player.inventory.getCurrentItem() != null && (player.inventory.getCurrentItem().itemID == ItemIds.PortalModifierUpgrade + 256 || player.inventory.getCurrentItem().itemID == ItemIds.NetworkCard + 256))
            {
                return false;
            }
            else if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == Item.flintAndSteel.itemID)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);

                if (modifier.createPortal())
                {
                    player.inventory.getCurrentItem().damageItem(1, player);
                }

                return true;
            }

            player.openGui(EnhancedPortals.instance, GuiIds.PortalModifier, world, x, y, z);
            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        boolean currentRedstoneState = world.getStrongestIndirectPower(x, y, z) > 0;

        ((TileEntityPortalModifier) world.getBlockTileEntity(x, y, z)).handleRedstoneChanges(currentRedstoneState);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        texture = iconRegister.registerIcon(Reference.MOD_ID + ":" + Localization.PortalModifier_Name + "_side");
    }
}
