package alz.mods.enhancedportals.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import alz.mods.enhancedportals.helpers.TeleportData;
import alz.mods.enhancedportals.reference.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;

public class ItemScroll extends Item
{
    Icon Texture;
    public ItemScroll()
    {
        super(Reference.ItemIDs.ItemScroll);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMisc);
        setUnlocalizedName(Reference.Strings.ItemScroll_Name);
        maxStackSize = 1;
        hasSubtypes = true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateIcons(IconRegister IconRegister)
    {
        Texture = IconRegister.registerIcon(Reference.Strings.ItemScroll_Icon);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        return Texture;
    }

    public void setLocationData(ItemStack Stack, TeleportData Data)
    {
        NBTTagCompound Tag = Stack.stackTagCompound;
        
        if (Tag == null)
        {
            Tag = new NBTTagCompound();
        }
        
        Tag.setInteger("x", Data.GetX());
        Tag.setInteger("y", Data.GetY());
        Tag.setInteger("z", Data.GetZ());
        Tag.setInteger("d", Data.GetDimension());
        Stack.stackTagCompound = Tag;
        Stack.setItemDamage(1);
    }
    
    public TeleportData getLocationdata(ItemStack Stack)
    {
        NBTTagCompound Tag = Stack.stackTagCompound;
        
        if (Tag == null)
        {
            return null;
        }
        
        int x = Tag.getInteger("x");
        int y = Tag.getInteger("y");
        int z = Tag.getInteger("z");
        int d = Tag.getInteger("d");
        
        return new TeleportData(x,y,z,d);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (par1ItemStack.getItemDamage() == 1)
        {
            TeleportData Data = getLocationdata(par1ItemStack);
            
            if (Data == null)
            {
                return;
            }
            
            par3List.add("x: " + Data.GetX());
            par3List.add("y: " + Data.GetY());
            par3List.add("z: " + Data.GetZ());            
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack)
    {
        return par1ItemStack.getItemDamage() == 1;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return EnumRarity.rare;
    }
}
