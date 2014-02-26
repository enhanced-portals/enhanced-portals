package uk.co.shadeddimensions.ep3.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.item.block.ItemFrame;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.network.PacketHandlerServer;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileFrame;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.portal.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileRedstoneInterface;
import uk.co.shadeddimensions.ep3.util.WorldUtils;

public class ItemUpgrade extends Item
{
	public static int ID;
	public static ItemUpgrade instance;

	static Icon baseIcon;
	static Icon[] overlayIcons = new Icon[BlockFrame.FRAME_TYPES - 2];

	public ItemUpgrade()
	{
		super(ID);
		ID += 256;
		instance = this;
		setCreativeTab(Reference.creativeTab);
		setUnlocalizedName("upgrade");
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
	public Icon getIconFromDamageForRenderPass(int damage, int pass)
	{
		if (pass == 1)
		{
			return overlayIcons[damage];
		}

		return baseIcon;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < overlayIcons.length; i++)
		{
			par3List.add(new ItemStack(itemID, 1, i));
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

		TileEntity tile = world.getBlockTileEntity(x, y, z);
		int blockMeta = stack.getItemDamage() + 2;

		if (tile instanceof TileFrame)
		{
			TileFrame frame = (TileFrame) tile;
			TileController controller = frame.getPortalController();

			if (controller == null)
			{
				frame = null;
				world.setBlock(x, y, z, BlockFrame.ID, blockMeta, 2);
				decrementStack(player, stack);
				return true;
			}
			else
			{
				if (controller.getHasBiometricIdentifier() && blockMeta == BlockFrame.BIOMETRIC_IDENTIFIER)
				{
					player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("multipleBiometricIdentifiers")));
					return false;
				}
				else if (controller.getDiallingDeviceCount() > 0 && blockMeta == BlockFrame.NETWORK_INTERFACE)
				{
					player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("dialDeviceAndNetworkInterface")));
					return false;
				}
				else if (controller.getNetworkInterfaceCount() > 0 && blockMeta == BlockFrame.DIALLING_DEVICE)
				{
					player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("dialDeviceAndNetworkInterface")));
					return false;
				}
				else if (controller.getHasModuleManipulator() && blockMeta == BlockFrame.MODULE_MANIPULATOR)
				{
					player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("multipleModuleManipulators")));
					return false;
				}

				controller.removeFrame(frame.getChunkCoordinates());
				frame = null;
				world.setBlock(x, y, z, BlockFrame.ID, blockMeta, 2);
				decrementStack(player, stack);
				TilePortalPart t = (TilePortalPart) world.getBlockTileEntity(x, y, z);

				if (t instanceof TileRedstoneInterface)
				{
					controller.addRedstoneInterface(t.getChunkCoordinates());
				}
				else if (t instanceof TileDiallingDevice)
				{
					controller.addDialDevice(t.getChunkCoordinates());
				}
				else if (t instanceof TileBiometricIdentifier)
				{
					controller.setBiometricIdentifier(t.getChunkCoordinates());
				}
				else if (t instanceof TileNetworkInterface)
				{
					controller.addNetworkInterface(t.getChunkCoordinates());
				}
				else if (t instanceof TileModuleManipulator)
				{
					controller.setModuleManipulator(t.getChunkCoordinates());
				}

				t.setPortalController(controller.getChunkCoordinates());
				WorldUtils.markForUpdate(controller);
				return true;
			}
		}

		return false;
	}

	@Override
	public void registerIcons(IconRegister register)
	{
		baseIcon = register.registerIcon("enhancedportals:blankUpgrade");

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
