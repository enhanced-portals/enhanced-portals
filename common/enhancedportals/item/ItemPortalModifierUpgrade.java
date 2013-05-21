package enhancedportals.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.ItemIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class ItemPortalModifierUpgrade extends Item
{
    Icon[] textures;
    public static String[] names = { "particles", "sounds", "dimension", "advancedDimension", "computer", "nether", "overworld", "camouflage" };

    public ItemPortalModifierUpgrade()
    {
        super(ItemIds.PortalModifierUpgrade);
        hasSubtypes = true;
        setMaxDamage(0);
        setCreativeTab(Reference.CREATIVE_TAB);
        setUnlocalizedName(Localization.PortalModifierUpgrade_Name);
        maxStackSize = 1;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4)
    {
        list.add(Localization.localizeString("upgrade.portalModifier"));

        if (itemStack.getItemDamage() == 2)
        {
            list.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.dimensional.text"));
        }
        else if (itemStack.getItemDamage() == 3)
        {
            list.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.advancedDimensional.text"));
            list.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.advancedDimensional.textB"));
            list.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.advancedDimensional.textC"));
        }
        else if (itemStack.getItemDamage() == 4)
        {
            list.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.computer.text"));
        }
        else if (itemStack.getItemDamage() == 5)
        {
            list.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.blocks.text"));
            list.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.blocks.textA"));
            list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.glowStone.getUnlocalizedName() + ".name"));
            list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.netherBrick.getUnlocalizedName() + ".name"));
            list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockNetherQuartz.getUnlocalizedName() + ".default.name"));
        }
        else if (itemStack.getItemDamage() == 6)
        {
            list.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.blocks.text"));
            list.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.blocks.textA"));
            list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockIron.getUnlocalizedName() + ".name"));
            list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockGold.getUnlocalizedName() + ".name"));
            list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockDiamond.getUnlocalizedName() + ".name"));
        }
        else if (itemStack.getItemDamage() == 7)
        {
            list.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.camouflage.textA"));
            list.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.camouflage.textB"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        if (par1 > textures.length)
        {
            return textures[0];
        }

        return textures[par1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemStack)
    {
        return EnumRarity.rare;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < textures.length; var4++)
        {
            if (names[var4].equals("computer"))
            {
                continue;
            }

            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 15);
        return super.getUnlocalizedName() + "." + names[i];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack)
    {
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (world.isRemote)
        {
            return false;
        }

        if (world.getBlockId(x, y, z) == BlockIds.PortalModifier)
        {
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);

            if (!modifier.hasUpgrade(stack.getItemDamage()))
            {
                if (stack.getItemDamage() == 3 && modifier.hasUpgradeNoCheck(2))
                {
                    player.sendChatToPlayer("You must remove the dimensional upgrade first.");
                    return false;
                }
                else if (stack.getItemDamage() == 2 && modifier.hasUpgradeNoCheck(3))
                {
                    player.sendChatToPlayer("You must remove the advanced dimensional upgrade first.");
                    return false;
                }

                modifier.installUpgrade(stack.getItemDamage());
                player.inventory.mainInventory[player.inventory.currentItem] = null;

                ((EntityPlayerMP) player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP) player);
                PacketDispatcher.sendPacketToAllAround(x + 0.5, y + 0.5, z + 0.5, 128, world.provider.dimensionId, new PacketTEUpdate(modifier).getPacket());
            }
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        textures = new Icon[names.length];

        for (int i = 0; i < textures.length; i++)
        {
            textures[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + Localization.PortalModifierUpgrade_Name + "_" + i);
        }
    }
}
