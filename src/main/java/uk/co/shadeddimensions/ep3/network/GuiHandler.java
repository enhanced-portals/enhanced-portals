package uk.co.shadeddimensions.ep3.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.client.gui.GuiBiometricIdentifier;
import uk.co.shadeddimensions.ep3.client.gui.GuiConfig;
import uk.co.shadeddimensions.ep3.client.gui.GuiDiallingDevice;
import uk.co.shadeddimensions.ep3.client.gui.GuiDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.client.gui.GuiGuide;
import uk.co.shadeddimensions.ep3.client.gui.GuiModuleManipulator;
import uk.co.shadeddimensions.ep3.client.gui.GuiNetworkInterface;
import uk.co.shadeddimensions.ep3.client.gui.GuiPortalController;
import uk.co.shadeddimensions.ep3.client.gui.GuiRedstoneInterface;
import uk.co.shadeddimensions.ep3.client.gui.GuiScanner;
import uk.co.shadeddimensions.ep3.client.gui.GuiTexture;
import uk.co.shadeddimensions.ep3.client.gui.GuiTextureDialler;
import uk.co.shadeddimensions.ep3.client.gui.GuiTransferEnergy;
import uk.co.shadeddimensions.ep3.client.gui.GuiTransferFluid;
import uk.co.shadeddimensions.ep3.client.gui.GuiTransferItem;
import uk.co.shadeddimensions.ep3.container.ContainerBiometricIdentifier;
import uk.co.shadeddimensions.ep3.container.ContainerDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.container.ContainerModuleManipulator;
import uk.co.shadeddimensions.ep3.container.ContainerScanner;
import uk.co.shadeddimensions.ep3.container.ContainerTexture;
import uk.co.shadeddimensions.ep3.container.ContainerTransferEnergy;
import uk.co.shadeddimensions.ep3.container.ContainerTransferFluid;
import uk.co.shadeddimensions.ep3.container.ContainerTransferItem;
import uk.co.shadeddimensions.ep3.item.ItemHandheldScanner;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileFrame;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileRedstoneInterface;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferEnergy;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferFluid;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferItem;
import uk.co.shadeddimensions.library.container.ContainerBase;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    public static final int PORTAL_CONTROLLER = 1;
    public static final int REDSTONE_INTERFACE = 2;
    public static final int NETWORK_INTERFACE = 3;
    public static final int DIALLING_DEVICE = 4;
    public static final int BIOMETRIC_IDENTIFIER = 5;
    public static final int MODULE_MANIPULATOR = 6;
    public static final int TRANSFER_FLUID = 7;
    public static final int TRANSFER_ENERGY = 8;
    public static final int TRANSFER_ITEM = 9;
    
    public static final int TEXTURE_FRAME = 10;
    public static final int TEXTURE_PORTAL = 11;
    public static final int TEXTURE_PARTICLE = 12;
    public static final int TEXTURE_DIALLER = 13;
    
    public static final int DIMENSIONAL_BRIDGE_STABILIZER = 14;
    public static final int SCANNER = 15;
    public static final int GUIDE = 16;
    
    public static final int CONFIG = 17;
    
    public static void openGui(EntityPlayer player, TileEntity tile, int gui)
    {
        player.openGui(EnhancedPortals.instance, gui, tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == SCANNER)
        {
            ItemStack stack = player.inventory.getCurrentItem();
            return new GuiScanner(ItemHandheldScanner.getInventory(stack), player, stack);
        }
        else if (ID == CONFIG)
        {
            return new GuiConfig();
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (ID == PORTAL_CONTROLLER)
            {
                return new GuiPortalController((TileController) tile);
            }
            else if (ID == REDSTONE_INTERFACE)
            {
                return new GuiRedstoneInterface((TileRedstoneInterface) tile);
            }
            else if (ID == NETWORK_INTERFACE)
            {
                return new GuiNetworkInterface((TileController) tile);
            }
            else if (ID == MODULE_MANIPULATOR)
            {
                return new GuiModuleManipulator((TileModuleManipulator) tile, player);
            }
            else if (ID == DIMENSIONAL_BRIDGE_STABILIZER)
            {
                return new GuiDimensionalBridgeStabilizer((TileStabilizerMain) tile, player);
            }
            else if (ID == DIALLING_DEVICE)
            {
                return new GuiDiallingDevice((TileDiallingDevice) tile);
            }
            else if (ID == TEXTURE_FRAME)
            {
                return new GuiTexture((TileController) tile, player, 0);
            }
            else if (ID == TEXTURE_PORTAL)
            {
                return new GuiTexture((TileController) tile, player, 1);
            }
            else if (ID == TEXTURE_PARTICLE)
            {
                return new GuiTexture((TileController) tile, player, 2);
            }
            else if (ID == TEXTURE_DIALLER)
            {
                return new GuiTextureDialler((TileDiallingDevice) tile, player);
            }
            else if (ID == BIOMETRIC_IDENTIFIER)
            {
                return new GuiBiometricIdentifier((TileBiometricIdentifier) tile, player);
            }
            else if (ID == GUIDE)
            {
                return new GuiGuide();
            }
            else if (ID == TRANSFER_FLUID)
            {
                return new GuiTransferFluid((TileTransferFluid) tile);
            }
            else if (ID == TRANSFER_ENERGY)
            {
                return new GuiTransferEnergy((TileTransferEnergy) tile);
            }
            else if (ID == TRANSFER_ITEM)
            {
                return new GuiTransferItem((TileTransferItem) tile);
            }
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == SCANNER)
        {
            ItemStack stack = player.inventory.getCurrentItem();
            return new ContainerScanner(ItemHandheldScanner.getInventory(stack), player, stack);
        }
        else if (ID == CONFIG)
        {
            return new ContainerBase();
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (ID == PORTAL_CONTROLLER)
            {
                PacketHandlerServer.sendGuiPacketToPlayer((TileController) tile, player);
                return new ContainerBase(tile);
            }
            else if (ID == REDSTONE_INTERFACE)
            {
                PacketHandlerServer.sendGuiPacketToPlayer((TileRedstoneInterface) tile, player);
                return new ContainerBase(tile);
            }
            else if (ID == NETWORK_INTERFACE)
            {
            	PacketHandlerServer.sendGuiPacketToPlayer((TileController) tile, player);
                return new ContainerBase(tile);
            }
            else if (ID == MODULE_MANIPULATOR)
            {
                return new ContainerModuleManipulator((TileModuleManipulator) tile, player);
            }
            else if (ID == DIMENSIONAL_BRIDGE_STABILIZER)
            {
                PacketHandlerServer.sendGuiPacketToPlayer((TileStabilizerMain) tile, player);
                return new ContainerDimensionalBridgeStabilizer((TileStabilizerMain) tile, player);
            }
            else if (ID == DIALLING_DEVICE)
            {
            	PacketHandlerServer.sendGuiPacketToPlayer((TileDiallingDevice) tile, player);
                return new ContainerBase(tile);
            }
            else if (ID == TEXTURE_FRAME || ID == TEXTURE_PORTAL || ID == TEXTURE_PARTICLE || ID == TEXTURE_DIALLER)
            {
                return new ContainerTexture((TileFrame) tile, player);
            }
            else if (ID == BIOMETRIC_IDENTIFIER)
            {
                PacketHandlerServer.sendGuiPacketToPlayer((TileBiometricIdentifier) tile, player);
                return new ContainerBiometricIdentifier((TileBiometricIdentifier) tile, player);
            }
            else if (ID == GUIDE)
            {
                return null;
            }
            else if (ID == TRANSFER_FLUID)
            {
                return new ContainerTransferFluid((TileTransferFluid) tile);
            }
            else if (ID == TRANSFER_ENERGY)
            {
                return new ContainerTransferEnergy((TileTransferEnergy) tile);
            }
            else if (ID == TRANSFER_ITEM)
            {
                return new ContainerTransferItem((TileTransferItem) tile);
            }
        }

        return null;
    }
}
