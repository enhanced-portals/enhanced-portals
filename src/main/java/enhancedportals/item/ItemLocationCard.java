package enhancedportals.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.ProxyCommon;
import enhancedportals.tile.TileDimensionalBridgeStabilizerController;
import enhancedportals.tile.TileFrameController;
import enhancedportals.util.DimensionCoordinates;

public class ItemLocationCard extends Item {
    IIcon texture;
    
    public ItemLocationCard(String n) {
        super();
        setCreativeTab(ProxyCommon.creativeTab);
        setUnlocalizedName(n);
        setMaxStackSize(16);
    }
    
    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return true;
    }

    @Override
    public IIcon getIconFromDamage(int par1) {
        return texture;
    }
    
    @Override
    public void registerIcons(IIconRegister register) {
        texture = register.registerIcon("enhancedportals:location_card");
    }
    
    public static boolean isDataSet(ItemStack stack) {
        if (stack != null && stack.getItem() != null && stack.getItem() instanceof ItemLocationCard) {
            return stack.getTagCompound() != null;
        }
        
        return false;
    }
    
    public static void setData(ItemStack stack, DimensionCoordinates d) {
        if (stack != null && stack.getItem() != null && stack.getItem() instanceof ItemLocationCard && d != null) {
            stack.setTagCompound(d.save(new NBTTagCompound()));
        }
    }
    
    public static void clearData(ItemStack stack) {
        if (stack != null && stack.getItem() != null && stack.getItem() instanceof ItemLocationCard) {
            stack.setTagCompound(null);
        }
    }
    
    public static DimensionCoordinates getData(ItemStack stack) {
        if (stack != null && stack.getItem() != null && stack.getItem() instanceof ItemLocationCard) {
            return new DimensionCoordinates(stack.getTagCompound());
        }
        
        return null;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        TileEntity t = world.getTileEntity(x, y, z);
        
        if (t instanceof TileFrameController) {
            if (world.isRemote) return false;
            TileFrameController control = (TileFrameController) t;
            if (!control.isFinalized()) return false;
            if (EnhancedPortals.proxy.portalMap.getPortalGlyphs(control.getDimensionCoordinates()) == null) { player.addChatMessage(new ChatComponentText("Set a UID first.")); return true; }
            EnhancedPortals.proxy.portalMap.setPortalDBS(control.getDimensionCoordinates(), getData(stack));
            player.addChatMessage(new ChatComponentText("Set."));
            return true;
        } else if (t instanceof TileDimensionalBridgeStabilizerController) {
            if (world.isRemote) return false;
            setData(stack, ((TileDimensionalBridgeStabilizerController) t).getDimensionCoordinates());
            player.addChatMessage(new ChatComponentText("Added."));
            return true;
        }
        
        return false;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            clearData(stack);
        }
        
        return super.onItemRightClick(stack, world, player);
    }
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
        super.addInformation(stack, player, list, b);
        
        if (isDataSet(stack)) {
            DimensionCoordinates d = getData(stack);
            list.add(d.posD + " [" + d.posX + ", " + d.posY + ", " + d.posZ + "]"); // TODO: Dimension name
        }
    }
}
