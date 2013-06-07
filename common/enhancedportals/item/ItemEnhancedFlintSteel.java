package enhancedportals.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.lib.ItemIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;

public class ItemEnhancedFlintSteel extends ItemFlintAndSteel
{
    Icon texture;
    
    public ItemEnhancedFlintSteel()
    {
        super(ItemIds.EnhancedFlintAndSteel);
        hasSubtypes = false;
        this.setMaxDamage(128);
        setCreativeTab(Reference.CREATIVE_TAB);
        setUnlocalizedName(Localization.EnhancedFlintSteel_Name);
        maxStackSize = 1;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        texture = iconRegister.registerIcon(Reference.MOD_ID + ":" + Localization.EnhancedFlintSteel_Name);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        return texture;
    }
}
