package uk.co.shadeddimensions.ep3.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.client.gui.GuiBiometricIdentifier;
import uk.co.shadeddimensions.ep3.client.gui.GuiDiallingDevice;
import uk.co.shadeddimensions.ep3.client.gui.GuiDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.client.gui.GuiModuleManipulator;
import uk.co.shadeddimensions.ep3.client.gui.GuiNetworkInterface;
import uk.co.shadeddimensions.ep3.client.gui.GuiPortalController;
import uk.co.shadeddimensions.ep3.client.gui.GuiRedstoneInterface;
import uk.co.shadeddimensions.ep3.client.gui.GuiScanner;
import uk.co.shadeddimensions.ep3.client.gui.GuiTexture;
import uk.co.shadeddimensions.ep3.container.ContainerBiometricIdentifier;
import uk.co.shadeddimensions.ep3.container.ContainerDiallingDevice;
import uk.co.shadeddimensions.ep3.container.ContainerDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.container.ContainerModuleManipulator;
import uk.co.shadeddimensions.ep3.container.ContainerNetworkInterface;
import uk.co.shadeddimensions.ep3.container.ContainerPortalController;
import uk.co.shadeddimensions.ep3.container.ContainerRedstoneInterface;
import uk.co.shadeddimensions.ep3.container.ContainerScanner;
import uk.co.shadeddimensions.ep3.container.ContainerTexture;
import uk.co.shadeddimensions.ep3.item.ItemHandheldScanner;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == GUIs.Scanner.ordinal())
        {
            ItemStack stack = player.inventory.getCurrentItem();
            return new GuiScanner(ItemHandheldScanner.getInventory(stack), player, stack);
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (ID == GUIs.PortalController.ordinal())
            {
                return new GuiPortalController((TilePortalController) tile, player);
            }
            else if (ID == GUIs.RedstoneInterface.ordinal())
            {
                return new GuiRedstoneInterface((TileRedstoneInterface) tile, player);
            }
            else if (ID == GUIs.NetworkInterface.ordinal())
            {
                return new GuiNetworkInterface((TilePortalController) tile, player);
            }
            else if (ID == GUIs.ModuleManipulator.ordinal())
            {
                return new GuiModuleManipulator((TileModuleManipulator) tile, player);
            }
            else if (ID == GUIs.DimensionalBridgeStabilizer.ordinal())
            {
                return new GuiDimensionalBridgeStabilizer((TileStabilizerMain) tile, player);
            }
            else if (ID == GUIs.DiallingDevice.ordinal())
            {
                return new GuiDiallingDevice((TileDiallingDevice) tile, player);
            }
            else if (ID == GUIs.TexturesFrame.ordinal())
            {
                return new GuiTexture((TilePortalController) tile, player, 0, true);
            }
            else if (ID == GUIs.TexturesPortal.ordinal())
            {
                return new GuiTexture((TilePortalController) tile, player, 1, true);
            }
            else if (ID == GUIs.TexturesParticle.ordinal())
            {
                return new GuiTexture((TilePortalController) tile, player, 2, true);
            }
            else if (ID == GUIs.TexturesDiallingDevice.ordinal())
            {
                return new GuiTexture((TileDiallingDevice) tile, player, 0, false);
            }
            else if (ID == GUIs.BiometricIdentifier.ordinal())
            {
                return new GuiBiometricIdentifier((TileBiometricIdentifier) tile, player);
            }
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == GUIs.Scanner.ordinal())
        {
            ItemStack stack = player.inventory.getCurrentItem();
            return new ContainerScanner(ItemHandheldScanner.getInventory(stack), player, stack);
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (ID == GUIs.PortalController.ordinal())
            {
                CommonProxy.sendUpdatePacketToPlayer((TilePortalController) tile, player);
                return new ContainerPortalController((TilePortalController) tile, player);
            }
            else if (ID == GUIs.RedstoneInterface.ordinal())
            {
                CommonProxy.sendUpdatePacketToPlayer((TileRedstoneInterface) tile, player);
                return new ContainerRedstoneInterface((TileRedstoneInterface) tile, player);
            }
            else if (ID == GUIs.NetworkInterface.ordinal())
            {
                return new ContainerNetworkInterface((TilePortalController) tile, player);
            }
            else if (ID == GUIs.ModuleManipulator.ordinal())
            {
                return new ContainerModuleManipulator((TileModuleManipulator) tile, player);
            }
            else if (ID == GUIs.DimensionalBridgeStabilizer.ordinal())
            {
                CommonProxy.sendUpdatePacketToPlayer((TileStabilizerMain) tile, player);
                return new ContainerDimensionalBridgeStabilizer((TileStabilizerMain) tile, player);
            }
            else if (ID == GUIs.DiallingDevice.ordinal())
            {
                return new ContainerDiallingDevice((TileDiallingDevice) tile, player);
            }
            else if (ID == GUIs.TexturesFrame.ordinal())
            {
                return new ContainerTexture((TilePortalController) tile, player);
            }
            else if (ID == GUIs.TexturesPortal.ordinal())
            {
                return new ContainerTexture((TilePortalController) tile, player);
            }
            else if (ID == GUIs.TexturesParticle.ordinal())
            {
                return new ContainerTexture((TilePortalController) tile, player);
            }
            else if (ID == GUIs.TexturesDiallingDevice.ordinal())
            {
                return new ContainerTexture((TilePortalController) tile, player);
            }
            else if (ID == GUIs.BiometricIdentifier.ordinal())
            {
                CommonProxy.sendUpdatePacketToPlayer((TileBiometricIdentifier) tile, player);
                return new ContainerBiometricIdentifier((TileBiometricIdentifier) tile, player);
            }
        }

        return null;
    }
}
