package enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.lib.BlockIds;
import enhancedportals.portal.PortalTexture;

public class BlockDummyPortal extends Block
{
    public BlockDummyPortal()
    {
        super(BlockIds.DummyPortal, Material.portal);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        return new PortalTexture(meta).getIcon(side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        // Stops from trying to register NULL
    }
}
