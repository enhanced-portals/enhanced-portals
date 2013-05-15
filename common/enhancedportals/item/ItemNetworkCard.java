package enhancedportals.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.ItemIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.lib.WorldLocation;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class ItemNetworkCard extends Item
{
    Icon texture;
    
    public ItemNetworkCard()
    {
        super(ItemIds.NetworkCard);
        hasSubtypes = false;
        setMaxDamage(0);
        setCreativeTab(Reference.CREATIVE_TAB);
        setUnlocalizedName(Localization.NetworkCard_Name);
        maxStackSize = 1;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        texture = iconRegister.registerIcon(Reference.MOD_ID + ":" + Localization.NetworkCard_Name);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        return texture;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4)
    {
        if (isSet(itemStack))
        {
            String[] network = itemStack.getTagCompound().getString("Network").split(" ");
            String[] net = new String[9];
            
            for (int i = 0; i < 9; i++)
            {
                if (network.length > i)
                {
                    net[i] = network[i];
                }
                else
                {
                    net[i] = "";
                }
            }
            
            list.add(net[0] + " " + net[1] + " " + net[2]);
            
            if (network.length > 3)
            {
                list.add(net[3] + " " + net[4] + " " + net[5]);
            }
            
            if (network.length > 6)
            {
                list.add(net[6] + " " + net[7] + " " + net[8]);
            }
        }
        else
        {
            list.add("Network is not set");
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemStack)
    {
        return EnumRarity.rare;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack)
    {
        return isSet(itemStack);
    }
    
    public boolean addData(ItemStack stack, TileEntityPortalModifier modifier)
    {
        if (isSet(stack))
        {
            return false;
        }
        
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("Network", modifier.network);
        stack.setTagCompound(tagCompound);
        
        return true;
    }
    
    public boolean isSet(ItemStack stack)
    {
        return stack.hasTagCompound();
    }
    
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (world.getBlockId(x, y, z) != BlockIds.PortalModifier)
        {
            return false;
        }
        
        TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);
        
        if (!isSet(itemStack))
        {
            addData(itemStack, modifier);
        }
        else
        {
            if (world.isRemote)
            {
                return true;
            }
            
            NBTTagCompound tagCompound = itemStack.getTagCompound();   
            String network = tagCompound.getString("Network");
            
            if (!modifier.network.equals(network) && !modifier.isActive())
            {
                modifier.network = network;
                EnhancedPortals.proxy.ModifierNetwork.removeFromAllNetworks(new WorldLocation(x, y, z, world));
                EnhancedPortals.proxy.ModifierNetwork.addToNetwork(network, new WorldLocation(x, y, z, world));
                
                player.inventory.mainInventory[player.inventory.currentItem] = null;
                ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
                
                player.sendChatToPlayer(Localization.localizeString("chat.networkSuccessful"));
            }
            else if (modifier.isActive())
            {
                player.sendChatToPlayer(Localization.localizeString("chat.modifierActive"));
            }
        }
        
        return true;
    }
}
