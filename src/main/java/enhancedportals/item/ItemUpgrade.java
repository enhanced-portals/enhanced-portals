package enhancedportals.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import enhancedportals.EnhancedPortals;
import enhancedportals.block.BlockFrame;
import enhancedportals.tileentity.portal.TileController;
import enhancedportals.tileentity.portal.TileDiallingDevice;
import enhancedportals.tileentity.portal.TileFrame;
import enhancedportals.tileentity.portal.TileModuleManipulator;
import enhancedportals.tileentity.portal.TileNetworkInterface;
import enhancedportals.tileentity.portal.TilePortalPart;
import enhancedportals.tileentity.portal.TileProgrammableInterface;
import enhancedportals.tileentity.portal.TileRedstoneInterface;
import enhancedportals.tileentity.portal.TileTransferEnergy;
import enhancedportals.tileentity.portal.TileTransferFluid;
import enhancedportals.tileentity.portal.TileTransferItem;
import enhancedportals.utility.WorldUtils;

public class ItemUpgrade extends Item
{
    public static ItemUpgrade instance;

    static IIcon baseIcon;
    static IIcon[] overlayIcons = new IIcon[BlockFrame.FRAME_TYPES - 2];

    public ItemUpgrade(String n)
    {
        super();
        instance = this;
        setCreativeTab(EnhancedPortals.creativeTab);
        setUnlocalizedName(n);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    private void decrementStack(EntityPlayer player, ItemStack stack)
    {
        if (!player.capabilities.isCreativeMode)
        {
            stack.stackSize--;

            if (stack.stackSize <= 0)
            {
                stack = null;
            }
        }
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int damage, int pass)
    {
        if (pass == 1)
        {
            return overlayIcons[damage];
        }

        return baseIcon;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < overlayIcons.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return super.getUnlocalizedName() + "." + ItemFrame.unlocalizedName[par1ItemStack.getItemDamage() + 2];
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return false;
        }

        TileEntity tile = world.getTileEntity(x, y, z);
        int blockMeta = stack.getItemDamage() + 2;

        if (tile instanceof TileFrame)
        {
            TileFrame frame = (TileFrame) tile;
            TileController controller = frame.getPortalController();

            if (controller == null)
            {
                frame = null;
                world.setBlock(x, y, z, BlockFrame.instance, blockMeta, 2);
                decrementStack(player, stack);
                return true;
            }
            else
            {
                if (controller.getProgrammableInterface() != null && blockMeta == BlockFrame.PROGRAMMABLE_INTERFACE)
                {
                    player.addChatComponentMessage(new ChatComponentText(EnhancedPortals.localizeError("multipleProgram")));
                    return false;
                }
                else if (controller.getDiallingDevices().size() > 0 && blockMeta == BlockFrame.NETWORK_INTERFACE)
                {
                    player.addChatComponentMessage(new ChatComponentText(EnhancedPortals.localizeError("dialAndNetwork")));
                    return false;
                }
                else if (controller.getNetworkInterfaces().size() > 0 && blockMeta == BlockFrame.DIALLING_DEVICE)
                {
                    player.addChatComponentMessage(new ChatComponentText(EnhancedPortals.localizeError("dialAndNetwork")));
                    return false;
                }
                else if (controller.getModuleManipulator() != null && blockMeta == BlockFrame.MODULE_MANIPULATOR)
                {
                    player.addChatComponentMessage(new ChatComponentText(EnhancedPortals.localizeError("multipleMod")));
                    return false;
                }

                controller.removeFrame(frame.getChunkCoordinates());
                frame = null;
                world.setBlock(x, y, z, BlockFrame.instance, blockMeta, 2);
                decrementStack(player, stack);
                TilePortalPart t = (TilePortalPart) world.getTileEntity(x, y, z);

                if (t instanceof TileRedstoneInterface)
                {
                    controller.addRedstoneInterface(t.getChunkCoordinates());
                }
                else if (t instanceof TileDiallingDevice)
                {
                    controller.addDialDevice(t.getChunkCoordinates());
                }
                else if (t instanceof TileProgrammableInterface)
                {
                    controller.setProgrammableInterface(t.getChunkCoordinates());
                }
                else if (t instanceof TileNetworkInterface)
                {
                    controller.addNetworkInterface(t.getChunkCoordinates());
                }
                else if (t instanceof TileModuleManipulator)
                {
                    controller.setModuleManipulator(t.getChunkCoordinates());
                }
                else if (t instanceof TileTransferEnergy)
                {
                    controller.addTransferEnergy(t.getChunkCoordinates());
                }
                else if (t instanceof TileTransferFluid)
                {
                    controller.addTransferFluid(t.getChunkCoordinates());
                }
                else if (t instanceof TileTransferItem)
                {
                    controller.addTransferItem(t.getChunkCoordinates());
                }

                t.setPortalController(controller.getChunkCoordinates());
                WorldUtils.markForUpdate(controller);
                return true;
            }
        }

        return false;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        baseIcon = register.registerIcon("enhancedportals:blank_upgrade");

        for (int i = 0; i < overlayIcons.length; i++)
        {
            overlayIcons[i] = register.registerIcon("enhancedportals:upgrade_" + i);
        }
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
}
