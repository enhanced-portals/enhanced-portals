package uk.co.shadeddimensions.enhancedportals.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.util.Texture;

public class ItemTextureDuplicator extends Item
{
    public ItemTextureDuplicator(int id, String name)
    {
        super(id);
        setUnlocalizedName(name);
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    public static boolean hasStoredTexture(ItemStack stack)
    {
        return stack.getTagCompound() != null && stack.getTagCompound().hasKey("Texture");
    }

    public static String getStoredTexture(ItemStack stack)
    {
        return stack.getTagCompound() == null ? "" : stack.getTagCompound().getString("Texture");
    }

    public static void setStoredTexture(ItemStack stack, String texture)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setString("Texture", texture);
    }

    public static void resetStoredTexture(ItemStack stack)
    {
        if (stack.getTagCompound() != null)
        {
            stack.getTagCompound().removeTag("Texture");
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (player.isSneaking())
        {
            if (hasStoredTexture(stack))
            {
                resetStoredTexture(stack);
            }
        }

        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        int blockID = world.getBlockId(x, y, z), blockMeta = world.getBlockMetadata(x, y, z);

        if (player.isSneaking() && blockID == CommonProxy.blockFrame.blockID)
        {
            TilePortalFrame frame = (TilePortalFrame) world.getBlockTileEntity(x, y, z);
            frame.texture = new Texture();
            world.markBlockForRenderUpdate(x, y, z);
            return true;
        }
        else if (!hasStoredTexture(stack) && blockID != CommonProxy.blockPortal.blockID && blockID != CommonProxy.blockFrame.blockID && Block.blocksList[blockID].isOpaqueCube())
        {
            setStoredTexture(stack, "B:" + blockID + ":" + blockMeta);
            return true;
        }

        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        if (!hasStoredTexture(stack))
        {
            list.add("No stored texture");
        }
        else
        {
            list.add(Texture.getTextureName(getStoredTexture(stack)));
        }
    }
}
